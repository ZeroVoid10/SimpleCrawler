package xyz.zerovoid.simplecrawler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Requested URL.
 * Let concrete Downloader know which page to Download.
 * @since 0.2.0
 * TODO: Change into be builder pattern.
 */
public class Request {

    private static final Logger logger = 
        LoggerFactory.getLogger(Request.class);
    private static final String defaultAgent = 
                "Mozilla/5.0 (X11; Linux x86_64) " 
                + "AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/73.0.3683.103 Safari/537.36";
    private static final String defaultMethod = "GET";
    private static final int defaultPriority = 0;
    
    protected String url;
    protected String method;
    protected int priority;
    protected HashMap<String, String> headers;
    /**
     * Let parser to know how to parser.
     * TODO: Move to Page class.
     */
    protected HashSet<String> tags;

    protected Request() {
        logger.debug("Create Request.");
        headers = new HashMap<String, String>();
        tags = new HashSet<String>();
        setHeader("User-Agent", defaultAgent);
        setMethod(defaultMethod);
        setPriority(defaultPriority);
    }

    public Request(String url) {
        this();
        this.setUrl(url);
    }

    public Request(String url, String method) {
        this(url);
        this.setMethod(method);
    }

    public Request(String url, String method, int priority) {
        this(url, method);
        this.setPriority(priority);
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public long getPriority() {
        return priority;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public HashSet<String> getAllTag() {
        return tags;
    }

    public Request setUrl(String url) {
        logger.debug("Set url: {}", url);
        this.url = url;
        return this;
    }

    public Request setMethod(String method) {
        logger.debug("Set method: {}", method);
        this.method = method;
        return this;
    }

    public Request setPriority(int priority) {
        logger.debug("Set priority: {}", priority);
        this.priority = priority;
        return this;
    }

    public Request setHeader(String key, String value) {
        logger.debug("Add header: ({}, {})", key, value);
        this.headers.put(key, value);
        return this;
    }

    public Request addTag(String tag) {
        logger.debug("Set tag: {}", tag);
        this.tags.add(tag);
        return this;
    }

    public Request addTags(ArrayList<String> tags) {
        for (String tag : tags) {
            addTag(tag);
        }
        return this;
    }

    public boolean containTag(String tag) {
        return tags.contains(tag);
    }

    public boolean tagsIsEmpty() {
        return tags.isEmpty();
    }
}
