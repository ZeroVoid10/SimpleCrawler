package xyz.zerovoid.simplecrawler.scheduler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleScheduler implements Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleScheduler.class);
    
    private HashSet<String> urlSet;
    private ArrayDeque<String> urls;

    public SimpleScheduler() {
        urlSet = new HashSet<String>();
        urls = new ArrayDeque<String>();
    }

	public boolean hasNext() {
		return !urls.isEmpty();
	}

	@Override
	public String pop() throws NoSuchElementException {
        logger.info("scheduler pop");
		return urls.pop();
	}

	@Override
	public boolean push(String url) {
        logger.info("Scheduler push:{}", url);
        if (urlSet.add(url))
            urls.add(url);
        else return false;
        return true;
	}

	@Override
	public void push(String[] urls) {
        for (String url : urls) {
            this.push(url);
        }
	}

}
