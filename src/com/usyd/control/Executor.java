/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.control;

/**
 *
 * @author yangyang
 */
public abstract class Executor implements Runnable {

    protected final Object lock;
    protected String rsp;
    public Executor() {
        lock = new Object();
        rsp = "";
    }
    public void run() {
    }

    public Object getLock() {
        return lock;
    }

    public String getRsp() {
        return rsp;
    }
}
