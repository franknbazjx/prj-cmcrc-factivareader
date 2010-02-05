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

        //Logger.log("Post: " + url + "\n");
        PostMethod post = new PostMethod(url);
        post.setRequestBody(data);
        post.setRequestHeader(new Header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));
        try {
            httpClient.executeMethod(post);
            String rsp = PageLoader.getPage(post);
            //Logger.log("done!\n");
            return rsp;
        } catch (IOException ex) {
            Logger.log("##\ttimeout!\n");
            return "";
        }
    }

    protected String getGetContent(String url) {


        //Logger.log("Get: " + url + "\n");
        GetMethod get = new GetMethod(url);
        try {
            httpClient.executeMethod(get);
            String rsp = PageLoader.getPage(get);
            //Logger.log("done!\n");
            return rsp;
        } catch (IOException ex) {
            Logger.log("##\ttimeout!\n");
            return "";
        }
    }
}
