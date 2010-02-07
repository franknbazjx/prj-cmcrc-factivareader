/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.control;

import com.usyd.log.Logger;

/**
 *
 * @author yangyang
 */






public class Controller implements Runnable {
    private final int TIMEOUT = 30000;
    private Executor executor;
    private final Object lock;

    public Controller(Executor executor) {
        this.executor = executor;
        lock = executor.getLock();
    }

    public void run() {

        Thread thread = new Thread(executor);
        thread.start();
        synchronized(lock){
            try {
                lock.wait(TIMEOUT);
                lock.wait(100);
                if(thread.isAlive()){
                    Logger.log("WARNING: timeout, requesting thread is killed");
                    thread.stop();
                    thread = null;
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}
