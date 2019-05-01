package xyz.zerovoid.simplecrawler.engine;

import xyz.zerovoid.simplecrawler.spider.Spider;
import xyz.zerovoid.simplecrawler.downloader.Downloader;
import xyz.zerovoid.simplecrawler.scheduler.*;
import xyz.zerovoid.simplecrawler.pipeline.*;

abstract class Engine {

    protected Spider spider;
    protected Downloader downloader;
    protected Scheduler scheduler;
    protected Pipeline pipeline;

}
