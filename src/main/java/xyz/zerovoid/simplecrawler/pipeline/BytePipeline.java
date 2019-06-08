package xyz.zerovoid.simplecrawler.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;

public class BytePipeline implements Pipeline {

    private static final Logger logger = 
        LoggerFactory.getLogger(BytePipeline.class);

    private File fileDir;
    private String fileType = "jpg";

    public BytePipeline(String dir) throws IOException {
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

    public BytePipeline(String dir, String type) throws IOException {
        this(dir);
        this.fileType = type;
    }

	@Override
	public void processItem(Items items) {
        if (items == null| items.getAll().size() == 0) {
            return;
        }
        Map<String, Object> map = items.getAll();
        getPic((String)map.get("download"), (String)map.get("title"));
    }

    protected void getPic(String picUrl, String fileName) {
        logger.info("Get picture: {}", fileName);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(picUrl);
        InputStream instream = null;
        FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = 
                new FileOutputStream(new File(fileDir, fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = instream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
                instream.close();
                fileOutputStream.close();
                instream.close();
                response.close();
            }
        } catch(IOException e) {
                e.printStackTrace();
        } 
    }
}
