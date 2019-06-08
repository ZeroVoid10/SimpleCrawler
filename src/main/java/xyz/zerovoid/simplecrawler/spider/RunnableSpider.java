package xyz.zerovoid.simplecrawler.spider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * FIXME: Maker thread safely.
 * @since 0.3.0
 */
public class RunnableSpider extends SimpleSpider implements Runnable {

    protected int nThread = 2;
    protected ExecutorService threadPool;
    protected int runningTread = 0;

    protected RunnableSpider(AbstractParser parser,
            SimpleScheduler scheduler, 
            SimpleDownloader downloader, 
            ArrayList<Pipeline> pipelines,
            int nThread) {
        super(parser, scheduler, downloader, pipelines);
        this.nThread = nThread;
        threadPool = Executors.newFixedThreadPool(nThread);
    }
    
    @Override
    public void crawl() {
        logger.info("Spider start crawl.");
        while(stillContinue()) {
            threadPool.execute(this);
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        threadPool.shutdown();
    }

    @Override
    public void run() {
    	runningTread++;
    	logger.debug("Thread start.");
    	Request request;
    	synchronized (scheduler) {
			request = scheduler.pop();
		}
    	logger.info("Process {}", request.getUrl());
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
        runningTread--;
        logger.debug("Thread end");
    }

    protected void pipelineProcess() {
        pipelines.get(0).processItem(items);
    }

    protected boolean stillContinue() {
    	logger.debug("Check continue.");
    	synchronized (scheduler) {
    		if (scheduler.hasNext()) {
                return true;
            } else {
                while (runningTread > 0) {
                    try {
    					Thread.sleep(1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
                    if (scheduler.hasNext())
                        return true;
                }
                return scheduler.hasNext();
            }
		}
    }
}
