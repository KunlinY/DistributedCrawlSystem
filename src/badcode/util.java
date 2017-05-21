package badcode;

import java.io.*;
import java.util.Date;

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
}
