package xyz.zerovoid.simplecrawler.parser;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.zerovoid.simplecrawler.item.Item;
import xyz.zerovoid.simplecrawler.item.SimpleItem;
import xyz.zerovoid.simplecrawler.parser.Parser;

/**
 * @author Zero Void <zerovoid10@163.com, zerolivenjoy@gmail.com>
 */
public class SimpleParser implements Parser {

    // TODO: Need A new class/metho for doing it.
    private String urlRender;

    public SimpleParser(String urlRender) {
        this.urlRender = urlRender;
    }

    /**
     * To get page title and new URLs.
     */
	@Override
	public Item parse(String rawPage) {
        Document doc = Jsoup.parse(rawPage);
        SimpleItem item = new SimpleItem();

        Element pageName = doc.selectFirst("h1[id=\"firstHeading\"]");
        item.setPageName(pageName.ownText());

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
