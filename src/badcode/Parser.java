package badcode;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Parser {

    public static HashSet<URL> extractLink(String html, URL url) {
        HashSet<URL> links = new HashSet<>();


        // link 为用正则表达式提取出来的URL，提取所有<a href="里面的URL
        // 引号可以是"也可以是'
        /*
        try {
            links.add(new URL(url, link));
        } catch (Exception e) {
            System.out.println("Error parsing url: " + link + " in " + url);
        }
        */

        return links;
    }

    public static Set<String> filterURL(String url){
        Set<String> links= new HashSet<>();

        try {
            NodeFilter filter = new NodeClassFilter(LinkTag.class);
            org.htmlparser.Parser parser = new org.htmlparser.Parser();

            parser.setURL(url);
            parser.setEncoding(parser.getEncoding());
            NodeList list = parser.extractAllNodesThatMatch(filter);

            String tmp;
            for (int i = 0; i < list.size(); i++) {
                LinkTag node = (LinkTag) list.elementAt(i);
                tmp = node.extractLink();

                int j;
                if(( j=tmp.indexOf("\'"))!=-1){
                    tmp = tmp.substring(j+1, tmp.lastIndexOf("\\"));//error
                }

                if((j=tmp.indexOf("javascript:;"))==-1)//去除javascript:;类语句
                    links.add(tmp);
            }  //结果将javascript中document。writeln中的src记录并自动补全前链接，用'\'作标识

            for (String s : links){
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }

    public static void main(String[] args) {
        filterURL("https://www.taobao.com/");
    }
}
