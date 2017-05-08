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

public class Fetcher {
    WebClient webClient=new WebClient();
    Set<Cookie> setc=new HashSet<Cookie>();
    int txtnum=1;
    Fetcher(){
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);
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
