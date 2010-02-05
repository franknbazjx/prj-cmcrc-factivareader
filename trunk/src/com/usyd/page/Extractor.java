/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.page;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yy
 */
public abstract class Extractor {

    protected String page;


    public Extractor(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public boolean isErrorPage(){
        String error = "We are unable to process your request at this time.";
        return page.contains(error);
    }

    public boolean isTwoExpressionError(){
        String error ="The connector NOT can only be used to connect two expressions.";
        return page.contains(error);
    }

    public boolean isNoResult(){
        String error = "<div id=\"headlines\" class=\"headlines\">&nbsp;No results.</div></div>";
        return page.contains(error);
    }

    public void setPage(String page) {
        this.page = page;
    }

    protected List<String> getByPattern(String regex, String str) {
        List<String> list = new ArrayList();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }
}
