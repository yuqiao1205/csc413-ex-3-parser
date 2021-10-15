package ast;

import visitor.ASTVisitor;

    public class DateTypeTree extends AST {

        public DateTypeTree() {
        }
        @Override
        public Object accept(ASTVisitor v) {
            return v.visitDateTypeTree(this);
        }

    }


