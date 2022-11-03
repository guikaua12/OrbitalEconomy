package me.approximations.orbitalEconomy.util;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static int random(int min, int max) {
        return random.nextInt(max-min) + min;
    }

}
