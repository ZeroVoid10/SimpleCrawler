package xyz.zerovoid.simplecrawler.example;
import java.io.IOException; import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.BytePipeline;
import xyz.zerovoid.simplecrawler.spider.RunnableSpider;
import xyz.zerovoid.simplecrawler.spider.RunnableSpiderBuilder;
import xyz.zerovoid.simplecrawler.spider.SimpleSpider;
import xyz.zerovoid.simplecrawler.spider.SimpleSpiderBuilder;
import xyz.zerovoid.simplecrawler.util.Page;

public class PicParser extends AbstractParser {

    private final String prefix = "http://p1.xiaoshidi.net/";
    private int pageNum = 0;

    @Override
	public Items parse(Page page) {
        if (page.getStatusCode() == 500) {
            return null;
        }
        pageNum++;
        Items items = new Items(page.getRequest());
        Pattern pattern = Pattern.compile("(.*)var mhurl=\"(.*jpg)\"(.*)");
        Matcher matcher;
        String picUrl = null;
        Document doc = Jsoup.parse(page.getRawText());
        Elements js = doc.select("script[type=text/javascript]");
        for (Element item : js) {
            String data = item.data();
            matcher = pattern.matcher(data);
            if (matcher.matches()) {
                picUrl = prefix + matcher.group(2);
                break;
            }
        }
        items.addRequest("https://manhua.fzdm.com/39/117/index_" + pageNum + 
                ".html");
        items.put("download", picUrl);
        items.put("title", "进击巨人117话-"+pageNum+".jpg");

        return items;
	}

    public static void main(String[] args) throws IOException {
        SimpleSpider spider = SimpleSpiderBuilder.getNewBuilder()
            .addRequest("https://manhua.fzdm.com/39/117/")
            .setParser(new PicParser())
            .addPipelens(new
                    BytePipeline("/home/zerovoid/Downloads/pic/single/"))
            .build();
        //RunnableSpider spider = RunnableSpiderBuilder.getNewBuilder()
        //    .addRequest("https://manhua.fzdm.com/39/117/")
        //    .setParser(new PicParser())
        //    .addPipelens(new
        //                BytePipeline("/home/zerovoid/Downloads/pic/multi/"))
        //    .setNThread(5)
        //    .build();
        spider.crawl();
    }
}
