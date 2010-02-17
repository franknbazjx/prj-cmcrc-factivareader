/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.control;

import com.usyd.util.PageLoader;
import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author yangyang
 */
public class PostAction extends Executor {
    private String url;
    private HttpClient httpClient;
    private NameValuePair[] data;

    public PostAction(String url, NameValuePair[] data, HttpClient httpClient) {
        super();
        this.url = url;
        this.httpClient = httpClient;
        this.data = data;
    }

    @Override
    public void run() {

        PostMethod post = new PostMethod(url);
        post.setRequestBody(data);
        post.setRequestHeader(new Header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));
        try {
            httpClient.executeMethod(post);
            System.out.println("execute post!");
            obj = PageLoader.getPage(post);
            System.out.println("load done!");
            synchronized (lock) {
                lock.notify();
            }
        } catch (IOException ex) {
        } finally {
            post.releaseConnection();
            System.out.println("release connection");
        }
    }
}
