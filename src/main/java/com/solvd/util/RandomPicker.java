package com.solvd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPicker {
    private final static Random random = new Random();

    public static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Selects randomly count elements from list, without replacing
     * (that is with unique indexes)
     *
     * @param list
     * @param count have to be lesser or equals to size of the list
     * @param <T>
     * @return
     */
    public static <T> List<T> getRandomElements(List<T> list, int count) {
        // TODO: add checking if count > list.size and throw readable message,
        //       now it would thrws index out of bound
        // TODO: improve, so that whole list don't need to be copied
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy, random);
        return copy.subList(0, count);
    }
}
