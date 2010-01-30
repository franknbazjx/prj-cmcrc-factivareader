/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author yy
 */
public class Logger {

    private static List<Appender> appenders = new ArrayList<Appender>();
    private static ProgressUpdater progress = null;
    private static StringBuilder sb = new StringBuilder();

    public static void log(String str) {
        try {
            for (Appender apd : appenders) {
                apd.append(str);
            }
            if (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1);
            }
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        sb.append(str);

        if (sb.length() > 2000) {
            write(sb.toString(), new File("log.txt"), true);
            sb.setLength(0);
        }
    }

    public static void registerAppender(Appender apd) {
        appenders.add(apd);
    }

    public static void updateProgress(int total, int finished, String name) {
        try {
            progress.finished(total, finished, name);
            if (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1);
            }
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void registerProgressUpdater(ProgressUpdater progress) {
        Logger.progress = progress;
    }

    public static void flush() {
        write(sb.toString(), new File("log.txt"), true);
        sb.setLength(0);
    }

    private static void write(String str, File file, boolean append) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
            String[] lines = str.split("\n");
            int counter = 1;
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
                if (counter++ % 500 == 0) {
                    bw.flush();
                }
            }
            bw.close();
        } catch (IOException ioe) {
        }
    }

    public static void store(String str, String fileName) {
        write(str, new File(fileName), true);
    }

    public static void error(String str) {
        flush();
        write(str, new File("error_" + System.currentTimeMillis() + ".txt"), false);
    }

    public static void miss(String name) {
        write(name, new File("missing.txt"), true);
    }
}
