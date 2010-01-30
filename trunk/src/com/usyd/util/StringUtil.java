/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yy
 */
public class StringUtil {


    public static String getXMLheader(){

            String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            content += "<ROOT>" + "\n";
            content += "\t<ARTICLES>\n";
            return content;
    }

    public static String getSearchProfile(String code, String ticker, String company){
            String content = "";
            content += "\t\t<CODE>" + code + "</CODE>\n";
            content += "\t<TICKET>" + ticker + "</TICKET>\n";
            content += "\t\t<COMPANY>" + company + "</COMPANY>\n";
            return content;
    }


    public static String getXMLfooter(){
            String content = "\t</ARTICLES>\n";
            content += "</ROOT>\n";
            return content;
    }

    public static int getLevenshteinDistance(String s, String t) {


        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        /*
        The difference between this impl. and the previous is that, rather
        than creating and retaining a matrix of size s.length()+1 by t.length()+1,
        we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
        is the 'current working' distance array that maintains the newest distance cost
        counts as we iterate through the characters of String s.  Each time we increment
        the index of String t we are comparing, d is copied to p, the second int[].  Doing so
        allows us to retain the previous cost counts as required by the algorithm (taking
        the minimum of the cost count to the left, up one, and diagonally up and to the left
        of the current cost count being calculated).  (Note that the arrays aren't really
        copied anymore, just switched...this is clearly much better than cloning an array
        or doing a System.arraycopy() each time  through the outer loop.)

        Effectively, the difference between the two implementations is this one does not
        cause an out of memory condition when calculating the LD over two very large strings.
         */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }


    //private static Logger LOG = Logger.getLogger(Util.class.getName());

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
            //LOG.log(Level.SEVERE, null, ex);
        } finally {
            return lines.toString();
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

//    public static void print2File(String file, String str) throws IOException {
//        Writer out = new FileWriter(file);
//        out.write(str);
//        out.close();
//    }

//    public static void main(String[] args) throws FileNotFoundException {
//        String[] ag = ",login,a123456,b123456,c123456,d123456,e123456,f123456,g123456,h123456".split(",");
//        System.out.println("login?a123456=b123456&c123456=d123456&user=finc&pass=finc2008".equals(Util.formatArg("#{1}?#{2}=#{3}&#{4}=#{5}&user=finc&pass=finc2008", '#', ag)));
//        String page = Util.loadFromStream(new FileInputStream(new File("agreed then")));
//        for (String s : autoForm(page)) {
//            System.out.println(s);
//        }
//    }

}
