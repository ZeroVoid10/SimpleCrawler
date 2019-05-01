package xyz.zerovoid.simplecrawler.parser;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.zerovoid.simplecrawler.item.Item;
import xyz.zerovoid.simplecrawler.item.SimpleItem;
import xyz.zerovoid.simplecrawler.parser.Parser;

public class SimpleParser implements Parser {

    private String urlRender;
    public SimpleParser(String urlRender) {
        this.urlRender = urlRender;
    }

	@Override
	public Item parse(String rawPage) {
        Document doc = Jsoup.parse(rawPage);
        SimpleItem item = new SimpleItem();

        Element pageName = doc.selectFirst("h1[id=\"firstHeading\"]");
        item.setPageName(pageName.ownText());

        //List<String> newUrls = doc.selectFirst(
        //        "div[id=\"mw-content-text\"]").select(
        //            "a[href][title]").eachAttr("href");
        Elements ul = doc.select("ul");
        Elements eleUrls = ul.select("a[href][title]");
        List<String> newUrls = eleUrls.eachAttr("href");
        for (String url : newUrls) {
            if (url.charAt(0) == '/') {
                item.addUrl(this.urlRender + url);
            } else {
                item.addUrl(url);
            }
        }

		return item;
	}
    
}
