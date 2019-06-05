package xyz.zerovoid.simplecrawler.scheduler;

import java.util.Collection;

import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @since 0.1.0
 */
public abstract class AbstractScheduler {

    public abstract Request pop();

    public abstract boolean push(Request request);
    public abstract boolean push(String url);
	public abstract boolean hasNext();

    public abstract void push(Collection<? extends Request> requests);
    
}
