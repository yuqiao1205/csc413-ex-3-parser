package visitor;

import ast.AST;

import java.util.LinkedHashMap;
import java.util.Map;

public class OffsetVisitor extends ASTVisitor {

    private final int[] nextAvailableOffset = new int[100];

    private final Map<AST, Position> layout = new LinkedHashMap<>();

    private int depth = 0;

    public Map<AST, Position> getLayout() {
        return layout;
    }

    public void calculateOffset(AST ast) {

        if (ast == null) {
            return;
        }

        depth++;
        visitKids(ast);
        depth--;

        // Set the offset for root or leaf
        if (ast.getKids().size() > 0) {
            AST firstKid = ast.getKids().get(0);
            AST lastKid = ast.getKids().get(ast.getKids().size() - 1);
            Position firstKidPosition = layout.get(firstKid);
            Position lastKidPosition = layout.get(lastKid);

            int desiredOffset = (firstKidPosition.getOffset() + lastKidPosition.getOffset()) / 2;

            // Set the position first and adjust later
            layout.put(ast, new Position(desiredOffset, depth));

            if (desiredOffset < nextAvailableOffset[depth]) {
                int additionalOffset = nextAvailableOffset[depth] - desiredOffset;
                adjustOffset(ast, additionalOffset);
                // No need to adjust the next available offset as it is already in the adjustOffset method
            } else {
                nextAvailableOffset[depth] = desiredOffset + 2;
            }
        } else {
            // Leaf node case
            layout.put(ast, new Position(nextAvailableOffset[depth], depth));

            // Advance the next available offset
            nextAvailableOffset[depth] += 2;
        }

    }

    /**
     * Recursively adjust the offset of the AST by additional offset and set next available offset array.
     *
     * @param ast              root node to adjust the offset,
     * @param additionalOffset additional offset.
     */
    public void adjustOffset(AST ast, int additionalOffset) {

        // Traverse kids
        depth++;
        for (int i = 0; i < ast.getKids().size(); i++) {
            adjustOffset(ast.getKids().get(i), additionalOffset);
        }
        depth--;

        // Adjust the offset
        Position position = layout.get(ast);

        position.addOffset(additionalOffset);
        int adjustedOffset = position.getOffset();

        // Adjust next available offset
        if (nextAvailableOffset[depth] < (adjustedOffset + 2)) {
            nextAvailableOffset[depth] = adjustedOffset + 2;
        }
    }

    @Override
    public Object visitProgramTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitBlockTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitFunctionDeclTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitCallTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitDeclTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitIntTypeTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitNumberTypeTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitDateTypeTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitBoolTypeTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitFormalsTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitActualArgsTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitIfTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitWhileTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitDoloopTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitReturnTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitAssignTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitIntTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitNumberTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitDateTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitIdTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitRelOpTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitAddOpTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitMultOpTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitListTree(AST t) {
        calculateOffset(t);
        return null;
    }

    @Override
    public Object visitForTree(AST t) {
        calculateOffset(t);
        return null;
    }
}



