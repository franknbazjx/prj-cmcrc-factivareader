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

    private String url;
    private String page;

    public GetNews(String url, String page) {
        super();
        this.url = url;
        this.page = page;
        this.obj = null;
    }

    @Override
    public void run() {

        NewsItemExtractor extractor = new NewsItemExtractor(page, url);
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
