package wordHashmap;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;

public class WordHashmap implements IWordHashmap{
    private int numBuckets;
    private ArrayList<LinkedList> buckets;
    public WordHashmap(int numBuckets){
        this.numBuckets = numBuckets;
        this.buckets = new ArrayList<LinkedList>();
        for (int i = 0; i < this.numBuckets; i++) {
            this.buckets.add(new LinkedList(null));
        }
    }

    @Override
    public void insert(String key) {
        int t = hashFunction(key, this.numBuckets);
        LinkedList l1 = this.buckets.get(t);
        WordOccurrence found = this.find(key);
        if (!isNull(found)) {
            this.increase(key);
        }
        else {
            this.buckets.get(t).add(new LLNode(new WordOccurrence(key)));
        }
    }

    @Override
    public void delete(String key) throws IllegalArgumentException {
        if (isNull(this.find(key))) {
            throw new IllegalArgumentException("key not found");
        }
        else {
            int t = hashFunction(key, this.numBuckets);
            LinkedList l1 = this.buckets.get(t);
            l1.delete(key);
        }


    }

    public WordOccurrence find(String key) {
        int t = hashFunction(key, this.numBuckets);
        LinkedList l1 = this.buckets.get(t);
        LLNode cursor = l1.getHead();
        while(!isNull(cursor)) {
            if (cursor.getWordOccurrence().getKey().equals(key)) {
                break;
            }
            cursor = cursor.getNext();
        }
        if (isNull(cursor)) {
            return null;
        }
        else {
            return cursor.getWordOccurrence();
        }
    }

    @Override
    public void increase(String key) {
        if (isNull(this.find(key))) {
            return;
        }
        int t = hashFunction(key, this.numBuckets);
        LinkedList l1 = this.buckets.get(t);

        LLNode cursor = l1.getHead();
        while(!isNull(cursor)) {
            if (cursor.getWordOccurrence().getKey().equals(key)) {
                break;
            }
            cursor = cursor.getNext();
        }
        cursor.getWordOccurrence().increaseByOne();
    }

    @Override
    public List<String> listAllKeys() {
        List<String> returnList = new ArrayList<>();

        for (int i = 0; i < this.numBuckets; i++) {
            LinkedList l1 = this.buckets.get(i);
            LLNode cursor = l1.getHead();
            while(!isNull(cursor)) {
                returnList.add(cursor.getWordOccurrence().getKey() + " " + cursor.getWordOccurrence().getWordCount());
                cursor = cursor.getNext();
            }
        }
        return returnList;
    }

    @Override
    public int hashFunction(String stringToHash, int M) {
        double A = (Math.sqrt(5) - 1)/2;
        int k = getASCIISum(stringToHash);
        double hash = Math.floor(M * ((k * A) % 1));
        return (int) hash;
    }

    /**
     * Get the sum of the ASCII value of each character in the string.
     * Additionally, each character's ASCII value is multiplied by
     * (its index in the string plus 1) to the power of 2.
     *
     * @param str the string
     * @return the sum
     */
    public int getASCIISum(String str) {
        int sum = 0;
        for(int i = 0; i<str.length(); i++) {
            int val = str.charAt(i);
            val = (int) Math.floor(val * Math.pow(i + 1, 2));
            sum += val;
        }
        return sum;
    }

    public int getNumBuckets() {
        return this.numBuckets;
    }

    public List<LinkedList> getBuckets() {
        return this.buckets;
    }

    public static void main(String[] args) {
        WordHashmap w1 = new WordHashmap(30);
        w1.insert("blog");
        w1.insert("asdfk");
        System.out.println(w1.listAllKeys().toString());
        System.out.println(w1.find("blogd"));
        w1.delete("blog");
        System.out.println(w1.listAllKeys().toString());
        System.out.println(w1.find("blog"));
        w1.insert("blog");
        w1.insert("blog");
        System.out.println(w1.find("blog").toString());
        System.out.println(w1.listAllKeys().toString());
    }
}
