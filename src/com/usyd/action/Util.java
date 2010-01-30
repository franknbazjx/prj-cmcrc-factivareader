/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author min
 */
public class Util {

    private static Logger LOG = Logger.getLogger(Util.class.getName());

    static public String formatArg(final String format, char varPrefix, String[] args) {
        StringBuffer rv = new StringBuffer(format);
        Pattern pat = Pattern.compile(varPrefix + "\\{\\d+\\}");
        Matcher matcher = pat.matcher(format);
        int offset = 0;
        while (matcher.find()) {
            String str = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            int index = Integer.parseInt(str.substring(2, str.length() - 1));
            if (index < args.length) {
                rv.replace(start + offset, end + offset, args[index]);
                offset += args[index].length() - (end - start);
            }
        }
        return rv.toString();
    }

    public static String loadFromStream(InputStream istream) {
        StringBuffer lines = new StringBuffer();
        try {
            BufferedReader resp = new BufferedReader(new InputStreamReader(istream));
            String line;
            while ((line = resp.readLine()) != null) {
                lines.append(line);
                lines.append(System.getProperty("line.separator"));
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            return lines.toString();
        }
    }

//    public static void print2File(String file, String str) throws IOException {
//        Writer out = new FileWriter(file);
//        out.write(str);
//        out.close();
//    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] ag = ",login,a123456,b123456,c123456,d123456,e123456,f123456,g123456,h123456".split(",");
        System.out.println("login?a123456=b123456&c123456=d123456&user=finc&pass=finc2008".equals(Util.formatArg("#{1}?#{2}=#{3}&#{4}=#{5}&user=finc&pass=finc2008", '#', ag)));
        String page = Util.loadFromStream(new FileInputStream(new File("agreed then")));
        for (String s : autoForm(page)) {
            System.out.println(s);
        }
    }

    /**
     * 
     * @param page
     * @return
     */
    public static ArrayList<String> autoForm(String page) {
        ArrayList<String> rv = new ArrayList<String>();
        int option = Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE;
        Pattern p0 = Pattern.compile("form.*?form", option);
        Pattern p1 = Pattern.compile("action=.([^'\"]*).*?>", option);
        Pattern p2 = Pattern.compile("name=.([^'\"]*)[^><]*?value=.([^'\"]*).", option);
        Matcher m0 = p0.matcher(page);

        while (m0.find()) {//each form
            String fm = m0.group();

            Matcher mi = p1.matcher(fm);
            StringBuffer qs;
            if (mi.find()) {
                qs = new StringBuffer(mi.group(1).trim());
                if (!qs.toString().contains("?")) {
                    qs.append("?");
                }
            } else {
                continue;
            }

            Matcher m2 = p2.matcher(fm);
            while (m2.find()) {
                qs.append(m2.group(1) + "=>");
                qs.append(m2.group(2) + "&");
            }
            qs.deleteCharAt(qs.length() - 1);
            rv.add(qs.toString());
        }
        return rv;
    }
}
