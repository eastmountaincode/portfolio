import static java.util.Objects.isNull;

public class BHNode {
    private BHNode p;
    private int key;
    private int degree;
    private BHNode child;
    private BHNode sibling;
    private BHNode previous;

    public BHNode(int key) {
        this.p = null;
        this.key = key;
        this.degree = 0;
        this.child = null;
        this.sibling = null;
        this.previous = null;
    }

    public BHNode getP() {
        return p;
    }

    public void setP(BHNode p) {
        this.p = p;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public BHNode getChild() {
        return child;
    }

    public void setChild(BHNode child) {
        this.child = child;
    }

    public BHNode getSibling() {
        return sibling;
    }

    public void setSibling(BHNode sibling) {
        this.sibling = sibling;
    }

    public BHNode getPrevious() {
        return previous;
    }

    public void setPrevious(BHNode previous) {
        this.previous = previous;
    }

    public String toString() {
        String childKey;
        String siblingKey;
        String prevKey;
        if (!isNull(this.getChild())) {
            childKey = String.valueOf(this.getChild().getKey());
        }
        else {
            childKey = null;
        }
        if (!isNull(this.getSibling())) {
            siblingKey = String.valueOf(this.getSibling().getKey());
        }
        else {
            siblingKey = null;
        }
        if (!isNull(this.getPrevious())) {
            prevKey = String.valueOf(this.getPrevious().getKey());
        }
        else {
            prevKey = null;
        }

        return "HEAD: " + this.getKey() + " DEGREE: " + this.getDegree() + " CHILD: " +
                childKey + " SIBLING: " + siblingKey + " PREV: " + prevKey;

    }

}
