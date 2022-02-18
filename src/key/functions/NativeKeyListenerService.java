/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package key.functions;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Abdul
 */
public class NativeKeyListenerService implements NativeKeyListener {

    private final int key_press_count = 3; // must not be smaller than 2
    private final int key_press_last_index = key_press_count - 1;
    private final long keyTimes[] = new long[key_press_count];
    private final Pattern patternSpecialChars = Pattern.compile("[^a-zA-Z0-9]+");
    private final Pattern patternMis = Pattern.compile("mis ");
    private final Robot robot = Utils.createRobot();
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
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            GlobalScreen.addNativeKeyListener(this);
        } else if ((!allowCursor && !allowClip) && GlobalScreen.isNativeHookRegistered()) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private static boolean ctrl_c_is_pressed(int key_code, int modifier) {
        // is "C" key released ? then check if Lctrl/Rctrl is pressed or not ?
        // 67 -> C
        return key_code == 67 && (modifier == NativeKeyEvent.CTRL_L_MASK || modifier == NativeKeyEvent.CTRL_R_MASK);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {

        int key_code = nke.getRawCode();
        int modifier = nke.getModifiers();

        if (ctrl_c_is_pressed(key_code, modifier)) {
            if (allowClip) {
                String clip = Utils.getClipboard();
                if (clip.indexOf("mis ") == 0) { // "mis " must be in start
                    String clipTrimmed = patternSpecialChars.matcher(clip).replaceAll(" ");
                    clipTrimmed = clipTrimmed.substring(4).trim();

                    setClipboard(clipTrimmed);

                    if (maintainClipboard && !clipTrimmed.isEmpty()) {
                        prependText(clipTrimmed + "\n\n");
                    }
                } else if (maintainClipboard) {
                    if (!clip.isEmpty()) {
                        prependText(clip + "\n\n");
                    }
                }
            } else if (maintainClipboard) {
                String clip = Utils.getClipboard();
                if (!clip.isEmpty()) {
                    prependText(clip + "\n\n");
                }
            }
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

    private void setClipboard(String copyText) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copyText), null);
    }

    private void prependText(String str) {
        clipboardTextArea.setText(str + clipboardTextArea.getText());
    }
}
