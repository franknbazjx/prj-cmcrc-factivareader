/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

import com.usyd.exception.TimeOutException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author yy
 */
public class PageLoader {

    static final long TIME_OUT = 60000;

    private static String getStringFromStream(InputStream is) throws IOException, TimeOutException {
        int bytesRead = 0;
        byte[] buffer = new byte[256];
        StringBuffer sb = new StringBuffer();
        InputStream bis = new BufferedInputStream(is);
        long startTime = System.currentTimeMillis();
        while ((bytesRead = bis.read(buffer)) != -1) {
            if(System.currentTimeMillis() - startTime > TIME_OUT){
                throw new TimeOutException();
            }
            String chunk = new String(buffer, 0, bytesRead);
            sb.append(chunk);
        }
        return sb.toString();
    }

    public static String getPage(GetMethod get) throws IOException, TimeOutException {

        InputStream is = null;
        is = get.getResponseBodyAsStream();
        return getStringFromStream(is);
    }

    public static String getPage(PostMethod post) throws IOException, TimeOutException {
        InputStream is = null;
        is = post.getResponseBodyAsStream();
        return getStringFromStream(is);
    }
}
