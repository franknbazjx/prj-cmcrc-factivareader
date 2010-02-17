/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.control;

import com.usyd.page.NewsItemExtractor;

/**
 *
 * @author yy
 */
public class GetNews extends Executor {

//    private String url;
//    private String page;
    private NewsItemExtractor extractor;

    public GetNews(NewsItemExtractor extractor) {
        super();
        this.extractor = extractor;
        this.obj = null;
    }

    @Override
    public void run() {
        this.obj = extractor.getNews();
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public Object getObj() {
        return this.obj;
    }
}
