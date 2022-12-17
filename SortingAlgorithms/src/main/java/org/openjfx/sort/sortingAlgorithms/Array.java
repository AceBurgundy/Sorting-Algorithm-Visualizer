package org.openjfx.sort.sortingAlgorithms;

import java.util.*;

class Array {

    final int minimum;
    final int maximum;

    public Array(int[] array) {
        final int buffer[] = new int[array.length];
        System.arraycopy(array, 0, buffer, 0, array.length);
        Arrays.sort(buffer);
        minimum = array[0];
        maximum = array[array.length - 1];
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }
}
