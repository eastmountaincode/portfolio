package wordHashmap;

import static java.util.Objects.isNull;

public class LinkedList {
    private LLNode head;

    public LinkedList(LLNode head) {
        this.head = head;
    }

    public void add(LLNode nodeToAdd) {
        if (isNull(this.head)) {
            this.head = nodeToAdd;
        }
        else {
            LLNode cursor = this.head;
            while (!isNull(cursor.getNext())) {
                cursor = cursor.getNext();
            }
            cursor.setNext(nodeToAdd);
            nodeToAdd.setPrev(cursor);
        }
    }

    public void delete(String key) throws IllegalStateException {
        if (isNull(this.head)) {
            throw new IllegalStateException("error: the list is empty");
        }
        else if (isNull(this.head.getNext())) {
            this.head = null;
            return;
        }
        // if we get here, we know there are at least two elements in the list
        // also, we are guaranteed to get cursor to the node where cursor's
        // wordOccurence's key is equal to key because we only call delete
        // if FIND returns true
        else {
            LLNode cursor = this.head;
            LLNode scout = this.head.getNext();
            while (!cursor.getWordOccurrence().getKey().equals(key)) {
                cursor = scout;
                scout = scout.getNext();
            }
            if (cursor.equals(this.head)) {
                this.head = scout;
            }
            if (!isNull(cursor.getPrev())) {
                cursor.getPrev().setNext(scout);
            }
            if (!isNull(scout)) {
                scout.setPrev(cursor.getPrev());
            }


        }

    }

    public LLNode getHead() {
        return head;
    }

    public void setHead(LLNode head) {
        this.head = head;
    }

    @Override
    public String toString() {
        String returnString = "";
        if (isNull(this.head)) {
            return "";
        }
        else {
            LLNode cursor = this.head;
            while(!isNull(cursor)) {
                returnString += cursor.getWordOccurrence().getKey() + " "
                        + cursor.getWordOccurrence().getWordCount() + ", ";
                cursor = cursor.getNext();
            }
            return returnString;
        }
    }

    public int getLength() {
        if (isNull(this.head)) {
            return 0;
        }
        else if (isNull(this.head.getNext())) {
            return 1;
        }
        else {
            int sum = 0;
            LLNode cursor = this.head;
            while(!isNull(cursor)) {
                sum += 1;
                cursor = cursor.getNext();
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        LinkedList l1 = new LinkedList(null);
        LLNode n1 = new LLNode(new WordOccurrence("ahoy"));
        LLNode n2 = new LLNode( new WordOccurrence("me hearties"));
        l1.add(n1);
        l1.add(n2);
        System.out.println(l1.toString());
        l1.delete("ahoy");
        System.out.println(l1.toString());
        l1.delete("me hearties");
        System.out.println(l1.toString());
        n1 = new LLNode(new WordOccurrence("ahoy"));
        n2 = new LLNode( new WordOccurrence("me hearties"));
        l1.add(n2);
        l1.add(n1);
        System.out.println(l1.toString());


    }
}
