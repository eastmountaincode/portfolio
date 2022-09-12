package driver;

import wordHashmap.LinkedList;
import wordHashmap.WordHashmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

import static java.util.Objects.isNull;

public class aliceMain {

    // SOURCE: https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    // SOURCE: https://www.javacodex.com/Files/Read-File-Word-By-Word
    public static List<String> ReadWords() throws FileNotFoundException {
        List<String> wordList = new ArrayList<>();

        File file = new File("src/driver/alice_in_wonderland.txt");
        Scanner input = new Scanner(file);

        int count = 0;
        while (input.hasNext()) {
            String word = input.next();
            word = removePunctuations(word).toLowerCase();
            if (word.equals(" ") || word.equals("")) {
                continue;
            }
            if (isNumeric(word)) {
                continue;
            }
            wordList.add(word);
            count = count + 1;
        }
        System.out.println("Word count: " + count);
        return wordList;
    }

    // SOURCE: https://www.techiedelight.com/remove-punctuation-from-string-java/
    public static String removePunctuations(String source) {
        return source.replaceAll("\\p{Punct}|\\p{Space}", "");
    }

    public static void produceHistogramTextFile(WordHashmap hashmap) {

        // SOURCE: https://www.w3schools.com/java/java_files_create.asp
        try {
            int numBuckets = hashmap.getNumBuckets();
            FileWriter myWriter = new FileWriter("src/driver/histogram" + numBuckets + ".txt");

            for (int i = 0; i < numBuckets; i++) {
                LinkedList ll = hashmap.getBuckets().get(i);
                int size = ll.getLength();
                myWriter.write(size + " ");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        WordHashmap w1 = new WordHashmap(30);
        List<String> wordList = ReadWords();
        for (int i = 0; i < wordList.size(); i++) {
            w1.insert(wordList.get(i));
        }
        produceHistogramTextFile(w1);
        
        System.out.println(w1.listAllKeys().toString());
        System.out.println(w1.find("alice").toString());
        w1.delete("alice");
        if (isNull(w1.find("alice"))) {
            System.out.println("null value");
        };
        w1.insert("alice");
        System.out.println(w1.listAllKeys().toString());
        System.out.println(w1.find("alice"));
        w1.increase("alice");
        System.out.println(w1.find("alice"));
        w1.delete("alice");
        System.out.println(w1.find("alice"));


//
//        WordHashmap w2 = new WordHashmap(300);
//        List<String> wordList2 = ReadWords();
//        for (int i = 0; i < wordList2.size(); i++) {
//            w2.insert(wordList.get(i));
//        }
//        produceHistogramTextFile(w2);
//
//        WordHashmap w3 = new WordHashmap(1000);
//        List<String> wordList3 = ReadWords();
//        for (int i = 0; i < wordList3.size(); i++) {
//            w3.insert(wordList.get(i));
//        }
//        produceHistogramTextFile(w3);

    }
}
