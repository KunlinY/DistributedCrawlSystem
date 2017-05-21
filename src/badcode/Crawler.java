package badcode;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class Crawler {
    private static Generator generator = () -> CrawlDB.getURL();
    private static String rootURL;
    private ArrayList<String> patterns = new ArrayList<>();
    private int threadsNum = 100;
    private ArrayList<Fetcher> fetchers = new ArrayList<>();
    private boolean doNLP = false;

    public Crawler() {

    }

    public Crawler(boolean isMaster, boolean NLP) {
        doNLP = NLP;
        if (!isMaster) {
            // TODO master & slave
        }
    }

    public void setGenerator(Generator g) {
        generator = g;
    }

    public void setRootURL(String url) {
        rootURL = url;
        CrawlDB crawlDB = new CrawlDB();
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
        if (rootURL == null) {
            System.out.println("Please input the root url");
            return;
        }
        multiThread();
    }

    public void inject(HashSet<URL> links) {
        for (URL link : links) {
            String url = link.toString();
            if (filter(url))
                CrawlDB.addDirtyURL(url);
        }
    }

    private void multiThread() {
        for (int i = 0; i < threadsNum; i++) {
            Fetcher fetcher = new Fetcher(this, generator, doNLP);
            fetchers.add(fetcher);
            fetcher.start();
        }
    }
}
