/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package key.functions;

import java.util.Arrays;

/**
 *
 * @author Abdul
 */
public class Utils {

    public static int[] arrayFill(int array_size, int val) {
        int[] array = new int[array_size];
        Arrays.fill(array, val);
        return array;
    }

    public static long[] arrayFill(int array_size, long val) {
        long[] array = new long[array_size];
        Arrays.fill(array, val);
        return array;
    }

    public static boolean isAlphabet(int char_code) {
        return char_code > 64 && char_code < 91;
    }
}
