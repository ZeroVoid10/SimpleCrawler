package xyz.zerovoid.simplecrawler.pipeline;

import java.io.Console;
import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;

/**
 * @since 0.2.0
 */
public class ConsolePipeline implements Pipeline {

    private static Logger logger = 
        LoggerFactory.getLogger(ConsolePipeline.class);

	@Override
	public void processItem(Items items) {
		Console console = System.console();
        if (console == null) {
            logger.warn("Can't not open the console.");
            return ;
        }
        PrintWriter writer = console.writer();
        writer.println("Page :" + items.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : items.getAll().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
	}
}
