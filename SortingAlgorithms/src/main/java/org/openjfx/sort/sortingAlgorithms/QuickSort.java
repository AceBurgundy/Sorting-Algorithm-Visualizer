package org.openjfx.sort.sortingAlgorithms;

import java.util.*;
import java.util.stream.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.openjfx.sort.Helper;

public class QuickSort {

    final Helper helper;
    private int currentIteration;
    final private ArrayList<ArrayList<Integer>> timeLineMapper = new ArrayList<>();
    final private ArrayList<TreeMap<Integer, Integer>> loopGuide = new ArrayList<>();
    private ArrayList<Integer> timeLine;
    private TreeMap<Integer, Integer> loopIndexGuide;

    public QuickSort(int array[], ArrayList<Rectangle> listOfRectangle, Helper helper) {

        this.helper = helper;
        ArrayList<Integer> arrayToList = new ArrayList<>((ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList()));
        quickSort(arrayToList, 0, arrayToList.size() - 1);
        getIndexGuide();
        renderSortingAnimation(listOfRectangle);

    }

    private void quickSort(ArrayList<Integer> array, int start, int end) {

        if (end < start) {
            return;
        }

        int guide = sort(array, start, end);
        quickSort(array, start, guide - 1);
        quickSort(array, guide + 1, end);

    }

    private int sort(ArrayList<Integer> array, int start, int end) {

        timeLine = new ArrayList<>();
        int guide = array.get(end);
        int index = start - 1;

        for (int current = start; current <= end; current++) {

            if (array.get(current) < guide) {

                index++;
                int buffer = array.get(index);
                array.set(index, array.get(current));
                array.set(current, buffer);

            }
        }

        index++;
        int buffer = array.get(index);
        array.set(index, array.get(end));
        array.set(end, buffer);

        for (int item : array) {
            timeLine.add(item);
        }

        if (!timeLineMapper.isEmpty()) {
            if (!Helper.isTheSame(array, timeLineMapper.get(timeLineMapper.size() - 1))) {
                timeLineMapper.add(timeLine);
            }
        } else {
            timeLineMapper.add(timeLine);
        }

        return index;

    }

    private void renderSortingAnimation(ArrayList<Rectangle> listOfRectangle) {

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();

                loopGuide.forEach(value -> {

                    runIteration(listOfRectangle, value, 10, timeLineMapper.get(currentIteration));

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException v) {
                        System.exit(0);
                    }

                    for (int number = 0; number < listOfRectangle.size(); number++) {

                        Rectangle bar = listOfRectangle.get(number);

                        Runnable updater = () -> {

                            KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                            KeyFrame updateRect = new KeyFrame(Duration.millis(10), event -> {
                                bar.setFill(Color.WHITE);

                            });

                            Timeline timeline = new Timeline(updateMaze, updateRect);
                            timeline.setCycleCount(1);
                            timeline.play();

                        };

                        Platform.runLater(updater);
                    }

                    currentIteration++;
                    helper.showAllButtons();

                });
            }
        };

        thread.setDaemon(true);
        thread.start();

    }

    public void runIteration(ArrayList<Rectangle> listOfRectangle, TreeMap<Integer, Integer> row, int speedInMilliseconds, ArrayList<Integer> timeLineValues) {

        int start = row.firstKey();
        int end = row.lastKey();

        IntStream.range(start, end).forEach(index -> {

            Runnable updater = () -> {

                KeyFrame updateMaze = new KeyFrame(Duration.ZERO);
                Rectangle bar = listOfRectangle.get(index);
                Rectangle previousBar = index > 0 ? listOfRectangle.get(index - 1) : null;
                KeyFrame updateRect = new KeyFrame(Duration.millis(10), event -> {

                    int value = row.get(index) == null ? timeLineValues.get(index) : row.get(index);

                    bar.setHeight(value * 5);
                    bar.setFill(Color.RED);

                    if (previousBar != null) {
                        previousBar.setFill(Color.WHITE);
                    }

                    listOfRectangle.get(listOfRectangle.size() - 1).setFill(Color.WHITE);
                    listOfRectangle.get(listOfRectangle.size() - 1).setHeight(timeLineValues.get(timeLineValues.size() - 1) * 5);

                });

                Timeline timeline = new Timeline(updateMaze, updateRect);
                timeline.setCycleCount(1);
                timeline.play();

            };

            try {
                Thread.sleep(speedInMilliseconds);
            } catch (InterruptedException v) {
                System.exit(0);
            }

            Platform.runLater(updater);

        });

    }

    private void getIndexGuide() {

        for (int row = 0; row < timeLineMapper.size(); row++) {

            loopIndexGuide = new TreeMap<>();

            for (int current = 0; current < timeLineMapper.get(row).size(); current++) {

                try {

                    if (!(Objects.equals(timeLineMapper.get(row).get(current), timeLineMapper.get(row + 1).get(current)))) {
                        loopIndexGuide.put(current, timeLineMapper.get(row).get(current));
                    }

                } catch (IndexOutOfBoundsException e) {
                    loopIndexGuide.put(current, timeLineMapper.get(row).get(current));
                }
            }

            loopGuide.add(loopIndexGuide);

        }
    }
}
