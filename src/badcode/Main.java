package badcode;


import cn.edu.hfut.dmic.htmlbot.contentextractor.ContentExtractor;

import java.net.URL;

public class Main {

    public static void main(String[] args) {
        try {
            URL url = new URL(new URL("https://www.taobao.com/"), "#");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Crawler crawler = new Crawler(true, true);
        //crawler.start();
    }
}
