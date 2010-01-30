/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.page;

import com.usdy.unit.NewsUnit;
import java.util.List;

/**
 *
 * @author yy
 */
public class NewsItemExtractor extends Extractor {

    public NewsItemExtractor(String page) {
        super(page);
    }

    public NewsUnit getNews(NewsUnit item) {

        page = page.replaceAll("\n", "");
        page = page.replaceAll("\r", "");

//      Example:
        
//      AngloGold mops up.
//      By PETER GOSNELL.
//      310 words
//      29 December 1999
//      DAITEL
//      43
//      English
//      (c) 1999 Nationwide News Proprietary Ltd

        List<String> lines = getByPattern("<div id=\"hd\">(.*?)<div id=\"pageFooter\">", page);
        String ret = "<div id=\"hd\">" + lines.get(0);


//        1. get SOURCE

        String source = getByPattern("<span class=\"colorLinks\">(.*?)</span>", ret).get(0);
        item.setSource(source);

//        2. split by <div>

        String[] tmp = ret.split("<p>");
        lines = getByPattern("<div[^>]*>(.*?)</div>", tmp[0]);

//        3. get and locate title from <b> .* </b> and remove lines above

        String title = lines.remove(0);
        while(lines.size() > 0 && !title.trim().startsWith("<b>")){
            title = lines.remove(0);
        }
        item.setTitle(title);

//        4. get the one next to title (author > x words)
        String author = lines.remove(0);
        String words = "0";
        if(author.trim().endsWith(" words")){
            item.setAuthor("");
            item.setWords(author);
        } else {
            item.setAuthor(author);
            while(lines.size() > 0 && !words.endsWith(" words")){
                words = lines.remove(0);
            }
            item.setWords(words);
        }

//        5. date next to words
        String date = lines.remove(0);
        item.setDate(date);
        String source_co = lines.remove(0);
        while(lines.size() >0 && ! source_co.trim().equals("English")){
            source_co = lines.remove(0);
        }

//        6 CopyRight next to English
        source_co = lines.remove(0);
        item.setSource_co(source_co);

        
        lines = getByPattern("<p>(.*?)</p>", ret);
        item.setDoc_id(lines.remove(lines.size() - 1));


        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            line = line.replaceAll("<.*?>", "");
            sb.append(line + "\n");
        }
        item.setText(sb.toString());

        if (item.validate()) {
            return item;
        } else {
            return null;
        }
    }
}
