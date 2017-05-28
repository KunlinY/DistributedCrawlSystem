package badcode;

import redis.clients.jedis.Jedis;
import static badcode.util.getTime;

public class CrawlDB {
    // Redis DB 连接设置
    private static final String host = "123.206.72.211";
    private static final String localhost = "127.0.0.1";
    private static final int port = 6379;
    private static Jedis jedis;

    // Redis 相关常量设置
    private static final String waitingSet = "waitingSet";
    private static final String finishHash = "finishHash";
    private static final String pagesList = "pagesList";
    private static final int crawlSec = 30;     // 单个页面爬取等待时间30s
    private static int recrawlSec = 3600 * 24;  // 热页重复爬取时间为一天
    private static long maxSec = recrawlSec * 365;     // 时间正无穷

    // 初始化Redis 优先采用服务器
    // 服务器不行则使用备选方案本地Redis
    // TODO 异常未捕获 无法采用备选方案
    public CrawlDB() {
        if (jedis == null) {
            try  {
                jedis = new Jedis(host, port);
            }
            catch (Exception e) {
                try {
                    jedis = new Jedis(localhost);
                }
                catch (Exception ee) {
                    e.printStackTrace();
                    System.out.println("Fatal error: cannot connect to Redis!");
                    System.exit(0);
                }
            }
        }
    }

    // 析构函数 关闭数据库链接
    protected void finalize() throws Throwable{
        jedis.close();
        super.finalize();
    }

    // 将解析出的URL加入waitingList待爬取队列
    synchronized public static long addDirtyURL(String url) {
        try {
            if (jedis.hexists(finishHash, url)) {
                long time = Integer.parseInt(jedis.hget(finishHash, url));
                if (time < getTime()) {
                    jedis.hdel(finishHash, url);
                    return jedis.sadd(waitingSet, url);
                }
                return 0;
            }
            else
                return jedis.sadd(waitingSet, url);
        } catch (Exception e) {
            System.out.println(url + " add failed...");
            e.printStackTrace();
            return -1;
        }
    }

    // 对于从waitingList中提取出的URL，将其放入finishHSet已爬取队列
    // 并设置等待时间crawlSec，如果指定时间内没有更新，下次访问时将重新爬取
    // 对于已爬完的URL，先加入结束队列
    // 根据其是否含有正文如果有正文则设置更新时间为一天，一天之后再次访问将重新爬取
    // 如果没有正文 则设置更新时间为maxSec，将不会再爬取或者一定时间后再爬
    synchronized public static long addCleanURL(String url) {
        try {
            return jedis.hset(finishHash, url, String.valueOf(getTime() + crawlSec));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(url + " add failed...");
            return -1;
        }
    }

    synchronized public static boolean addCleanURL(String url, boolean hasContent) {
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

    synchronized public static boolean addPages(String url, String html) {
        try {
            jedis.lpush(pagesList, url + "\r\n" + html);
        } catch (Exception e) {
            System.out.println(url + " add failed...");
            return false;
        }
        return true;
    }

    synchronized public static String getPage() throws Exception {
        try {
            return jedis.lpop(pagesList);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // 从waitList待爬取队列中返回URL
    // 无URL则返回空字符串
    synchronized public static String getURL() {
        String url = jedis.spop(waitingSet);
        if (url == null)
            return "";
        return url.equals("nil") ? "" : url;
    }

    public static void flushDB() {
        jedis.flushAll();
    }

    // 返回URL的剩余时间
    public static long getTTL(String url) {
        String time = jedis.hget(finishHash, url);
        if (time == null)
            return 0;
        return time.equals("nil") ? 0 : Integer.parseInt(time);
    }
}
