/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.control.Controller;
import com.usyd.control.Executor;
import com.usyd.control.GetAction;
import com.usyd.control.GetNews;
import com.usyd.control.PostAction;
import com.usyd.page.NewsItemExtractor;
import com.usyd.unit.NewsUnit;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class Action {

    protected HttpClient httpClient;

    private Object controllabeRunning(Executor executor){
        System.out.println(">>");
        Controller controller = new Controller(executor);
        Thread thread = new Thread(controller);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            System.out.println("Controller finished");
        }
        System.out.println("<<");
        return executor.getObj();
    }


    protected String getPostContent(String url, NameValuePair[] data) {

        Executor executor = new PostAction(url, data, httpClient);
        Object obj = controllabeRunning(executor);
        if(obj != null){
            return (String)obj;
        } else {
            return "";
        }
    }

    protected String getGetContent(String url) {

        Executor executor = new GetAction(url, httpClient);
        Object obj = controllabeRunning(executor);
        if(obj != null){
            return (String)obj;
        } else {
            return "";
        }
    }

    protected NewsUnit getNewsUnit(NewsItemExtractor extractor){
        Executor executor = new GetNews(extractor);
        Object obj = controllabeRunning(executor);
        if(obj != null){
            return (NewsUnit)obj;
        } else {
            return null;
        }
    }

}
