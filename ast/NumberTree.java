package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.ASTVisitor;

public class NumberTree extends AST{
    private Symbol symbol;
    /**
     *  @param tok is the Token containing the String representation of the integer
     *  literal; we keep the String rather than converting to an integer value
     *  so we don't introduce any machine dependencies with respect to integer
     *  representations
     */
    public NumberTree(Token tok) {
        this.symbol = tok.getSymbol();
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitNumberTree(this);
    }

    public Symbol getSymbol() {
        return symbol;
    }

}

