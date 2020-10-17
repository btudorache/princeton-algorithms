import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class CircularSuffixArray {
    private SuffixArray[] circularSuffixArray;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.circularSuffixArray = new SuffixArray[s.length()];

        for (int i = 0; i < circularSuffixArray.length; i++) {
            this.circularSuffixArray[i] = new SuffixArray(s, i);
        }
        Arrays.sort(this.circularSuffixArray);
    }

    public int length() {
        return this.circularSuffixArray.length;
    }

    public int index(int i) {
        if (i < 0 || i > this.length() - 1) {
            throw new IllegalArgumentException();
        }
        return this.circularSuffixArray[i].index;
    }

    private class SuffixArray implements Comparable<SuffixArray> {
        private String suffixArray;
        private int index;

        public SuffixArray(String word, int index) {
            this.suffixArray = word;
            this.index = index;
        }

        public int compareTo(SuffixArray that) {
            int i = this.index;
            int j = that.index;
            for (int k = 0; k < this.suffixArray.length(); k++) {
                if (this.suffixArray.charAt(i) != that.suffixArray.charAt(j)) {
                    return (int) this.suffixArray.charAt(i) - (int) that.suffixArray.charAt(j);
                }
                if (i == this.suffixArray.length() - 1) {
                    i = 0;
                }
                else {
                    i++;
                }
                if (j == that.suffixArray.length() - 1) {
                    j = 0;
                }
                else {
                    j++;
                }
            }
            return 0;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = this.index; i < this.suffixArray.length(); i++) {
                builder.append(this.suffixArray.charAt(i));
            }
            for (int i = 0; i < this.index; i++) {
                builder.append(this.suffixArray.charAt(i));
            }
            return builder.toString();
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String suffix = in.readString();
        CircularSuffixArray csa = new CircularSuffixArray(suffix);

        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i));
        }
    }
}
