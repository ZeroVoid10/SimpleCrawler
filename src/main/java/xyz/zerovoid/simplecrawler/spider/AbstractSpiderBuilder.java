package xyz.zerovoid.simplecrawler.spider;

import java.util.Collection;

import xyz.zerovoid.simplecrawler.downloader.AbstractDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.AbstractScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

public abstract class AbstractSpiderBuilder {

    //protected AbstractEngine engine;

    /**
     * Abstract methods.
     */
    public abstract AbstractSpider build();
    protected abstract void createScheduler();
    protected abstract void createDownloader();
    protected abstract void createPipelines();

	public abstract AbstractSpiderBuilder setParser(AbstractParser parser);
	public abstract AbstractSpiderBuilder 
        setScheduler(AbstractScheduler scheduler);
	public abstract AbstractSpiderBuilder 
        setDownloader(AbstractDownloader downloader);
	public abstract AbstractSpiderBuilder addPipelens(Pipeline pipeline);
	public abstract AbstractSpiderBuilder addRequest(Request request);
    public abstract AbstractSpiderBuilder addRequest(String url);
    public abstract AbstractSpiderBuilder addAllRequest(
            Collection<? extends Request> requests);
}
