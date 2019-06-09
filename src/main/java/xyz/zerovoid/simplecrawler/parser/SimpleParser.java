package xyz.zerovoid.simplecrawler.parser;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.util.Page;

/**
 * Simple parser example.
 * @since 0.1.0
 */
public class SimpleParser extends AbstractParser {

    public SimpleParser() {
        super();
    }

    /**
     * To get page title and new URLs.
     */
	@Override
	public Items parse(Page page) {
        Document doc = Jsoup.parse(page.getRawText());
        Items items = new Items(page.getRequest());

        Element pageName = doc.selectFirst("h1[id=\"firstHeading\"]");
        items.put("Title", pageName.ownText());
        items.put("URL", page.getRequest().getUrl());

        Elements ul = doc.select("ul");
        Elements eleUrls = ul.select("a[href][title]");
        List<String> newUrls = eleUrls.eachAttr("href");
        for (String url : newUrls) {
            if (url.charAt(0) == '/') {
                items.addRequest(getRender(page.getRequest()) + url);
            } else {
                items.addRequest(url);
            }
        }

		return items;
	}
}
