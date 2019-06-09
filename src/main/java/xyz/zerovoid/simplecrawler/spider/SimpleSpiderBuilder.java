package xyz.zerovoid.simplecrawler.spider;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.ConsolePipeline;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @since 0.2.0
 */
public class SimpleSpiderBuilder extends AbstractSpiderBuilder {

    protected static Logger logger = 
        LoggerFactory.getLogger(SimpleSpiderBuilder.class);

    protected AbstractParser parser;
    protected SimpleScheduler scheduler;
    protected SimpleDownloader downloader;
    protected ArrayList<Pipeline> pipelines;

    protected ArrayDeque<Request> feedRequest;
    protected int maxUrl = 0;
    protected HttpClientBuilder httpClientBuilder;

    public static SimpleSpiderBuilder getNewBuilder() {
        return new SimpleSpiderBuilder();
    }

    protected SimpleSpiderBuilder() {
        pipelines = new ArrayList<Pipeline>();
        feedRequest = new ArrayDeque<Request>();
    }

	@Override
	public SimpleSpider build() {
        if (parser == null) {
            logger.error("Can not build spider without set parser.");
            throw new NullPointerException();
        }

        if (scheduler == null) {
            createScheduler();
        }

        if (downloader == null) {
            createDownloader();
        }

        if (pipelines.isEmpty()) {
            createPipelines();
        }

        return createSpider();
	}

    @Override
    protected SimpleSpider createSpider() {
		return new SimpleSpider(parser, scheduler, downloader, pipelines);
    }

	@Override
	protected void createScheduler() {
        scheduler = new SimpleScheduler(feedRequest, maxUrl);
	}

	@Override
	protected void createDownloader() {
        if (httpClientBuilder == null) {
            downloader = new SimpleDownloader();
        } else {
            downloader = new SimpleDownloader(httpClientBuilder);
        }
	}

	@Override
    protected void createPipelines() {
        pipelines.add(new ConsolePipeline());
	}

	@Override
	public SimpleSpiderBuilder setParser(AbstractParser parser) {
        this.parser = parser;
        return this;
	}

	@Override
	public SimpleSpiderBuilder addPipelens(Pipeline pipeline) {
        pipelines.add(pipeline);
		return this;
	}

	@Override
	public SimpleSpiderBuilder addRequest(Request request) {
        feedRequest.add(request);
		return this;
	}

	@Override
	public SimpleSpiderBuilder addRequest(String url) {
        feedRequest.add(new Request(url));
		return this;
	}

	@Override
	public SimpleSpiderBuilder addAllRequest(Collection<? extends Request> requests) {
        feedRequest.addAll(requests);
		return this;
	}

    public SimpleSpiderBuilder setMaxUrl(int maxUrl) {
        this.maxUrl = maxUrl;
        return this;
    }

    public SimpleSpiderBuilder setHttpClientBuilder(
            HttpClientBuilder builder) {
        this.httpClientBuilder = builder;
        return this;
    }
}

