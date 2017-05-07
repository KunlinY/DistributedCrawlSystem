package badcode;

import java.util.Date;

public class util {
    public static long getTime() {
        return (new Date()).getTime() / 1000;
    }
}
