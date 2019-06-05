package xyz.zerovoid.simplecrawler.downloader;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * All downloader need to be expends from this.
 * @since 0.1.0
 */
abstract public class AbstractDownloader {

    /**
     * Http Client for downloader.
     */
    protected CloseableHttpClient httpClient;
    protected CloseableHttpResponse httpResponse;

    /**
     * Download page requested by the request.
     * TODO: May use Apache HttpResponse handler to do this done.
     * @param reqest: contains uri and some other requests.
     * @return a {@link Page} wrapped html content.
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    abstract public Page download(Request request) 
            throws URISyntaxException, ClientProtocolException, IOException;
}
