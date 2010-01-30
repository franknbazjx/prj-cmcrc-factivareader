/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

import com.usyd.util.StringUtil;

/**
 *
 * @author yy
 */
public class CompanyUnit implements Comparable {

    private String shortName;
    private String fullName;
    private String searchName;
    private String ticker;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    private int difference;

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    
    public CompanyUnit(String shortName, String fullName, String searchName) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.searchName = searchName;

        String s = fullName.toLowerCase();
        String t = searchName.toLowerCase();
        if(s.matches(".* ltd")){
            String[] tmp = s.split(" ltd");
            s = tmp[0];
        }
        difference = StringUtil.getLevenshteinDistance(s, t);
    }

    public double getRatio(){
        double diff = this.getDifference();
        return diff / this.getFullName().length();
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int compareTo(Object unit) {
        
        CompanyUnit target = (CompanyUnit)unit;
        if(this.getDifference() < target.getDifference()){
            return -1;
        } else if(this.getDifference() == target.getDifference()){
            return 0;
        } else {
            return 1;
        }
    }
}
