package xyz.zerovoid.simplecrawler.spider;

import java.util.Collection;

import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @since 0.2.0
 * TODO: Need to modify framework, maybe add Factory class. And 
 *       make builder easier to use.
 */
public abstract class AbstractSpiderBuilder {

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
