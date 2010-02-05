/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usyd.action;

import com.usyd.log.Logger;
import com.usyd.unit.CompanyUnit;
import com.usyd.unit.NewsUnit;
import com.usyd.page.CompanyNameExtractor;
import com.usyd.page.NewsItemExtractor;
import com.usyd.page.NewsListExtractor;
import com.usyd.unit.SearchUnit;
import com.usyd.util.FileLoader;
import com.usyd.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

/**
 *
 * @author yy
 */
public class FactivaSearch extends Action {


    /*
     *  Pre-condition: 'LoginUNSW'
     *
     *  1.  The fastiva search webpage has been opened
     *  2.  Authentication has been setup.
     */
    private Login login;
    private List<String> companyList;

    public FactivaSearch(List<String> companyList,String user, String pass) {
        this.login = new LoginUSYD(user,pass);
        this.httpClient = login.getHttpclient();
        this.companyList = companyList;
    }

    public Login getLogin() {
        return login;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CompanyUnit getCompanyName(String code, String ticker, String company, boolean fuzzy) {

        CompanyUnit unit = null;

        int sleep = 1;
        while (true) {
            String _xformsessstate = login.getXFORMSESSSTATE();
            String rsp;

            String url = login.getSbService();
            //  String url = "http://global.factiva.com/sb/sbservice.aspx";
            //  String url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/sb/sbservice.aspx";
            NameValuePair[] data = {
                new NameValuePair("_XFORMSESSSTATE", _xformsessstate),
                new NameValuePair("hideInfo", "false"),
                new NameValuePair("grpOnly", "false"),
                new NameValuePair("showFii", "true"),
                new NameValuePair("iType", "co"),
                new NameValuePair("query", company),};

            rsp = this.getPostContent(url, data);

            CompanyNameExtractor extractor = new CompanyNameExtractor(rsp);
            unit = extractor.loadCompanyName(company, fuzzy);

            if (unit != null) {
                unit.setCode(code);
                unit.setTicker(ticker);
//                Get the name of the company, return!
                break;
            } else if (extractor.isErrorPage()) {
//                Reach the service quota
                sleep = reset(sleep);
                continue;
            } else if (unit == null && company.contains("limited")) {
//                remove 'limited' from the compay name and try again
                company = company.replaceAll("limited", "");
            } else {
//                failed in every attempt, return!
                break;
            }
        }
        return unit;
    }

    public List<String> getNewsLinks(String rsp) {

//        rsp page is an valid artical searching page;

        Logger.log("collecting links ... ");

        NewsListExtractor extractor = new NewsListExtractor(rsp);
        int numOfItems = extractor.getNumOfNews();
        int numOfPages = numOfItems / 100 + 1;
        int currentPage = 2;

        List<String> list = extractor.getLinks();

        while (currentPage <= numOfPages) {

            int sleep = 1;
            while (true) {

                login.updateViewState(rsp);
                //  update viewstate for each page change;

                NameValuePair[] data = FileLoader.getNextPage(login.getXFORMSESSSTATE(),
                        login.getXFORMSTATE(), (currentPage - 1) * 100, numOfItems);

                String url = login.getDefault();
                //String url = "http://global.factiva.com/ha/default.aspx";

                //String url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/ha/default.aspx";
                rsp = this.getPostContent(url, data);

                extractor = new NewsListExtractor(rsp);

                if (extractor.isErrorPage() || extractor.isTwoExpressionError()) {

                    sleep = reset(sleep);
                    if (sleep > 300) {
                        Logger.log("\n============== Turning Response Error ========================\n");
                        return null;
                    }
                } else {
                    List<String> newsList = extractor.getLinks();

                    if (newsList.size() == 0) {
                        Logger.error("\n============== Turning Page Error ========================\n");
                        Logger.error(rsp);
                    }
                    for (String str : newsList) {
                        list.add(str);
                    }
                    break;
                }
            }
            currentPage++;
        }
        Logger.log("expected: " + numOfItems + " ");

        return list;
    }

    public void getNewsByCompany(CompanyUnit unit, StringBuffer buffer) {

        List<NewsUnit> output = new ArrayList<NewsUnit>();
        List<String> newsList = new ArrayList();
        
        String url = login.getDefault();
//        String url = "http://global.factiva.com/ha/default.aspx";
//        String url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/ha/default.aspx";


        String _COMPANY_NAME = "[{30:0,5:\""
                + unit.getShortName().trim() + "\",29:0,28:\""
                + unit.getFullName().trim() + "\",33:0,21:0,20:0}]";
        NameValuePair[] data = FileLoader.getPostValues(login.getXFORMSESSSTATE(),
                login.getXFORMSTATE(), _COMPANY_NAME);

        int sleep = 1;
        while (true) {

            String rsp = this.getPostContent(url, data);
            NewsListExtractor extractor = new NewsListExtractor(rsp);
            if (!extractor.isErrorPage()) {
                newsList = this.getNewsLinks(rsp);
                if (newsList == null) {
                    continue;
                } else {
                    Logger.log("collected: " + newsList.size() + "\n\n");
                    break;
                }
            } else {
                sleep = reset(sleep);
            }
        }

        int counter = 1;
        for (String link : newsList) {

            // open each articles

            url = login.getAa(link);

//            url = "http://global.factiva.com/aa/?" + link;
//            url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/aa/?" + link;

            sleep = 1;
            while (true) {

                String newsPage = this.getGetContent(url);
                newsPage = newsPage.replaceAll("\r\n", "");
                // format the page
                NewsItemExtractor extractor = new NewsItemExtractor(newsPage);
                if (extractor.isErrorPage()) {
                    sleep = reset(sleep);
                    continue;
                } else {

                    NewsUnit item = new NewsUnit(url);
                    item = extractor.getNews(item);

                    if (item != null) {
                        Logger.log("------" + counter + "------\n"
                                + "Retrieving: " + item.getTitle() + " | "
                                + item.getDate() + "\n" + "\n");
                        Logger.updateProgress(newsList.size(), counter, unit.getSearchName());
                        output.add(item);

                    } else {
                        Logger.log("\n\n\n####--" + counter + "--####\n"
                                + "ERROR: " + url + "\n" + "####--" + counter + "--####\n\n" + "\n");
                        Logger.error(extractor.getPage());
                    }
                    counter++;
                    break;
                }
            }
        }
        Collections.sort(output);
        for (NewsUnit news : output) {
            String searchProfile = StringUtil.getSearchProfile(unit.getCode(),
                    unit.getTicker(), unit.getSearchName());
            buffer.append(searchProfile);
            buffer.append(news.show());
        }
    }

    private int reset(int time) {

        if (time > 600) {
            Logger.log("WARNING: no connection available, mission failed, reset the timer!\n");
            time = 1;
        }
        String text1 = "\nNOTICE: The server has blocked this token ..." + "\n";
        String text2 = "NOTICE: Get a new token in " + time + " secs..." + "\n\n";

        Logger.log(text1);
        Logger.log(text2);

        try {

            Thread.sleep(time * 1000);
            httpClient = login.getHttpclient();
        } catch (Exception e) {
        }
        return time * 2;
    }





    public void start(boolean fuzzy) {


        List<SearchUnit> searchList = FileLoader.filter(companyList, fuzzy);
       
//        filter out finished projects


        for (SearchUnit searchUnit : searchList) {

            String name = searchUnit.getName();
            String ticker = searchUnit.getTicker();
            String code = searchUnit.getCode();

            Logger.log("PARSING: " + name + " ... \n");

            CompanyUnit unit = getCompanyName(code, ticker, name, fuzzy);
            StringBuffer buffer = new StringBuffer();
            String header = StringUtil.getXMLheader();
            buffer.append(header);
            String path = "";

            if (unit != null) {
                Logger.log("SUCCESS: " + unit.getShortName() + " => " + unit.getFullName() + "\n\n");
                getNewsByCompany(unit, buffer);
                path = "out/";
            } else {
                Logger.log("FAILURE: " + name + " NOT FOUND!" + "\n\n");
                Logger.miss(searchUnit.getCsvRecord());
                path = "emp/";
            }
            String footer = StringUtil.getXMLfooter();
            buffer.append(footer);
            Logger.store(buffer.toString(), path + searchUnit.getXmlFile());

        }
    }
}
