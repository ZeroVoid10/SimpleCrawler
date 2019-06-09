package xyz.zerovoid.simplecrawler.spider;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.pipeline.TextPipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

public class RunnableSpiderBuilder extends AbstractSpiderBuilder {
	protected static Logger logger = 
	        LoggerFactory.getLogger(RunnableSpiderBuilder.class);

    protected AbstractParser parser;
    protected SimpleScheduler scheduler;
    protected SimpleDownloader downloader;
    protected ArrayList<Pipeline> pipelines;

    protected ArrayDeque<Request> feedRequest;
    protected int maxUrl = 0;
    protected HttpClientBuilder httpClientBuilder;
    
    protected int nThread = 2;
    private String fileDir;
    private String fileType = "txt";
    
    @Override
	public RunnableSpider build() {
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

    public RunnableSpiderBuilder setNThread(int num) {
        this.nThread = num;
        return this;
    }

    public static RunnableSpiderBuilder getNewBuilder() {
        return new RunnableSpiderBuilder();
    }
    
    protected RunnableSpiderBuilder() {
        pipelines = new ArrayList<Pipeline>();
        feedRequest = new ArrayDeque<Request>();
    }

    @Override
    protected RunnableSpider createSpider() {
		return new 
            RunnableSpider(parser, scheduler, downloader, pipelines, nThread);
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
	public RunnableSpiderBuilder addRequest(Request request) {
        feedRequest.add(request);
		return this;
	}
	
	@Override
	public RunnableSpiderBuilder addRequest(String url) {
        feedRequest.add(new Request(url));
		return this;
	}

	@Override
	public RunnableSpiderBuilder addAllRequest(Collection<? extends Request> requests) {
        feedRequest.addAll(requests);
		return this;
	}
	
	@Override
	public RunnableSpiderBuilder setParser(AbstractParser parser) {
        this.parser = parser;
        return this;
	}

    @Override
    protected void createPipelines() {
        if (fileDir == null) {
            logger.error("Can not create TextPipeline without directory.");
        }
        try {
			pipelines.add(new TextPipeline(fileDir, fileType));
		} catch (IOException e) {
            logger.error("Can not create pipline.");
			e.printStackTrace();
		}
    }
    
    @Override
	public RunnableSpiderBuilder addPipelens(Pipeline pipeline) {
        pipelines.add(pipeline);
		return this;
	}
    
    public RunnableSpiderBuilder setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public RunnableSpiderBuilder setFileDir(String dir) {
        this.fileDir = dir;
        return this;
    }
    
    public RunnableSpiderBuilder setMaxUrl(int maxUrl) {
        this.maxUrl = maxUrl;
        return this;
    }

    public RunnableSpiderBuilder setHttpClientBuilder(
            HttpClientBuilder builder) {
        this.httpClientBuilder = builder;
        return this;
    }

}
        

