package badcode;

import java.util.ArrayList;

public class Crawler {
    private Generator generator = () -> CrawlDB.getURL();
    private String rootURL;
    private ArrayList<String> patterns = new ArrayList<>();
    private int threadsNum = 10;

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
    public boolean filter(String url) {
        if (patterns.isEmpty())
            return true;
        for (String pattern : patterns) {
            if (url.matches(pattern))
                return true;
        }
        return false;
    }

}
