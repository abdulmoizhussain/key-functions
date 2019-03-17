package key.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class MainForm extends javax.swing.JFrame implements NativeKeyListener {

  private int key1, key2, key3, key_pressed;
  private String _text;
  private long timeKey1, timeKey2, timeKey3;
  private boolean allowClip, allowCursor;

  public MainForm() {
    _text = "";
    timeKey1 = timeKey2 = timeKey3 = key1 = key2 = key3 = key_pressed = 0;
    initComponents();
    try {
      GlobalScreen.registerNativeHook();
    } catch (NativeHookException e) {
      e.printStackTrace();
    }
    GlobalScreen.addNativeKeyListener(this);
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
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
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
    // this function is made to return from first if it was a copy function, cannot return from here, because this override function has void
  }

  private int perform(int KEY) {
    if (allowClip && KEY == 67 && (key_pressed == 162 || key_pressed == 163)) {
      // is "C" key released ? then check is Lctrl/Rctrl pressed ?
      String clip = getClipboard();
      if (clip.trim().indexOf("mis ") == 0) { // must be in start
        clip = clip.replaceAll("[^a-zA-Z0-9]+", " ").trim();
        clip = clip.replaceFirst("mis ", "");
        setClipboard(clip);
        _text = clip + "\n" + _text;
        display(_text);
        key_pressed = 0;
      }
    } else if (allowCursor && isAlphabet(KEY)) {
      key1 = key2;
      key2 = key3;
      key3 = KEY;
      if (key1 == key2 && key1 == key3) { // if all three keys are equal to each other
        return 0;
      }
      timeKey1 = timeKey2;
      timeKey2 = timeKey3;
      timeKey3 = System.currentTimeMillis();
      if (((timeKey2 - timeKey1) + (timeKey3 - timeKey2)) / 2 < 300) {
        setMousePosition(0, 0);
      }
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
    StringSelection stringSelection = new StringSelection(copyText);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  private String getClipboard() {
    try {
      new JFXPanel();
      return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    } catch (UnsupportedFlavorException | IOException ex) {
      Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
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
