package badcode;


import redis.clients.jedis.Jedis;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        Crawler crawler = new Crawler("http://info.ruc.edu.cn/");
        CrawlDB crawlDB = new CrawlDB();
        crawlDB.addDirtyURL("test");
        crawlDB.addCleanURL("clean");
    }
}
