package wordHashmap;

public class LLNode {
    private LLNode next;
    private LLNode prev;
    private WordOccurrence wordOccurrence;

    public LLNode(WordOccurrence wordOccurrence) {
        this.wordOccurrence = wordOccurrence;
        this.next = null;
        this.prev = null;
    }

    public LLNode getNext() {
        return next;
    }

    public void setNext(LLNode next) {
        this.next = next;
    }

    public LLNode getPrev() {
        return prev;
    }

    public void setPrev(LLNode prev) {
        this.prev = prev;
    }

    public WordOccurrence getWordOccurrence() {
        return wordOccurrence;
    }

    public void setWordOccurrence(WordOccurrence wordOccurrence) {
        this.wordOccurrence = wordOccurrence;
    }

}
