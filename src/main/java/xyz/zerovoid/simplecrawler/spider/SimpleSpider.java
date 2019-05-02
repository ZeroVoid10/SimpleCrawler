package xyz.zerovoid.simplecrawler.spider;

import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.engine.SimpleEngine;
import xyz.zerovoid.simplecrawler.parser.SimpleParser;

/**
 * Just a simple example of a spider.
 * TODO: Abstract important part to make Spider interface.
 * @author Zero Void <zerovoid10@163.com, zerolivenjoy@gmail.com>
 */
public class SimpleSpider extends Spider {

	private final static Logger logger = 
        LoggerFactory.getLogger(SimpleSpider.class);

    private SimpleEngine engine;
    private ArrayDeque<String> feedUrl;

    /**
     * Simplespider constructor.
     */
    public SimpleSpider(String urlRender) {
    	logger.trace("Construct");
        this.engine = new SimpleEngine(new SimpleParser(urlRender));
        this.feedUrl = new ArrayDeque<String>();
    }

    public SimpleSpider(String url, String urlRender) {
        this(urlRender);
        feedUrl.add(url);
    }

    public void run() {
        for (String url : feedUrl) {
        	logger.trace("run. url:{}", url);
            engine.run(url);
        }
    }

    public void run(String url) {
        engine.run(url);
    }

    /**
     * Test class.
     * The same code in test folder.
     */
    public static void main(String[] args) {
        SimpleSpider spider = new SimpleSpider(
                "https://zh.moegirl.org/CAROLE_%26_TUESDAY",
                "https://zh.moegirl.org");
        spider.run();
    }
}
