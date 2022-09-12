package wordHashmap;

public class WordOccurrence {
    private String key;
    private int wordCount;

    public int getWordCount() {
        return wordCount;
    }

    public WordOccurrence(String key) {
         this.key = key;
         this.wordCount = 1;
     }

    public String getKey() {
        return key;
    }

    public void increaseByOne() {
        this.wordCount += 1;
    }

    public String toString() {
        return this.getKey() + " " + this.getWordCount();
    }



}
