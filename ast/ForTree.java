package ast;

import visitor.*;

public class ForTree extends AST {

    public ForTree() {
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitForTree(this);
    }

}

