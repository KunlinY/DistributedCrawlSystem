package badcode;


import java.util.Date;

public class Main {

    public static void main(String[] args)  throws Exception {
        Date d = new Date();
        System.out.println(d);

        Crawler crawler = new Crawler();
        crawler.setRootURL("http://info.ruc.edu.cn/");
        crawler.addRegex(".*ruc.edu.cn.*");
        crawler.start();
    }
}
