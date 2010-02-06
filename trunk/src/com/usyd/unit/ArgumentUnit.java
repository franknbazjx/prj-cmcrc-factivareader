/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

import java.util.List;

/**
 *
 * @author yangyang
 */
public class ArgumentUnit {
    private List<String> companyList;
    private DatePairs datePairs;

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
