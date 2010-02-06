/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

import com.usyd.log.Logger;
import com.usyd.unit.DatePairs;
import com.usyd.unit.SearchUnit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class FileLoader {

    public static List<SearchUnit> filter(List<String> companyList, boolean fuzzy) {
//        List<File> finishedList = new File(".");
        HashSet finished = new HashSet();
        //String[] paths;
        String[] paths = {"emp", "out"};


        for (String path : paths) {
            if (fuzzy && path.equals("emp")) {
                continue;
            }
            File root = new File(path);
            String[] list = root.list();
            for (String line : list) {
                if (line.endsWith(".xml")) {
                    finished.add(line);
                }
            }
        }

        List<SearchUnit> newCompList = new ArrayList<SearchUnit>();
        for (String line : companyList) {

            String[] tmp = line.split(",[\\s]*");
            SearchUnit unit = new SearchUnit(tmp[0], tmp[1], tmp[2]);
            String file = unit.getXmlFile();
            if (finished.contains(file)) {
                Logger.log("SKIPPED: " + unit.getName() + "\n");
            } else {
                newCompList.add(unit);
            }
        }
        return newCompList;
    }

    public static String getFileString(String fileName) {

        StringBuffer fileString = new StringBuffer();

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                fileString.append(line);
            }
        } catch (Exception e) {
        }
        return fileString.toString();
    }

    public static NameValuePair[] getNextPage(String _XFORMSESSSTATE,
            String _XFORMSTATE, int offset, int total) {
        List<NameValuePair> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("npage.ini")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split("=");
                String name = items[0];
                String value;
                if (items.length > 1) {
                    value = items[1];
                } else {
                    value = "";
                }
                if (name.equals("_XFORMSESSSTATE")) {
                    value = _XFORMSESSSTATE;
                } else if (name.equals("_XFORMSTATE")) {
                    value = _XFORMSTATE;
                } else if (name.equals("hs")) {
                    value = offset + "";
                } else if (name.endsWith("th")) {
                    value = total + "";
                }
                NameValuePair nvp = new NameValuePair(name, value);
                list.add(nvp);
            }
        } catch (Exception e) {
        }
        return listToArray(list);
    }

    private static NameValuePair[] listToArray(List<NameValuePair> list) {
        NameValuePair[] nvps = new NameValuePair[list.size()];
        for (int i = 0; i < list.size(); i++) {
            nvps[i] = list.get(i);
        }
        return nvps;
    }

    public static NameValuePair[] getPostValues(DatePairs datePairs, String _XFORMSESSSTATE,
            String _XFORMSTATE, String _COMPANY_NAME) {
        List<NameValuePair> list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("search.ini")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split("=");
                String name = items[0];
                String value;
                if (items.length > 1) {
                    value = items[1];
                } else {
                    value = "";
                }
                if (name.equals("_XFORMSESSSTATE")) {
                    value = _XFORMSESSSTATE;
                } else if (name.equals("_XFORMSTATE")) {
                    value = _XFORMSTATE;
                } else if (name.equals("cos")) {
                    value = _COMPANY_NAME;
                } else if (name.equals("frd")) {
                    value = "" + datePairs.getFromD();
                } else if (name.equals("frm")) {
                    value = "" + datePairs.getFromM();
                } else if (name.equals("fry")) {
                    value = "" + datePairs.getFromY();
                } else if (name.equals("tod")) {
                    value = "" + datePairs.getToD();
                } else if (name.equals("tom")) {
                    value = "" + datePairs.getToM();
                } else if (name.equals("toy")) {
                    value = "" + datePairs.getToY();
                }
                NameValuePair nvp = new NameValuePair(name, value);
                list.add(nvp);
            }
        } catch (Exception e) {
        }
        return listToArray(list);
    }
}
