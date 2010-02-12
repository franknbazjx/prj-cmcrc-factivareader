/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.util;

import com.usyd.log.Logger;
import com.usyd.unit.CompanyUnit;
import com.usyd.unit.DatePairs;
import com.usyd.unit.NewsUnit;
import com.usyd.unit.SearchUnit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.httpclient.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author yy
 */
public class FileLoader {

    public static String getValueByTag(Node fstNode, String tag) {

        Element fstElmnt = (Element) fstNode;
        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(tag);
        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
        NodeList fstNm = fstNmElmnt.getChildNodes();
        if (fstNm.getLength() > 0) {
            return ((Node) fstNm.item(0)).getNodeValue();
        } else {
            return "";
        }
    }

    public static List<NewsUnit> parseXml(File file) {

        List<NewsUnit> list = new ArrayList<NewsUnit>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("ARTICLE");


            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node fstNode = nodeLst.item(s);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    String url = getValueByTag(fstNode, "URL");
                    String source = getValueByTag(fstNode, "SOURCE");
                    String title = getValueByTag(fstNode, "TITLE");
                    String author = getValueByTag(fstNode, "AUTHOR");
                    String words = getValueByTag(fstNode, "WORDS");
                    String date = getValueByTag(fstNode, "DATE");
                    String source_co = getValueByTag(fstNode, "SOURCE_CO");
                    String doc_id = getValueByTag(fstNode, "DOC_ID");
                    String text = getValueByTag(fstNode, "TEXT");
                    NewsUnit unit = new NewsUnit(url, source, title, author,
                            words, date, source_co, doc_id, text);
                    list.add(unit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void cleanTempFiles(){
        String path = "tmp";
        File root = new File(path);
        String[] list = root.list();
        for(String line : list){
            if(line.endsWith(".xml")){
                File file = new File(path + "/" + line);
                file.delete();
                Logger.log("DELETING " + line + "\n");
            }
        }

    }

    public static List<NewsUnit> collectTempFiles() {

        String path = "tmp";
        File root = new File(path);
        String[] list = root.list();
        List<NewsUnit> output = new ArrayList<NewsUnit>();
        for (String line : list) {
            if (line.endsWith(".xml")) {
                Logger.log("PARSING " + line + "\n");
                File file = new File(path + "/" + line);
                List<NewsUnit> tempList = parseXml(file);
                for(NewsUnit unit : tempList){
                    output.add(unit);
                }
            }
        }
        return output;
    }

    public static List<DatePairs> tempFileFilter(List<DatePairs> dateList, CompanyUnit unit) {
//        List<File> finishedList = new File(".");
        HashSet finished = new HashSet();
        //String[] paths;
        String[] paths = {"tmp"};


        for (String path : paths) {
            File root = new File(path);
            String[] list = root.list();
            for (String line : list) {
                if (line.endsWith(".xml")) {
                    finished.add(line);
                }
            }
        }

        List<DatePairs> newDateList = new ArrayList<DatePairs>();
        for (DatePairs date : dateList) {
            String file = date.file() + "#" + unit.getSearchName() + ".xml";
            if (finished.contains(file)) {
                Logger.log("SKIPPED: " + file + "\n");
            } else {
                newDateList.add(date);
            }
        }
        return newDateList;
    }

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
