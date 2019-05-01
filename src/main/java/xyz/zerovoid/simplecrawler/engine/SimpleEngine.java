package xyz.zerovoid.simplecrawler.engine;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.item.SimpleItem;
import xyz.zerovoid.simplecrawler.parser.Parser;
import xyz.zerovoid.simplecrawler.parser.SimpleParser;
import xyz.zerovoid.simplecrawler.pipeline.SimplePipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;

public class SimpleEngine {

	final static Logger logger = LoggerFactory.getLogger(SimpleEngine.class);
	
    private SimpleScheduler scheduler;
    private SimpleDownloader downloader;
    private SimplePipeline pipeline;
    private SimpleParser parser;
    //private ArrayList<SimpleItem> items;
    private SimpleItem item;
    private int urlCount = 50;

    public SimpleEngine(Parser parser) {
    	logger.info("Construct");
        this.scheduler = new SimpleScheduler();
        this.downloader = new SimpleDownloader();
        this.pipeline = new SimplePipeline();
        //this.parser = new SimpleParser();
        this.parser = (SimpleParser)parser;
        this.item = new SimpleItem();
    }

    public void run(String url) {
        String rawPage = "";
        String toFetchUrl = "";
        this.urlCount -= (this.urlCount > 0 && 
                scheduler.push(url)? 1 : 0);
        while(scheduler.hasNext()) {
            try {
            	toFetchUrl = scheduler.pop();
				rawPage = downloader.download(toFetchUrl);
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            item = (SimpleItem)parser.parse(rawPage);
            item.setPageUrl(toFetchUrl);
            for (String newUrl : item.getUrls()) {
            	if (this.urlCount == 0) break;
                this.urlCount -= (this.urlCount > 0 && 
                    scheduler.push(newUrl)? 1 : 0);
            }
            pipeline.dump(item);
            logger.info("Sleep 0.1ec.");
            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
                logger.error("Sleep failed.");
				e.printStackTrace();
			}
        }
    }
}