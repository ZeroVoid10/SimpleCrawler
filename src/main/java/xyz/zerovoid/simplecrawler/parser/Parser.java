package xyz.zerovoid.simplecrawler.parser;

import xyz.zerovoid.simplecrawler.item.Item;

/**
 * Parser interface.
 * For parse Html page.
 * TODO: add Url Render (class or method).
 *
 */
public interface Parser {
    
    public Item parse(String rawPage);
}
