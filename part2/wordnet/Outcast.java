import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;

    public Outcast(WordNet wordnet) {
        this.net = wordnet;
    }

    public String outcast(String[] nouns) {
        int max = Integer.MIN_VALUE;
        String ind = "";
        for (String wordi : nouns) {
            int sum = 0;
            for (String wordj : nouns) {
                sum += this.net.distance(wordi, wordj);
            }
            if (sum > max) {
                max = sum;
                ind = wordi;
            }
        }
        return ind;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
