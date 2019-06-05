package xyz.zerovoid.simplecrawler.parser;

import java.net.URI;
import java.net.URISyntaxException;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

public abstract class AbstractParser {
    public abstract Items parse(Page page);
    protected String getRender(Request request) {
        URI uri = null;
        try {
			uri = new URI(request.getUrl());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        return uri.getScheme() + "://" + uri.getAuthority();
    }
}
