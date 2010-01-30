/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class FileLoader {

    public static String getFileString(String fileName) {

        StringBuffer fileString = new StringBuffer();

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                fileString.append(line);
            }
        } catch(Exception e){
        }
        return fileString.toString();
    }

    public static NameValuePair[] getNextPage(String _XFORMSESSSTATE,
            String _XFORMSTATE, int offset, int total){
        List<NameValuePair> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("npage.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split("=");
                String name = items[0];
                String value;
                if(items.length > 1){
                    value = items[1];
                } else {
                    value = "";
                }
                if(name.equals("_XFORMSESSSTATE")){
                    value = _XFORMSESSSTATE;
                } else if(name.equals("_XFORMSTATE")){
                    value = _XFORMSTATE;
                } else if(name.equals("hs")){
                    value = offset + "";
                } else if(name.endsWith("th")){
                    value = total + "";
                }
                NameValuePair nvp = new NameValuePair(name, value);
                list.add(nvp);
            }
        } catch(Exception e){
        }
        return listToArray(list);
    }

    private static NameValuePair[] listToArray(List<NameValuePair> list){
        NameValuePair[] nvps = new NameValuePair[list.size()];
        for(int i = 0; i < list.size(); i ++){
            nvps[i] = list.get(i);
        }
        return nvps;
    }



    public static NameValuePair[] getPostValues(String _XFORMSESSSTATE,
            String _XFORMSTATE, String _COMPANY_NAME){
        List<NameValuePair> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("search.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split("=");
                String name = items[0];
                String value;
                if(items.length > 1){
                    value = items[1];
                } else {
                    value = "";
                }
                if(name.equals("_XFORMSESSSTATE")){
                    value = _XFORMSESSSTATE;
                } else if(name.equals("_XFORMSTATE")){
                    value = _XFORMSTATE;
                } else if(name.equals("cos")){
                    value = _COMPANY_NAME;
                } else {}

                NameValuePair nvp = new NameValuePair(name, value);
                list.add(nvp);
            }
        } catch(Exception e){
        }
        return listToArray(list);
    }
}
