package xyz.zerovoid.simplecrawler.downloader;

import org.apache.http.impl.client.HttpClientBuilder;

import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * All downloader need to be expends from this.
 * @since 0.1.0
 */
abstract public class AbstractDownloader {

    protected Request request;
    protected HttpClientBuilder httpClientBuilder;

    /**
     * Download page requested by the request.
     * TODO: May use Apache HttpResponse handler to do this done.
     * @param reqest: contains uri and some other requests.
     * @return a {@link Page} wrapped html content.
     */
    abstract public Page download(Request request);
}
