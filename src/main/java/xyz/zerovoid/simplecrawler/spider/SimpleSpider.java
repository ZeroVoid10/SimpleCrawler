package xyz.zerovoid.simplecrawler.spider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.parser.SimpleParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;
import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * Just a simple example of a spider.
 * @since 0.1.0
 * TODO: Abstract important part to make Spider interface.
 */
public class SimpleSpider extends AbstractSpider {

	private final static Logger logger = 
        LoggerFactory.getLogger(SimpleSpider.class);
    //protected SimpleParser parser;
    //protected SimpleScheduler scheduler;
    //protected SimpleDownloader downloader;
    //protected ArrayList<Pipeline> pipelines;

    /**
     * Simplespider constructor.
     */
    protected SimpleSpider(AbstractParser parser,
            SimpleScheduler scheduler, 
            SimpleDownloader downloader, 
            ArrayList<Pipeline> pipelines) {
        super(parser, scheduler, downloader, pipelines);
    }

    //public SimpleSpider(String url, String urlRender) {
    //    this(urlRender);
    //    feedUrl.add(url);
    //}

    @Override
    public void run() {
        logger.info("Spider start crawl.");
        Page page = null;
        Items items = null;
        Request request = null;
        while(scheduler.hasNext()) {
            request = scheduler.pop();
            try {
				page = downloader.download(request);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            items = parser.parse(page);
            scheduler.push(items.getNewRequest());
            pipelines.get(0).processItem(items);
        }
    }

    /**
     * Test.
     */
    public static void main(String[] args) {
        //SpiderFactory spiderFactory = SpiderFactory.getFactory();
        SimpleSpider spider = SimpleSpiderBuilder.getNewBuilder()
                        .addRequest("https://zh.moegirl.org/CAROLE_%26_TUESDAY")
                        .setParser(new SimpleParser())
                        .setMaxUrl(10)
                        .build();
        spider.run();
    }
}
