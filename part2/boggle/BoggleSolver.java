import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {
    private Trie dictionary;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String s : dictionary) {
            this.dictionary.add(s);
        }
    }

    private void findWords(TreeSet<String> words, int n, int m, boolean[][] visited, String word,
                           BoggleBoard board) {

        visited[n][m] = true;
        if (board.getLetter(n, m) == 'Q') {
            word = word + "QU";
        }
        else {
            word = word + board.getLetter(n, m);
        }

        if (word.length() >= 3 && this.dictionary.contains(word)) {
            words.add(word);
        }

        if (word.length() >= 3 && !this.dictionary.hasKeysWithPrefix(word)) {
            visited[n][m] = false;
            if (board.getLetter(n, m) == 'Q') {
                word = "" + word.substring(0, word.length() - 2);
            }
            else {
                word = "" + word.substring(0, word.length() - 1);
            }
            return;
        }

        for (int i = n - 1; i <= n + 1; i++) {
            for (int j = m - 1; j <= m + 1; j++) {
                if (i >= 0 && j >= 0 && i < board.rows() && j < board.cols()
                        && !visited[i][j]) {
                    findWords(words, i, j, visited, word, board);
                }
            }
        }

        visited[n][m] = false;
        if (board.getLetter(n, m) == 'Q') {
            word = "" + word.substring(0, word.length() - 2);
        }
        else {
            word = "" + word.substring(0, word.length() - 1);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> words = new TreeSet<String>();
        int n = board.rows();
        int m = board.cols();

        boolean[][] visited = new boolean[n][m];
        String word = "";

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                findWords(words, i, j, visited, word, board);
            }
        }
        return words;
    }

    public int scoreOf(String word) {
        if (this.dictionary.contains(word)) {
            if (word.length() < 3) {
                return 0;
            }
            else if (word.length() == 3 || word.length() == 4) {
                return 1;
            }
            else if (word.length() == 5) {
                return 2;
            }
            else if (word.length() == 6) {
                return 3;
            }
            else if (word.length() == 7) {
                return 5;
            }
            else {
                return 11;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
