import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        BinomialHeap b1 = new BinomialHeap();
        int[] randomIntsArray = {7, 2, 4, 17, 1, 11, 6, 8, 15, 10, 20, 5};
        for (int i: randomIntsArray) {
            System.out.println("inserting " + i);
            b1 = b1.insert(b1, new BHNode(i));
            BinomialHeap.printAllHeaps(b1);
            System.out.println();
        }

//        System.out.println("extracting min");
//        ArrayList returnList = BinomialHeap.ExtractMin(b1);
//        b1 = (BinomialHeap) returnList.get(0);
//        System.out.println("Extracted min was " + returnList.get(1));
//        System.out.println("Printing after extraction:");
//        BinomialHeap.printAllHeaps(b1);
//        System.out.println();
//
//        b1 = b1.Delete(b1, b1.getHead().getSibling().getSibling().getChild().getSibling().getChild());
//        System.out.println("printing after delete");
//        BinomialHeap.printAllHeaps(b1);
//        System.out.println();

    }
}
