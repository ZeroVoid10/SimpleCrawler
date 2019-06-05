package xyz.zerovoid.simplecrawler.downloader;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A simple implementation of downloader.
 * Get {@link util.Request}, and download {@link util.Page}.
 * Just can download some static web site.
 */
public class SimpleDownloader extends AbstractDownloader {
    
	final static Logger logger = 
        LoggerFactory.getLogger(SimpleDownloader.class);

    protected HttpGet httpGet = new HttpGet();

    //TODO: Need to let Request set the HttpRequestBase and header.
    public SimpleDownloader() {
        this.httpClient = HttpClients.createDefault();
    }

    public SimpleDownloader(CloseableHttpClient client) {
        this.httpClient = client;
    }

    public CloseableHttpResponse getResponse() {
        if (httpResponse == null) {
            logger.warn("Response is null.");
        }
        return httpResponse;
    }

    /**
     * Download the page.
     * @throws IOException 
     * @throws UnsupportedOperationException 
     */
	@Override
	public Page download(Request request) 
        throws URISyntaxException, ClientProtocolException, IOException {
        System.out.println("Downloading page: " + request.getUrl());
		setHttpGet(request);
        httpResponse = httpClient.execute(httpGet);
		return createPage(request);
	}

    protected void setHttpGet(Request request) throws URISyntaxException {
        //httpGet.reset();
        httpGet.setURI(new URI(request.getUrl()));
        //FIXME: the headers in request may be null.
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
    }

    //FIXME: Let resonse charset for pageText.
    protected Page createPage(Request request) 
            throws UnsupportedOperationException, IOException {
        HttpEntity entity = httpResponse.getEntity();
        String pageText = IOUtils.toString(
                entity.getContent(),
                StandardCharsets.UTF_8
                ); 
        Page page = Page.Builder.getBuilder()
            .setRequest(request)
            .setText(pageText)
            .setStatuCode(httpResponse.getStatusLine().getStatusCode())
            .setContentLength(entity.getContentLength())
            .build();
        httpResponse.close();

        return page;
    }
    
    /**
     * Just for single class testing.
     */
    public static void main(String[] args) throws IOException {
        SimpleDownloader downloader = 
            SimpleDownloaderBuilder.getBuilder().build();
        Page page = null;
        Request request = new Request("http://www.bilibili.com");

        try {
			page = downloader.download(request);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(page);
	}
}
