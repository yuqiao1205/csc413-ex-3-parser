package ast;

import visitor.*;

public class DoloopTree extends AST {

    public DoloopTree() {
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitDoloopTree(this);
    }
}

