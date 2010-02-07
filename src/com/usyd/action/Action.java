/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.log.Logger;
import com.usyd.util.PageLoader;
import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author yy
 */
public abstract class Action {

    protected HttpClient httpClient;

    protected String getPostContent(String url, NameValuePair[] data) {

        System.out.println("Post: " + url + "");
        PostMethod post = new PostMethod(url);
        post.setRequestBody(data);
        post.setRequestHeader(new Header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));
        try {
            httpClient.executeMethod(post);
            System.out.println("execute done!");
            String rsp = PageLoader.getPage(post);
            System.out.println("load done!");
            return rsp;
        } catch (IOException ex) {
            Logger.log("##\ttimeout!\n");
            return "";
        } finally {
            post.releaseConnection();
        }
    }

    protected String getGetContent(String url) {

        System.out.println("Get: " + url + "");
        GetMethod get = new GetMethod(url);
        try {
            httpClient.executeMethod(get);
            System.out.println("execute done!");
            String rsp = PageLoader.getPage(get);
            System.out.println("load done!");
            return rsp;
        } catch (IOException ex) {
            Logger.log("##\ttimeout!");
            return "";
        } finally {
            get.releaseConnection();
            System.out.println("release connection\n");
        }
    }

    protected int reset(int time, Login login) {

        if (time > 600) {
            Logger.log("WARNING: no connection available, mission failed, reset the timer!\n");
            time = 1;
        }
        String text2 = "NOTICE: Get a new token in " + time + " secs..." + "\n\n";

        Logger.log(text2);

        try {

            Thread.sleep(time * 1000);
            httpClient = login.getHttpclient();
        } catch (Exception e) {
        }
        return time * 2;
    }
}
