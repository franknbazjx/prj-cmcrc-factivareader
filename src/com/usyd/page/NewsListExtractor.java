/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.page;

import com.usyd.log.Logger;
import java.util.List;

/**
 *
 * @author yy
 */
public class NewsListExtractor extends Extractor {

    public NewsListExtractor(String page) {
        super(page);
    }

    public int getNumOfNews() {

        List<String> list = getByPattern("<span class=\"resultsBar\">&nbsp;Headlines (.*)&nbsp;", page);
        int rows = 0;

        try {
            String pageInfo = list.get(0);
            String[] tmp = pageInfo.split(" of ");
            rows = Integer.parseInt(tmp[1]);
        } catch (Exception e) {

            if (this.isNoResult()) {
                Logger.log("no result!"+ "\n");
            } else {
                Logger.log("\n\n\n\n----------EORROR-----------------------------------------" + "\n");
                Logger.error(page);
                Logger.log("--------EORROR-------------------------------------------\n\n\n\n" + "\n");
            }
        }
        return rows;
    }

    public List<String> getLinks() {
        List<String> list = getByPattern("<a href=\"..\\/aa\\/\\?([^\"]*)\"", page);
        page = null;
        return list;
    }
}
