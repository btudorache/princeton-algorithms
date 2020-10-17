import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    public static void encode() {
        char[] orderedLetters = new char[256];
        for (int i = 0; i < 256; i++) {
            orderedLetters[i] = (char) i;
        }

        int pos = 0;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i < 256; i++) {
                if (c == orderedLetters[i]) {
                    BinaryStdOut.write((char) i);
                    pos = i;
                    break;
                }
            }
            for (int i = pos; i >= 1; i--) {
                orderedLetters[i] = orderedLetters[i - 1];
            }
            orderedLetters[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] orderedLetters = new char[256];
        for (int i = 0; i < 256; i++) {
            orderedLetters[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char cpy = orderedLetters[c];
            BinaryStdOut.write(orderedLetters[c]);
            for (int i = c; i >= 1; i--) {
                orderedLetters[i] = orderedLetters[i - 1];
            }
            orderedLetters[0] = cpy;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        }
        else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
