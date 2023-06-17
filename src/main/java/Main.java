import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

        Thread threadInput = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 1000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadInput.start();

        Thread threadOutput1 = new Thread(() -> {
            dissectQueue((ArrayBlockingQueue<String>) queue1, 'a');
        });
        threadOutput1.start();

        Thread threadOutput2 = new Thread(() -> {
            dissectQueue((ArrayBlockingQueue<String>) queue2, 'b');
        });
        threadOutput2.start();

        Thread threadOutput3 = new Thread(() -> {
            dissectQueue((ArrayBlockingQueue<String>) queue3, 'c');
        });
        threadOutput3.start();

        while (threadInput.isAlive() || !queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()) {

        }

        threadOutput1.interrupt();
        threadOutput2.interrupt();
        threadOutput3.interrupt();

    }

    public static void dissectQueue(ArrayBlockingQueue<String> queue, char letter) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
        int max = 0;
        String text = "";
        while (true) {
            try {
                String textV = queue.take();

                int currentMax = countLetters(letter, textV);
                if (currentMax > max) {
                    max = currentMax;
                    text = textV;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("Букв " + letter + " больше всего в строке " + text + ": " + max);
    }

    public static int countLetters(char letter, String text) {
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                counter++;
            }
        }
        return counter;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
