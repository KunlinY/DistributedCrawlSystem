package badcode;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Fetcher extends Thread {
    private static AtomicInteger startThread = new AtomicInteger(0);
    private int threadID;

    private WebClient webClient=new WebClient();
    private static Set<Cookie> cookies = new HashSet<Cookie>();
    private static int txtnum = 1;

    private Parser parser = new Parser();
    private Generator generator;
    private Crawler crawler;
    private boolean alive = true;

    Fetcher(Crawler crawler, Generator generator){
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);

        this.generator = generator;
        this.crawler = crawler;
    }

    // 启动入口
    @Override
    public void run(){
        super.run();

        threadID = startThread.incrementAndGet();
        String url;

        while (alive) {
            try {
                url = generator.generate();

                if (url == null || url.equals("")) {
                    Thread.sleep(500);
                    continue;
                }

                if (CrawlDB.addDirtyURL(url) <= 0)
                    continue;

                // TODO 获取HTML
                // 函数入口

                // 返回的HTML String
                // 调用
                // crawler.inject(parser.filterURL(HTMLString));
            }
            catch (Exception e) {
                System.out.println("Error fetching url!");
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        alive = false;
        interrupt();
    }

    public String getMainContent(String temp){
        if (!(temp.toLowerCase().startsWith("http://")
                || temp.toLowerCase().startsWith("https://"))) {
            temp += "http://";
        }

        String head = "http://183.174.228.9:8282/du/jsonp/ExtractMainContent?";
        head += temp;
        String mainContent="";

        try{
            URL url = new URL(head);
            mainContent = getXmlResponse(url);
            System.out.println(mainContent);
        }
        catch(Exception ee){
            System.out.println("Get ExtractMainContent @" + temp + " Error!");
            ee.printStackTrace();
        }

        return mainContent;
    }

    public String getPdoc(String temp) throws Exception {
        URL u = new URL("http://websensor.playbigdata.com/du/Service.svc/pdoc");
        WebRequest webrequest = new WebRequest(u,"POST");
        webrequest.setRequestBody(temp);

        webClient.addRequestHeader("Host","http://websensor.playbigdata.com");
        webClient.addRequestHeader("Connection","keep-alive");
        webClient.addRequestHeader("Content-Length","32673");
        for(Cookie c: cookies){
            webClient.addRequestHeader("Cookies",c.toString());
        }

        return ((HtmlPage)webClient.getPage(webrequest)).asXml();
    }

    public String getXmlResponse(URL url)throws IOException {
        HtmlPage page = webClient.getPage(url);
        String re = page.asText();

        cookies.addAll(webClient.getCookies(url));

        return re;
    }

    public void writeFile(String string) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String txtname = txtnum++ +".txt";
        PrintWriter out = new PrintWriter(new FileWriter(txtname));
        out.println(df.format(new Date())+'\n'+string);
        out.close();
    }

    public static void main(String argv[]) throws Exception {
        WebClient webClient = new WebClient();
        URL url = new URL("https://www.baidu.com/");
        HtmlPage page = webClient.getPage(url);
        String re = page.asText();

        cookies.addAll(webClient.getCookies(url));


        System.out.println(re);

    }

}