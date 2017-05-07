package badcode;

import java.util.Date;

/**
 * Created by 97520 on 05/07/2017.
 */
public class util {
    public static long getTime() {
        return (new Date()).getTime() / 1000;
    }
}
