package org.openjfx.sort.sortingAlgorithms;

import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.openjfx.sort.Helper;

public final class MergeSort {

    final Helper helper;

    final ArrayList<ArrayList<Integer>> mergeTimeLine = new ArrayList<>();

    public MergeSort(int[] array, ArrayList<Rectangle> listOfRectangle, Helper helper) {
        this.helper = helper;
        mergeSort((ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList()));
        renderSortingAnimation(listOfRectangle);
    }

    public void mergeSort(ArrayList<Integer> array) {

        int arrayLength = array.size();
        int rightIndex = 0;
        int middleValue = arrayLength / 2;

        ArrayList<Integer> leftArray = new ArrayList<>();
        ArrayList<Integer> rightArray = new ArrayList<>();

        if (arrayLength <= 1) {
            return;
        }

        for (int leftIndex = 0; leftIndex < arrayLength; leftIndex++) {
            if (leftIndex < middleValue) {
                leftArray.add(array.get(leftIndex));
            } else {
                rightArray.add(array.get(leftIndex));
                rightIndex++;
            }
        }

        mergeSort(leftArray);
        mergeSort(rightArray);
        merge(leftArray, rightArray, array);

    }

    private void merge(ArrayList<Integer> leftArray, ArrayList<Integer> rightArray, ArrayList<Integer> array) {

        int leftSize = array.size() / 2;
        int rightSize = array.size() - leftSize;
        int index = 0, leftIndex = 0, rightIndex = 0;

        while (leftIndex < leftSize && rightIndex < rightSize) {
            if (leftArray.get(leftIndex) < rightArray.get(rightIndex)) {
                array.set(index, leftArray.get(leftIndex));
                index++;
                leftIndex++;
            } else {
                array.set(index, rightArray.get(rightIndex));
                index++;
                rightIndex++;
            }
        }

        while (leftIndex < leftSize) {
            array.set(index, leftArray.get(leftIndex));
            index++;
            leftIndex++;
        }

        while (rightIndex < rightSize) {
            array.set(index, rightArray.get(rightIndex));
            index++;
            rightIndex++;
        }

        mergeTimeLine.add(array);

    }

    private void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();
                int index = 0;
                int iteratedRow = 0;

                for (ArrayList<Integer> row : mergeTimeLine) {

                    for (int cell : row) {

                        Rectangle bar = listOfRectangle.get(index);

                        Runnable updater = () -> {

                            KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                            KeyFrame updateRect = new KeyFrame(Duration.millis(10), event -> {
                                bar.setFill(Color.WHITE);
                                bar.setHeight(cell * 5);
                            });

                            Timeline timeline = new Timeline(updateMaze, updateRect);
                            timeline.setCycleCount(1);
                            timeline.play();

                        };

                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException ex) {
                        }

                        index++;
                        Platform.runLater(updater);
                        bar.setFill(Color.RED);

                    }

                    iteratedRow++;
                    index = 0;

                    if (iteratedRow >= 24) {
                        index = 24;
                    }

                    if (iteratedRow >= 48) {
                        index = 0;
                    }

                    if (iteratedRow >= 49) {
                        index = 49;
                    }

                    if (iteratedRow >= 73) {
                        index = 75;
                    }

                    if (iteratedRow >= 97) {
                        index = 50;
                    }

                    if (iteratedRow >= 98) {
                        index = 0;
                    }

                }
                helper.showAllButtons();
            }
        };

        thread.setDaemon(true);
        thread.start();

    }
}
