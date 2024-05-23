package com.solvd.util;

import java.util.List;
import java.util.Random;

public class RandomPicker {
    private final static Random random = new Random();

    public static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
