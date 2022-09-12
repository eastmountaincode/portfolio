import java.util.ArrayList;

import static java.util.Objects.isNull;

public class BinomialHeap {
    private BHNode head;

    public BinomialHeap(){
        this.head = null;
    }

    public BHNode getHead() {
        return this.head;
    }
    public void setHead(BHNode newHead) {
        this.head = newHead;
    }

    public static BinomialHeap MakeHeap() {
        return new BinomialHeap();
    }

    /**
     * Return a pointer to the node with the lowest key.
     * @return the lowest BHNode
     */
    public static BHNode Minimum(BinomialHeap H) {
        BHNode y = null;
        BHNode x = H.getHead();
        int min = Integer.MAX_VALUE;
        while (!isNull(x)) {
            if (x.getKey() < min) {
                min = x.getKey();
                y = x;
            }
            x = x.getSibling();
        }
        return y;
    }

    // "Makes node y the new head of the linked list of node z's children"
    public static void BinomialLink(BHNode y, BHNode z) {
        y.setP(z);
        y.setSibling(z.getChild());
        z.setChild(y);
        z.setDegree(z.getDegree() + 1);
    }

    // Merge the root lists of binomial heaps H1 and H2 into a single linked list H that is sorted
    // by degree in monotonically increasing order
    public static BHNode BinomialHeapMerge(BinomialHeap h1, BinomialHeap h2) {
        BHNode h1Cursor = h1.getHead();
        BHNode h2Cursor = h2.getHead();
        //System.out.println("h1 cursor is " + h1Cursor);
        //System.out.println("h2 cursor is " + h2Cursor);
        BHNode newHead = null;
        // First, find the node that will be our new head.
        // If both are null...
        if (isNull(h2Cursor) && isNull(h1Cursor)) {
            return null;
        }
        // If one of the cursors is null...
        if (isNull(h2Cursor) && !isNull(h1Cursor)) {
            newHead = h1Cursor;
            h1Cursor = h1Cursor.getSibling();
        }
        else if (!isNull(h2Cursor) && isNull(h1Cursor)) {
            newHead = h2Cursor;
            h2Cursor = h2Cursor.getSibling();
        }
        // If neither cursor is null
        if (!isNull(h1Cursor) && !isNull(h2Cursor)) {
            if (h1Cursor.getDegree() < h2Cursor.getDegree()) {
                newHead = h1Cursor;
                h1Cursor = h1Cursor.getSibling();
            }
            else {
                newHead = h2Cursor;
                h2Cursor = h2Cursor.getSibling();

            }
        }
        // Now, the "sorting" process
        BHNode headCursor = newHead;
        while (!isNull(h1Cursor) && !isNull(h2Cursor)) {
            if (h1Cursor.getDegree() < h2Cursor.getDegree()) {

                headCursor.setSibling(h1Cursor);
                h1Cursor = h1Cursor.getSibling();
                headCursor = headCursor.getSibling();
            }
            else {
                headCursor.setSibling(h2Cursor);
                h2Cursor = h2Cursor.getSibling();
                headCursor = headCursor.getSibling();
            }
        }
        // Get remaining elements from ONE of the heaps, h1 or h2...
        if (!isNull(h1Cursor)) {
            headCursor.setSibling(h1Cursor);

        }
        else if (!isNull(h2Cursor)) {
            headCursor.setSibling(h2Cursor);
        }
        //System.out.println("newHead is " + newHead);
        return newHead;
    }

    public static BinomialHeap BinomialHeapUnion(BinomialHeap h1, BinomialHeap h2) {
        BinomialHeap newHeap = MakeHeap();
        newHeap.setHead(BinomialHeapMerge(h1, h2));
        if (isNull(newHeap.getHead())) {
            return newHeap;
        }
        BHNode prevX = null;
        BHNode x = newHeap.getHead();
        BHNode nextX = x.getSibling();
        while (!isNull(nextX)) {
            if (x.getDegree() != nextX.getDegree() ||
                    ( !isNull(nextX.getSibling()) && nextX.getSibling().getDegree() == x.getDegree() )) {
                prevX = x;
                x = nextX;
            }
            else {
                if (x.getKey() <= nextX.getKey()) {
                    x.setSibling(nextX.getSibling());
                    BinomialLink(nextX, x);
                }
                else {
                    if (isNull(prevX)) {
                        newHeap.setHead(nextX);
                    }
                    else {
                        prevX.setSibling(nextX);
                    }
                    BinomialLink(x, nextX);
                    x = nextX;
                }
            }
            nextX = x.getSibling();
        }
        return newHeap;
    }

    public BinomialHeap insert(BinomialHeap H, BHNode x) {
        BinomialHeap newHeap = MakeHeap();
        x.setP(null);
        x.setChild(null);
        x.setSibling(null);
        x.setDegree(0);
        newHeap.setHead(x);
        //System.out.println("x is " + x);
        H = BinomialHeapUnion(H, newHeap);
        //System.out.println("after insert, H's head is " + H.getHead());
        return H;

    }

    public static ArrayList ExtractMin(BinomialHeap H) {
        // #### Find the root x with the minimum key in the root list of H... ####
        BHNode x = Minimum(H);
        // #### Remove x from the root list of H ####
        BHNode cursor = H.getHead();
        // If minimum is the first item in the root list...
        if (cursor.equals(x)) {
            if (!isNull(cursor.getSibling())) {
                H.setHead(cursor.getSibling());
            }
            else {
                H.setHead(null);
            }
        }
        // If there are only two items in the root list and the minimum is the second
        else if (!isNull(cursor.getSibling()) && isNull(cursor.getSibling().getSibling())) {
            System.out.println("only two items, and minimum is " + cursor.getSibling());
            cursor.setSibling(null);
        }
        else {
            cursor = cursor.getSibling();
            BHNode prevCursor = H.getHead();
            while (!cursor.equals(x)) {
                cursor = cursor.getSibling();
                prevCursor = prevCursor.getSibling();
            }
            // now cursor is minimum node and prevCursor is the node before it
            prevCursor.setSibling(cursor.getSibling());
        }
        // #### Done removing x from H's root list ####
        BinomialHeap newHeap = MakeHeap();
        // "Reverse the order of the linked list of x's children"
        BHNode dummyNode = new BHNode(-9999999);
        BHNode newNodeToAdd;
        cursor = x.getChild();
        while (!isNull(cursor)) {
            newNodeToAdd = new BHNode(cursor.getKey());
            newNodeToAdd.setChild(cursor.getChild());
            newNodeToAdd.setDegree(cursor.getDegree());
            if (!isNull(dummyNode.getSibling())) {
                newNodeToAdd.setSibling(dummyNode.getSibling());
                dummyNode.setSibling(newNodeToAdd);
            }
            else {
                dummyNode.setSibling(newNodeToAdd);
            }
            cursor = cursor.getSibling();
        }
        x.setChild(dummyNode.getSibling());
        // "Set head of newHeap to the head of the resulting list"
        newHeap.setHead(x.getChild());

        H = BinomialHeapUnion(H, newHeap);

        ArrayList returnList = new ArrayList<>();
        returnList.add(H);
        returnList.add(x);

        return returnList;

    }


    public static BinomialHeap BinomialHeapDecreaseKey(BinomialHeap H, BHNode x, int k) {
        if (k > x.getKey()) {
            throw new IllegalArgumentException("new key is greater than current key");
        }
        x.setKey(k);
        BHNode y = x;
        BHNode z = y.getP();
        int tempValue;
        while (!isNull(z) && y.getKey() < z.getKey()) {
            // swap y and z
            tempValue = y.getKey();
            y.setKey(z.getKey());
            z.setKey(tempValue);
            y = z;
            z = y.getP();
        }
        return H;
    }

    public BinomialHeap Delete(BinomialHeap H, BHNode x) {
        BinomialHeap b1 = BinomialHeapDecreaseKey(H, x, Integer.MIN_VALUE);
        b1 = (BinomialHeap) ExtractMin(b1).get(0);
        return b1;
    }

    public Boolean isRootListEmpty() {
        if (isNull(this.head)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void printHeap(BHNode node) {
        if (isNull(node)) {
            return;
        }
        if (!isNull(node.getChild())) {
            BHNode cursor = node.getChild();
            while (!isNull(cursor)) {
                System.out.println(cursor.getKey() + ", child of " + node.getKey());
                printHeap(cursor);
                cursor = cursor.getSibling();
            }

        }
    }

    public static void printAllHeaps(BinomialHeap H) {
        System.out.println("######### PRINTING ALL TREES #########");
        BHNode cursor = H.getHead();
        String childKey;
        String siblingKey;
        String prevKey;

        //System.out.println("cursor: " + cursor);

        while (!isNull(cursor)) {
            if (!isNull(cursor.getChild())) {
                childKey = String.valueOf(cursor.getChild().getKey());
            }
            else {
                childKey = null;
            }
            if (!isNull(cursor.getSibling())) {
                siblingKey = String.valueOf(cursor.getSibling().getKey());
            }
            else {
                siblingKey = null;
            }
            if (!isNull(cursor.getPrevious())) {
                prevKey = String.valueOf(cursor.getPrevious().getKey());
            }
            else {
                prevKey = null;
            }

            System.out.println("HEAD: " + cursor.getKey() + " DEGREE: " + cursor.getDegree() + " CHILD: " +
                    childKey + " SIBLING: " + siblingKey + " PREV: " + prevKey);
            printHeap(cursor);
            cursor = cursor.getSibling();
        }
    }

}
