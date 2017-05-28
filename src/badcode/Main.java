package badcode;


public class Main {

    public static void main(String[] args)  throws Exception {
        Crawler crawler = new Crawler(true, true);
        crawler.setRootURL("http://www.ruc.edu.cn/");
        crawler.addRegex(".*ruc.edu.cn.*");
        crawler.setThreadsNum(10);
        crawler.start();
    }

}
