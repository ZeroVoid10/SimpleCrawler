package xyz.zerovoid.simplecrawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Html content and some other things the parser may needed.
 * @since 0.2.0
 */
public class Page {

    private static final Logger logger = 
        LoggerFactory.getLogger(Page.class);
    
    protected Request request;
    protected String rawText;
    protected int statusCode;
    protected boolean successDownload = true;
    protected String contentEncoding;
    protected String contentType;
    protected long contentLength;

    public static class Builder {
        public static Builder builder = new Builder();
        private Request request;
        private String rawText;
        private int statusCode;
        private boolean successDownload;
        private String contentEncoding;
        private String contentType;
        private long contentLength;

        public static Builder getBuilder() {
            return builder;
        }

        public Page build() {
            logger.info("Create Page.");
            Page page = new Page();
            page.request = this.request;
            page.rawText = this.rawText;
            page.statusCode = this.statusCode;
            page.successDownload = this.successDownload;
            page.contentEncoding = this.contentEncoding;
            page.contentType = this.contentType;
            page.contentLength = this.contentLength;

            return page;
        }

        private Builder() {
            this.request = null;
            this.rawText = "";
            this.statusCode = 0;
            this.successDownload = false;
            this.contentEncoding = "gzip";
            this.contentType = "text/html; utf-8";
            this.contentLength = 0L;
        }

        public Builder setRequest(Request request) {
            this.request = request;
            return this;
        }

        public Builder setText(String text) {
            this.rawText = text;
            return this;
        }

        public Builder setStatuCode(int code) {
            this.statusCode = code;
            return this;
        }

        public Builder setSuccessDonwload(boolean flag) {
            this.successDownload = flag;
            return this;
        }

        public Builder setContentEncoding(String encode) {
            this.contentEncoding = encode;
            return this;
        }

        public Builder setContentType(String type) {
            this.contentType = type;
            return this;
        }

        public Builder setContentLength(long length) {
            this.contentLength = length;
            return this;
        }
    }

    protected Page() {
        super();
    }

    public Request getRequest() {
        return request;
    }

    public String getRawText() {
        return rawText;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }


    @Override
    public String toString() {
        String str = "";
        if (rawText != null) 
            str += rawText + "\n";
        if (this.request != null) 
            str += "URL: " + request.getUrl() + "\n";
        str += "StatusCode: " + statusCode + "\n";
        str += "ContentLength: " + contentLength + "\n";
        if (this.contentEncoding != null)
            str += "ContentEncoding: " + contentEncoding + "\n";
        if (this.contentType != null)
            str += "ContentType: " + contentType + "\n";
        return str;
    }
}
