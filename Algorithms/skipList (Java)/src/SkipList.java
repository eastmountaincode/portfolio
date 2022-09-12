import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class SkipList {
    private SLNode head;
    private final int MAXLEVEL = 10;

    public SkipList() {
        // BUILD THE HEAD TOWER
        SLNode cursor = new SLNode(Integer.MIN_VALUE);
        SLNode scout = null;
        for (int i = 0; i < MAXLEVEL - 1; i++) {
            scout = new SLNode(Integer.MIN_VALUE);
            // VERTICAL CONNECTING
            cursor.setAbove(scout);
            scout.setBelow(cursor);
            cursor = scout;
        }

        // BUILD THE TAIL TOWER
        SLNode tailCursor = new SLNode(Integer.MAX_VALUE);
        SLNode tailScout = null;
        for (int i = 0; i < MAXLEVEL - 1; i++) {
            tailScout = new SLNode(Integer.MAX_VALUE);
            // VERTICAL CONNECTING
            tailCursor.setAbove(tailScout);
            tailScout.setBelow(tailCursor);
            tailCursor = tailScout;
        }

        // SET HEAD
        this.head = cursor;

        // CONNECT THE TOWERS HORIZONTALLY
        while (!isNull(cursor)) {
            cursor.setNext(tailCursor);
            tailCursor.setPrev(cursor);
            cursor = cursor.getBelow();
            tailCursor = tailCursor.getBelow();
        }
    }

    public SLNode lookup(int key) {
        SLNode cursor = this.head;
        SLNode backup = null;
        while (!isNull(cursor)) {
            System.out.println("LOG: Comparing " + key + " and " + cursor.getNext().getKey());
            if (key < cursor.getNext().getKey()) {
                System.out.println("LOG: Going down.");
                backup = cursor;
                cursor = cursor.getBelow();
                continue;
            }
            else if (key >= cursor.getNext().getKey()) {
                System.out.println("LOG: Going right.");
                backup = cursor;
                cursor = cursor.getNext();
                continue;
            }
        }
        return backup;
    }

    public void insert(int key) {
        SLNode extraNewNode;
        SLNode foundNode = lookup(key);
        if (foundNode.getKey() == key) {
            foundNode = foundNode.getPrev();
            this.delete(key);
        }
        SLNode newNode = new SLNode(key);
        newNode.setNext(foundNode.getNext());
        newNode.setPrev(foundNode);
        foundNode.getNext().setPrev(newNode);
        foundNode.setNext(newNode);
        while (Coin.flip() == 1) {
            System.out.println("going up");
            extraNewNode = new SLNode(key);
            newNode.setAbove(extraNewNode);
            extraNewNode.setBelow(newNode);
            while (isNull(foundNode.getAbove())) {
                foundNode = foundNode.getPrev();
            }
            foundNode = foundNode.getAbove();

            extraNewNode.setPrev(foundNode);
            extraNewNode.setNext(foundNode.getNext());
            foundNode.getNext().setPrev(extraNewNode);
            foundNode.setNext(extraNewNode);
            newNode = extraNewNode;
        }

    }

    public void delete(int key) {
        SLNode foundNode = lookup(key);
        if (foundNode.getKey() != key) {
            return;
        }
        while (!isNull(foundNode)) {
            foundNode.getPrev().setNext(foundNode.getNext());
            foundNode.getNext().setPrev(foundNode.getPrev());
            foundNode = foundNode.getAbove();
        }
    }


    public String HorizontalPrint() {
        System.out.println("######## PRINTING THE SKIPLIST ########");
        String returnString = "";
        // Get bottom feeder to the bottom
        SLNode bottomFeeder = this.head;
        while(!isNull(bottomFeeder.getBelow())) {
            bottomFeeder = bottomFeeder.getBelow();
        }

        // DELETE THIS TO SHOW LEFT TOWERS
        bottomFeeder = bottomFeeder.getNext();

        // Bottom feeder begins its crawl
        SLNode angel;
        while(!isNull(bottomFeeder)) {
            angel = bottomFeeder;
            // Angel rises up the stack
            while(!isNull(angel)) {
                returnString += angel.getKey() + " ";
                angel = angel.getAbove();
            }
            returnString += "\n";
            bottomFeeder = bottomFeeder.getNext();

            // DELETE THIS TO SHOW RIGHT TOWERS
            if (isNull(bottomFeeder.getNext())) {
                break;
            }
        }
        return returnString;
    }



    public static void main(String[] args) {
        SkipList s1 = new SkipList();
        String response;
        String response2;

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter command");
        response = myObj.nextLine();  // Read user input
        while (!response.equals("q")) {
            if (response.equals("insert")) {
                response2 = myObj.nextLine();
                s1.insert(Integer.parseInt(response2));
            }
            if (response.equals("delete")) {
                response2 = myObj.nextLine();
                s1.delete(Integer.parseInt(response2));
            }
            if (response.equals("lookup")) {
                response2 = myObj.nextLine();
                System.out.println(s1.lookup(Integer.parseInt(response2)));
            }
            System.out.println(s1.HorizontalPrint());
            response = myObj.nextLine();

        }


    }
}
