package xyz.zerovoid.simplecrawler.pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;

/**
 * @author 孙艺佳
 * @since 0.3.0
 */
public class TextPipeline implements Pipeline {

    private static final Logger logger = 
        LoggerFactory.getLogger(TextPipeline.class);

    private File fileDir;
    private String fileType = "txt";

    public TextPipeline(String dir) throws IOException {
        this.fileDir = new File(dir);
        if (!fileDir.exists()) {
            logger.warn("Given directory does not exists.");
            logger.info("Try to make directory : {}", fileDir);
            if (!fileDir.mkdir()) {
                logger.error("Can not make directory : {}", fileDir);
                throw new IOException();
            }
        }
    }

    public TextPipeline(String dir, String type) throws IOException {
        this(dir);
        setFileType(type);
    }
    
    @Override
	public void processItem(Items items) {
    	if (items.getRequest().containTag("text")) {
        	logger.debug("Have tag text");
            processText(items);
        }
	}

	public void processText(Items items) {
        
        for (Entry<String, Object> entry : 
                items.getAll().entrySet()) {
            File file = new File(fileDir, 
                    entry.getKey() + "." + fileType);
                try {
                	logger.debug("Writting...");
					FileOutputStream fos = new FileOutputStream(file);
                    fos.write(((String)entry.getValue()).getBytes());
                    fos.close();
				} catch(FileNotFoundException e) {
					logger.error("Can not open file {} to write.", file);
					e.printStackTrace();
                } catch(IOException e) {
                    logger.error("Error in writting.");
                    e.printStackTrace();
                } 
        }
	}

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getFileType() {
        return fileType;
    }
}
