/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

/**
 *
 * @author yy
 */
public class SearchUnit {

    private String code;
    private String ticker;
    private String name;

    public SearchUnit(String code, String ticker, String name) {
        this.code = code;
        this.ticker = ticker;
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getXmlFile(){
        return code + "_" + ticker + "_" + name + ".xml";
    }

    public String getCsvRecord(){
        return code + "," + ticker + "," + name;
    }
}
