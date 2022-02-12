/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package key.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
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

    private int key_pressed = 0;
    private final int key_press_count = 3; // must not be smaller than 2
    private final int keys[] = new int[key_press_count];
    private final long keyTimes[] = new long[key_press_count];
    private boolean allowClip, allowCursor;
    private final java.awt.TextArea clipboardTextArea;
    private Robot robot;
    private FlavorListenerService flavorListenerService;
    private boolean ctrl_key;

    public NativeKeyListenerService(java.awt.TextArea clipboardTextArea) {
        this.clipboardTextArea = clipboardTextArea;
        Arrays.fill(keys, 0);
        Arrays.fill(keyTimes, 0);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        // not being used for now.
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        int key = nke.getRawCode();

        ctrl_key = key == 162 || key == 163; // either LCTRL (162) or RCTRL (163)

//        System.out.println("nativeKeyPressed." + key + "." + ctrl_key);
//        if (!isAlphabet(key)) {
//            key_pressed = key;
//        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        // 67 -> C
        // 162 -> Lctrl
        // 163 -> Rctrl
        int key_code = nke.getRawCode();

        if (key_code == 162 || key_code == 163) {
            ctrl_key = false;
        }

//        System.out.println("nativeKeyReleased." + allowClip + "." + key_code + "." + ctrl_key);
        // is "C" key released ? then check if Lctrl/Rctrl is pressed or not ?
//        if (allowClip && key_code == 67 && (key_pressed == 162 || key_pressed == 163)) {
        if (allowClip && key_code == 67 && ctrl_key) {
            String clip = getClipboard();
            if (clip.trim().indexOf("mis ") == 0) { // must be in start
                clip = clip.replaceAll("[^a-zA-Z0-9]+", " ").trim();
                clip = clip.replaceFirst("mis ", "");
                setClipboard(clip);
                prependText(clip + "\n\n");
            }
        } else if (allowCursor && isAlphabet(key_code)) {
            {
                // match occurence of same character codes with length of keys
                String keys_str = "";
                for (int i = 0; i < keys.length - 1; i++) {
                    keys[i] = keys[i + 1];
                    keys_str += Integer.toString(keys[i]);
                }
                keys[keys.length - 1] = key_code;
                keys_str += Integer.toString(key_code);
//      e.g regex:  "(23){5,5}"
//      e.g str to match: 322332332323232323
                if (Pattern.matches("(" + Integer.toString(key_code) + "){" + keys.length + "," + keys.length + "}", keys_str)) {
                    return;
                }
            }

            for (int i = 0; i < keyTimes.length - 1; i++) {
                keyTimes[i] = keyTimes[i + 1];
            }
            keyTimes[keyTimes.length - 1] = System.currentTimeMillis();

            long[] diff = new long[keyTimes.length - 1];
            Arrays.fill(diff, 0);
            for (int i = 0; i < diff.length; i++) {
                diff[i] = keyTimes[i + 1] - keyTimes[i];
            }
            long sum = 0;
            for (int i = 0; i < diff.length; i++) {
                sum += diff[i];
            }
            if (sum / diff.length < 300) {
                robot.mouseMove(0, 0);
            }
        }
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
        if (enabled) {
            flavorListenerService = new FlavorListenerService();
            Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(flavorListenerService);
        } else {
            Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(flavorListenerService);
            flavorListenerService = null;
        }
    }

    // ---------------- PRIVATE METHODS:
    private void manageKeyListenerRegistration() {
        if ((allowCursor || allowClip) && !GlobalScreen.isNativeHookRegistered()) {
            try {
                robot = new Robot();
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException | AWTException e) {
                System.out.println(e.getMessage());
            }
            GlobalScreen.addNativeKeyListener(this);
        } else if ((!allowCursor && !allowClip) && GlobalScreen.isNativeHookRegistered()) {
            robot = null;
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                System.out.println(e.getMessage());
            }
            GlobalScreen.removeNativeKeyListener(this);
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

    // ---------------- PRIVATE STATIC METHODS:
    private static boolean isAlphabet(int char_code) {
        return char_code > 64 && char_code < 91;
//        return CHAR >= 65 && CHAR <= 90;
        //    Pattern.matches("[A-Z]", KEY)
        //    String newClip = Pattern.compile("[^a-zA-Z0-9]+").matcher(clip).replaceAll(" ");
    }

    // ---------------- CLIPBOARD LISTENER
    public class FlavorListenerService implements FlavorListener {

        @Override
        public void flavorsChanged(FlavorEvent e) {
            String a = getClipboard();
            System.out.println(a);
            prependText(a + "\n\n");
        }
    }
}
