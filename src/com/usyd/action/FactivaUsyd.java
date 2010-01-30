package com.usyd.action;

import com.usdy.log.Logger;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author min
 */
public class FactivaUsyd {

    //private static Logger LOG = Logger.getLogger(FactivaUsyd.class.getName());
    private static HttpClient httpclient;// = new HttpClient();

    private static String get(String url, String referer) throws IOException {
        //LOG.info("loading from:" + url);//https://albany.library.unsw.edu.au:443/goto/http://sirius.library.unsw.edu.au:80/V/8QSHXQP5RBANV4EJQ46GKPKA78B4MPFAI55EUL6YGH9797LFMJ-01235?FUNC=FIND-DB-1-LOCATE&MODE=locate&FORMAT=001&F-IDN=NSW00645&pds_handle=GUEST
        GetMethod get = new GetMethod(url);
        get.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);// need this line to login into usyd
        get.addRequestHeader("Referer", referer);
        String redir = null;
        try {
            httpclient.executeMethod(get);
            redir = getRedirectUrl(get);
        } catch (IOException ex) {
            //Logger.getLogger(FactivaUsyd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            String rv = Util.loadFromStream(get.getResponseBodyAsStream());
            get.releaseConnection();
            if (redir != null) {
                return get(redir, url);
            } else {
                return rv;
            }
        }
    }

    private static String post(String para, String url, String referer) throws IOException {
        PostMethod post = new PostMethod(url);
        post.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);// need this line to login into usyd
        String[] paras = para.split("&");
        NameValuePair[] data = new NameValuePair[paras.length];
        for (int i = 0; i < paras.length; i++) {
            String[] v = paras[i].split("=>");
            String name = v[0];
            String value = "";
            if (v.length == 2) {
                value = v[1];
            }
            NameValuePair nv = new NameValuePair(name, value);
            data[i] = nv;
//            LOG.info(nv.toString());
        }
        post.setRequestBody(data);
        post.addRequestHeader(new Header(
                "User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));

        post.addRequestHeader("Referer", referer);
        httpclient.executeMethod(post);
        String redir = getRedirectUrl(post);

        String rsp = Util.loadFromStream(post.getResponseBodyAsStream());
//        LOG.info(rsp);
        post.releaseConnection();
        if (redir != null) {
            return get(redir, url);
        } else {
            return rsp;
        }
    }

    private static String login(String user, String pass, String para, String url, String referer) throws IOException {
        url = "https://login.ezproxy1.library.usyd.edu.au/login";//force to use this https
        PostMethod post = new PostMethod(url);
        post.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);// need this line to login into usyd
        String[] paras = para.split("&");
        NameValuePair[] data = new NameValuePair[paras.length];
        for (int i = 0; i < paras.length; i++) {
            String[] v = paras[i].split("=>");
            NameValuePair nv = new NameValuePair(v[0], v[1]);
            data[i] = nv;
            //LOG.info(nv.toString());
        }
        post.setRequestBody(data);
        post.addRequestHeader(new Header(
                "User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.7) Gecko/20100106 Ubuntu/9.10 (karmic) Firefox/3.5.7"));

        post.addRequestHeader("Referer", referer);
        httpclient.executeMethod(post);
        String rsp = Util.loadFromStream(post.getResponseBodyAsStream());
        //LOG.info(rsp);
        String newuri = getRedirectUrl(post);
        post.releaseConnection();
        //System.out.println("Redirect target: " + newuri);
        return newuri;
    }

    private static String extractParameter(String resp, String pat, String format) {
        Pattern pattern = Pattern.compile(pat, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(resp);
        String url = null;
        if (matcher.find()) {
            String[] args = new String[matcher.groupCount() + 1];
            for (int j = 0; j < matcher.groupCount() + 1; j++) {
                args[j] = matcher.group(j);
            }
            url = Util.formatArg(format, '#', args);
        }
        return url;
    }

    public static String refresh(HttpClient httpclient) throws IOException {
        FactivaUsyd.httpclient = httpclient;
        String url = "http://www.library.usyd.edu.au/databases/dbtitlef.html";
        //LOG.info("1:" + url);
        Logger.log("loading ");
        String page = get(url, null);

        String newurl = extractParameter(page, "a href=\"(http://ez[^'\"]*factiva[^'\"]*)", "#{1}");
        //LOG.info("2:" + newurl);
        Logger.log(".");
        int ind = newurl.indexOf("/", 8);
        String base = newurl.substring(0, ind);
        page = get(newurl, url);

        url = newurl;
        newurl = extractParameter(page, "form action=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?(input).*?(input).*?form", "#{2}=>#{3}&#{4}=>#{5}&user=>finc&pass=>finc2008");
//        LOG.info("3(login):" + base + "/login," + url);


        String redir = null;
        try{
            redir = login("finc", "finc2008", newurl, base + "/login", url);
            Logger.log(".");
        }catch(Exception e){
            //System.out.println("##\tLogin Exc: " + redir);
        }
        

        if (redir != null) {
            //System.out.println("##\tIf----------: " + redir);
            page = get(redir, newurl);
            //Util.print2File("redir.html", page);

            newurl = extractParameter(page, "form action=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?(input).*?(input).*?form", "#{2}=>#{3}&#{4}=>#{5}");
//            LOG.info("4(redirect):" + base + "/login," + url);
            Logger.log(".");
            url = newurl;
            newurl = extractParameter(page, "<a href=\"([^'\"]*)\"><strong>i agree", "#{1}");
            Logger.log(".");
//            LOG.info("5(agree):" + newurl);
            page = get(newurl, url);//agree it

            url = newurl;
            newurl = extractParameter(page, "action=.([^'\"]*)", "#{1}");
//            LOG.info("6:" + newurl);
            Logger.log(".");

            String format = "#{2}=>#{3}&#{4}=>#{5}&#{6}=>#{7}&#{8}=>#{9}&#{10}=>#{11}&#{12}=>#{13}";
            String paras = extractParameter(page,
                    "action=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?", format);
            page = post(paras, newurl, url);
            url = newurl;
            newurl = extractParameter(page, "\\.action\\s*=\\s*['\"]([^'\"]*)", "#{1}");
//            LOG.info("7:" + newurl);
            Logger.log(".");


            String data = Util.autoForm(page).get(0);
            page = post(data.substring(1), newurl, url);
            url = newurl;
            newurl = extractParameter(page, "form.*action\\s*=\\s*['\"]([^'\"]*)", "#{1}");
//            LOG.info("8:" + newurl);
            Logger.log(". finished!\n\n");

            data = extractParameter(page, "form.*name=.([^'\"]*)[^><]*value=.([^'\"]*).*form", "#{1}=>#{2}");
            page = post(data, newurl, url);

        } else {
            System.out.println("================");
           
        }
        return page;
    }

    public static void main(String[] args) throws IOException {
        refresh(httpclient);
    }

    private static String getRedirectUrl(HttpMethod method) {
        int statuscode = method.getStatusCode();
        String newuri = null;
        if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
                || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
                || (statuscode == HttpStatus.SC_SEE_OTHER)
                || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
            Header header = method.getResponseHeader("location");
            if (header == null) {
                header = method.getResponseHeader("Location");
            }
            if (header != null) {
                newuri = header.getValue();
                if ((newuri == null) || (newuri.equals(""))) {
                    newuri = "/";
                }
            }
        }
        return newuri;
    }
}
