package lexer;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 *  The Lexer class is responsible for scanning the source file
 *  which is a stream of characters and returning a stream of
 *  tokens; each token object will contain the string (or access
 *  to the string) that describes the token along with an
 *  indication of its location in the source program to be used
 *  for error reporting; we are tracking line numbers; white spaces
 *  are space, tab, newlines
 */
public class Lexer {
  private boolean atEOF = false;
  // next character to process
  private char ch;
  private SourceReader source;
  private int lineNumber;

  // positions in line of current token
  private int startPosition, endPosition;

  /**
   *  Lexer constructor
   *
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer( String sourceFile ) throws Exception {
    // init token table
    new TokenType();
    source = new SourceReader( sourceFile );
    ch = source.read();
  }

  /**
   *  newIdTokens are either ids or reserved words; new id's will be inserted
   *  in the symbol table with an indication that they are id's
   *
   *  @param id is the String just scanned - it's either an id or reserved word
   *  @return the Token; either an id or one for the reserved words
   */
  public Token newIdToken( String id ) {
    return new Token(
      startPosition,
      endPosition,
      lineNumber, Symbol.symbol( id, Tokens.Identifier ));
  }

  public Token newIdToken( String id,int startPosition,int endPosition ) {
    return new Token(
            startPosition,
            endPosition,
            lineNumber, Symbol.symbol( id, Tokens.Identifier ));
  }
  /**
   *   Number are inserted in the symbol table; we don't convert the
   *   numeric strings to numbers until we load the bytecodes for interpreting;
   *   this ensures that any machine numeric dependencies are deferred
   *   until we actually run the program; i.e. the numeric constraints of the
   *   hardware used to compile the source program are not used
   *
   *   @param number is the int String just scanned
   *   @param kind token kind
   *   @return created token
 */

  public Token newNumberToken(String number, Tokens kind) {
    return new Token(
      startPosition,
      endPosition,
      lineNumber,
      Symbol.symbol(number, kind)
    );
  }

  /**
   *  build the token for operators (+ -) or separators (parens, braces)
   *  filter out comments which begin with two slashes
   *
   *  @param tokenString is the String representing the token
   *  @return the Token just found
   */
  public Token makeToken( String tokenString) {
    // filter comments
    if( tokenString.equals("//") ) {
      try {
        int oldLine = source.getLineNo();

        do {
          ch = source.read();
        } while( oldLine == source.getLineNo() );
      } catch (Exception e) {
        atEOF = true;
      }

      return nextToken();
    }

    // ensure it's a valid token
    // Why not just set to null as we have already found illegal token
    Symbol symbol = Symbol.symbol( tokenString, Tokens.BogusToken );

    if( symbol == null ) {
      printInvalidTokenContext(tokenString);

      atEOF = true;
      return nextToken();
    }

    return new Token( startPosition, endPosition, lineNumber,symbol );
  }

  /**
   *  @return the next Token found in the source file
   */
  public Token nextToken() {
    // ch is always the next char to process
    if( atEOF ) {
      if( source != null ) {
        source.close();
        source = null;
      }
      return null;
    }

    try {
      // scan past whitespace
      while( Character.isWhitespace( ch )) {
        ch = source.read();
      }
    } catch( Exception e ) {
      atEOF = true;
      return nextToken();
    }

    startPosition = source.getPosition();
    endPosition = startPosition - 1;
    lineNumber = source.getLineNo();

    if( Character.isJavaIdentifierStart( ch )) {
      return getIdToken();
    }

    if( Character.isDigit( ch )) {
      return getDigitToken();
    }

    // At this point the only tokens to check for are one or two
    // characters; we must also check for comments that begin with
    // 2 slashes
    String charOld = "" + ch;
    String operator = charOld;
    Symbol sym;
    try {
      endPosition++;
      ch = source.read();
      operator += ch;

      // check if valid 2 char operator; if it's not in the symbol
      // table then don't insert it since we really have a one char
      // token
      sym = Symbol.symbol( operator, Tokens.BogusToken );
      if (sym == null) {
        // it must be a one char token
        return makeToken( charOld );
      }

      endPosition++;
      ch = source.read();

      return makeToken( operator );
    } catch( Exception e ) { /* no-op */ }

    atEOF = true;
    if( startPosition == endPosition ) {
      operator = charOld;
    }

    return makeToken( operator );
  }

  private Token getIdToken() {
    // return tokens for ids and reserved words
    String id = "";

    try {
      do {
        endPosition++;
        id += ch;
        ch = source.read();
      } while( Character.isJavaIdentifierPart( ch ));
    } catch( Exception e ) {
      atEOF = true;
    }

    return newIdToken( id );
  }

  /**
   * Handle and return the Integer/Date/Number token.
   *
   * @return token of types found.
   */
  private Token getDigitToken() {
    // Set default value to Integer
    String token = "";
    Tokens kind = Tokens.INTeger;

    try {
      token += readInteger();
    } catch (Exception e) {
      atEOF = true;
    }

    try {
      // Handle the case of Number or Date
      if ('.' == ch || '~' == ch) {
        // Check First Date Part
        if (Integer.parseInt(token) > 12) {
          printInvalidTokenContext(token);
          atEOF = true;

          throw new IllegalArgumentException("Invalid date format " + token);
        }

        endPosition++;
        token += ch;
        ch = source.read();
        token += readInteger();

        if (isNumberLit(token)) { // Number case
          kind = Tokens.NumberLit;
        } else if ('~' == ch) {  // Date case
          // Check second date part
          String datePart2 = token.substring(token.lastIndexOf("~") + 1);
          if (datePart2.length() > 2) {
            printInvalidTokenContext(datePart2);

            atEOF = true;

            throw new IllegalArgumentException("Invalid date format " + token);
          }

          token += ch;
          ch = source.read();
          token += readInteger();

          // Check third date part
          String datePart3 = token.substring(token.lastIndexOf("~") + 1);
          if (datePart3.length() != 2 && datePart3.length() != 4) {
            printInvalidTokenContext(datePart3);

            atEOF = true;
            throw new IllegalArgumentException("Invalid date format " + token);
          }

          if (isDateLit(token)) {
            kind = Tokens.DateLit;
          } else {
            printInvalidTokenContext(token);

            atEOF = true;
            return nextToken();
          }

        }
      }
    } catch (NumberFormatException nfe) {
      throw nfe;
    } catch (IllegalArgumentException iae) {
      throw iae;
    } catch (Exception e) {
      atEOF = true;
    }

    return newNumberToken( token, kind );
  }

  private void printInvalidTokenContext(String token) {
    System.out.printf("******** illegal character: %s left: %d right: %d line: %d current error line: %s%n",
            token,
            startPosition,
            endPosition,
            lineNumber,
            source.getLine());
  }


  /**
   * Read and the return the integer value.
   * The criteria to call the method is the current character is digit.
   *
   * @return integer value.
   * @throws IOException if failed to read the source.
   */
  private String readInteger() throws IOException {
    String number = "";
    do {
      endPosition++;
      number += ch;
      ch = source.read();
    } while (Character.isDigit(ch));

    try {
      Integer.parseInt(number);
    } catch(NumberFormatException nfe) {
      printInvalidTokenContext(number);

      throw nfe;
    }

    return number;
  }

  private boolean isNumberLit(String number) {
    return number.matches("\\d+\\.\\d+");
  }

  private boolean isDateLit(String date) {
    return date.matches("(\\d\\d?)~(\\d\\d?)~(\\d\\d?)")||
            date.matches("(\\d\\d?)~(\\d\\d?)~(\\d\\d\\d\\d)");
  }

  public static void main(String[] args) {

    if (args.length == 0){
      System.out.println("usage: java lexer.Lexer filename.x");
      return;
    }

    Token token;

    try {
      Lexer lex = new Lexer(args[0]);

      while (true) {
        token = lex.nextToken();

        if (token == null) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}