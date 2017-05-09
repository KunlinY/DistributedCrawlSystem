package badcode;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.*;
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
    private static Set<Cookie> setc = new HashSet<Cookie>();
    private static int txtnum = 1;

    private Parser parser = new Parser();
    private Generator generator;
    private Crawler crawler;

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

        while (true) {
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
                //
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

    String getXmlResponse(URL url, WebClient client)throws IOException {
        String str=null;      //��ȡxml
        StringBuffer temp = new StringBuffer();
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(10000);
        uc.setDoOutput(true);
        InputStream in = new BufferedInputStream(uc.getInputStream());
        Reader rd = new InputStreamReader(in,"UTF-8");
        int c = 0;

        while ((c = rd.read()) != -1) {
            temp.append((char) c);
        }

        in.close();
        str = temp.toString();

        setc.addAll(client.getCookies(url));//����cookie

//        Iterator<Cookie> i = setc.iterator();
//        while (i.hasNext())
//        {
//            client.getCookieManager().addCookie(i.next());
//        }
//        CookieManager CM = client.getCookieManager();
//        setc = CM.getCookies();
        return str;
    }

    void writeFile(String string) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String txtname=txtnum+".txt";
        txtnum++;
        PrintWriter out = new PrintWriter(new FileWriter(txtname));
        out.println(df.format(new Date())+'\n'+string);
        out.close();
    }

}
