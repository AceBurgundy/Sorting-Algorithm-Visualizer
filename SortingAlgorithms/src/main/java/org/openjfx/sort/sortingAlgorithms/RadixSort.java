package org.openjfx.sort.sortingAlgorithms;

import java.util.*;
import java.util.stream.IntStream;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.openjfx.sort.Helper;

public final class RadixSort {

    final Helper helper;

    final int numbers[];

    ArrayList<ArrayList<Integer>> timeLineRecord = new ArrayList<>();
    ArrayList<Integer> timeLineData;

    private int divider = 1;
    final int radii = 10;

    public RadixSort(int[] array, ArrayList<Rectangle> listOfRectangle, Helper helper) {

        this.helper = helper;
        numbers = array;
        int position[] = new int[numbers.length];
        Array arrayManager = new Array(numbers);

        while (divider == 1 || divider <= arrayManager.getMaximum()) {

            int occurences[] = new int[10];
            timeLineData = new ArrayList<>();

            for (int number = 0; number < numbers.length; number++) {
                occurences[(numbers[number] / divider) % radii]++;
            }

            for (int index = 0; index < occurences.length; index++) {
                try {
                    occurences[index + 1] = occurences[index] + occurences[index + 1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }

            for (int number = numbers.length - 1; number >= 0; number--) {
                position[--occurences[(numbers[number] / divider) % radii]] = numbers[number];
            }

            System.arraycopy(position, 0, numbers, 0, numbers.length);
            divider *= radii;

            for (int number = 0; number < position.length; number++) {
                timeLineData.add(position[number]);
            }

            timeLineRecord.add(timeLineData);

        }

        renderSortingAnimation(listOfRectangle);
    }

    public void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();

                for (ArrayList<Integer> row : timeLineRecord) {

                    IntStream.range(0, listOfRectangle.size()).forEach(index -> {

                        Runnable updater = () -> {

                            KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                            Rectangle bar = listOfRectangle.get(index);
                            Rectangle previousBar = index > 0 ? listOfRectangle.get(index - 1) : null;
                            KeyFrame updateRect = new KeyFrame(Duration.millis(10), event -> {

                                bar.setFill(Color.RED);
                                bar.setHeight(row.get(index) * 5);

                                if (previousBar != null) {
                                    previousBar.setFill(Color.WHITE);
                                }

                                listOfRectangle.get(listOfRectangle.size() - 1).setFill(Color.WHITE);

                            });

                            Timeline timeline = new Timeline(updateMaze, updateRect);
                            timeline.setCycleCount(1);

                            timeline.play();

                        };

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                        }

                        Platform.runLater(updater);

                    });
                }
                helper.showAllButtons();
            }
        };

        thread.setDaemon(true);
        thread.start();

    }
}
