package badcode;

public class Crawler {
    private static CrawlDB crawlDB = new CrawlDB();
    private static String rootURL;
    private static boolean externLink = false;

    public Crawler(String url) {
        rootURL = url;
    }

    public Crawler(String url, boolean extern) {
        rootURL = url;
        externLink = extern;
    }
}
