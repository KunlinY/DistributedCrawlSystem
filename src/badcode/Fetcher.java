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

    public String getMainContent(String temp){
        if (!temp.toLowerCase().startsWith("http://")) {
            temp = "http://" + temp;
        }
        String head = "http://183.174.228.9:8282/du/jsonp/ExtractMainContent?";
        head.concat(temp);
        String urlsource="";

        try{
            URL url = new URL(head);
            urlsource = getURLSource(url);
            System.out.println(urlsource);
        }
        catch(Exception ee){
            System.out.println("get ExtractMainContent Error!");
        }

        return urlsource;
    }

    public static String getURLSource(URL url) throws Exception    {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
        byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
        String htmlSource = new String(data);
        return htmlSource;
    }

    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
        }
        instream.close();
        return outStream.toByteArray();
    }


    public String getpdoc(String temp) throws Exception {
        URL u = new URL("http://websensor.playbigdata.com/du/Service.svc/pdoc");
        WebRequest webrequest = new WebRequest(u,"POST");
        webrequest.setRequestBody(temp);

        webClient.addRequestHeader("Host","http://websensor.playbigdata.com");
        webClient.addRequestHeader("Connection","keep-alive");
        webClient.addRequestHeader("Content-Length","32673");
        for(Cookie c:setc){
            webClient.addRequestHeader("Cookies",c.toString());
        }

        HtmlPage htmlPage = webClient.getPage(webrequest);
        HtmlForm form = htmlPage.getFormByName("f");
        HtmlTextInput text = form.getInputByName("inputText");
        text.setText(temp);
        HtmlSubmitInput button = form.getInputByName("btnG");
        HtmlPage listPage = button.click();

        System.out.println(listPage.asXml());
        //webClient.closeAllWindows();
        return listPage.asXml();
    }

    public String getXmlResponse(URL url, WebClient client)throws IOException {
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
        String txtname = txtnum+".txt";
        txtnum++;
        PrintWriter out = new PrintWriter(new FileWriter(txtname));
        out.println(df.format(new Date())+'\n'+string);
        out.close();
    }

}