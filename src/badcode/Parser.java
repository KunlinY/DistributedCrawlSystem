package badcode;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Parser {
    private static final String aString = "(<a\\s[^>]+>)";
    private static final String hString = "href\\s?=\\s?(['\"]?)([^'\">\\s]+)\\1[>\\s]";

    public static HashSet<URL> extractLink(String html, URL url) {
        HashSet<URL> links = new HashSet<>();
        Matcher aTag = Pattern.compile(aString).matcher(html);

        while(aTag.find()){
            Matcher hrefLink = Pattern.compile(hString).matcher(aTag.group());
            while(hrefLink.find()) {
                String link = hrefLink.group(2);
                try {
                    links.add(new URL(url, link));
                } catch (Exception e) {

                }
            }
        }

        return links;
    }

    @Deprecated
    public static Set<String> filterURL(String url){
        Set<String> links= new HashSet<>();

        try {
            NodeFilter filter = new NodeClassFilter(LinkTag.class);
            org.htmlparser.Parser parser = new org.htmlparser.Parser();

            parser.setURL(url);
            parser.setEncoding(parser.getEncoding());
            NodeList list = parser.extractAllNodesThatMatch(filter);

            String newUrl;
            String urlStart = url.startsWith("https") == true ? "https" : "http";// 协议头
            String urlRoot = url.replaceAll("^(http(s)?://[^/]+)(/.*)?$", "$1");// 域名
            for (int i = 0; i < list.size(); i++) {
                LinkTag node = (LinkTag) list.elementAt(i);
                newUrl = node.extractLink();

                int j;
                if(( j=newUrl.indexOf("\'"))!=-1){
                    newUrl = newUrl.substring(j+1, newUrl.lastIndexOf("\\"));//error
                }
                while (!newUrl.startsWith("http")) {// 查找http标记
                    if (newUrl.startsWith("//")) {// 双斜杠开头不为相对链接
                        newUrl = urlStart + ":" + newUrl;
                    } else if (newUrl.startsWith("/")) {// 单斜杠开头指向根域名
                        newUrl = urlRoot + newUrl;
                        // 相对链接处理
                    } else if (newUrl.startsWith("../")) {
                        newUrl = url.replaceAll("(.+/)[^/]+/[^/]+$", "$1") + newUrl.replace("../", "");
                    } else if (newUrl.startsWith("./")) {
                        newUrl = url.replaceAll("(.+/)[^/]+$", "$1") + newUrl.replace("./", "");
                    } else {
                        newUrl = url.replaceAll("(.+/)[^/]+$", "$1") + newUrl;
                    }
                }
                //System.out.println(newUrl+"\r\n");
                if((j=newUrl.indexOf("javascript:;"))==-1)//去除javascript:;类语句
                    links.add(newUrl);
            }  //结果将javascript中document。writeln中的src记录并自动补全前链接，用'\'作标识

            for (String s : links){
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }

    public static void main(String[] args) throws Exception{
        extractLink(util.readFile("taobao.html"), new URL("http://info.ruc.edu.cn/"));
    }
}
