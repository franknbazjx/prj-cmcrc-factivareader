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
import com.usyd.unit.ArgumentUnit;
import com.usyd.unit.DatePairs;
import com.usyd.unit.PageUnit;
import com.usyd.unit.SearchUnit;
import com.usyd.util.FileLoader;
import com.usyd.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
//    private List<String> companyList;
    private ArgumentUnit argument;

    public FactivaSearch(ArgumentUnit argument, String user, String pass) {
        //this.login = new LoginUSYD(user, pass);
        this.login = new LoginUNSW();
        this.httpClient = login.getHttpclient();
        this.argument = argument;
    }

    public Login getLogin() {
        return login;
    }


    public CompanyUnit getCompanyName(String code, String ticker, String company, boolean fuzzy) {

        CompanyUnit unit = null;

        int sleep = 1;
        while (true) {
            String _xformsessstate = login.getXFORMSESSSTATE();
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

            String rsp = this.getPostContent(url, data);

            CompanyNameExtractor extractor = new CompanyNameExtractor(rsp);
            unit = extractor.loadCompanyName(company, fuzzy);

            if (unit != null) {
                unit.setCode(code);
                unit.setTicker(ticker);
//                Get the name of the company, return!
                break;
            } else if (extractor.isErrorPage()) {
//                Reach the service quota
                sleep = reset(sleep, login);
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



    public void loadNewsLinks(String rsp, PageUnit pageUnit) {

//        rsp page is an valid artical searching page;

        //int numOfPages = pageUnit.getNumOfPages();
        //int currentPage = pageUnit.getCurrentPage();
        //int numOfLinks = pageUnit.getNumOfLinks();
        NewsListExtractor extractor;

        if (pageUnit.getCurrentPage() == 0) {
            /*
             * For every new Searching Action, in the first returnning page,
             * a number of data will be captured:
             *
             * 1. The total number of news links
             * 2. The links in the first page
             * 3. The total number of pages will be computed dynamically
             *
             * This procedure only happends when the program launch a probing for
             * the first time. It means if the links collecting procedure was interrupted,
             * when the program tried to re-fill the pageUnit, this procedure will be
             * skipped, and pageUnit filling will continue instead of doing repeated work.
             */
            Logger.log("collecting links ... ");
            extractor = new NewsListExtractor(rsp);
            int numOfLinks = extractor.getNumOfNews();
            List<String> list = extractor.getLinks();
            pageUnit.setList(list);
            pageUnit.setNumOfLinks(numOfLinks);
            pageUnit.setNumOfPages((numOfLinks - 1) / 100 + 1);
            pageUnit.setCurrentPage(2);
        }

        while (pageUnit.getCurrentPage() <= pageUnit.getNumOfPages()) {

            Logger.log("\n============== Continue with page [" + pageUnit.getCurrentPage()
                    + "/" + pageUnit.getNumOfPages() + "] ========================\n");

            while (true) {

                login.updateViewState(rsp);
                //  update viewstate for each page change;
                NameValuePair[] data = FileLoader.getNextPage(login.getXFORMSESSSTATE(),
                        login.getXFORMSTATE(), (pageUnit.getCurrentPage() - 1) * 100, pageUnit.getNumOfPages());
                String url = login.getDefault();
                //String url = "http://global.factiva.com/ha/default.aspx";
                //String url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/ha/default.aspx";
                
                rsp = this.getPostContent(url, data);
                extractor = new NewsListExtractor(rsp);

                if (rsp.equals("") || extractor.isErrorPage() || extractor.isTwoExpressionError()) {
                    /*
                     *  Normally then the program reaches here, it means the
                     *  Server has detected the abnomoral client behaviour
                     *
                     *  Since the Turning Page action is not stateless, thus
                     *  return to the Callee can let the Callee re-launch the
                     *  Probing is necessary
                     */
                    Logger.log("\n== Turning Page Error at page [" + pageUnit.getCurrentPage() + "] ==\n");
                    Logger.error(rsp);
                    return;
                } else {
                    List<String> newsList = extractor.getLinks();
                    if (newsList.size() == 0) {
                        Logger.error("\n== Empty Page Error at page [" + pageUnit.getCurrentPage() + "] ==\n");
                        Logger.error(rsp);
                    } else {
                        for (String str : newsList) {
                            pageUnit.add(str);
                        }
                        break;
                    }
                }
            }
            pageUnit.nextPage();
        }
        pageUnit.setFinish();
    }

    private List<DatePairs> getDateList(String url, NameValuePair[] data) {
        List<DatePairs> dateList = new ArrayList<DatePairs>();
        int sleep = 1;
        while (true) {
            String rsp = this.getPostContent(url, data);
            NewsListExtractor extractor = new NewsListExtractor(rsp);
            if (!extractor.isErrorPage()) {
                int links = extractor.getNumOfNews();
                Logger.log("expected: " + links + " ");
                if (links > 9999) {
                    Logger.log("page number exceeds limitation, divide and conque\n");
                    dateList = argument.divide();
                } else {
                    dateList.add(argument.getDatePairs());
                }
                break;
            } else {
                sleep = reset(sleep, login);
            }
        }
        return dateList;
    }

    public void getNewsByCompany(CompanyUnit unit, StringBuffer buffer) {

        List<NewsUnit> output = new ArrayList<NewsUnit>();
        String url = login.getDefault();
//        String url = "http://global.factiva.com/ha/default.aspx";
//        String url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/ha/default.aspx";


        String _COMPANY_NAME = "[{30:0,5:\""
                + unit.getShortName().trim() + "\",29:0,28:\""
                + unit.getFullName().trim() + "\",33:0,21:0,20:0}]";
        NameValuePair[] data = FileLoader.getPostValues(argument.getDatePairs(), login.getXFORMSESSSTATE(),
                login.getXFORMSTATE(), _COMPANY_NAME);

        List<DatePairs> dateList = this.getDateList(url, data);
        /*
         *  If the page number exceeds 100;
         *  if it does, divide the datePairs
         */

        PageUnit mainPageUnit = new PageUnit();

        for (DatePairs datePairs : dateList) {
            Logger.log("Collecting links " + datePairs.show() + "\n");
            data = FileLoader.getPostValues(datePairs, login.getXFORMSESSSTATE(),
                    login.getXFORMSTATE(), _COMPANY_NAME);
            int sleep = 1;
            PageUnit pageUnit = new PageUnit();
            //  links carrier
            while (true) {

                String rsp = this.getPostContent(url, data);
                NewsListExtractor extractor = new NewsListExtractor(rsp);
                if (!extractor.isErrorPage()) {
                    loadNewsLinks(rsp, pageUnit);
                    if (!pageUnit.isFinished()) {
                        //  links fetching interrupted
                        Logger.log("too many pages, sleep for 30 secs\n");
                        reset(30, login);
                        continue;
                    } else {
                        Logger.log("collected: " + pageUnit.size() + "\n\n");
                        break;
                    }
                } else {
                    sleep = reset(sleep, login);
                }
            }
            mainPageUnit.concat(pageUnit.getList());
        }

        Logger.log("start processing " + mainPageUnit.size() + " links\n\n");

        int counter = 1;
        for (String link : mainPageUnit.getList()) {

            // open each article links
            url = login.getAa(link);
//            url = "http://global.factiva.com/aa/?" + link;
//            url = "http://global.factiva.com.ezproxy1.library.usyd.edu.au/aa/?" + link;

            int sleep = 1;
            while (true) {

                String newsPage = this.getGetContent(url);
                newsPage = newsPage.replaceAll("\r\n", "");
                // format the page
                NewsItemExtractor extractor = new NewsItemExtractor(newsPage);

                if (newsPage.equals("") || extractor.isErrorPage()) {
                    sleep = reset(sleep, login);
                    continue;
                } else {

                    NewsUnit item = new NewsUnit(url);
                    item = extractor.getNews(item);
                    if (item != null) {
                        Logger.log("------" + counter + "------\n"
                                + "Retrieving: " + item.getTitle() + " | "
                                + item.getDate() + "\n" + "\n");
                        Logger.updateProgress(mainPageUnit.size(), counter, unit.getSearchName());
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



    public void start(boolean fuzzy) {


        List<SearchUnit> searchList = FileLoader.filter(argument.getCompanyList(), fuzzy);

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
