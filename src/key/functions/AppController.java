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
public class AppController {

    private boolean allowClip, allowCursor;
    private final java.awt.TextArea clipboardTextArea;

    public AppController(java.awt.TextArea clipboardTextArea) {
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
            robot = null;
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                System.out.println(e.getMessage());
            }
            GlobalScreen.removeNativeKeyListener(this);
        }
    }

}
