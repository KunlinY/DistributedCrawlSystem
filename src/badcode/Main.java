package badcode;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

public class Main {

    public static void main(String[] args)  throws Exception {
        Crawler crawler = new Crawler(true, true);
        crawler.setRootURL("http://www.ruc.edu.cn/");
        crawler.addRegex(".*ruc.edu.cn.*");
        crawler.setThreadsNum(1);
        crawler.flush();
        crawler.start();
    }

}
