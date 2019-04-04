package key.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class MainForm extends javax.swing.JFrame implements NativeKeyListener {

  private int key_pressed;
  private int keys[];
  private long keyTimes[];
  private int key_press_count = 5;
  private String _text;
  private long timeKey1, timeKey2, timeKey3, timeKey4, timeKey5;
  private boolean allowClip, allowCursor;

  public MainForm() {
    _text = "";
    key_pressed = 0;
    key_press_count = 5; // must not be smaller than 2
    keys = new int[key_press_count];
    keyTimes = new long[key_press_count];
    Arrays.fill(keys, 0);
    Arrays.fill(keyTimes, 0);
    initComponents();
    try {
      GlobalScreen.registerNativeHook();
    } catch (NativeHookException e) {
    }
    GlobalScreen.addNativeKeyListener(MainForm.this);
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    switchCursor = new java.awt.Checkbox();
    switchClip = new java.awt.Checkbox();
    clipboard = new java.awt.TextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    switchCursor.setLabel("Set Cursor position (0, 0) while typing");
    switchCursor.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        switchCursorItemStateChanged(evt);
      }
    });

    switchClip.setLabel("Clean special characters from clipboard");
    switchClip.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        switchClipItemStateChanged(evt);
      }
    });

    clipboard.setEditable(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(clipboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(switchCursor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(switchClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 135, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(switchCursor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(switchClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(clipboard, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void switchCursorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_switchCursorItemStateChanged
    allowCursor = evt.getStateChange() == 1;
  }//GEN-LAST:event_switchCursorItemStateChanged

  private void switchClipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_switchClipItemStateChanged
    allowClip = evt.getStateChange() == 1;
  }//GEN-LAST:event_switchClipItemStateChanged

  public static void main(String args[]) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MainForm().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private java.awt.TextArea clipboard;
  private java.awt.Checkbox switchClip;
  private java.awt.Checkbox switchCursor;
  // End of variables declaration//GEN-END:variables

  @Override
  public void nativeKeyTyped(NativeKeyEvent nke) {
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nke) {
    int key = nke.getRawCode();
    if (!isAlphabet(key)) {
      key_pressed = key;
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nke) {
    // 67 -> C
    // 162 -> Lctrl
    // 163 -> Rctrl
    perform(nke.getRawCode());
    // this function is written to return from first if it was a copy function, cannot return from here, because this override function has void
  }

  private int perform(int KEY) {
    if (allowClip && KEY == 67 && (key_pressed == 162 || key_pressed == 163)) {
      // is "C" key released ? then check is Lctrl/Rctrl pressed ?
      String clip = getClipboard();
      if (clip.trim().indexOf("mis ") == 0) { // must be in start
        clip = clip.replaceAll("[^a-zA-Z0-9]+", " ").trim();
        clip = clip.replaceFirst("mis ", "");
        setClipboard(clip);
        if (_text.contains(clip)) {
          return 0;
        }
        _text = clip + "\n" + _text;
        display(_text);
      }
    } else if (allowCursor && isAlphabet(KEY)) {
      {
        // match occurence of same character codes with length of keys
        String keys_str = "";
        for (int i = 0; i < keys.length - 1; i++) {
          keys[i] = keys[i + 1];
          keys_str += Integer.toString(keys[i]);
        }
        keys[keys.length - 1] = KEY;
        keys_str += Integer.toString(KEY);
//      e.g regex:  "(23){5,5}"
//      e.g str to match: 322332332323232323
        if (Pattern.matches("(" + Integer.toString(KEY) + "){" + keys.length + "," + keys.length + "}", keys_str)) {
          return 0;
        }
      }

      for (int i = 0; i < keyTimes.length - 1; i++) {
        keyTimes[i] = keyTimes[i + 1];
      }
      keyTimes[keyTimes.length - 1] = System.currentTimeMillis();

      {
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
          setMousePosition(0, 0);
        }
      }
//      if (((timeKey2 - timeKey1) + (timeKey3 - timeKey2) + (timeKey4 - timeKey3) + (timeKey5 - timeKey4)) / 4 < 300) {
//        setMousePosition(0, 0);
//      }
    }
    return 0;
  }

  private void setMousePosition(int x, int y) {
    try {
      new Robot().mouseMove(x, y);
    } catch (AWTException e) {
    }
  }

  private boolean isAlphabet(int CHAR) {
    return CHAR >= 65 && CHAR <= 90;
    //    Pattern.matches("[A-Z]", KEY)
    //      String newClip = Pattern.compile("[^a-zA-Z0-9]+").matcher(clip).replaceAll(" ");
  }

  private void setClipboard(String copyText) {
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copyText), null);
  }

  private String getClipboard() {
    try {
      return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    } catch (UnsupportedFlavorException | IOException e) {
      return "";
    }
  }

  private void display(String str) {
    clipboard.setText(str);
  }

  //  private String getKey(NativeKeyEvent nke) {
  //    return NativeKeyEvent.getKeyText(nke.getKeyCode());
  //  }
}
