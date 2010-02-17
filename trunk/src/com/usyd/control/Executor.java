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
    protected Object obj;
    
    public Executor() {
        lock = new Object();
        obj = null;
    }
    public void run() {
    }

    public Object getLock() {
        return lock;
    }

    public Object getObj(){
        return obj;
    }
}
