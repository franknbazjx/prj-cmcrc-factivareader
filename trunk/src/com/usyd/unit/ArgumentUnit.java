/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

import com.usyd.type.LoginType;
import java.util.List;

/**
 *
 * @author yangyang
 */
public class ArgumentUnit {
    private List<String> companyList;
    private DatePairs datePairs;
    private String user;
    private String pass;
    private LoginType type;

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<String> companyList) {
        this.companyList = companyList;
    }

    public DatePairs getDatePairs() {
        return datePairs;
    }

    public void setDatePairs(DatePairs datePairs) {
        this.datePairs = datePairs;
    }

    public List<DatePairs> divide(){
        return datePairs.divide();
    }
}
