package org.openjfx.sort.sortingAlgorithms;

import org.openjfx.sort.Helper;
import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public final class HeapSort {

    final Helper helper;
    private int iteration = 0;

    final private ArrayList<Map<Integer, Integer>> switchTimeLine = new ArrayList<>();
    final private ArrayList<ArrayList<Integer>> heapTimeLine = new ArrayList<>();
    private ArrayList<Integer> timeLine;
    private Map<Integer, Integer> switchIndex;

    public HeapSort(int array[], ArrayList<Rectangle> listOfRectangle, Helper helper) {

        this.helper = helper;
        heapSort(new ArrayList<>((ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList())));
        checkIndexes();
        renderSortingAnimation(listOfRectangle);

    }

    public void heapSort(ArrayList<Integer> unsortedArray) {
        int arrayLength = unsortedArray.size();

        for (int index = arrayLength / 2 - 1; index >= 0; index--) {
            heapify(unsortedArray, arrayLength, index);
        }

        for (int index = arrayLength - 1; index >= 0; index--) {
            // Move current root to end
            int buffer = unsortedArray.get(0);
            unsortedArray.set(0, unsortedArray.get(index));
            unsortedArray.set(index, buffer);

            heapify(unsortedArray, index, 0);
        }
    }

    void heapify(ArrayList<Integer> unsortedArray, int arrayLength, int rootNode) {

        timeLine = new ArrayList<>();

        int currentLargest = rootNode;
        int leftNode = 2 * rootNode + 1;
        int rightNode = 2 * rootNode + 2;

        if (leftNode < arrayLength && unsortedArray.get(leftNode) > unsortedArray.get(currentLargest)) {
            currentLargest = leftNode;
        }

        if (rightNode < arrayLength && unsortedArray.get(rightNode) > unsortedArray.get(currentLargest)) {
            currentLargest = rightNode;
        }

        if (currentLargest != rootNode) {
            int swap = unsortedArray.get(rootNode);
            unsortedArray.set(rootNode, unsortedArray.get(currentLargest));
            unsortedArray.set(currentLargest, swap);

            heapify(unsortedArray, arrayLength, currentLargest);
        }

        for (Integer value : unsortedArray) {
            if (timeLine.size() == unsortedArray.size()) {
                break;
            }
            timeLine.add(value);
        }

        if (!heapTimeLine.isEmpty()) {
            if (!(heapTimeLine.get(heapTimeLine.size() - 1).equals(timeLine))) {
                heapTimeLine.add(timeLine);
            }
        } else {
            heapTimeLine.add(timeLine);
        }

    }

    private void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();

                for (ArrayList<Integer> row : heapTimeLine) {

                    Map<Integer, Integer> pointerIndex = switchTimeLine.get(iteration);

                    pointerIndex.forEach((index, value) -> {

                        KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                        Rectangle bar = listOfRectangle.get(index);
                        KeyFrame updateRect = new KeyFrame(Duration.millis(15), event -> {

                            Platform.runLater(() -> {
                                bar.setFill(Color.WHITE);
                                bar.setHeight(value * 5);
                            });
                            bar.setFill(Color.RED);
                        });

                        Timeline timeline = new Timeline(updateMaze, updateRect);
                        timeline.setCycleCount(1);
                        timeline.play();

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException v) {
                            System.exit(0);
                        }

                    });
                    iteration++;
                }
                helper.showAllButtons();
            }
        };

        thread.setDaemon(true);
        thread.start();

    }

    private void checkIndexes() {

        for (int row = 0; row < heapTimeLine.size(); row++) {

            switchIndex = new HashMap<>();

            try {

                ArrayList<Integer> currentRow = heapTimeLine.get(row);
                ArrayList<Integer> nextRow = heapTimeLine.get(row + 1);
                int length = currentRow.size();

                for (int number = 0; number < length; number++) {
                    if (!(Objects.equals(currentRow.get(number), nextRow.get(number)))) {
                        switchIndex.put(number, nextRow.get(number));
                        System.out.print(number + " " + nextRow.get(number) + " |");
                    }
                    System.out.println();
                }
            } catch (IndexOutOfBoundsException e) {

            }

            switchTimeLine.add(switchIndex);
        }
    }

}
