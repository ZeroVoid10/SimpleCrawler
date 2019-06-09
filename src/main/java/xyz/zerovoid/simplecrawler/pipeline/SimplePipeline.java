package xyz.zerovoid.simplecrawler.pipeline;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.item.SimpleItem;

/**
 * @since 0.1.0
 */
public class SimplePipeline implements Pipeline {

    /**
     * Emmmm Just print on screen.
     */
    public boolean dump(SimpleItem item) {
        System.out.println(item.getPageName() + ": " +
                item.getPageUrl());

        return true;
    }

	@Override
	public void processItem(Items items) {
		
	}
}
