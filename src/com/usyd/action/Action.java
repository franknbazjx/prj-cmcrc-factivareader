/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.control.Controller;
import com.usyd.control.Executor;
import com.usyd.control.GetAction;
import com.usyd.control.PostAction;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class Action {

    protected HttpClient httpClient;

    private String controllabeRunning(Executor executor){
        Controller controller = new Controller(executor);
        Thread thread = new Thread(controller);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            System.out.println("Controller finished");
        }
        return executor.getRsp();
    }


    protected String getPostContent(String url, NameValuePair[] data) {

        Executor executor = new PostAction(url, data, httpClient);
        String rsp = controllabeRunning(executor);
        return rsp;
    }

    protected String getGetContent(String url) {

        Executor executor = new GetAction(url, httpClient);
        String rsp = controllabeRunning(executor);
        return rsp;
    }
}
