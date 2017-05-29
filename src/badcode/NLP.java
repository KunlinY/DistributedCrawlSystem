package badcode;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jsoup.nodes.Element;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class NLP {
    private static String user = "postgres";
    private static String password = "java";
    private static String url = "jdbc:postgresql://123.206.72.211:5432/postgres";
    private static Connection connection = null;

    NLP() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver").newInstance();
                connection = DriverManager.getConnection(url, user, password);
                DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES_9_3);
                Table<Record> table = DSL.table("test");

                Result<Record> result = dslContext.select().from(table).fetch();
                System.out.println(result.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Cannot connect to NLP SQL!");
            }
        }
    }

    public class News {
        private String url = null;
        private String title = null;
        private String content = null;
        private String time = null;

        private Element contentElement = null;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url.equals("") ? null : url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title.trim().equals("") ? "" : title;
        }

        public String getContent() {
            if (content == null) {
                if (contentElement != null) content = contentElement.text();
                else content = "";
            }
            return content;
        }

        public void setContent(String content) {
            this.content = content.trim().equals("") ? null : content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time.trim().equals("") ? (new Date()).toString() : time;
        }

        @Override
        public String toString() {
            return "URL:\n" + url + "\nTITLE:\n" + title + "\nTIME:\n" + time + "\nCONTENT:\n" + getContent() + "\nCONTENT(SOURCE):\n" + contentElement;
        }

        public Element getContentElement() {
            return contentElement;
        }

        public void setContentElement(Element contentElement) {
            this.contentElement = contentElement;
        }
    }

    public class Words {
        HashMap<String, Integer> organization, idiom, person, location, properNoun,
                topic, positive, negative;

        Words(String html) {
            recognition(html);
        }

        public void recognition(String html) {
            Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true);
            List<Term> termList = segment.seg(html);
        }

        public void dump() {

        }
    }
}
