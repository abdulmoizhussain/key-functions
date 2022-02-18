/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package key.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Abdul
 */
public class NativeKeyListenerService implements NativeKeyListener {

    private final int key_press_count = 5; // must not be smaller than 2
    private final int key_press_last_index = key_press_count - 1;
    private final long keyTimes[] = new long[key_press_count];
    private final Pattern patternSpecialChars = Pattern.compile("[^a-zA-Z0-9]+");
    private final Pattern patternMis = Pattern.compile("mis ");
    private final Robot robot = createRobot();
    private final java.awt.TextArea clipboardTextArea;
    private boolean allowClip, allowCursor, maintainClipboard;

    public NativeKeyListenerService(java.awt.TextArea clipboardTextArea) {
        this.clipboardTextArea = clipboardTextArea;
    }

    // ---------------- PUBLIC METHODS:
    public void setAllowClip(boolean allowClip) {
        this.allowClip = allowClip;
        manageKeyListenerRegistration();
    }

    public void setAllowCursor(boolean allowCursor) {
        this.allowCursor = allowCursor;
        manageKeyListenerRegistration();
    }

    public void maintainClipboardHistory(boolean enabled) {
        maintainClipboard = enabled;
    }

    // ---------------- PRIVATE METHODS:
    private void manageKeyListenerRegistration() {
        if ((allowCursor || allowClip) && !GlobalScreen.isNativeHookRegistered()) {
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                System.out.println(e.getMessage());
            }
            GlobalScreen.addNativeKeyListener(this);
        } else if ((!allowCursor && !allowClip) && GlobalScreen.isNativeHookRegistered()) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                System.out.println(e.getMessage());
            }
            GlobalScreen.removeNativeKeyListener(this);
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        // not being used for now.
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        // not being used for now.
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        // 67 -> C
        int key_code = nke.getRawCode();
        final int modifier = nke.getModifiers();

        // is "C" key released ? then check if Lctrl/Rctrl is pressed or not ?
        if (allowClip && (modifier == NativeKeyEvent.CTRL_L_MASK || modifier == NativeKeyEvent.CTRL_R_MASK) && key_code == 67) {
            System.out.println("inside" + (new Random().nextInt(99 - 10) + 10));
            String clip = getClipboard();

            if (clip.trim().indexOf("mis ") == 0) { // must be in start
                clip = patternSpecialChars.matcher(clip).replaceAll(" ").trim();
                clip = patternMis.matcher(clip).replaceFirst("");

                setClipboard(clip);

                if (maintainClipboard) {
                    prependText(clip + "\n\n");
                }
            } else if (maintainClipboard) {
                prependText(clip + "\n\n");
            }
//        } else if (allowCursor && Utils.isAlphabet(key_code)) {
        } else if (allowCursor) {
            // match occurence of same character codes with length of keys
//            String keys_str = "";
//            for (int i = 0; i < keys.length - 1; i++) {
//                keys[i] = keys[i + 1];
//                keys_str += Integer.toString(keys[i]);
//            }
//            keys[keys.length - 1] = key_code;
//            keys_str += Integer.toString(key_code);
            // e.g regex:  "(23){5,5}"
            // e.g str to match: 322332332323232323
//            if (Pattern.matches("(" + Integer.toString(key_code) + "){" + keys.length + "," + keys.length + "}", keys_str)) {
//                return;
//            }

            for (int i = 0; i < key_press_last_index; i++) {
                keyTimes[i] = keyTimes[i + 1];
            }
            keyTimes[key_press_last_index] = System.currentTimeMillis();

            long sum = 0;
            for (int i = 0; i < key_press_last_index; i++) {
                sum += (keyTimes[i + 1] - keyTimes[i]);
            }
            if ((sum / key_press_last_index) < 300) {
                robot.mouseMove(0, 0);
            }
        }
    }

    private static Robot createRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String getClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            return "";
        }
    }

    private void setClipboard(String copyText) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copyText), null);
    }

    private void prependText(String str) {
        clipboardTextArea.setText(str + clipboardTextArea.getText());
    }
}
