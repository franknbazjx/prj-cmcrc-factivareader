/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.log.Logger;
import com.usyd.util.StringUtil;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 *
 * @author yy
 */
public class LoginUSYDProxy extends Login {

    private String user;
    private String pass;

    public LoginUSYDProxy(String user, String pass) {
        super();
        this.user = user;
        this.pass = pass;
    }


    private static String get1(HttpClient client, String url, String referer) throws IOException {
        Logger.log("\nloading from:" + url + "\n");//https://albany.library.unsw.edu.au:443/goto/http://sirius.library.unsw.edu.au:80/V/8QSHXQP5RBANV4EJQ46GKPKA78B4MPFAI55EUL6YGH9797LFMJ-01235?FUNC=FIND-DB-1-LOCATE&MODE=locate&FORMAT=001&F-IDN=NSW00645&pds_handle=GUEST
        GetMethod get = new GetMethod(url);
        get.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);// need this line to login into usyd
        get.addRequestHeader("Referer", referer);
        String redir = null;
        try {
            client.executeMethod(get);
            redir = getRedirectUrl(get);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            String rv = StringUtil.loadFromStream(get.getResponseBodyAsStream());
            get.releaseConnection();
            if (redir != null) {
                return get1(client, redir, url);
            } else {
                return rv;
            }
        }
    }

    private static String getWithoutRedirect(HttpClient client, String url, String referer) throws IOException {
        Logger.log("\nloading from:" + url + "\n");//https://albany.library.unsw.edu.au:443/goto/http://sirius.library.unsw.edu.au:80/V/8QSHXQP5RBANV4EJQ46GKPKA78B4MPFAI55EUL6YGH9797LFMJ-01235?FUNC=FIND-DB-1-LOCATE&MODE=locate&FORMAT=001&F-IDN=NSW00645&pds_handle=GUEST
        GetMethod get = new GetMethod(url);
        get.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);// need this line to login into usyd
        get.addRequestHeader("Referer", referer);
        String redir = null;
        try {
            client.executeMethod(get);
            Logger.log("==============execute method in get\n");
            redir = getRedirectUrl(get);
            Logger.log("redir:" + redir + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            String rv = StringUtil.loadFromStream(get.getResponseBodyAsStream());
            get.releaseConnection();
            return rv;
        }
    }


    private static String post1(HttpClient client, String para, String url, String referer) throws IOException {
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
        client.executeMethod(post);
        String redir = getRedirectUrl(post);

        String rsp = StringUtil.loadFromStream(post.getResponseBodyAsStream());
//        LOG.info(rsp);
        post.releaseConnection();
        if (redir != null) {
            return get1(client, redir, url);
        } else {
            return rsp;
        }
    }

    private static String login1(HttpClient client, String user, String pass, String para, String url, String referer) throws IOException {
        url = "https://login.ezproxy1.library.usyd.edu.au/login";//force to use this https
        para += "&user=>" + user + "&pass=>" + pass;
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
        client.executeMethod(post);
        String rsp = StringUtil.loadFromStream(post.getResponseBodyAsStream());
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
            url = StringUtil.formatArg(format, '#', args);
        }
        return url;
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

    public String refresh() throws IOException {
        HttpClient ssl = this.httpClient;
        HostConfiguration conf = new HostConfiguration();
        HostConfiguration conf1 = new HostConfiguration();
        conf1.setProxy("www-proxy.cse.unsw.edu.au", 3128);

        String url = "http://www.library.usyd.edu.au/databases/dbtitlef.html";
        ssl.setHostConfiguration(conf1);

        String page = get1(ssl, url, "");
//        System.out.println(page);
        String newurl = "";

        newurl = extractParameter(page, "a href=\"(http://ez[^'\"]*factiva[^'\"]*)", "#{1}");
        newurl = "https" + newurl.substring(4);
        System.out.println(newurl);


        ssl.setHostConfiguration(conf);
        HttpClient a = ssl;
        HttpMethod get = new GetMethod(newurl);
        a.executeMethod(get);
        page = get.getResponseBodyAsString();
        String para = extractParameter(page, "form action=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?(input).*?(input).*?form", "#{2}=>#{3}&#{4}=>#{5}");
        System.out.println(para);

        url = newurl;
        newurl = login1(a, user, pass, para, newurl, url);
        System.out.println("1:" + newurl);
        page = get1(a, newurl, url);
//        //Util.print2File("redir.html", page);
//        System.out.println(page);
        url = newurl;

        newurl = extractParameter(page, "<a href=\"([^'\"]*)\"><strong>i agree", "#{1}");
        System.out.println("2: " + newurl);
        ssl.setHostConfiguration(conf1);
        page = get1(a, newurl, url);

//
//        System.out.println("3: agreed" + page);

        url = newurl;
        newurl = extractParameter(page, "action=.([^'\"]*)", "#{1}");
        String format = "#{2}=>#{3}&#{4}=>#{5}&#{6}=>#{7}&#{8}=>#{9}&#{10}=>#{11}&#{12}=>#{13}";
        String paras = extractParameter(page,
                "action=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?name=.([^'\"]*).*?value=.([^'\"]*).*?", format);
        System.out.println("3: agreed: " + paras);
        page = post1(a, paras, newurl, url);
//        System.out.println(page);
        url = newurl;
        newurl = extractParameter(page, "\\.action\\s*=\\s*['\"]([^'\"]*)", "#{1}");
        System.out.println(newurl);
        String data = StringUtil.autoForm(page).get(0);
        page = post1(a, data.substring(1), newurl, url);
        url = newurl;
        newurl = extractParameter(page, "form.*action\\s*=\\s*['\"]([^'\"]*)", "#{1}");
        System.out.println(newurl);
        data = extractParameter(page, "form.*name=.([^'\"]*)[^><]*value=.([^'\"]*).*form", "#{1}=>#{2}");
        page = post1(a, data, newurl, url);
//        System.out.println(page);

        return page;
    }

    @Override
    protected String getSbService() {
        return "http://global.factiva.com.ezproxy1.library.usyd.edu.au/sb/sbservice.aspx";
    }

    @Override
    protected String getDefault() {
        return "http://global.factiva.com.ezproxy1.library.usyd.edu.au/ha/default.aspx";
    }

    @Override
    protected String getAa(String link) {
        return "http://global.factiva.com.ezproxy1.library.usyd.edu.au/aa/?" + link;
    }
}

