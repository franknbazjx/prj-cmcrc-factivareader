/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.action;

import com.usyd.page.HiddenFieldExtractor;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;

/**
 *
 * @author yy
 */
public abstract class Login extends Action {

    private String _XFORMSESSSTATE;
    private String _XFORMSTATE;

    public Login() {
        httpClient = new HttpClient();
    }

    public HttpClient getHttpclient() {
        try {
            this.httpClient = new HttpClient();
            String rsp = refresh();
            updateViewState(rsp);
        } catch (IOException ex) {
        }
        return httpClient;
    }

    protected abstract String refresh() throws IOException;

    public String getXFORMSESSSTATE() {
        return _XFORMSESSSTATE;
    }

    public void setXFORMSESSSTATE(String _XFORMSESSSTATE) {
        this._XFORMSESSSTATE = _XFORMSESSSTATE;
    }

    public String getXFORMSTATE() {
        return _XFORMSTATE;
    }

    public void setXFORMSTATE(String _XFORMSTATE) {
        this._XFORMSTATE = _XFORMSTATE;
    }

    public void updateViewState(String rsp){
        HiddenFieldExtractor extractor = new HiddenFieldExtractor(rsp);
        extractor.loadInput();
        _XFORMSESSSTATE = extractor.getValueByName("_XFORMSESSSTATE");
        _XFORMSTATE = extractor.getValueByName("_XFORMSTATE");
    }
}
