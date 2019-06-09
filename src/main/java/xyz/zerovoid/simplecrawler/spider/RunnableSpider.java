package xyz.zerovoid.simplecrawler.spider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.downloader.SimpleDownloader;
import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.Pipeline;
import xyz.zerovoid.simplecrawler.scheduler.SimpleScheduler;
import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * FIXME: Maker thread safely.
 * @since 0.2.1
 */
public class RunnableSpider extends AbstractSpider {

    private static final Logger logger = 
        LoggerFactory.getLogger(RunnableSpider.class);

    protected int nThread = 2;
    protected ExecutorService threadPool;
    protected AtomicInteger runningTread = new AtomicInteger();
    protected ReentrantLock schedulerLock = new ReentrantLock();
    protected Condition newUrlCondition = schedulerLock.newCondition();
    protected final long sleepTime = 2000L;
    protected ReentrantLock poolLock = new ReentrantLock();
    protected Condition poolCondition = poolLock.newCondition();

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
            Request request;
            synchronized (scheduler) {
                request = scheduler.pop();
            }
            if (runningTread.get() >= nThread) {
                try {
                    poolLock.lock();
                    while (runningTread.get() >= nThread) {
                        try {
                            poolCondition.await();
                        } catch (InterruptedException e) {
                        } 
                    }
                } finally {
                    poolLock.unlock();
                }
            }
            runningTread.incrementAndGet();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
    	            logger.debug("Thread start.");
                    try {
                        process(request);
                    } finally {
                        try {
                            poolLock.lock();
                            runningTread.decrementAndGet();
                            poolCondition.signal();
                        } finally {
                            poolLock.unlock();
                        }
                    }
                    logger.debug("Thread end");
                }
            });
            //try {
			//	Thread.sleep(10);
			//} catch (InterruptedException e) {
			//	e.printStackTrace();
			//}
        }
        threadPool.shutdown();
    }

    public void process(Request request) {
        try {
            logger.info("Process {}", request.getUrl());
            try {
                Page page = downloader.download(request);
                Items items = parser.parse(page);
                if (items != null) {
                    pipelines.get(0).processItem(items);
                    scheduler.push(items.getNewRequest());
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                schedulerLock.lock();
                newUrlCondition.signalAll();
            } finally {
                schedulerLock.unlock();
            }
        }
    }

    protected void pipelineProcess(Items items) {
        pipelines.get(0).processItem(items);
    }

    protected boolean stillContinue() {
    	logger.debug("Check continue.");
        if (scheduler.hasNext()) {
            return true;
        } else if (runningTread.get() == 0) {
            return false;
        }
        schedulerLock.lock();
        try {
            if (runningTread.get() == 0) {
                logger.info("In double check.");
                return stillContinue();
            }
            logger.debug("Main thread await.");
            newUrlCondition.await(sleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("Wait URL interrupt. Exception {}", e);
        } finally {
            schedulerLock.unlock();
        }
        return stillContinue();
    }
}
