/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] list;
    private int size;

    public RandomizedQueue() {
        this.list = (Item[]) new Object[1];
        this.size = 0;
    }

    public boolean isEmpty() {
        return (this.size == 0);
    }

    public int size() {
        return this.size;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.size; i++) {
            copy[i] = this.list[i];
        }
        this.list = copy;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (this.size == list.length) {
            this.resize(2 * list.length);
        }
        this.list[this.size++] = item;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        int position = StdRandom.uniform(this.size);
        Item item = this.list[position];
        this.list[position] = this.list[this.size - 1];
        this.list[--this.size] = null;

        if (this.size > 0 && this.size == this.list.length / 4) {
            this.resize(this.list.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        return this.list[StdRandom.uniform(this.size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int copySize;
        private Item[] copyList;

        public RandomizedQueueIterator() {
            copySize = size;
            copyList = (Item[]) new Object[copySize];
            for (int i = 0; i < copySize; i++) {
                copyList[i] = list[i];
            }
        }

        public boolean hasNext() {
            return (copySize > 0);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int pos = StdRandom.uniform(copySize);
            Item item = this.copyList[pos];
            this.copyList[pos] = this.copyList[copySize - 1];
            this.copyList[--copySize] = null;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> que = new RandomizedQueue<Integer>();

        for (int i = 0; i < 5; i++) {
            que.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(que.dequeue());
        }

        for (int a : que) {
            for (int b : que) {
                System.out.print(a + "-" + b + " ");
            }
            System.out.println();
        }
    }

}
