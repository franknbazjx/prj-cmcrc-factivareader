/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.type.LoginType;

/**
 *
 * @author yy
 */
public class LoginFactory {

    public Login createLogin(LoginType type, String user, String pass) {

        if (type.getCode() == LoginType.UNSW.getCode()) {
            return new LoginUNSW(false);
        } else if (type.getCode() == LoginType.UNSWPROXY.getCode()) {
            return new LoginUNSW(true);
        } else if (type.getCode() == LoginType.USYD.getCode()) {
            return new LoginUSYD(user, pass);
        } else if (type.getCode() == LoginType.UNSWPROXY.getCode()) {
            return new LoginUSYDProxy(user, pass);
        } else {
            return null;
        }
    }
}
