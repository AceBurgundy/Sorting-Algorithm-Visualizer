package org.openjfx.sort;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;
import javafx.scene.control.Button;

public class Helper {

    final ArrayList<Button> buttonList;

    public Helper(ArrayList<Button> origin) {
        buttonList = origin;
    }

    public void hideAllButtons() {
        buttonList.forEach(button -> {
            button.setVisible(false);
        });
    }

    /*
    *    Title: Check whether an array is sorted in Java
    *    Availability: https://www.techiedelight.com/check-array-sorted-java/
     */
    public static boolean isSorted(ArrayList<Integer> array) {

        if (array == null || array.size() <= 1) {
            return true;
        }

        return IntStream.range(0, array.size() - 1).noneMatch(index -> array.get(index) > array.get(index + 1));
    }

    public static boolean isTheSame(ArrayList<Integer> array, ArrayList<Integer> arrayToBeCompared) {

        boolean passed = true;

        if (array.size() != arrayToBeCompared.size()) {
            return false;
        }

        for (int index = 0; index < array.size(); index++) {
            if (!Objects.equals(array.get(index), arrayToBeCompared.get(index))) {
                passed = false;
            }
        }

        return passed;
    }

    public void showAllButtons() {
        buttonList.forEach(button -> {
            button.setVisible(true);
        });
    }
}
