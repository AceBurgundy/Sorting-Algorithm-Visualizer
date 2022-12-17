package org.openjfx.sort.sortingAlgorithms;

import java.util.*;
import java.util.stream.*;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;
import javafx.util.Duration;
import org.openjfx.sort.Helper;

public final class ShellSort {

    final private Helper helper;
    final private ArrayList<Map<Integer, Integer>> shellSortTimeLine = new ArrayList<>();
    private Map<Integer, Integer> timeLine;

    public ShellSort(int[] array, ArrayList<Rectangle> listOfRectangle, Helper helper) {

        this.helper = helper;
        sort(IntStream.of(array).boxed().collect(Collectors.toCollection(ArrayList::new)));
        renderSortingAnimation(listOfRectangle);

    }

    public void sort(ArrayList<Integer> unsortedArray) {

        for (int gap = unsortedArray.size() / 2; gap > 0; gap /= 2) {

            for (int start = gap; start < unsortedArray.size(); start++) {

                timeLine = new HashMap<>();
                int current = start;

                while (current >= gap && unsortedArray.get(current).compareTo(unsortedArray.get(current - gap)) < 0) {

                    int temporary = unsortedArray.get(current);
                    int currentMinusGap = current - gap;

                    unsortedArray.set(current, unsortedArray.get(currentMinusGap));
                    timeLine.put(current, unsortedArray.get(currentMinusGap));

                    unsortedArray.set(currentMinusGap, temporary);
                    timeLine.put(currentMinusGap, temporary);

                    shellSortTimeLine.add(timeLine);
                    current -= gap;

                }
            }
        }
    }

    public void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread(() -> {

            helper.hideAllButtons();

            shellSortTimeLine.forEach(row -> {

                Runnable updater = () -> {

                    row.forEach((key, value) -> {

                        Rectangle bar = listOfRectangle.get(key);
                        KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                        KeyFrame updateRect = new KeyFrame(Duration.millis(50), event -> {
                            bar.setHeight(value * 5);
                            bar.setFill(Color.WHITE);
                        });

                        Timeline timeline = new Timeline(updateMaze, updateRect);
                        timeline.setCycleCount(1);
                        timeline.play();
                        bar.setFill(Color.RED);

                    });
                };

                try {
                    Thread.sleep(15);
                } catch (InterruptedException ex) {
                }
                Platform.runLater(updater);
            });

            helper.showAllButtons();

        });

        thread.setDaemon(true);
        thread.start();
    }
}
