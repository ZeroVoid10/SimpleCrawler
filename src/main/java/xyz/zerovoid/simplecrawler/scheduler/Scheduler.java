package xyz.zerovoid.simplecrawler.scheduler;

public interface Scheduler {

    public String pop();

    public boolean push(String url);

    public void push(String[] urls);
}
