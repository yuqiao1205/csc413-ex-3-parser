package ast;

import visitor.*;

public class NumberTypeTree extends AST {

    public NumberTypeTree() {
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitNumberTypeTree(this);
    }

}

