package badcode;


import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.util.HashSet;
import java.util.Set;

public class Parser {
    static Set<String> filterURL(String url){
        Set<String> links= new HashSet<String>();
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
