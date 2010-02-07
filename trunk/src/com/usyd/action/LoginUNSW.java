/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.exception.TimeOutException;
import com.usyd.log.Logger;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class LoginUNSW extends Login {

    private Pattern pattern1 = Pattern.compile("https://([^'\"]*)", Pattern.CASE_INSENSITIVE);
    private Pattern pattern2 = Pattern.compile("location\\s*=\\s*['\"]([^'\"]*)", Pattern.CASE_INSENSITIVE);
    private Pattern pattern3 = Pattern.compile("td><a href\\s*=\\s*['\"]([^'\"]*)", Pattern.CASE_INSENSITIVE);
    private Pattern pattern4 = Pattern.compile("refresh[^>]*(http://[^'\"]*)", Pattern.CASE_INSENSITIVE);
    private Pattern pattern5 = Pattern.compile("form.*action\\s*=\\s*['\"]([^'\"]*).*'REMOTE_ADDR' value='([^']*)'.*'HTTP_REFERER' value='([^']*)'.*'TargetSite' value='([^']*)'.*'InterfaceLanguage' value='([^']*).*'LandingPage' value='([^']*)'.*'ReferedSite' value='([^']*)'.*", Pattern.CASE_INSENSITIVE);
    private Pattern pattern6 = Pattern.compile("\\.action\\s*=\\s*['\"]([^'\"]*)", Pattern.CASE_INSENSITIVE);
    private Pattern pattern7 = Pattern.compile("form.*action\\s*=\\s*['\"]([^'\"]*)", Pattern.CASE_INSENSITIVE);


    public LoginUNSW() {
        super();
    }


    @Override
    protected String refresh() throws IOException, TimeOutException {

        String url = "http://sfx.nun.unsw.edu.au/V/?func=find-db-1-locate&mode=locate&format=001&F-IDN=NSW00645";
        String rsp = this.getGetContent(url);

        Matcher matcher = pattern1.matcher(rsp);

        if (matcher.find()) {
            url = matcher.group();
            //Logger.log("1:" + url);//https://albany.library.unsw.edu.au:443/pds?func=sso&amp;calling_system=metalib&amp;institute=UNSW&amp;url=http://sirius.library.unsw.edu.au:80/V/B61CC9GLDQQUT3UY8N9I8GNVIBXI4FUTU7U5FA889N1KCBUKKX-01117?FUNC%3DFIND%2DDB%2D1%2DLOCATE%26MODE%3Dlocate%26FORMAT%3D001%26F%2DIDN%3DNSW00645
            Logger.log("loading.");
        } else {
            throw new TimeOutException();
        }

        rsp = this.getGetContent(url);
        matcher = pattern2.matcher(rsp);
        if (matcher.find()) {
            url = "https://albany.library.unsw.edu.au:443" + matcher.group(1);
            //Logger.log("2:" + url);//https://albany.library.unsw.edu.au:443/goto/http://sirius.library.unsw.edu.au:80/V/8QSHXQP5RBANV4EJQ46GKPKA78B4MPFAI55EUL6YGH9797LFMJ-01235?FUNC=FIND-DB-1-LOCATE&MODE=locate&FORMAT=001&F-IDN=NSW00645&pds_handle=GUEST
            Logger.log(".");
        }else {
            throw new TimeOutException();
        }

        rsp = this.getGetContent(url);
//            Logger.logln(rsp);
        matcher = pattern3.matcher(rsp);
        if (matcher.find()) {
            url = matcher.group(1);
            url = url.replaceAll("&amp;", "&");
            //Logger.log("3:" + url);//http://sirius.library.unsw.edu.au:80/V/5KX3B3P1YKXDN854T24LTPGDQ5A4VD6U9XDQKA34MCBFYEFX3X-09086?func=native-link&amp;resource=NSW00645
            Logger.log(".");
        }else {
            throw new TimeOutException();
        }

        rsp = this.getGetContent(url);
//            Logger.logln(rsp);
        matcher = pattern4.matcher(rsp);
        if (matcher.find()) {
            url = matcher.group(1);
            Logger.log(".");
            //Logger.log("4:" + url);//http://global.factiva.com/en/sess/login.asp?xsid=S003Wvp3sEmMXmnOHmnMDMnODatODIp5DByWcNGOTNHYdNZUUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUEA
        }else {
            throw new TimeOutException();
        }

        //https://viviena.library.unsw.edu.au/login?qurl=http://global.factiva.com/en/sess/login.asp%3fxsid%3dS003Wvp3sEmMXmnOHmnMDMnODatODIp5DByWcNGOTNHYdNZUUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUEA

        rsp = this.getGetContent(url);
        matcher = pattern5.matcher(rsp);
        String b, c, d, e, f, g;
        b = c = d = e = f = g = null;
        if (matcher.find()) {
            String action = matcher.group(1);
            b = matcher.group(2);
            c = matcher.group(3);
            d = matcher.group(4);
            e = matcher.group(5);
            f = matcher.group(6);
            g = matcher.group(7);
            url = action;
            Logger.log(".");
            //Logger.log("5:" + url);//http://global.factiva.com/en/sess/login.asp?xsid=S003Wvp3sEmMXmnOHmnMDMnODatODIp5DByWcNGOTNHYdNZUUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUEA
        }else {
            throw new TimeOutException();
        }
        NameValuePair[] data = {
            new NameValuePair("REMOTE_ADDR", b),
            new NameValuePair("HTTP_REFERER", c),
            new NameValuePair("TargetSite", d),
            new NameValuePair("InterfaceLanguage", e),
            new NameValuePair("LandingPage", f),
            new NameValuePair("ReferedSite", g),};
        rsp = this.getPostContent(url, data);
        matcher = pattern6.matcher(rsp);
        if (matcher.find()) {
            url = matcher.group(1);
            Logger.log(".");
            //Logger.log("6:" + url);
        }else {
            throw new TimeOutException();
        }
        rsp = this.getGetContent(url);
        matcher = pattern7.matcher(rsp);
        if (matcher.find()) {
            url = matcher.group(1);
            Logger.log(".");
            //Logger.log("7:" + url);
        }else {
            throw new TimeOutException();
        }
        rsp = this.getGetContent(url);
        Logger.log("finished!" + "\n\n");

        return rsp;
    }

    @Override
    protected String getSbService() {
        return "http://global.factiva.com/sb/sbservice.aspx";
    }

    @Override
    protected String getDefault() {
return "http://global.factiva.com/ha/default.aspx";


    }

    @Override
    protected String getAa(String link) {
return "http://global.factiva.com/aa/?" + link;

    }
}
