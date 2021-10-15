package compiler;

import ast.*;
import parser.Parser;
import visitor.*;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;


/**
 * The Compiler class contains the main program for compiling a source program
 * to bytecodes
 */
public class Compiler {

  /**
   * The Compiler class reads and compiles a source program
   */

  protected String sourceFile;

  public Compiler(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  private void compileProgram() {
    try {
      Parser parser = new Parser(sourceFile);
      AST ast = parser.execute();

      System.out.println("---------------AST-------------");
      PrintVisitor printVisitor = new PrintVisitor();
      ast.accept(printVisitor);

      OffsetVisitor offsetVisitor = new OffsetVisitor();
      ast.accept(offsetVisitor);

      drawOffsetDiagram(ast);

    } catch (Exception e) {
      System.out.println("******** exception *******" + e);
    }
  }

  private void drawOffsetDiagram(AST ast) {
    CountVisitor ov = new CountVisitor();
    ast.accept(ov);

    OffsetVisitor offsetVisitor = new OffsetVisitor();
    ast.accept(offsetVisitor);

    DrawOffsetVisitor drawOffsetVisitor = new DrawOffsetVisitor(ov.getCount(), offsetVisitor.getLayout());
    ast.accept(drawOffsetVisitor);
    displayImage(drawOffsetVisitor.getImage());
  }

  private void displayImage(BufferedImage image) {
    try {
      File imagefile = new File(sourceFile + ".png");
      ImageIO.write(image, "png", imagefile);
    } catch (Exception e) {
      System.out.println("Error in saving image: " + e.getMessage());
    }

    final JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        f.dispose();
        System.exit(0);
      }
    });
    JLabel imagelabel = new JLabel(new ImageIcon(image));
    f.add("Center", imagelabel);
    f.pack();
    f.setSize(new Dimension(image.getWidth() + 30, image.getHeight() + 40));
    f.setVisible(true);
    f.setResizable(false);
    f.repaint();
  }

  public static void main(String args[]) {
    String filePath = (args[0]);

    try (LineNumberReader lineReader = new LineNumberReader(new FileReader(filePath))){
      String lineText;

      while ((lineText = lineReader.readLine()) != null) {
        System.out.printf( "%3d: %s%n",lineReader.getLineNumber(), lineText);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (args.length == 0) {
      System.out.println("***Incorrect usage, try: java compiler.Compiler <file>");
      System.exit(1);
    }
    (new Compiler(args[0])).compileProgram();
  }
}