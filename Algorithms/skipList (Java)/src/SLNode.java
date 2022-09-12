import static java.util.Objects.isNull;

public class SLNode {
    private SLNode next;
    private SLNode prev;

    private SLNode above;
    private SLNode below;
    private int key;


    public SLNode(int key) {
        this.next = null;
        this.prev = null;
        this.key = key;
        this.above = null;
        this.below = null;
    }

    public String toString() {
        String nextKey;
        String prevKey;
        String aboveKey;
        String belowKey;
        String dummyKey;

        if (!isNull(this.getKey())) {
            dummyKey = String.valueOf(this.getKey());
        }
        else {
            dummyKey = "null";
        }

        if (!isNull(this.getNext())) {
            nextKey = String.valueOf(this.next.getKey());
        }
        else {
            nextKey = "null";
        }

        if (!isNull(this.getPrev())) {
            prevKey = String.valueOf(this.prev.getKey());
        }
        else {
            prevKey = "null";
        }

        if (!isNull(this.getAbove())) {
            aboveKey = String.valueOf(this.above.getKey());
        }
        else {
            aboveKey = "null";
        }

        if (!isNull(this.getBelow())) {
            belowKey = String.valueOf(this.below.getKey());
        }
        else {
            belowKey = "null";
        }

        return "KEY: " + dummyKey + " NEXT: " + nextKey + " PREV: " + prevKey + " ABOVE: " +
                aboveKey + " BELOW: " + belowKey;
    }


    //#################### GETTERS AND SETTERS #####################

    public SLNode getNext() {
        return next;
    }

    public void setNext(SLNode next) {
        this.next = next;
    }

    public SLNode getPrev() {
        return prev;
    }

    public void setPrev(SLNode prev) {
        this.prev = prev;
    }

    public SLNode getAbove() {
        return above;
    }

    public void setAbove(SLNode above) {
        this.above = above;
    }

    public SLNode getBelow() {
        return below;
    }

    public void setBelow(SLNode below) {
        this.below = below;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

}
