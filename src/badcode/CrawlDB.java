package badcode;

import redis.clients.jedis.Jedis;
import static badcode.util.getTime;

/**
 * Created by 97520 on 05/06/2017.
 */
public class CrawlDB {
    // Redis DB 连接设置
    private static final String host = "123.206.72.211";
    private static final int port = 6379;
    private static Jedis jedis;

    // Redis 相关常量设置
    private static final String waitingList = "waitingList";
    private static final String finishHash = "finishHash";
    private static final int crawlSec = 30;     // 单个页面爬取等待时间30s
    private static int recrawlSec = 3600 * 24;  // 热页重复爬取时间为一天
    private static long maxSec = getTime();     // 时间正无穷

    public CrawlDB() {
        if (jedis == null) {
            jedis = new Jedis(host, port);
        }
        System.out.println("Server is running: "+jedis.ping());
    }

    // 将解析出的URL加入waitingList待爬取队列
    public boolean addDirtyURL(String url) {
        try {
            if (jedis.hexists(finishHash, url)) {
                long time = Integer.parseInt(jedis.hget(finishHash, url));
                if (time > getTime()) {
                    jedis.hdel(finishHash, url);
                    jedis.lpush(waitingList, url);
                }
            }
            else
                jedis.lpush(waitingList, url);
        } catch (Exception e) {
            System.out.println(url + " add failed...");
            return false;
        }
        return true;
    }

    // 对于从waitingList中提取出的URL，将其放入finishHSet已爬取队列
    // 并设置等待时间crawlSec，如果指定时间内没有更新，下次访问时将重新爬取
    // 对于已爬完的URL，先加入结束队列
    // 根据其是否含有正文如果有正文则设置更新时间为一天，一天之后再次访问将重新爬取
    // 如果没有正文 则设置更新时间为maxSec，将不会再爬取或者一定时间后再爬
    public boolean addCleanURL(String url) {
        try {
            jedis.hset(finishHash, url, String.valueOf(getTime() + crawlSec));
        } catch (Exception e) {
            System.out.println(url + " add failed...");
            return false;
        }
        return true;
    }

    public boolean addCleanURL(String url, boolean hasContent) {
        try {
            if (hasContent)
                jedis.hset(finishHash, url, String.valueOf(getTime() + maxSec));
            else
                jedis.hset(finishHash, url, String.valueOf(getTime() + recrawlSec));
        } catch (Exception e) {
            System.out.println(url + " add failed...");
            return false;
        }
        return true;
    }

    // 从waitList待爬取队列中返回URL
    // 无URL则返回空字符串
    public String getURL() {
        String url = jedis.lpop(waitingList);
        return url.equals("nil") ? "" : url;
    }
}
