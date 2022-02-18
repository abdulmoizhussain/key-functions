/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package key.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;

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

    public static void arrayPrint(long[] array) {
        for (int i = 1; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
    }

    public static Robot createRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static String getClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }
}
