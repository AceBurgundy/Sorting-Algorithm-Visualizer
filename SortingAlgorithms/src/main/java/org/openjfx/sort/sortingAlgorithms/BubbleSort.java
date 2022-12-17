package org.openjfx.sort.sortingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.openjfx.sort.Helper;

public class BubbleSort {

    private final Helper helper;

    ArrayList<ArrayList<Integer>> timeLine = new ArrayList<>();

    public BubbleSort(int array[], ArrayList<Rectangle> listOfRectangle, Helper helper) {

        this.helper = helper;
        ArrayList<Integer> bufferArray = new ArrayList<>();
        ArrayList<Integer> unsortedArray = new ArrayList<>((ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList()));

        for (int outerIndex = 0; outerIndex < unsortedArray.size() - 1; outerIndex++) {
            ArrayList<Integer> test = new ArrayList<>();

            if (!Helper.isSorted(unsortedArray)) {
                for (int index = 0; index < unsortedArray.size(); index++) {
                    test.add(unsortedArray.get(index));
                }
                timeLine.add(test);
            }

            for (int innerIndex = 0; innerIndex < unsortedArray.size() - outerIndex - 1; innerIndex++) {
                if ((int) (unsortedArray.get(innerIndex)) > (int) (unsortedArray.get(innerIndex + 1))) {

                    int temporary = unsortedArray.get(innerIndex);

                    unsortedArray.set(innerIndex, unsortedArray.get(innerIndex + 1));

                    unsortedArray.set(innerIndex + 1, temporary);
                }
            }
        }

        for (int index : unsortedArray) {
            bufferArray.add(index);
        }

        timeLine.add(bufferArray);

        renderSortingAnimation(listOfRectangle);

    }

    private void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();

                int indexForGreen = listOfRectangle.size() - 1;

                for (ArrayList<Integer> row : timeLine) {

                    IntStream.range(0, row.size()).forEach(index -> {

                        Runnable updater = () -> {

                            KeyFrame updateMaze = new KeyFrame(Duration.ZERO);

                            Rectangle previousBar = index > 0 ? listOfRectangle.get(index - 1) : null;
                            Rectangle currentBar = listOfRectangle.get(index);

                            KeyFrame updateRect = new KeyFrame(Duration.millis(25), event -> {

                                currentBar.setHeight(row.get(index) * 5);

                                if (previousBar != null) {
                                    previousBar.setFill(Color.RED);
                                }

                                currentBar.setFill(Color.RED);

                                if (previousBar != null) {
                                    previousBar.setFill(Color.WHITE);
                                }

                            });

                            Timeline timeline = new Timeline(updateMaze, updateRect);
                            timeline.setCycleCount(1);

                            timeline.play();
                        };

                        listOfRectangle.get(listOfRectangle.size() - 1).setFill(Color.WHITE);

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                        }

                        Platform.runLater(updater);

                    });

                    indexForGreen--;

                }

                helper.showAllButtons();

            }
        };

        thread.setDaemon(true);
        thread.start();

    }
}
