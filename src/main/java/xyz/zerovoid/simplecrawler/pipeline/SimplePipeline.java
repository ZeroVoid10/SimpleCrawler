package xyz.zerovoid.simplecrawler.pipeline;

import xyz.zerovoid.simplecrawler.item.SimpleItem;

/**
 * @author Zero Void <zerovoid10@163.com, zerolivenjoy@gmail.com>
 */
public class SimplePipeline {

    /**
     * Emmmm Just print on screen.
     */
    public boolean dump(SimpleItem item) {
        System.out.println(item.getPageName() + ": " +
                item.getPageUrl());

        return true;
    }
}
