package badcode;

public class Main {
    public static Crawler crawler = new Crawler();

    public static void main(String[] args)  throws Exception {
        Crawler crawler = new Crawler(false, true);
        //crawler.flush();  // 清空Redis数据库重新爬取，建议不要取消注释
        crawler.setRootURL("http://www.ruc.edu.cn/");   // 设置根节点
        crawler.addRegex(".*ruc.edu.cn.*");     // 设置URL所需满足的正则表达式，可以取消
        crawler.setThreadsNum(5);                       // 设置爬虫线程数

        // 设置是否为主节点，如果为从节点则注释
        crawler.setMaster();
        // 设置是否进行信息抽取，如果只对主节点有效，从节点设置无效
        crawler.setNews();

        // 只对主节点有效
        // 可以不设置，使用默认设置
        // Fetcher.pagePath = "输入网页源代码保存路径"
        // Fetcher.infoPath = "输入网页正文提取后的文件保存路径"

        crawler.start();
    }

}
