package xyz.zerovoid.simplecrawler.downloader;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * SimpleDownloader: a downloader example.
 * @author Zero Void <zerovoid10@163.com, zerolivenjoy@gmail.com>
 * TODO: Abstract important part to make Downloader interface.
 * TODO: Configuture class add.
 */
public class SimpleDownloader extends Downloader {
    
	final static Logger logger = LoggerFactory.getLogger(SimpleDownloader.class);

    /**
     * SimpleDownloader default constructor.
     * set default header
     */
    public SimpleDownloader() {
        super();
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) " 
                + "AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/73.0.3683.103 Safari/537.36");
    }

	protected void setURI(String url) {
		logger.info("Set url:{}", url);
		try {
			this.httpGet.setURI(new URI(url));
		} catch (URISyntaxException e) {
            logger.error("URI Syntax error.");
			e.printStackTrace();
		}
	}

    public String getURI() {
        return httpGet.getURI().toString();
    }

    /**
     * Download the page.
     * @throws IOException 
     * @throws UnsupportedOperationException 
     */
    public String download(String url) throws UnsupportedOperationException, IOException {
    	logger.info("Downloading...:{}", url);
    	setURI(url);
        String rawPage = "";
    	try {
			response = httpclient.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
            System.out.println("Error: Abort connection.");
        } finally {
            // Downloading will block if not to close response.
            rawPage = IOUtils.toString(response.getEntity().getContent(), 
                StandardCharsets.UTF_8); 
            response.close();
        }
    	logger.info("Donwloaded:{}", url);
        return rawPage;
    }

    public CloseableHttpResponse getResponse() {
        return response;
    }
    
    /**
     * Just for single class testing.
     */
    public static void main(String[] args) throws IOException {
        SimpleDownloader downloader = new SimpleDownloader();

        downloader.download("http://bilibili.com");
        CloseableHttpResponse response = downloader.getResponse();
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println(response.getStatusLine().toString());
                for (Header header : response.getAllHeaders()) {
                    System.out.println(header.toString());
                }
                System.out.println(downloader.getURI());
            }
        } finally {
            response.close();
        }
	}
}
