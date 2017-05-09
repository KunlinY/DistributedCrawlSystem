package badcode;

import java.util.ArrayList;
import java.util.Set;

public class Crawler {
    private Generator generator = () -> CrawlDB.getURL();
    private String rootURL;
    private ArrayList<String> patterns = new ArrayList<>();
    private int threadsNum = 10;
    private ArrayList<Fetcher> fetchers = new ArrayList<>();

    public Crawler(boolean isMaster) {
        if (!isMaster) {
            // TODO master & slave
        }
    }

    public void setGenerator(Generator g) {
        generator = g;
    }

    public void setRootURL(String url) {
        rootURL = url;
        CrawlDB.addDirtyURL(rootURL);
    }

    public void addRegex(String pattern) {
        patterns.add(pattern);
    }

    public void setThreadsNum(int num) {
        threadsNum = num;
    }

    public int getThreadsNum() {
        return this.threadsNum;
    }

    // 过滤添加到待爬取队列的URL
    private boolean filter(String url) {
        if (patterns.isEmpty())
            return true;
        for (String pattern : patterns) {
            if (url.matches(pattern))
                return true;
        }
        return false;
    }

    public void start() {
        multiThread();
    }

    public void inject(Set<String> links) {
        for (String link : links) {
            if (filter(link))
                CrawlDB.addDirtyURL(link);
        }
    }

    private void multiThread() {
        for (int i = 0; i < threadsNum; i++) {
            Fetcher fetcher = new Fetcher(this, generator);
            fetchers.add(fetcher);
            fetcher.start();
        }
    }
}
