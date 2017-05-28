package badcode;

import java.util.concurrent.atomic.AtomicInteger;

public class Master extends Thread {
    private static AtomicInteger startThread = new AtomicInteger(0);
    private boolean alive = true;
    private boolean isNews = true;
    private int threadID;

    Master(boolean isNews) {
        this.isNews = isNews;
    }

    public void run() {
        super.run();

        threadID = startThread.incrementAndGet();

        try {
            Thread.sleep(threadID * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (alive) {
            String url = "";
            try {
                String page = CrawlDB.getPage();
                Fetcher.writeHtml(page);

                if (isNews) {
                    url = page.substring(0, page.indexOf("\r\n"));
                    String html = page.substring(page.indexOf("\r\n"));
                    CrawlDB.addCleanURL(url, Fetcher.NLP(html, url));
                }
            } catch (Exception e) {
                try {
                    System.out.println(url);
                    Thread.sleep(1000);
                } catch (Exception ee) {

                }
            }
        }
    }

    public void kill() {
        alive = false;
        interrupt();
    }

}
