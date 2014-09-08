package org.ninjacave.jarsplice.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.ninjacave.jarsplice.core.WinExeSplicer;

public class WinExePanel extends JPanel
        implements ActionListener {
    JFileChooser fileChooser;
    JButton winExeButton;
    JarSpliceFrame jarSplice;
    WinExeSplicer winExeSplicer = new WinExeSplicer();

    public WinExePanel (JarSpliceFrame jarSplice) {
        this.jarSplice = jarSplice;

        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        this.fileChooser = getFileChooser();

        setLayout(new BorderLayout(5, 20));

        TitledBorder border = BorderFactory.createTitledBorder("Create EXE file for Windows");
        border.setTitleJustification(TitledBorder.CENTER);
        setBorder(border);

        add(createAppPanel(), "First");

        add(createButtonPanel(), "Center");
    }

    private JPanel createAppPanel () {
        JPanel descriptionPanel = new JPanel();
        JLabel label = new JLabel();
        label.setText(
                String.format("<html><div style=\"width:%dpx;\">%s</div><html>",
                        300,
                        "This is an optional step and will create a Windows EXE File. "));

        descriptionPanel.add(label);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 20));

        panel.add(descriptionPanel, "First");

        return panel;
    }

    private JPanel createButtonPanel () {
        JPanel buttonPanel = new JPanel();
        this.winExeButton = new JButton("Create Windows EXE file");
        this.winExeButton.addActionListener(this);
        buttonPanel.add(this.winExeButton);

        return buttonPanel;
    }

    public String getOutputFile (File file) {
        String outputFile = file.getAbsolutePath();

        if (!outputFile.endsWith(".exe")) {
            outputFile = outputFile + ".exe";
        }

        return outputFile;
    }

    private JFileChooser getFileChooser () {
        this.fileChooser = new JFileChooser() {
            public void approveSelection () {
                File f = getSelectedFile();
                if ((f.exists()) && (getDialogType() == JFileChooser.SAVE_DIALOG)) {
                    int result =
                            JOptionPane.showConfirmDialog(
                                    this, "The file already exists. Do you want to overwrite it?",
                                    "Confirm Replace", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                        case 0:
                            super.approveSelection();
                            return;
                        case 1:
                            return;
                        case 2:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        this.fileChooser.setAcceptAllFileFilterUsed(false);

        FileFilter filter = new FileFilter() {
            public boolean accept (File file) {
                if (file.isDirectory()) return true;
                String filename = file.getName();
                return filename.endsWith(".exe");
            }

            public String getDescription () {
                return "*.exe";
            }
        };
        this.fileChooser.setFileFilter(filter);

        return this.fileChooser;
    }

    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == this.winExeButton) {
            this.fileChooser.setCurrentDirectory(this.jarSplice.lastDirectory);
            int value = this.fileChooser.showSaveDialog(this);
            this.jarSplice.lastDirectory = this.fileChooser.getCurrentDirectory();

            if (value == 0) {
                String[] sources = this.jarSplice.getJarsList();
                String[] natives = this.jarSplice.getNativesList();
                String output = getOutputFile(this.fileChooser.getSelectedFile());
                String mainClass = this.jarSplice.getMainClass();
                String vmArgs = this.jarSplice.getVmArgs();
                try {
                    this.winExeSplicer.createFatJar(sources, natives, output, mainClass, vmArgs);

                    JOptionPane.showMessageDialog(this,
                            "EXE Successfully Created.",
                            "Success", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "EXE creation failed due to the following exception:\n" + ex.getMessage(),
                            "Failed", JOptionPane.ERROR_MESSAGE);
                }

                System.out.println("File Saved as " + output);
            }
        }
    }
}
