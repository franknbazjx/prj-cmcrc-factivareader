/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
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

    public static void log(String str) {

        if (!str.startsWith(".")) {

            Time date = new Time(System.currentTimeMillis());
            str = "[" + date.toString() + "] " + str;
        }
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
        write(str, new File("log/log.txt"), true);
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
        System.out.println("store in " + fileName);
        write(str, new File(fileName), true);
    }

    public static void error(String str) {
        write(str, new File("log/error_" + System.currentTimeMillis() + ".txt"), false);
    }

    public static void miss(String name) {
        write(name, new File("log/missing.csv"), true);
    }
}
