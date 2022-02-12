package key.functions;

public class MainForm extends javax.swing.JFrame {

    private final NativeKeyListenerService nativeKeyListenerService;

    public MainForm() {
        initComponents();
        nativeKeyListenerService = new NativeKeyListenerService(clipboard);
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
      nativeKeyListenerService.setAllowCursor(evt.getStateChange() == 1);
  }//GEN-LAST:event_switchCursorItemStateChanged

  private void switchClipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_switchClipItemStateChanged
      nativeKeyListenerService.setAllowClip(evt.getStateChange() == 1);
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
        java.awt.EventQueue.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private java.awt.TextArea clipboard;
  private java.awt.Checkbox switchClip;
  private java.awt.Checkbox switchCursor;
  // End of variables declaration//GEN-END:variables
}
