package badcode;

import java.util.ArrayList;
import java.util.HashSet;

public class NLP {
    NLP() {

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
        HashSet<String> organization, idiom, person, location, properNoun,
                topic, positive, negative;

        Words (ArrayList<HashSet<String>> list) {

        }

        public void dump() {

        }
    }
}
