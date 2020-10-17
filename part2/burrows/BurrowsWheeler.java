import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder builder = new StringBuilder();

        int startingIndex = 0;
        for (int i = 0; i < csa.length(); i++) {
            int lastLetter = csa.index(i) - 1;
            if (csa.index(i) == 0) {
                startingIndex = i;
                lastLetter = csa.length() - 1;
            }
            builder.append(s.charAt(lastLetter));
        }
        BinaryStdOut.write(startingIndex);
        BinaryStdOut.write(builder.toString());
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String lastLetters = BinaryStdIn.readString();
        char[] charFirstLetters = new char[lastLetters.length()];
        int[] next = new int[lastLetters.length()];
        int[] count = new int[256 + 1];
        for (int i = 0; i < lastLetters.length(); i++) {
            count[lastLetters.charAt(i) + 1]++;
        }
        for (int r = 0; r < 256; r++) {
            count[r + 1] += count[r];
        }

        for (int i = 0; i < lastLetters.length(); i++) {
            next[count[lastLetters.charAt(i)]] = i;
            charFirstLetters[count[lastLetters.charAt(i)]] = lastLetters.charAt(i);
            count[lastLetters.charAt(i)]++;
        }

        String firstLetters = new String(charFirstLetters);

        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(firstLetters.charAt(first));
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        }
        else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
