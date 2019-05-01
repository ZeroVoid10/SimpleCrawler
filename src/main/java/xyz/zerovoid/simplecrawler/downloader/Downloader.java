package xyz.zerovoid.simplecrawler.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Abstract Downloader.
 * All Downloaders need to extend from it.
 * Being adapted to {@link #SimpleDownloader}. 
 * @version 0.1.0
 */
abstract public class Downloader {

    protected  CloseableHttpClient httpclient;
    protected HttpGet httpGet;
    protected CloseableHttpResponse response;

    public Downloader() {
        httpclient = HttpClients.createDefault();
        httpGet = new HttpGet();
    }

    abstract protected void setURI(String url);
    abstract public String getURI();
}
