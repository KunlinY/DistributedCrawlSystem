package badcode;

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        public String title;
        public String content;

        News(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    public class Words {
        HashMap<String, Integer> organization, idiom, person, location, properNoun,
                topic, positive, negative;

        Words (ArrayList<HashMap<String, Integer> > list) {

        }

        public void dump() {

        }
    }
}
