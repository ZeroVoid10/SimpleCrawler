package xyz.zerovoid.simplecrawler.pipeline;

import xyz.zerovoid.simplecrawler.item.SimpleItem;

public class SimplePipeline {

    public SimplePipeline() {
        
    }

    public boolean dump(SimpleItem item) {
        System.out.println(item.getPageName() + ": " +
                item.getPageUrl());

        return true;
    }
}
