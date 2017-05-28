package badcode;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static badcode.util.getTime;

public class Fetcher extends Thread {
    private static AtomicInteger startThread = new AtomicInteger(0);
    private static AtomicInteger htmlCount = new AtomicInteger(0);
    private static String pagePath = ".\\pages\\";
    private static String infoPath = ".\\info\\";
    private int threadID;

    private WebClient webClient=new WebClient();
    private static Set<Cookie> cookies = new HashSet<Cookie>();

    private Parser parser = new Parser();
    private Generator generator;
    private Crawler crawler;
    private boolean alive = true;
    private boolean isNews = true;

    String url = "";

    Fetcher(Crawler crawler, Generator generator, boolean isNews){
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);

        this.isNews = isNews;
        this.generator = generator;
        this.crawler = crawler;
    }

    // 启动入口
    @Override
    public void run(){
        super.run();

        threadID = startThread.incrementAndGet();

        try {
            Thread.sleep(threadID * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (alive) {
            try {
                url = generator.generate();

                if (url == null || url.equals("")) {
                    Thread.sleep(500);
                    continue;
                }

                if (CrawlDB.addCleanURL(url) < 0)
                    continue;

                String html = getXmlResponse(url);
                crawler.inject(parser.extractLink(html, new URL(url)));

                if (Crawler.isMaster) {
                    writeHtml(url + "\r\n" + html);
                    CrawlDB.addCleanURL(url, NLP(html, url));
                }
                else {
                    CrawlDB.addPages(url, html);
                    CrawlDB.addCleanURL(url, false);
                }
            }
            catch (Exception e) {
                System.out.println("Error fetching url " + url);
                try {
                    Thread.sleep(100);
                } catch (Exception ee) {

                }
            }
        }
    }

    public void kill() {
        alive = false;
        interrupt();
    }

    private String getXmlResponse(String str) throws Exception {
        URL url = new URL(str);

        Page page = webClient.getPage(url);
        String re = page.getWebResponse().getContentAsString();

        cookies.addAll(webClient.getCookies(url));

        return re;
    }

    private String getRawResponse(String url) throws Exception {
        URL u = new URL(url);
        InputStream in =u.openStream();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(isr);

        String str = "", temp;
        while ((temp = bufferedReader.readLine()) != null) {
            str += temp;
        }

        bufferedReader.close();
        isr.close();
        in.close();

        return str;
    }

    synchronized public static void writeHtml(String html) throws IOException {
        htmlCount.incrementAndGet();
        File file = new File(pagePath + htmlCount + "_" + getTime()  + ".html");
        if (!file.exists())
            file.createNewFile();
        (new FileOutputStream(file)).write((html).getBytes());
    }

    synchronized private static void writeNews(NLP.News news) throws IOException {
        File file = new File(infoPath + news.getTime() + "_" + news.getTitle().replaceAll("[\\/:*?\"<>|]", "")  + ".txt");

        if (!file.exists())
            file.createNewFile();

        (new FileOutputStream(file)).write((
                news.getTitle() + "\r\n" +
                news.getTime() + "\r\n" +
                news.getUrl() + "\r\n" +
                news.getContent()
        ).getBytes());
    }

    public static boolean NLP(String html, String url) throws Exception{
        NLP.News news = ContentExtractor.getNewsByHtml(html, url);
        if (news.getContent() != null && news.getContent().trim().length() > 20) {
            writeNews(news);
            return true;
        }
        return false;
    }
}