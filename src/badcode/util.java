package badcode;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class util {
    public static long getTime() {
        return (new Date()).getTime() / 1000;
    }

    public static String readFile(String path) {
        String item = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String temp;
            while ((temp = reader.readLine()) != null) {
                item += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot open file " + path);
        }

        return item;
    }

    public static void insertHashMap(HashMap<String, Integer> hashMap, String item) {
        if (hashMap.containsKey(item))
            hashMap.put(item, hashMap.get(item) + 1);
        else
            hashMap.put(item, 1);
    }
}
