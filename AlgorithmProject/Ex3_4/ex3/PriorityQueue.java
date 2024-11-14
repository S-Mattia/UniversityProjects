package ex3;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 04/05/2024;
 * @details Program that implement the ADS Min priorty heap;
 */

public class PriorityQueue<E> implements AbstractQueue<E> {

    // Comparator of the <E> element;
    private final Comparator<E> Comparator;

    // Number of Elements in the minHeap;
    private int nItem;

    // Binary three implemented as an array;
    private ArrayList<E> array;

    // HashMap that contain all the element in the minHeap, used to lower the
    // complexity;
    private final HashMap<E, Integer> hash;

    /**
     * DESCRIPTION: Constructor of the class ;
     */
    public PriorityQueue(Comparator<E> e) {
        Comparator = e;
        nItem = 0;
        array = new ArrayList<>();
        hash = new HashMap<>();
    }

    /**
     * DESCRIPTION: Method that return true if the MinHeap is empty, otherwise
     * false;
     */
    public boolean empty() {
        return nItem == 0;
    }

    /**
     * DESCRIPTION: method that insert a new element at the end of the minHeap;
     */
    public boolean push(E e) {
        if (this.contains(e)) {
            return false;
        }
        array.add(nItem, e);
        hash.put(e, nItem);
        shiftUp(nItem);
        nItem++;
        return true;
    }

    /**
     * DESCRIPTION: Method that return the min element in the minHeap;
     */
    public E top() {
        if (empty())
            return null;
        return this.array.get(0);
    }

    /**
     * DESCRIPTION: Method that remove the min element in the minHeap;
     */
    public void pop() {
        E root = array.get(0);
        array.set(0, array.get(--nItem));
        array.remove(nItem);
        hash.remove(root);
        if (!empty()) {
            hash.put(array.get(0), 0);
            shiftDown(0);
        }
    }

    /**
     * DESCRIPTION: Method that return true if the e element in input, is in the
     * minHeap otherwise false;
     */
    public boolean contains(E e) {
        return hash.containsKey(e);
    }

    /**
     * DESCRIPTION: Sub-method of Push() that set the last elment pushed in the
     * correct position of priority;
     */
    private void shiftUp(int k) {
        while (k > 0) {
            int parent = (k - 1) / 2;
            if (Comparator.compare(array.get(k), array.get(parent)) < 0) {
                swap(k, parent);
                k = parent;
            } else {
                break;
            }
        }
    }

    /**
     * DESCRIPTION: The implementation of the heapify method of the minHeap that is
     * called to put the min element in the top position,after the the call of
     * remove or pop;
     */
    private void shiftDown(int k) {
        int left = 2 * k + 1;
        while (left < nItem) {
            int min = left, right = left + 1;
            if (right < nItem) {
                if (Comparator.compare(array.get(right), array.get(left)) <= 0) {
                    min = right;
                }
            }
            if (Comparator.compare(array.get(k), array.get(min)) <= 0) {
                break;
            }
            swap(k, min);
            k = min;
            left = 2 * k + 1;
        }
    }

    /**
     * DESCRIPTION: Method used to remove an element e from the minHeap;
     */
    public boolean remove(E e) {
        if (!contains(e)) {
            return false;
        }

        int index = hash.get(e);
        swap(index, nItem - 1);

        array.set(nItem - 1, null);
        hash.remove(e);
        nItem--;

        if (index < nItem) {
            shiftUp(index);
            shiftDown(index);
        }
        return true;
    }

    /**
     * DESCRIPTION: Sub-method used in Shift-up, shift-down, and remove;
     */
    private void swap(int i, int j) {
        E tmp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, tmp);
        hash.put(array.get(i), i);
        hash.put(array.get(j), j);
    }
}