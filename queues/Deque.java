import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = this.first;
        newNode.prev = null;
        if (this.size == 0) {
            this.first = newNode;
            this.last = newNode;
        }
        else {
            this.first.prev = newNode;
            this.first = newNode;
        }
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = null;
        newNode.prev = this.last;
        if (this.size == 0) {
            this.last = newNode;
            this.first = newNode;
        }
        else {
            this.last.next = newNode;
            this.last = newNode;
        }
        this.size++;
    }

    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        Node removedNode = this.first;
        if (this.size() == 1) {
            this.first = null;
            this.last = null;
            this.size--;
        }
        else {
            removedNode.next.prev = null;
            this.first = removedNode.next;
            this.size--;
        }
        return removedNode.item;
    }

    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        Node removedNode = this.last;
        if (this.size() == 1) {
            this.last = null;
            this.first = null;
            this.size--;
        }
        else {
            removedNode.prev.next = null;
            this.last = removedNode.prev;
            this.size--;
        }
        return removedNode.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deq = new Deque<Integer>();

        deq.addFirst(1);
        deq.addLast(2);
        deq.addFirst(3);
        deq.addLast(4);
        deq.addFirst(5);
        deq.addLast(6);
        while (!deq.isEmpty()) {
            System.out.println(deq.removeLast());
            System.out.println(deq.removeFirst());
        }

        for (int i = 0; i < 5; i++) {
            deq.addFirst(i);
        }
        for (int a : deq) {
            for (int b : deq) {
                System.out.print(a + "-" + b + " ");
            }
            System.out.println();
        }
    }
}

