package badcode;

public class Main {
    public static Crawler crawler = new Crawler();

    public static void main(String[] args)  throws Exception {
        Crawler crawler = new Crawler(false, true);
        //crawler.flush();
        crawler.setRootURL("http://www.ruc.edu.cn/");
        crawler.addRegex(".*ruc.edu.cn.*");
        crawler.setThreadsNum(5);
        crawler.start();
        crawler = null;
    }

}
