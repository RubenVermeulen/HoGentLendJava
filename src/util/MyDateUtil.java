package util;

import java.time.LocalDateTime;

public class MyDateUtil {

    public static boolean doesFirstPairOverlapWithSecond(LocalDateTime a1, LocalDateTime a2, LocalDateTime b1, LocalDateTime b2) {
        if (a1 == null && a2 == null){
            return false;
        }
        if (a1 == null) {
            a1 = LocalDateTime.MIN;
        }
        if (a2 == null) {
            a2 = LocalDateTime.MAX;
        }
        if (a2.isBefore(a1) || b2.isBefore(b1)){
            return false;
        }
        if (a1.isEqual(a2)) {
            return a1.isAfter(b1) && a1.isBefore(b2);
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
