package visitor;

public class Position {
    private int offset;
    private int depth;

    public Position(int offset, int depth) {
        this.offset = offset;
        this.depth = depth;
    }

    public void addOffset(int additionalOffset) {
        this.offset += additionalOffset;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getDepth() {
        return this.depth;
    }

    public String toString() {
        return "(" + offset + ", " + depth + ")";
    }
}
