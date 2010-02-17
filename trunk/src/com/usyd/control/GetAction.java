/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.control;

import com.usyd.util.PageLoader;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author yangyang
 */
public class GetAction extends Executor {

    private String url;
    private HttpClient httpClient;

    public GetAction(String url, HttpClient httpClient) {
        super();
        this.url = url;
        this.httpClient = httpClient;
    }

    @Override
    public void run() {

        GetMethod get = new GetMethod(url);
        try {
            httpClient.executeMethod(get);
            System.out.println("execute get!");
            obj = PageLoader.getPage(get);
            System.out.println("load done!");
            synchronized (lock) {
                lock.notify();
            }
        } catch (IOException ex) {
        } finally {
            get.releaseConnection();
            System.out.println("release connection");
        }
    }
}
