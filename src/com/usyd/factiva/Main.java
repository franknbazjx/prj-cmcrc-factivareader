package com.usyd.factiva;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Hello world!
 *
 */
public class Main {

    public static void main(String argv[]) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                MainFrame frm = new MainFrame();
                frm.pack();
                frm.setLocationRelativeTo(null);
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.setVisible(true);
            }
        });
    }
}
