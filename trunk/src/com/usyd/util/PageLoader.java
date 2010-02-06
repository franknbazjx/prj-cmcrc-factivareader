/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author yy
 */
public class PageLoader {

    private static String getStringFromStream(InputStream is) throws IOException {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);

            }
            is.close();
        } catch (IOException ioe) {
        }
        return sb.toString();
    }

    public static String getPage(GetMethod get) throws IOException {

        InputStream is = null;
        is = get.getResponseBodyAsStream();
        return getStringFromStream(is);
    }

    public static String getPage(PostMethod post) throws IOException {
        InputStream is = null;
        is = post.getResponseBodyAsStream();
        return getStringFromStream(is);
    }
}
