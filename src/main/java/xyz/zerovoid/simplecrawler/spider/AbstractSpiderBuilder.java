package xyz.zerovoid.simplecrawler.spider;

import java.util.Collection;

import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
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
    protected abstract AbstractSpider createSpider();

	public abstract AbstractSpiderBuilder setParser(AbstractParser parser);
	public abstract AbstractSpiderBuilder addPipelens(Pipeline pipeline);
	public abstract AbstractSpiderBuilder addRequest(Request request);
    public abstract AbstractSpiderBuilder addRequest(String url);
    public abstract AbstractSpiderBuilder addAllRequest(
            Collection<? extends Request> requests);
}
