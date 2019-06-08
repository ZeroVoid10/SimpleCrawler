package xyz.zerovoid.simplecrawler.example;

import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.parser.AbstractParser;
import xyz.zerovoid.simplecrawler.pipeline.DatabasePipeline;
import xyz.zerovoid.simplecrawler.spider.SimpleSpider;
import xyz.zerovoid.simplecrawler.spider.SimpleSpiderBuilder;
import xyz.zerovoid.simplecrawler.util.Page;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @Author 李曼婷
 * TODO: Make information more useful.
 * TODO: Properly design database table.
 */
public class JobParser extends AbstractParser {

    private static final Logger logger = 
        LoggerFactory.getLogger(JobParser.class);

    Integer id = 0;

	@Override
	public Items parse(Page page) {
        Request request = page.getRequest();
        if (request.containTag("detail")) {
            return parseDetail(page);
        } else {
            return parseList(page);
        }
	}

	protected Items parseList(Page page) {
        logger.debug("Parser List.");
        Items items = new Items(page.getRequest());
        Document doc = Jsoup.parse(page.getRawText());

        Element positionListBox = 
            doc.selectFirst("[class=position-list-box]");
        Element positionList = 
            positionListBox.getElementsByClass("position-list").first();
		Element pageBar = positionListBox.getElementById("pagebar");
		Elements info1 = positionList.select("[class=info1]");
		Elements info2 = positionList.select("[class=info2]");
		Elements elementPosition = info1.select("[class=name]");
		Elements elementimformation = info1.select("span");
		Elements elementCompany = info2.select("[class=company]");
		Elements elementType = info2.select("[class=type]");
		Elements pages = pageBar.select("a");

        for (int i = 0; i < elementPosition.size(); i++) {
            ArrayList<String> positonInfo = new ArrayList<String>();

            String position = elementPosition.get(i).text();
            positonInfo.add(position);
            String uri = elementPosition.get(i).attr("href");
            String url = getRender(page.getRequest()) + uri;
            positonInfo.add(url);
            String company = elementCompany.get(i).text();
            positonInfo.add(company);
            String type = elementType.get(i).text();
            positonInfo.add(type);

            int m = i / 5;
            String postTime = elementimformation.get(5 * m + 0).text();
            positonInfo.add(postTime);
            String salay = elementimformation.get(5 * m + 1).text();
            positonInfo.add(salay);
            String workingTime = elementimformation.get(5 * m + 2).text();
            positonInfo.add(workingTime);
            String duration = elementimformation.get(5 * m + 3).text();
            positonInfo.add(duration);
            positonInfo.add((new Date()).toString());
            id++;
            items.put(id.toString(), positonInfo);
            Request positionRequest = new Request(url);
            positionRequest.addTag("detail");
            items.addRequest(positionRequest);
		}
        int next = pages.size() - 2;
        String nextPageUri = pages.get(next).attr("href");
        String nextPageUrl = getRender(page.getRequest()) + nextPageUri;
        Request newRequest = new Request(nextPageUrl);
        newRequest.addTag("list");
        items.addRequest(newRequest);

        return items;
	}

    protected Items parseDetail(Page page) {
        logger.debug("Parser detail.");
        Items items = new Items(page.getRequest());
        Document doc = Jsoup.parse(page.getRawText());

        Element jobIntroduce = 
            doc.selectFirst("[class=con-job job_introduce]");
		Element deadLine = 
            doc.selectFirst("[class=con-job deadline]");
		Element jobGood = jobIntroduce.selectFirst("[class=job_good]");
		Element jobPart = jobIntroduce.selectFirst("[class=job_part]");

		String job_good = jobGood.text();
		String job_part = jobPart.text();
		String dead_line = deadLine.text();

        ArrayList<String> detialList = new ArrayList<String>();
        detialList.add(job_good);
        detialList.add(job_part);
        detialList.add(dead_line);
        items.put(page.getRequest().getUrl(), detialList);

        return items;
    }

    public static void main(String[] args) {
        DatabasePipeline pipeline = 
            new DatabasePipeline("localhost", 
                                "simplecrawler", 
                                "3306", 
                                "mariadb",
                                "zerovoid",
                                "zerovoid" 
                );
        CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
                    .build(); // TO fix apache invalid cookie warn.
        SimpleSpider spider = SimpleSpiderBuilder.getNewBuilder()
            .addRequest("https://www.xiaobaishixi.com/jobs/c-100000c-110000_?k=%E8%BF%90%E8%90%A5&p=1")
            .setParser(new JobParser())
            .setClient(httpClient)
            .addPipelens(pipeline)
            .build();
        spider.crawl();
    }
}
