/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.log;

/**
 *
 * @author miny
 */
public interface ProgressUpdater {
    public void finished(int total, int finished, String name);
}
