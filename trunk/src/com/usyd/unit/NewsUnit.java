/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.unit;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author yy
 */
public class NewsUnit implements Comparable {
    private String url;
    private String source;
    private String title;
    private String author;
    private String words;
    private String date;
    private String source_co;
    private String doc_id;
    private String text;

    public NewsUnit(String url) {
        
        this.url = StringEscapeUtils.escapeXml(url);
    }

    public NewsUnit(String url, String source, String title, String author,
            String words, String date, String source_co, String doc_id, String text) {
        this.url = StringEscapeUtils.escapeXml(url);
        this.source = StringEscapeUtils.escapeXml(source);
        this.title = StringEscapeUtils.escapeXml(title);
        this.author = StringEscapeUtils.escapeXml(author);
        this.words = StringEscapeUtils.escapeXml(words);
        this.date = StringEscapeUtils.escapeXml(date);
        this.source_co = StringEscapeUtils.escapeXml(source_co);
        this.doc_id = StringEscapeUtils.escapeXml(doc_id);
        this.text = StringEscapeUtils.escapeXml(text);
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = StringEscapeUtils.escapeXml(date);
    }

    public void setAuthor(String author) {
        author = author.replaceFirst("^By[\\s]+", "");
        author = author.replaceFirst("\\.$", "");
        this.author = StringEscapeUtils.escapeXml(author.trim());
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        doc_id = doc_id.replaceFirst("Document[\\s]*", "");
        this.doc_id = StringEscapeUtils.escapeXml(doc_id);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = StringEscapeUtils.escapeXml(source);
    }

    public String getSource_co() {
        return source_co;
    }

    public void setSource_co(String source_co) {
        source_co = source_co.replaceFirst("^\\(c\\)[\\s]+", "");
        source_co = source_co.replaceFirst("\\.$", "");
        source_co = source_co.replaceFirst(",(\\s)*(\\d)+(\\s)*$", "");
        this.source_co = StringEscapeUtils.escapeXml(source_co);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = StringEscapeUtils.escapeXml(text);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title.replaceAll("<.*?>", "");
        title = title.replaceAll("\\.$", "");
        this.title = StringEscapeUtils.escapeXml(title);
    }

    public String getUrl() {
        return url;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        words = words.replaceAll("[^\\d]*", "");
        this.words = words;
    }

    public boolean validate() {

        if (this.getUrl() == null || this.getUrl().length() == 0
                || this.getDate() == null || this.getDate().length() == 0
                || this.getDoc_id() == null || this.getDoc_id().length() == 0
                || this.getSource() == null || this.getSource().length() == 0
                || this.getSource_co() == null || this.getSource_co().length() == 0
                || this.getText() == null || this.getText().length() == 0
                || this.getTitle() == null || this.getTitle().length() == 0
                || this.getUrl() == null || this.getUrl().length() == 0
                || this.getWords() == null || this.getWords().length() == 0
                || this.getAuthor() == null) {
            return false;
        }
        try {
            Integer.parseInt(this.getWords());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String show() {
//
//      <URL> defines " http://global.factiva.com/aa/?ref=daitel0020010903dvct013l5&pp=1&fcpil=en&napc=S&sa_from="
//      <SOURCE> defines "Daily Telegraph"
//      <TITLE> defines "AngloGold mops up."
//      <AUTHOR> defines "PETER GOSNELL"
//      <WORDS> defines "310"
//      <DATE> defines "29 December 1999"
//      <SOURCE_CO> defines "Nationwide News Proprietary Ltd"
//      <DOC_ID> defines "daitel0020010903dvct013l5"
        String ret = "\t\t<ARTICLE>\n";
        ret += "\t\t\t<URL>" + this.getUrl() + "</URL>\n";
        ret += "\t\t\t<SOURCE>" + this.getSource() + "</SOURCE>\n";
        ret += "\t\t\t<TITLE>" + this.getTitle() + "</TITLE>\n";
        ret += "\t\t\t<AUTHOR>" + this.getAuthor() + "</AUTHOR>\n";
        ret += "\t\t\t<WORDS>" + this.getWords() + "</WORDS>\n";
        ret += "\t\t\t<DATE>" + this.getDate() + "</DATE>\n";
        ret += "\t\t\t<SOURCE_CO>" + this.getSource_co() + "</SOURCE_CO>\n";
        ret += "\t\t\t<DOC_ID>" + this.getDoc_id() + "</DOC_ID>\n";
        ret += "\t\t\t<TEXT>" + this.getText() + "</TEXT>\n";
        ret += "\t\t</ARTICLE>\n";
        return ret;
    }

    public int compareTo(Object o) {
        NewsUnit unit = (NewsUnit)o;
        return this.getSource().compareTo(unit.getSource());
    }
}
