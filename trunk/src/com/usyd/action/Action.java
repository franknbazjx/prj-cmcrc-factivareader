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
public abstract class Action {

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
//        System.out.println("Post: " + url + "");
//        PostMethod post = new PostMethod(url);
//        post.setRequestBody(data);
//        post.setRequestHeader(new Header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));
//        try {
//            httpClient.executeMethod(post);
//            System.out.println("execute done!");
//            String rsp = PageLoader.getPage(post);
//            System.out.println("load done!");
//            return rsp;
//        } catch (IOException ex) {
//            Logger.log("##\ttimeout!\n");
//            return "";
//        } finally {
//            post.releaseConnection();
//        }
    }

    protected String getGetContent(String url) {

        Executor executor = new GetAction(url, httpClient);
        String rsp = controllabeRunning(executor);
        return rsp;
//        System.out.println("Get: " + url + "");
//        GetMethod get = new GetMethod(url);
//        try {
//            httpClient.executeMethod(get);
//            System.out.println("execute done!");
//            String rsp = PageLoader.getPage(get);
//            System.out.println("load done!");
//            return rsp;
//        } catch (IOException ex) {
//            Logger.log("##\ttimeout!");
//            return "";
//        } finally {
//            get.releaseConnection();
//            System.out.println("release connection\n");
//        }
    }
}
