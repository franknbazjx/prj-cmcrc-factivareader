/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

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



    private static String getStringFromStream(InputStream is){
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        StringBuffer sb = new StringBuffer();
        InputStream bis = new BufferedInputStream(is);
        try {
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, bytesRead);
                sb.append(chunk);
            }
        } catch (IOException ioe) {
        }
        return sb.toString();
    }

    public static String getPage(GetMethod get) {

        InputStream is = null;
        try {
            is = get.getResponseBodyAsStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return getStringFromStream(is);
    }

    public static String getPage(PostMethod post){
        InputStream is = null;
        try {
            is = post.getResponseBodyAsStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return getStringFromStream(is);
    }
}
