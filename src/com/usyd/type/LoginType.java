/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.type;

/**
 *
 * @author yy
 */
public enum LoginType {
    UNSW(1), UNSWPROXY(2), USYD(3), USYDPROXY(4);

    private int code;

    private LoginType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        switch(this){
            case UNSW: return "UNSW";
            case UNSWPROXY: return "UNSWPROXY";
            case USYD: return "USYD";
            case USYDPROXY: return "USYDPROXY";
            default: return "ERROR";
        }
    }
}
