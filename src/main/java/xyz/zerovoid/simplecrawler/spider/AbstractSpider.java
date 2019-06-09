package xyz.zerovoid.simplecrawler.spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import xyz.zerovoid.simplecrawler.downloader.AbstractDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.AbstractScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @since 0.2.0
 */
abstract public class AbstractSpider {

    protected AbstractParser parser;
    protected AbstractScheduler scheduler;
    protected AbstractDownloader downloader;
    protected ArrayList<Pipeline> pipelines;
    protected Set<Request> feedRequest;

    /**
     * Get this class by SpiderBuilder class.
     */
    protected AbstractSpider(AbstractParser parser,
            AbstractScheduler scheduler, 
            AbstractDownloader downloader, 
            ArrayList<Pipeline> pipelines) {
        this.parser = parser;
        this.scheduler = scheduler;
        this.downloader = downloader;
        this.pipelines = pipelines;
    }

    protected AbstractSpider() {
        pipelines = new ArrayList<Pipeline>();
        feedRequest = new HashSet<Request>();
    }

    /**
     * Run spider.
     * TODO: May change method name to run.
     **/
    public abstract void crawl();

    public void addUrl(String url) {
        this.scheduler.push(url);
    }

    public void addUrls(ArrayList<String> urls) {
        for (String url : urls) {
            addUrl(url);
        }
    }
}
