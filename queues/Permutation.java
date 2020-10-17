/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> seq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            seq.enqueue(StdIn.readString());
        }
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            System.out.println(seq.dequeue());
        }
    }
}
