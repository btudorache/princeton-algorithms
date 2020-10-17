import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {
    private HashMap<String, Bag<Integer>> map;
    private HashMap<Integer, String> synsets;
    private Set<String> words;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        this.map = new HashMap<String, Bag<Integer>>();
        this.synsets = new HashMap<Integer, String>();
        this.words = new HashSet<String>();
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String line = in.readLine();
            int index = Integer.parseInt(line.split(",")[0]);
            String synset = line.split(",")[1];
            String[] wordsArray = synset.split(" ");
            this.synsets.put(index, synset);
            for (String word : wordsArray) {
                this.words.add(word);
                if (this.map.containsKey(word)) {
                    this.map.get(word).add(index);
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(index);
                    this.map.put(word, bag);
                }
            }
        }
        Digraph graph = new Digraph(this.map.size());
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] wordsArray = line.split(",");
            for (int i = 1; i < wordsArray.length; i++) {
                graph.addEdge(Integer.parseInt(wordsArray[0]), Integer.parseInt(wordsArray[i]));
            }
        }
        int root = 0;
        for (int v = 0; v < graph.V(); v++) {
            if (graph.outdegree(v) == 0 && graph.indegree(v) != 0) {
                root++;
            }
        }
        if (root != 1) {
            throw new IllegalArgumentException();
        }

        Topological DAG = new Topological(graph);
        if (!DAG.hasOrder()) {
            throw new IllegalArgumentException();
        }
        this.sap = new SAP(graph);
    }

    public Iterable<String> nouns() {
        return this.words;
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return this.words.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        return this.sap.length(this.map.get(nounA), this.map.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }

        return this.synsets.get(this.sap.ancestor(this.map.get(nounA), this.map.get(nounB)));
    }

    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        System.out.println("distance of worm and bird: " + net.distance("worm", "bird"));
        System.out.println("ancestor of worm and bird: " + net.sap("worm", "bird"));
        System.out.println("ancestor of individual and edible_fruit: " + net
                .sap("individual", "edible_fruit"));
        System.out.println(net.distance("white_marlin", "mileage"));
        System.out.println(net.distance("Black_Plague", "black_marlin"));
        System.out.println(net.distance("American_water_spaniel", "histology"));
        System.out.println(net.distance("Brown_Swiss", "barrel_roll"));

    }
}
