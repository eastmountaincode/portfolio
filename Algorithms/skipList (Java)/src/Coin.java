import java.util.Random;

public class Coin {
    public static int flip() {
        Random rand = new Random();
        int outcome = rand.nextInt(2);
        return outcome;
    }
}


