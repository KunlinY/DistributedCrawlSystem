package badcode;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.jooq.data.Tables;
import com.jooq.data.tables.Locations;
import com.jooq.data.tables.Organizations;
import com.jooq.data.tables.Persons;
import com.jooq.data.tables.Urls;
import com.jooq.data.tables.records.LocationsRecord;
import com.jooq.data.tables.records.OrganizationsRecord;
import com.jooq.data.tables.records.PersonsRecord;
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

        private List<String> summary = null;
        private List<String> keywords = null;
        private List<String> phrases = null;

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

        public String getSummary() {
            if (summary == null) {
                summary = HanLP.extractSummary(getContent(), 3);
            }

            StringBuilder key = new StringBuilder();
            for (String string : summary) {
                key.append(string);
            }
            return key.toString();
        }

        public String getKeywords() {
            if (keywords == null) {
                keywords = HanLP.extractKeyword(getContent(), 5);
            }

            StringBuilder key = new StringBuilder();
            for (String string : keywords) {
                key.append(string);
            }
            return key.toString();
        }

        public String getPhrases() {
            if (phrases == null) {
                phrases = HanLP.extractPhrase(getContent(), 10);
            }

            StringBuilder key = new StringBuilder();
            for (String string : phrases) {
                key.append(string);
            }
            return key.toString();
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
        private HashMap<String, Integer> organization = new HashMap<>();
        private HashMap<String, Integer> location = new HashMap<>();
        private HashMap<String, Integer> person = new HashMap<>();
        private HashMap<String, Integer> others = new HashMap<>();
        private DSLContext dslContext;
        private String url;

        Words(String html, String url) {
            try {
                dslContext = DSL.using(connection, SQLDialect.POSTGRES_9_3);
                this.url = url;
                recognition(html);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void recognition(String html) {
            Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true);
            List<Term> termList = segment.seg(html);

            for (Term term : termList) {
                switch (term.nature) {
                    case nr:
                    case nrf:
                    case nrj:
                    case nr1:
                    case nr2:
                        util.insertHashMap(person, term.word);
                        break;
                    case ns:
                    case nsf:
                        util.insertHashMap(location, term.word);
                        break;
                    case nt:
                    case ni:
                    case nis:
                    case nic:
                    case nit:
                    case ntc:
                    case ntcb:
                    case ntcf:
                    case ntch:
                    case nth:
                    case nts:
                    case nto:
                    case ntu:
                        util.insertHashMap(organization,term.word);
                        break;
                    case c:
                    case cc:
                    case dl:
                    case e:
                    case h:
                    case p:
                    case pba:
                    case pbei:
                    case r:
                    case rg:
                    case Rg:
                    case rr:
                    case ry:
                    case rys:
                    case ryt:
                    case ryv:
                    case rz:
                    case rzs:
                    case u:
                    case ud:
                    case ude1:
                    case ude2:
                    case ude3:
                    case udeng:
                    case udh:
                    case ug:
                    case uguo:
                    case ul:
                    case ule:
                    case ulian:
                    case uls:
                    case usuo:
                    case uv:
                    case uyy:
                    case uz:
                    case uzhe:
                    case uzhi:
                    case vshi:
                    case w:
                    case wb:
                    case wd:
                    case wf:
                    case wh:
                    case wj:
                    case wky:
                    case wkz:
                    case wm:
                    case wn:
                    case wp:
                    case ws:
                    case wt:
                    case ww:
                    case wyy:
                    case wyz:
                    case xu:
                    case y:
                    case yg:
                        continue;
                    default:
                        util.insertHashMap(others, term.word);
                }
            }
        }

        public void dump() {
            insertPOL();
            insertURL();
        }

        private void insertPOL() {
            Long uNum = dslContext
                    .selectCount()
                    .from(Tables.PERSONS)
                    .fetchOne(0, Long.class) + 1;
            Long[] list = {uNum};

            for (String key : person.keySet()) {
                Result<PersonsRecord> result = dslContext
                        .selectFrom(Tables.PERSONS)
                        .where(Persons.PERSONS.PERSON.eq(key))
                        .fetch();

                if (result.isEmpty()) {
                    dslContext.insertInto(Tables.PERSONS)
                            .set(Persons.PERSONS.PERSON, key)
                            .set(Persons.PERSONS.ULIST, list)
                            .execute();
                }
                else {
                    Long[] temp = dslContext
                            .select(Persons.PERSONS.ULIST)
                            .from(Tables.PERSONS)
                            .where(Persons.PERSONS.PERSON.eq(key))
                            .fetch()
                            .get(0)
                            .value1();
                    Long[] newList = new Long[temp.length + 1];
                    System.arraycopy(temp, 0, newList, 0, temp.length);
                    newList[temp.length] = uNum;

                    dslContext.update(Tables.PERSONS)
                            .set(Persons.PERSONS.ULIST, newList)
                            .where(Persons.PERSONS.PERSON.eq(key))
                            .execute();
                }
            }

            for (String key : location.keySet()) {
                Result<LocationsRecord> result = dslContext
                        .selectFrom(Tables.LOCATIONS)
                        .where(Locations.LOCATIONS.LOCATION.eq(key))
                        .fetch();

                if (result.isEmpty()) {
                    dslContext.insertInto(Tables.LOCATIONS)
                            .set(Locations.LOCATIONS.LOCATION, key)
                            .set(Locations.LOCATIONS.ULIST, list)
                            .execute();
                }
                else {
                    Long[] temp = dslContext
                            .select(Locations.LOCATIONS.ULIST)
                            .from(Tables.LOCATIONS)
                            .where(Locations.LOCATIONS.LOCATION.eq(key))
                            .fetch()
                            .get(0)
                            .value1();
                    Long[] newList = new Long[temp.length + 1];
                    System.arraycopy(temp, 0, newList, 0, temp.length);
                    newList[temp.length] = uNum;

                    dslContext.update(Tables.LOCATIONS)
                            .set(Locations.LOCATIONS.ULIST, newList)
                            .where(Locations.LOCATIONS.LOCATION.eq(key))
                            .execute();
                }
            }

            for (String key : organization.keySet()) {
                Result<OrganizationsRecord> result = dslContext
                        .selectFrom(Tables.ORGANIZATIONS)
                        .where(Organizations.ORGANIZATIONS.ORGANIZATION.eq(key))
                        .fetch();

                if (result.isEmpty()) {
                    dslContext.insertInto(Tables.ORGANIZATIONS)
                            .set(Organizations.ORGANIZATIONS.ORGANIZATION, key)
                            .set(Organizations.ORGANIZATIONS.ULIST, list)
                            .execute();
                }
                else {
                    Long[] temp = dslContext
                            .select(Organizations.ORGANIZATIONS.ULIST)
                            .from(Tables.ORGANIZATIONS)
                            .where(Organizations.ORGANIZATIONS.ORGANIZATION.eq(key))
                            .fetch()
                            .get(0)
                            .value1();
                    Long[] newList = new Long[temp.length + 1];
                    System.arraycopy(temp, 0, newList, 0, temp.length);
                    newList[temp.length] = uNum;

                    dslContext.update(Tables.ORGANIZATIONS)
                            .set(Organizations.ORGANIZATIONS.ULIST, newList)
                            .where(Organizations.ORGANIZATIONS.ORGANIZATION.eq(key))
                            .execute();
                }
            }
        }

        private void insertURL() {
            Long[] p = new Long[person.size()];
            Long[] o = new Long[organization.size()];
            Long[] l = new Long[location.size()];

            int index = 0;
            for (String key : person.keySet()) {
                p[index++] = dslContext.select(Tables.PERSONS.ID)
                        .from(Tables.PERSONS)
                        .where(Persons.PERSONS.PERSON.eq(key))
                        .fetch()
                        .get(0)
                        .value1();
            }

            index = 0;
            for (String key : organization.keySet()) {
                o[index++] = dslContext.select(Tables.ORGANIZATIONS.ID)
                        .from(Tables.ORGANIZATIONS)
                        .where(Organizations.ORGANIZATIONS.ORGANIZATION.eq(key))
                        .fetch()
                        .get(0)
                        .value1();
            }

            index = 0;
            for (String key : location.keySet()) {
                l[index++] = dslContext.select(Tables.LOCATIONS.ID)
                        .from(Tables.LOCATIONS)
                        .where(Locations.LOCATIONS.LOCATION.eq(key))
                        .fetch()
                        .get(0)
                        .value1();
            }

            dslContext.insertInto(Tables.URLS)
                    .set(Urls.URLS.URL, url)
                    .set(Urls.URLS.PLIST, p)
                    .set(Urls.URLS.OLIST, o)
                    .set(Urls.URLS.LLIST, l)
                    .execute();
        }
    }

}
