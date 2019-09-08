/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * mainGui.java
 *
 * Created on Apr 23, 2013, 10:21:01 PM
 */
package tomtimer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Tom
 *
 * todo:
 * timer options: remove all, start all, stop all
 */
public class mainGui extends javax.swing.JFrame
{
    ArrayList<TimerEntry> timerList;
    JPanel timerPanel;
    Image sysTrayIcon;
    TrayIcon icon = null;
    boolean isMinimized = false;
    int timerIdToEdit; // user will edit timer settings, apply the changes to this ID
    boolean globalTimerAutoRestartMode = false; //false = timer only runs once, true = timer restarts immediately after triggering
    File customSound = null; //user may choose a custom sound file for a specific timer this is a temporary helper
    
    /** Creates new form mainGui */
    public mainGui()
    {
        initComponents();
        setMainGuiPosition();
        timerList = new ArrayList<TimerEntry>();

        timerPanel = new JPanel();
        timerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        jScrollPane1.getViewport().add(timerPanel);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        loadData();
        
        if (SoundPlayer.globalLoopOn == true)
            loopOnMenuItem.setEnabled(false);
        else
            loopOffMenuItem.setEnabled(false);
        
        if (globalTimerAutoRestartMode == true)
            timerAutoRestartOnMenuItem.setEnabled(false);
        else
            timerAutoRestartOffMenuItem.setEnabled(false);


        //system tray
        URL imageURL = mainGui.class.getResource("/resources/clock.png");
        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
        setIconImage(image);
        if (SystemTray.isSupported())
        {
            icon = new TrayIcon(image);
            icon.setToolTip("TomTimer");

            //action listener for icon (double click)
            icon.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    mainGui.this.setVisible(true);
                    mainGui.this.setExtendedState(mainGui.NORMAL);
                    SystemTray.getSystemTray().remove(icon);
                    mainGui.this.inputField.requestFocus();
                }
            });

            //mouse listener for icon (single click on any button restores window)
            icon.addMouseListener(new MouseAdapter()
            {

                @Override
                public void mousePressed(MouseEvent e)
                {
                    mainGui.this.setVisible(true);
                    mainGui.this.setExtendedState(mainGui.NORMAL);
                    SystemTray.getSystemTray().remove(icon);
                }
            });

            //window listener, to minimize to system tray (iconified)
            addWindowListener(new WindowAdapter()
            {

                @Override
                public void windowIconified(WindowEvent e)
                {
                    mainGui.this.setVisible(false);
                    try
                    {
                        SystemTray.getSystemTray().add(icon);
                        isMinimized = true;
                    } catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            });
        } //end system tray supported

        //Global key listener:
        //modify the keyboard manager to support a global key listener, to capture key event when ANY component has focus, not just one JButton.
        KeyboardFocusManager manager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());

    }

    //Global key listener's custom key dispatcher
    class KeyDispatcher implements KeyEventDispatcher
    {

        public boolean dispatchKeyEvent(KeyEvent e)
        {
            if (e.getID() == KeyEvent.KEY_PRESSED) //When user presses 'Esc' key
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    inputField.requestFocus(); //move the focus to the input text field
            
            return false;
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        timerSettingsDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        timerRestartCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        soundModeCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        customSoundField = new javax.swing.JTextField();
        chooseCustomSoundButton = new javax.swing.JButton();
        timerSettingsOkButton = new javax.swing.JButton();
        timerSettingsCancelButton = new javax.swing.JButton();
        clearCustomSoundButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        timerNoteTextField = new javax.swing.JTextField();
        inputField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        timerAutoRestartOnMenuItem = new javax.swing.JMenuItem();
        timerAutoRestartOffMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemSelectAudio = new javax.swing.JMenuItem();
        jMenuItemTestAudio = new javax.swing.JMenuItem();
        clearAudioMenuItem = new javax.swing.JMenuItem();
        loopOptionsMenu = new javax.swing.JMenu();
        loopOnMenuItem = new javax.swing.JMenuItem();
        loopOffMenuItem = new javax.swing.JMenuItem();

        timerSettingsDialog.setTitle("Timer Settings");
        timerSettingsDialog.setAlwaysOnTop(true);
        timerSettingsDialog.setLocationByPlatform(true);
        timerSettingsDialog.setMinimumSize(new java.awt.Dimension(289, 350));
        timerSettingsDialog.setPreferredSize(new java.awt.Dimension(289, 350));

        jLabel1.setText("Timer Auto Restart:");

        timerRestartCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Off", "On" }));

        jLabel2.setText("Sound Mode:");

        soundModeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Play once", "Loop until dismissed" }));

        jLabel3.setText("Custom Sound:");

        customSoundField.setText("Select a .wav file");

        chooseCustomSoundButton.setText("Select Sound");
        chooseCustomSoundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chooseCustomSoundButtonActionPerformed(evt);
            }
        });

        timerSettingsOkButton.setText("OK");
        timerSettingsOkButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                timerSettingsOkButtonActionPerformed(evt);
            }
        });

        timerSettingsCancelButton.setText("Cancel");
        timerSettingsCancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                timerSettingsCancelButtonActionPerformed(evt);
            }
        });

        clearCustomSoundButton.setText("Clear");
        clearCustomSoundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clearCustomSoundButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Timer Note:");

        timerNoteTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                timerNoteTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout timerSettingsDialogLayout = new javax.swing.GroupLayout(timerSettingsDialog.getContentPane());
        timerSettingsDialog.getContentPane().setLayout(timerSettingsDialogLayout);
        timerSettingsDialogLayout.setHorizontalGroup(
            timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timerSettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timerRestartCombo, 0, 269, Short.MAX_VALUE)
                    .addComponent(soundModeCombo, 0, 269, Short.MAX_VALUE)
                    .addComponent(customSoundField, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                    .addComponent(timerNoteTextField)
                    .addGroup(timerSettingsDialogLayout.createSequentialGroup()
                        .addGroup(timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(timerSettingsDialogLayout.createSequentialGroup()
                                .addComponent(chooseCustomSoundButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearCustomSoundButton))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(timerSettingsDialogLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(timerSettingsDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(timerSettingsOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timerSettingsCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        timerSettingsDialogLayout.setVerticalGroup(
            timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timerSettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerNoteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerRestartCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(soundModeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customSoundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseCustomSoundButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearCustomSoundButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(timerSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timerSettingsOkButton)
                    .addComponent(timerSettingsCancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Timer");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
        });

        inputField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        inputField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                inputFieldActionPerformed(evt);
            }
        });
        inputField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                inputFieldFocusGained(evt);
            }
        });

        addButton.setText("Add");
        addButton.setMargin(new java.awt.Insets(2, 1, 2, 1));
        addButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addButtonActionPerformed(evt);
            }
        });
        addButton.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                addButtonKeyPressed(evt);
            }
        });

        jScrollPane1.setAlignmentX(0.0F);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(4, 4));

        jMenu2.setText("Timer Options");

        jMenu3.setText("Auto Restart Mode");

        timerAutoRestartOnMenuItem.setText("On");
        timerAutoRestartOnMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                timerAutoRestartOnMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(timerAutoRestartOnMenuItem);

        timerAutoRestartOffMenuItem.setText("Off");
        timerAutoRestartOffMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                timerAutoRestartOffMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(timerAutoRestartOffMenuItem);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Sound Options");

        jMenuItemSelectAudio.setText("Select Audio");
        jMenuItemSelectAudio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemSelectAudioActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemSelectAudio);

        jMenuItemTestAudio.setText("Test Audio");
        jMenuItemTestAudio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemTestAudioActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemTestAudio);

        clearAudioMenuItem.setText("Clear Audio");
        clearAudioMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clearAudioMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(clearAudioMenuItem);

        loopOptionsMenu.setText("Loop Options");

        loopOnMenuItem.setText("On");
        loopOnMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loopOnMenuItemActionPerformed(evt);
            }
        });
        loopOptionsMenu.add(loopOnMenuItem);

        loopOffMenuItem.setText("Off");
        loopOffMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loopOffMenuItemActionPerformed(evt);
            }
        });
        loopOptionsMenu.add(loopOffMenuItem);

        jMenu1.add(loopOptionsMenu);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
    {//GEN-HEADEREND:event_addButtonActionPerformed
        addTimer();
    }//GEN-LAST:event_addButtonActionPerformed

    private void jMenuItemSelectAudioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSelectAudioActionPerformed
    {//GEN-HEADEREND:event_jMenuItemSelectAudioActionPerformed
        // Select a global sound file
//        JFileChooser filechooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.WAV files", "wav");
//        filechooser.setFileFilter(filter);
//
//        int returnVal = filechooser.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION)
//        {
//            SoundPlayer.globalSoundFile = filechooser.getSelectedFile();
//        }

        FileDialog fileDialog;
        fileDialog = new FileDialog(this, "Choose file(s)", FileDialog.LOAD);
        fileDialog.setFile("*.wav"); //filter .wav files only
        fileDialog.setVisible(true);

        String temp = fileDialog.getFile();
        if (temp != null)
        {
            String directory = fileDialog.getDirectory();
            SoundPlayer.globalSoundFile = new File(directory + temp);
        }

    }//GEN-LAST:event_jMenuItemSelectAudioActionPerformed

    private void jMenuItemTestAudioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemTestAudioActionPerformed
    {//GEN-HEADEREND:event_jMenuItemTestAudioActionPerformed
        //play alarm sound
        (new Thread(new SoundPlayer(true))).start(); //true = play audio once only.
    }//GEN-LAST:event_jMenuItemTestAudioActionPerformed

    private void inputFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_inputFieldActionPerformed
    {//GEN-HEADEREND:event_inputFieldActionPerformed
        addTimer();
    }//GEN-LAST:event_inputFieldActionPerformed

    private void inputFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_inputFieldFocusGained
    {//GEN-HEADEREND:event_inputFieldFocusGained
        inputField.selectAll();
    }//GEN-LAST:event_inputFieldFocusGained

    private void loopOnMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loopOnMenuItemActionPerformed
    {//GEN-HEADEREND:event_loopOnMenuItemActionPerformed
        SoundPlayer.globalLoopOn = true;
        loopOffMenuItem.setEnabled(true);
        loopOnMenuItem.setEnabled(false);
    }//GEN-LAST:event_loopOnMenuItemActionPerformed

    private void loopOffMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loopOffMenuItemActionPerformed
    {//GEN-HEADEREND:event_loopOffMenuItemActionPerformed
        SoundPlayer.globalLoopOn = false;
        loopOffMenuItem.setEnabled(false);
        loopOnMenuItem.setEnabled(true);
    }//GEN-LAST:event_loopOffMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        cleanUpTimerList();
        saveData();
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void addButtonKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_addButtonKeyPressed
    {//GEN-HEADEREND:event_addButtonKeyPressed
        //int keyCode = evt.getKeyCode();
        //if (keyCode == KeyEvent.VK_ENTER)
        //addTimer();
    }//GEN-LAST:event_addButtonKeyPressed

    private void timerSettingsOkButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_timerSettingsOkButtonActionPerformed
    {//GEN-HEADEREND:event_timerSettingsOkButtonActionPerformed
        // User clicks ok button on the "edit timer" dialog window
        saveTimerEdits();
    }//GEN-LAST:event_timerSettingsOkButtonActionPerformed

    private void timerSettingsCancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_timerSettingsCancelButtonActionPerformed
    {//GEN-HEADEREND:event_timerSettingsCancelButtonActionPerformed
        timerSettingsDialog.setVisible(false);
        //TOM reset the combo boxes and w/e?
       
    }//GEN-LAST:event_timerSettingsCancelButtonActionPerformed

    private void chooseCustomSoundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chooseCustomSoundButtonActionPerformed
    {//GEN-HEADEREND:event_chooseCustomSoundButtonActionPerformed
        //launch file chooser for custom sound
        /*JFileChooser filechooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.WAV files", "wav");
        filechooser.setFileFilter(filter);

        int returnVal = filechooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            customSound = filechooser.getSelectedFile();
            customSoundField.setText(customSound.getName());
        }*/
        FileDialog fileDialog;
        fileDialog = new FileDialog(this, "Choose file(s)", FileDialog.LOAD);
        fileDialog.setFile("*.wav"); //filter .wav files only
        fileDialog.setVisible(true);
        
        String temp = fileDialog.getFile();
        if (temp != null)
        {
            String directory = fileDialog.getDirectory();
            customSound = new File(directory + temp);
            String fileName = fileDialog.getFile();
            customSoundField.setText(fileName);
        }
    }//GEN-LAST:event_chooseCustomSoundButtonActionPerformed

    private void timerAutoRestartOnMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_timerAutoRestartOnMenuItemActionPerformed
    {//GEN-HEADEREND:event_timerAutoRestartOnMenuItemActionPerformed
        globalTimerAutoRestartMode = true;
        timerAutoRestartOnMenuItem.setEnabled(false);
        timerAutoRestartOffMenuItem.setEnabled(true);
        
    }//GEN-LAST:event_timerAutoRestartOnMenuItemActionPerformed

    private void timerAutoRestartOffMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_timerAutoRestartOffMenuItemActionPerformed
    {//GEN-HEADEREND:event_timerAutoRestartOffMenuItemActionPerformed
        globalTimerAutoRestartMode = false;
        timerAutoRestartOnMenuItem.setEnabled(true);
        timerAutoRestartOffMenuItem.setEnabled(false);
    }//GEN-LAST:event_timerAutoRestartOffMenuItemActionPerformed

    private void clearAudioMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearAudioMenuItemActionPerformed
    {//GEN-HEADEREND:event_clearAudioMenuItemActionPerformed
        SoundPlayer.globalSoundFile = null;
    }//GEN-LAST:event_clearAudioMenuItemActionPerformed

    private void clearCustomSoundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearCustomSoundButtonActionPerformed
    {//GEN-HEADEREND:event_clearCustomSoundButtonActionPerformed
       TimerEntry timer = timerList.get(timerIdToEdit);
       timer.setSoundFile(null);
       customSoundField.setText("Select .wav file");
    }//GEN-LAST:event_clearCustomSoundButtonActionPerformed

    private void timerNoteTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_timerNoteTextFieldActionPerformed
    {//GEN-HEADEREND:event_timerNoteTextFieldActionPerformed
        // User enters a timer note and presses enter key
        saveTimerEdits();
    }//GEN-LAST:event_timerNoteTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                new mainGui().setVisible(true);
            }
        });
    }

    public int getNextId()
    {
        for (int i = 0; i < timerList.size(); i++)
        {
            if (timerList.get(i) == null)
                return i;
        }

        //no empty slots
        timerList.add(null);
        return timerList.size() - 1; //index to add new TimerEntry into
    }

    public void removeTimerEntry(int idx)
    {
        TimerEntry temp;
        temp = timerList.get(idx);
        timerList.set(idx, null);
        JPanel tempPanel = temp.getJPanel();
        timerPanel.remove(tempPanel);
        timerPanel.revalidate();
        timerPanel.repaint();
    }

    //remove any null timers (empty spaces), then reassign id's to match index in arrayList
    private void cleanUpTimerList()
    {
        for (int i = 0; i < timerList.size(); i++)
        {
            if (timerList.get(i) == null)
            {
                timerList.remove(i);
                i--;
            }
        }

        for (int i = 0; i < timerList.size(); i++)
            timerList.get(i).setId(i);
    }

    private String padTwoDigits(String str)
    {
        if (str.length() == 1)
            str = "0" + str;
        return str;
    }

    private void loadData()
    {
        try
        {
            FileInputStream fin = new FileInputStream("TomTimerData.TOM");
            ObjectInputStream oin = new ObjectInputStream(fin);

            //load audio file
            try
            {
                File f = (File) oin.readObject();
                SoundPlayer.globalSoundFile = f;
            } catch (Exception e)
            {
                SoundPlayer.globalSoundFile = null;
            }

            //load sound continuous loop option
            try
            {
                boolean b = (Boolean) oin.readObject();
                SoundPlayer.globalLoopOn = b;
            } catch (Exception e)
            {
                SoundPlayer.globalLoopOn = false; //by default, do not loop
            }
            
            //load timer repeat mode
            try
            {
                boolean b = (Boolean) oin.readObject();
                globalTimerAutoRestartMode = b;
            } catch (Exception e)
            {
                globalTimerAutoRestartMode = false; //by default, do not loop
            }
            
            //load timers
            try
            {
                int size = (Integer) oin.readObject(); //number of timers to read in
                TimerEntry t;
                for (int i = 0; i < size; i++)
                {
                    t = (TimerEntry) oin.readObject();
                    if (t == null)
                        continue; //if computer shut down, then timer did not have a chance to clean up array list (remove nulls) and save. so ignore null timers.
                    t.setGui(this);
                    t.setId(i);
                    t.GUIConstruction();
                    timerList.add(t);
                    timerPanel.add(t.getJPanel());
                }

                if (timerList.size() != 0) //use timerList.size() b/c "size" may include nulls, which are not added to timerList.
                {
                    timerPanel.revalidate();
                    timerPanel.repaint();
                }
                
                //check if need to resume any timers:
                // must do this afterwards to prevent instantly resumed timers from saving the timerList again while in the middle of loading, would cause EOF exception.
                for (int i = 0; i < timerList.size(); i++)
                    timerList.get(i).checkIfNeedToResumeTimer();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
           
        } catch (Exception e) //no data if file not found
        {
            //no action necessary
        }
    }

    public void saveData()
    {
        try
        {
            FileOutputStream fout = new FileOutputStream("TomTimerData.TOM");
            ObjectOutputStream out = new ObjectOutputStream(fout);

            out.writeObject(SoundPlayer.globalSoundFile);
            out.writeObject(SoundPlayer.globalLoopOn);
            out.writeObject(globalTimerAutoRestartMode);
            out.writeObject(timerList.size());
            for (int i = 0; i < timerList.size(); i++)
                out.writeObject(timerList.get(i));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //when user clicks on timer, it will open and edit that specific timer's settings
    public void editTimerSettings(int id)
    {
        TimerEntry timer = timerList.get(id);
        
        // set the note textfield
        timerNoteTextField.setText(timer.getNote());
        
        //set the timer's repeat mode
        switch (timer.timerRepeatMode)
        {
            case DEFAULT:
                timerRestartCombo.setSelectedIndex(0);
                break;

            case SINGLE:
                timerRestartCombo.setSelectedIndex(1);
                break;
                
            case REPEAT:
                timerRestartCombo.setSelectedIndex(2);
                break;
        }
        
        //set the timer's sound repeat mode
        switch (timer.soundRepeatMode)
        {
            case DEFAULT:
                soundModeCombo.setSelectedIndex(0);
                break;
                
            case SINGLE:
                soundModeCombo.setSelectedIndex(1);
                break;
                
            case REPEAT:
                soundModeCombo.setSelectedIndex(2);
                break;
        }
        
        //set the timer's custom sound file if any, in the timerSettingsDialog textfield
        File soundFile = timer.getSoundFile();
        if (soundFile == null)
            customSoundField.setText("Select .wav file");
        else
            customSoundField.setText(soundFile.getName());
        
        Point p = this.getLocationOnScreen();
        timerSettingsDialog.setLocation(p);
        timerSettingsDialog.setVisible(true);
        timerIdToEdit = id;
    }
    
    
    private void saveTimerEdits()
    {
        //get settings from combo box1, 2, and File
        int timerRestart = timerRestartCombo.getSelectedIndex();
        int soundMode = soundModeCombo.getSelectedIndex();
        TimerEntry myTimer = timerList.get(timerIdToEdit);
        
        timerSettingsDialog.setVisible(false); // hide the settings panel
        
        // set the timer note
        myTimer.setNote(timerNoteTextField.getText());
        
        //set the timer repeat mode
        switch (timerRestart)
        {
            case 0: //default
                myTimer.timerRepeatMode = RepeatMode.DEFAULT;
                break;
                
            case 1:
                myTimer.timerRepeatMode = RepeatMode.SINGLE;
                break;
                
            case 2:
                myTimer.timerRepeatMode = RepeatMode.REPEAT;
                break;
        }
        
        //set the timer's sound repeat mode
        switch (soundMode)
        {
            case 0: //default
                myTimer.soundRepeatMode = RepeatMode.DEFAULT;
                break;
            case 1:
                myTimer.soundRepeatMode = RepeatMode.SINGLE;
                break;
            case 2:
                myTimer.soundRepeatMode = RepeatMode.REPEAT;
                break;
        }
        
        //set the timer's custom sound file
        if (customSound != null)
            myTimer.setSoundFile(customSound);

    }
    
    private void addTimer() 
    {
        TimerEntry timer = parseTimerText(inputField.getText());

        if (timer == null)
        {
            showErrorMessage(inputField.getText());
            return; //error parsing timer
        }

        timerList.set(timer.getId(), timer);
        timerPanel.add(timer.getJPanel());
        timerPanel.revalidate();
        timerPanel.repaint();

        inputField.setText("");

        timer.startTimer();
    }
    
    private TimerEntry parseTimerText(String input)
    {
        String tempNumArray = "";
        int hours = 0, minutes = 0; // alarm timer's hours/minutes
        String temp;
        int h = 0, m = 0, s = 0; //countdown timer's hour/minute/seconds

        boolean amPmSeen = false; //indicate if we have already seen am/pm, h, m, s, at least one time.
        boolean hSeen = false;
        boolean mSeen = false;
        boolean sSeen = false;

        TimerType myTimerType = TimerType.NULL; 

        int indexNoteStart = 0; //index that points to where the note starts
        String note;

        boolean stop = false;
        char c; //temp character for processing
        int index = 0; //used to iterate thru the input string

        while (index < input.length() && stop == false)
        {
            c = input.charAt(index);
            if (Character.isDigit(c))
            {
                tempNumArray = tempNumArray + c;
                index++;
                continue;
            }

            if (Character.isLetter(c)) //process "h", "m", "s", or "am" / "a", "pm" / "p"
            {
                c = Character.toLowerCase(c);
                switch (c)
                {
                    case 'a':  //handle am or pm alarms:
                    case 'p':
                        if (myTimerType != TimerType.NULL || amPmSeen == true)  //make sure we can set new timer
                        {
                            stop = true;
                            break;
                        }

                        myTimerType = TimerType.ALARM; //mark the alarm type
                        amPmSeen = true; // lock onto am/pm processing

                        //check if next character is 'm', if yes, skip over it.
                        if (index + 1 < input.length() - 1) //check for index out of bounds
                        {
                            char c2 = input.charAt(index + 1);
                            if (Character.isLetter(c2))
                                if (Character.toLowerCase(c2) == 'm')
                                    index++; //skip the "m" in "pm"
                        }
                        indexNoteStart = index + 1; //mark next index as the start of the note, even if it exceeds input.length()

                        //begin extracting the ALARM time
                        int timeLength = tempNumArray.length();
                        if (timeLength < 1 || timeLength > 4) // error checking, must be: 0 < #digits <= 4
                        {
                            stop = true;
                            return null;
                        }

                        if (timeLength == 1 || timeLength == 2)
                        {
                            hours = Integer.parseInt(tempNumArray); //parse hours
                        }

                        if (timeLength == 3)
                        {
                            temp = tempNumArray.substring(0, 1); //parse hours
                            hours = Integer.parseInt(temp);

                            temp = tempNumArray.substring(1, 3); //parse minutes
                            minutes = Integer.parseInt(temp);
                        }

                        if (timeLength == 4)
                        {
                            temp = tempNumArray.substring(0, 2); //parse hours
                            hours = Integer.parseInt(temp);

                            temp = tempNumArray.substring(2, 4); //parse minutes
                            minutes = Integer.parseInt(temp);
                        }

                        if (hours < 1 || hours > 12)
                            return null; //error in hours
                        if (minutes < 0 || minutes > 60)
                            return null; //error in minutes

                        //convert to 24h mode
                        if (c == 'p') //handle pm mode
                        {
                            if (hours < 12)
                                hours = hours + 12;
                        }

                        if (c == 'a') //handle am mode
                        {
                            if (hours == 12)
                                hours = 0;
                        }

                        break; //end of processing ALARM 

                    case 'h': //process count down timer.
                        if (myTimerType == TimerType.ALARM || hSeen == true)  //make sure this is first time processing "h"
                        {
                            stop = true;
                            break;
                        }

                        myTimerType = TimerType.COUNTDOWN;
                        hSeen = true;

                        try
                        {
                            h = Integer.parseInt(tempNumArray);
                            tempNumArray = "";
                            indexNoteStart = index + 1; //mark next index as the start of the note
                        } catch (Exception e)
                        {
                            tempNumArray = "";
                        }

                        break;

                    case 'm':
                        if (myTimerType == TimerType.ALARM || mSeen == true)  //make sure this is first time processing "m"
                        {
                            stop = true;
                            break;
                        }

                        myTimerType = TimerType.COUNTDOWN;
                        mSeen = true;

                        try
                        {
                            m = Integer.parseInt(tempNumArray);
                            tempNumArray = "";
                            indexNoteStart = index + 1; //mark next index as the start of the note
                        } catch (Exception e)
                        {
                            tempNumArray = "";
                        }
                        break;

                    case 's':
                        if (myTimerType == TimerType.ALARM || sSeen == true)  //make sure this is first time processing "s"
                        {
                            stop = true;
                            break;
                        }

                        myTimerType = TimerType.COUNTDOWN;
                        sSeen = true;

                        try 
                        {
                            s = Integer.parseInt(tempNumArray);
                            tempNumArray = "";
                            indexNoteStart = index + 1; //mark next index as the start of the note
                        } catch (Exception e) 
                        {
                            tempNumArray = "";
                        }
                        break;

                    default:
                        stop = true; //stop parsing time units from input string.
                } //end switch
            }

            index++;
        } //end while loop

        // ------- extract the note string here (if any note), and ignore spaces in front of note string:
        if (indexNoteStart < input.length() - 1) //check for index out of bounds
        {
            c = input.charAt(indexNoteStart);
            while (c == ' ')
            {
                indexNoteStart++;
                c = input.charAt(indexNoteStart);
            }
            note = input.substring(indexNoteStart);
        } else
            note = "";
        // ------ create the TimerEntry object
        if (myTimerType == TimerType.ALARM)
        {
            int id = getNextId();
            TimerEntry timer = new TimerEntry(this, id, hours, minutes, note);
            return timer;
        }

        if (myTimerType == TimerType.COUNTDOWN)
        {
            int id = getNextId();
            TimerEntry timer = new TimerEntry(this, id, h, m, s, note);
            return timer;
        }

        //System.out.println("Fatal Parsing error in time parser");
        return null;
    }

    public void bringToFront()
    {
        toFront();
    }

    //restore window if it has been minimized to system tray
    public void restoreWindow()
    {
        if (SystemTray.isSupported() == false)
            return; //return if system tray is not applicable

        isMinimized = false;
        mainGui.this.setVisible(true);
        mainGui.this.setExtendedState(mainGui.NORMAL);
        SystemTray.getSystemTray().remove(icon);
    }
    
    // move main gui into bottom right corner
    private void setMainGuiPosition()
    {
        // Get the screen size  
        GraphicsConfiguration gc = this.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();

        // Set the Location and Activate  
        Dimension size = this.getPreferredSize();
        this.setLocation((int) ((bounds.width) - (size.getWidth())), (int) ((bounds.height) - (size.getHeight()) - 30)); //constant is for taskbar adjustment
    }
    
    //show an error message in the input text box, and play a sound
    private void showErrorMessage(String debug)
    {
        inputField.setText("Error: " + debug);
        java.awt.Toolkit.getDefaultToolkit().beep();
        inputField.selectAll();
    }
    
    public void myBringToFront() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(this != null) {
                    toFront();
                    repaint();
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton chooseCustomSoundButton;
    private javax.swing.JMenuItem clearAudioMenuItem;
    private javax.swing.JButton clearCustomSoundButton;
    private javax.swing.JTextField customSoundField;
    private javax.swing.JTextField inputField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemSelectAudio;
    private javax.swing.JMenuItem jMenuItemTestAudio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem loopOffMenuItem;
    private javax.swing.JMenuItem loopOnMenuItem;
    private javax.swing.JMenu loopOptionsMenu;
    private javax.swing.JComboBox soundModeCombo;
    private javax.swing.JMenuItem timerAutoRestartOffMenuItem;
    private javax.swing.JMenuItem timerAutoRestartOnMenuItem;
    private javax.swing.JTextField timerNoteTextField;
    private javax.swing.JComboBox timerRestartCombo;
    private javax.swing.JButton timerSettingsCancelButton;
    private javax.swing.JDialog timerSettingsDialog;
    private javax.swing.JButton timerSettingsOkButton;
    // End of variables declaration//GEN-END:variables
}
