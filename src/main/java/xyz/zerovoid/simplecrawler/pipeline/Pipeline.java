package xyz.zerovoid.simplecrawler.pipeline;

import xyz.zerovoid.simplecrawler.item.Items;

/**
 * @since 0.2.0
 */
public interface Pipeline {
    
    public void processItem(Items items);
}
