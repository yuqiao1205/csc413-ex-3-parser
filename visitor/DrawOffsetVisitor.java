package visitor;

import ast.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 *
 * @author Lowell Milliken
 */
public class DrawOffsetVisitor extends ASTVisitor {

    private final int nodew = 80;
    private final int nodeh = 30;
    private final int vertSep = 50;
    private final int horizSep = 10;
    private final int hstep;
    private final int vstep;

    private int width;
    private int height;

    private BufferedImage bimg;
    private Graphics2D g2;
    private Map<AST, Position> offset;



    public DrawOffsetVisitor(int[] nCount, Map<AST, Position> offset) {
        this.offset = offset;
        this.hstep = nodew + horizSep;
        this.vstep = nodeh + vertSep;

        width = (getMaxOffset(offset) + 1) * this.hstep;
        height = nCount.length * this.vstep;

        g2 = createGraphics2D(width, height);
    }

    private int getMaxOffset(Map<AST, Position> offset) {
        int maxOffset = 0;
        for (Object key : offset.keySet()) {
            int offsets = offset.get(key).getOffset();
            if (offsets > maxOffset) {
                maxOffset = offsets;
            }
        }
        return maxOffset;
    }

    public void draw(String s, AST t) {
        Position position = offset.get(t);

        int x = position.getOffset() * hstep + horizSep/2;
        int y = position.getDepth() * vstep + vertSep/2;

        g2.setColor(Color.BLACK);
        g2.setPaint(new Color(160, 202, 252));
        g2.fillOval(x, y, nodew, nodeh);
        g2.setColor(Color.BLACK);
        g2.drawString(s, x + 10, y + 2 * nodeh / 3);

        int startx = x + nodew / 2;
        int starty = y + nodeh;
        int endx;
        int endy;
        g2.setColor(Color.BLACK);

        for (int i = 0; i < t.kidCount(); i++) {
            Position kidPosition = offset.get(t.getKids().get(i));
            endx = kidPosition.getOffset() * hstep + horizSep/2 + nodew / 2;
            endy = kidPosition.getDepth() * vstep + vertSep/2;
            g2.drawLine(startx, starty, endx, endy);
        }

        visitKids(t);
    }

    private Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2;

        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        }

        g2 = bimg.createGraphics();
        g2.setBackground(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    public BufferedImage getImage() {
        return bimg;
    }

    public Object visitProgramTree(AST t) {
        draw("Program", t);
        return null;
    }

    public Object visitBlockTree(AST t) {
        draw("Block", t);
        return null;
    }

    public Object visitFunctionDeclTree(AST t) {
        draw("FunctionDecl", t);
        return null;
    }

    public Object visitCallTree(AST t) {
        draw("Call", t);
        return null;
    }

    public Object visitDeclTree(AST t) {
        draw("Decl", t);
        return null;
    }

    public Object visitIntTypeTree(AST t) {
        draw("IntType", t);
        return null;
    }

    @Override
    public Object visitNumberTypeTree(AST t) {
        draw("NumberType", t);
        return null;
    }

    @Override
    public Object visitDateTypeTree(AST t) {
        draw("DateType", t);
        return null;
    }

    public Object visitBoolTypeTree(AST t) {
        draw("BoolType", t);
        return null;
    }

    public Object visitFloatTypeTree(AST t) {
        draw("FloatType", t);
        return null;
    }

    public Object visitVoidTypeTree(AST t) {
        draw("VoidType", t);
        return null;
    }

    public Object visitFormalsTree(AST t) {
        draw("Formals", t);
        return null;
    }

    public Object visitActualArgsTree(AST t) {
        draw("ActualArgs", t);
        return null;
    }

    public Object visitIfTree(AST t) {
        draw("If", t);
        return null;
    }

    public Object visitUnlessTree(AST t) {
        draw("Unless", t);
        return null;
    }

    public Object visitWhileTree(AST t) {
        draw("While", t);
        return null;
    }

    @Override
    public Object visitDoloopTree(AST t) {
        draw("Doloop", t);
        return null;
    }

    public Object visitForTree(AST t) {
        draw("For", t);
        return null;
    }

    public Object visitReturnTree(AST t) {
        draw("Return", t);
        return null;
    }

    public Object visitAssignTree(AST t) {
        draw("Assign", t);
        return null;
    }

    public Object visitIntTree(AST t) {
        draw("Int: " + ((IntTree) t).getSymbol().toString(), t);
        return null;
    }

    @Override
    public Object visitNumberTree(AST t) {
        draw("Number: " + ((NumberTree) t).getSymbol().toString(), t);
        return null;
    }

    @Override
    public Object visitDateTree(AST t) {
        draw("Date: " + ((DateTree) t).getSymbol().toString(), t);
        return null;
    }

    public Object visitIdTree(AST t) {
        draw("Id: " + ((IdTree) t).getSymbol().toString(), t);
        return null;
    }

    public Object visitRelOpTree(AST t) {
        draw("RelOp: " + ((RelOpTree) t).getSymbol().toString(), t);
        return null;
    }

    public Object visitAddOpTree(AST t) {
        draw("AddOp: " + ((AddOpTree) t).getSymbol().toString(), t);
        return null;
    }

    public Object visitMultOpTree(AST t) {
        draw("MultOp: " + ((MultOpTree) t).getSymbol().toString(), t);
        return null;
    }

    @Override
    public Object visitListTree(AST t) {
        draw("List", t);
        return null;
    }

    public Object visitStringTypeTree(AST t) {
        draw("StringType", t);
        return null;
    }

    public Object visitCharTypeTree(AST t) {
        draw("CharType", t);
        return null;
    }

    public Object visitSwitchTree(AST t) {
        draw("Switch", t);
        return null;
    }

    public Object visitSwitchBlockTree(AST t) {
        draw("SwitchBlock", t);
        return null;
    }

    public Object visitCaseTree(AST t) {
        draw("Case", t);
        return null;
    }

    public Object visitDefaultTree(AST t) {
        draw("Default", t);
        return null;
    }
}
