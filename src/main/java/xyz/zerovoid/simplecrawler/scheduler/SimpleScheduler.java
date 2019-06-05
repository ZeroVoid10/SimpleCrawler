package xyz.zerovoid.simplecrawler.scheduler;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @since 0.1.0
 */
public class SimpleScheduler extends AbstractScheduler {

    private static final Logger logger = 
        LoggerFactory.getLogger(SimpleScheduler.class);
    
    protected HashSet<String> urlSet;
    protected ArrayDeque<Request> requests;
    protected boolean limitedUrl = false;
    protected int countUrl;

    public SimpleScheduler() {
        urlSet = new HashSet<String>();
        requests = new ArrayDeque<Request>();
    }

    public SimpleScheduler(Collection<? extends Request> requests) {
        this();
        this.push(requests);
    }

    public SimpleScheduler(int maxUrl) {
        this();
        if (maxUrl > 0) {
            this.limitedUrl = true;
            setCountUrl(maxUrl);
        }
    }

    public SimpleScheduler(Collection<? extends Request> requests, int maxUrl) {
        this(maxUrl);
        this.push(requests);
    }

    @Override
	public boolean hasNext() {
        if (limitedUrl) {
            return (countUrl > 0 && !requests.isEmpty());
        }
		return !requests.isEmpty();
	}

	@Override
	public Request pop() throws NoSuchElementException {
        logger.info("scheduler pop");
        if (hasNext()) {
            countUrl -= (limitedUrl? 1:0);
            return requests.pop();
        }
        logger.warn("Try to pop from empty scheduler.");
        //throw new IllegalStateException();
        return null;
	}

	@Override
	public boolean push(Request request) {
        logger.info("Scheduler push:{}", request.getUrl());
        if (urlSet.add(request.getUrl())) {
            requests.add(request);
            return true;
        }
        return false;
	}

	@Override
	public void push(Collection<? extends Request> requests) {
        for (Request request: requests) {
            this.push(request);
        }
	}

    public void setCountUrl(int maxUrl) {
        this.countUrl = maxUrl;
    }

    public int getCountUrl() {
        return countUrl;
    }

	@Override
	public boolean push(String url) {
        if (urlSet.add(url)) {
            requests.add(new Request(url));
            return true;
        }
        return false;
	}
}
