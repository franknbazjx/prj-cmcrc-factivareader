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
public class LoginUSYD extends Action {

    private String _XFORMSESSSTATE;
    private String _XFORMSTATE;

    public LoginUSYD() {
        this.httpClient = new HttpClient();
    }




    public HttpClient getHttpclient() {
        try {
            this.httpClient = new HttpClient();
            String rsp = FactivaUsyd.refresh(httpClient);
            this.updateViewState(rsp);
        } catch (IOException ex) {
            
        }
        return httpClient;
    }



    public void updateViewState(String rsp){
        HiddenFieldExtractor extractor = new HiddenFieldExtractor(rsp);
        extractor.loadInput();
        _XFORMSESSSTATE = extractor.getValueByName("_XFORMSESSSTATE");
        _XFORMSTATE = extractor.getValueByName("_XFORMSTATE");
    }

    public String getXFORMSESSSTATE() {
        return _XFORMSESSSTATE;
    }

    public String getXFORMSTATE() {
        return _XFORMSTATE;
    }
}
