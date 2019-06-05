package xyz.zerovoid.simplecrawler.spider;

import java.util.ArrayList;
import java.util.Set;

import xyz.zerovoid.simplecrawler.downloader.AbstractDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.AbstractScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

abstract public class AbstractSpider {

    protected AbstractParser parser;
    protected AbstractScheduler scheduler;
    protected AbstractDownloader downloader;
    protected ArrayList<Pipeline> pipelines;
    protected Set<Request> feedRequest;
    
    protected AbstractSpider(AbstractParser parser,
            AbstractScheduler scheduler, 
            AbstractDownloader downloader, 
            ArrayList<Pipeline> pipelines) {
        this.parser = parser;
        this.scheduler = scheduler;
        this.downloader = downloader;
        this.pipelines = pipelines;
    }

    /**
     * Run spider.
     **/
    public abstract void run();

    public void addUrl(String url) {
        this.scheduler.push(url);
    }

    public void addUrls(ArrayList<String> urls) {
        for (String url : urls) {
            addUrl(url);
        }
    }
}
