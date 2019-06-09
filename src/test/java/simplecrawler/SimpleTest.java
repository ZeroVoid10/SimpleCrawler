package simplecrawler;

import xyz.zerovoid.simplecrawler.spider.SimpleSpider;

public class SimpleTest {
    
    public static void main(String[] args) {
        SimpleSpider spider = new SimpleSpider(
                "https://zh.moegirl.org/CAROLE_%26_TUESDAY",
                "https://zh.moegirl.org");
        spider.run();
    }
}
