/*
 * MainFrame.java
 *
 * Created on 27/01/2010, 10:21:24 PM
 */
package com.usyd.factiva;

import com.usyd.log.Appender;
import com.usyd.log.ProgressUpdater;
import com.usyd.action.FactivaSearch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author min
 */
public class MainFrame extends javax.swing.JFrame {

    private List<String> companies;

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        com.usyd.log.Logger.registerAppender(new Appender() {

            public void append(final String msg) {
                Runnable doUpdate = new Runnable() {

                    public void run() {
                        int lines = 1000;
                        textArea.append(msg);
                        Document doc = textArea.getDocument();
                        int cnt = textArea.getLineCount();
                        int diff = cnt - lines;
                        if (diff > 0) {
                            try {
                                doc.remove(0, textArea.getLineEndOffset(diff - 1));
                            } catch (BadLocationException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        textArea.setCaretPosition(textArea.getText().length());
                    }
                };
                SwingUtilities.invokeLater(doUpdate);
            }
        });

        com.usyd.log.Logger.registerProgressUpdater(new ProgressUpdater() {

            public void finished(final int total, final int finished, final String name) {
                Runnable doUpdate = new Runnable() {

                    public void run() {
                        pBar.setMaximum(total);
                        pBar.setValue(finished);
//                        pBar.setToolTipText( "retrieving articles about '" + name + "'");
                        pBar.setString(name + " (" + finished + "/" + total + ")");
                    }
                };
                SwingUtilities.invokeLater(doUpdate);
            }
        });
        pBar.setValue(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        openBtn = new javax.swing.JButton();
        startBtn = new javax.swing.JButton();
        pBar = new javax.swing.JProgressBar();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Factiva");

        textArea.setColumns(20);
        textArea.setEditable(false);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        openBtn.setText("Open CSV File");
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });

        startBtn.setText("Start");
        startBtn.setEnabled(false);
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Fuzzy Search");
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(openBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pBar, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(openBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(startBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("csv files", "csv");
        fileopen.addChoosableFileFilter(filter);
        fileopen.setCurrentDirectory(new File("."));

        int ret = fileopen.showDialog(this, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            csvFile = fileopen.getSelectedFile();
            try {
                BufferedReader rd = new BufferedReader(new FileReader(csvFile));
                String line;
                StringBuffer lines = new StringBuffer();
                companies = new ArrayList<String>();
                while ((line = rd.readLine()) != null) {
                    companies.add(line);
                    lines.append(line);
                    lines.append(System.getProperty("line.separator"));
                }
                //textArea.setText(lines.toString());
                startBtn.setEnabled(true);

            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openBtnActionPerformed
    SwingWorker worker;
    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        PasswordPanel passPanel = new PasswordPanel();
        int action = JOptionPane.showConfirmDialog(null, passPanel, "Enter User Name and Password", JOptionPane.OK_CANCEL_OPTION);
        if (action < 0) {
            return;
        } else {
            user = passPanel.getUser();
            pass = passPanel.getPassword();
            if (user.trim().length() == 0 || pass.trim().length() == 0) {
                return;
            }
        }

        startBtn.setEnabled(false);
        pBar.setStringPainted(true);

        worker = new SwingWorker<String, Void>() {

            @Override
            public String doInBackground() throws InterruptedException {
                FactivaSearch search = new FactivaSearch(companies, user,pass);
                search.start(jCheckBox1.isSelected());
                return null;
            }

            @Override
            public void done() {
//                System.out.println("done!");
            }
        };
        worker.execute();
    }//GEN-LAST:event_startBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton openBtn;
    private javax.swing.JProgressBar pBar;
    private javax.swing.JButton startBtn;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
    private File csvFile;
    private String user;
    private String pass;
}