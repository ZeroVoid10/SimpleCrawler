package xyz.zerovoid.simplecrawler.example;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.TextPipeline;
import xyz.zerovoid.simplecrawler.spider.RunnableSpider;
import xyz.zerovoid.simplecrawler.spider.RunnableSpiderBuilder;
import xyz.zerovoid.simplecrawler.spider.SimpleSpider;
import xyz.zerovoid.simplecrawler.spider.SimpleSpiderBuilder;
import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @Author 
 * @since 0.2.1
 */
public class NovelParser extends AbstractParser {

    private static final Logger logger = 
        LoggerFactory.getLogger(NovelParser.class);

    @Override
    public Items parse(Page page) {
        if (page.getRequest().tagsIsEmpty()) {
            return getContent(page);
        } else {
            return getArticle(page);
        }
    }

	private Items getContent(Page page) {
        Items items = new Items(page.getRequest());
        Document doc = Jsoup.parse(page.getRawText());
        try {
            Elements links = doc.select("span").select("a[href]");
            for (Element link : links) {
                logger.debug("Get link {} : {}", link.text(), link.attr("href"));
                String title = link.text();
                String url = link.attr("href");
                Request request = new Request(url);
                request.addTag("text");
                items.put(title, url);
                items.addRequest(request);
            }
        } catch(Exception e) {
            logger.error("unfind content.");
            e.printStackTrace();
        }

		return items;
	}

    private Items getArticle(Page page) {
        logger.debug("Get Article.");
        Items items = new Items(page.getRequest());
        Document doc = Jsoup.parse(page.getRawText());
        try {
            Element text = doc.selectFirst("div[id=BookText]");
            Element titleElement = doc.selectFirst("title");
            items.put(titleElement.text(), text.text());
        } catch(Exception e) {
            logger.warn("unfind article or title " + page.getRequest().getUrl());
            e.printStackTrace();
        }

        return items;
    }

    public static void main(String[] args) throws IOException {
        RunnableSpider spider = RunnableSpiderBuilder.getNewBuilder()
            .addRequest("http://www.shizongzui.cc/santi/")
            .setParser(new NovelParser())
            .setNThread(5)
            .setFileDir("/home/zerovoid/Downloads/text/")     
            .build();
//    	SimpleSpider spider = SimpleSpiderBuilder.getNewBuilder()
//    			.addRequest("http://www.shizongzui.cc/santi/")
//    			.setParser(new NovelParser())
//    			.addPipelens(new TextPipeline("/home/zerovoid/Downloads/text/single/"))
//    			.build();
        spider.crawl();
    }
}
