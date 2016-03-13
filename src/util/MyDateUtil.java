package util;

import java.time.LocalDateTime;

public class MyDateUtil {

    public static boolean doesFirstPairOverlapWithSecond(LocalDateTime a1, LocalDateTime a2, LocalDateTime b1, LocalDateTime b2) {
        if (a1 == null && a2 == null){
            return false;
        }
        if (a1 == null) {
            a1 = a2;
        }
        if (a2 == null) {
            a2 = a1;
        }
        if (a2.isBefore(a1) || b2.isBefore(b1)){
            return false;
        }
        if (a1.isEqual(a2)) {
            a1 = a1.withHour(0).withMinute(0).withSecond(0);
            a2 = a1.withHour(23).withMinute(59).withSecond(59);
        }
        if (a2.isAfter(b1) && a2.isBefore(b2)) {
            return true;
        }
        if (a1.isAfter(b1) && a1.isBefore(b2)) {
            return true;
        }
        if (a1.isBefore(b1) && a2.isAfter(b2)) {
            return true;
        }
        return false;
    }

}
