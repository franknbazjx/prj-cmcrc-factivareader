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
public class DatePairs {

    private int fromY;
    private int fromM;
    private int fromD;
    private int toY;
    private int toM;
    private int toD;

    public DatePairs(int fromY, int fromM, int fromD, int toY, int toM, int toD) {
        this.fromY = fromY;
        this.fromM = fromM;
        this.fromD = fromD;
        this.toY = toY;
        this.toM = toM;
        this.toD = toD;
    }

    public int getFromD() {
        return fromD;
    }

    public void setFromD(int fromD) {
        this.fromD = fromD;
    }

    public int getFromM() {
        return fromM;
    }

    public void setFromM(int fromM) {
        this.fromM = fromM;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToD() {
        return toD;
    }

    public void setToD(int toD) {
        this.toD = toD;
    }

    public int getToM() {
        return toM;
    }

    public void setToM(int toM) {
        this.toM = toM;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }

    public List<DatePairs> divide(){
        List<DatePairs> list = new ArrayList();
        for(int i = this.fromY; i <= this.toY; i ++){
            list.add(new DatePairs(i, 1, 1, i, 12, 31));
        }

        return list;
    }

    public String show(){
        return "from " + this.fromD + "/" +  this.fromM + "/" + this.fromY +
                " to " + this.toD + "/" + this.toM + "/" + this.toY;
    }

    public String file(){
        return this.fromD+"_"+this.fromM+"_"+this.fromY+"-"+
                this.toD + "_" + this.toM + "_" + this.toY;
    }

}
