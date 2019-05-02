package xyz.zerovoid.simplecrawler.item;

import java.util.ArrayList;

/**
 * @author Zero Void <zerovoid10@163.com, zerolivenjoy@gmail.com>
 */
public class SimpleItem implements Item {
    
    private String pageUrl;
    private ArrayList<String> urls;
    private String pageName;

    public SimpleItem() {
        this.urls = new ArrayList<String>();
    }

    public void addUrl(String url) {
        urls.add(url);
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

}
