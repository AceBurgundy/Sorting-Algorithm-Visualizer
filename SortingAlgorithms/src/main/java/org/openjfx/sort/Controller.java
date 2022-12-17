package org.openjfx.sort;

import org.openjfx.sort.sortingAlgorithms.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import java.util.*;
import java.util.stream.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {

    private int array[] = {150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249};
    private List<Integer> sortedArray;
    final ArrayList<Rectangle> rectangleList = new ArrayList<>();

    @FXML
    public AnchorPane mainPanel;

    @FXML
    public Button randomButton;

    @FXML
    public Button mergeSortButton;

    @FXML
    public Button bubbleSortButton;

    @FXML
    public Button quickSortButton;

    @FXML
    public Button radixSortButton;

    @FXML
    public Button heapSortButton;

    @FXML
    public Button shellSortButton;

    @FXML
    public HBox buttonPanel;

    private Helper helper;

    public void initialize() {
        randomize();

        helper = new Helper(new ArrayList<>(Arrays.asList(
                randomButton,
                mergeSortButton,
                bubbleSortButton,
                quickSortButton,
                radixSortButton,
                heapSortButton,
                shellSortButton
        )
        ));

        renderBars(array);
    }

    public void randomize() {
        sortedArray = (ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList());
        Collections.shuffle(sortedArray);
        array = sortedArray.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public void renderBars(int[] array) {

        if (!mainPanel.getChildren().isEmpty()) {
            mainPanel.getChildren().clear();
        }

        if (!rectangleList.isEmpty()) {
            rectangleList.clear();
        }

        Thread thread = new Thread() {

            @Override
            public void run() {

                helper.hideAllButtons();
                int nextBar = 0;

                for (int index = 0; index < array.length; index++) {

                    Rectangle bar = new Rectangle();
                    bar.setWidth(12);
                    bar.setFill(Color.WHITE);
                    bar.setX(nextBar);
                    nextBar += bar.getWidth() + 1;
                    bar.setY(-700);
                    bar.setHeight(array[index] * 5);
                    rectangleList.add(bar);
                    ;
                    Platform.runLater(() -> mainPanel.getChildren().add(bar));

                    try {
                        Thread.sleep(01);
                    } catch (InterruptedException v) {
                        System.out.println(v);
                    }
                }

                helper.showAllButtons();

            }
        };

        thread.setDaemon(true);
        thread.start();

    }

    public void randomButtonClicked(ActionEvent e) {
        randomize();
        renderBars(array);
    }

    public void mergeSortButtonClicked(ActionEvent e) {
        new MergeSort(array, rectangleList, helper);
    }

    public void bubbleSortButtonClicked(ActionEvent e) {
        new BubbleSort(array, rectangleList, helper);
    }

    public void quickSortButtonClicked(ActionEvent e) {
        new QuickSort(array, rectangleList, helper);
    }

    public void radixSortButtonClicked(ActionEvent e) {
        new RadixSort(array, rectangleList, helper);
    }

    public void heapSortButtonClicked(ActionEvent e) {
        new HeapSort(array, rectangleList, helper);
    }

    public void shellSortButtonClicked(ActionEvent e) {
        new ShellSort(array, rectangleList, helper);

    }

}
