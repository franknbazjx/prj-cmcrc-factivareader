/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yangyang
 */
public class PageUnit {
    private List<String> list;
    private boolean finished;
    private int currentPage;
    private int numOfPages;
    private int numOfLinks;

    public PageUnit() {
        finished = false;
        list = new ArrayList<String>();
        currentPage = 0;
        numOfPages = 0;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinish() {
        finished = true;
    }

    public List<String> getList() {
        return list;
    }

    public void add(String link) {
        list.add(link);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getNumOfLinks() {
        return numOfLinks;
    }

    public void setNumOfLinks(int numOfLinks) {
        this.numOfLinks = numOfLinks;
    }

    public int size(){
        return list.size();
    }

    public void concat(List<String> conList){
        for(String str : conList){
            list.add(str);
        }
    }
}
