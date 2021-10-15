package ast;

import visitor.*;

public class ListTree extends AST {

    public ListTree() {
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitListTree(this);
    }

}