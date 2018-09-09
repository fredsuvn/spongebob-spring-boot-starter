package com.tousie.securities.common;

import java.util.Random;

public class Randoms {

    public static String ofRange(Random random, String range, int length) {
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = range.charAt(random.nextInt(length));
        }
        return new String(result);
    }
}
