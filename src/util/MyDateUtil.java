package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDateUtil {

    public static boolean doesFirstPairOverlapWithSecond(LocalDateTime a1, LocalDateTime a2, LocalDateTime b1, LocalDateTime b2) {
        if (a1 == null && a2 == null) {
            return false;
        }
        if (a1 == null) {
            a1 = a2;
        }
        if (a2 == null) {
            a2 = a1;
        }
        if (a2.isBefore(a1) || b2.isBefore(b1)) {
            return false;
        }

        a1 = a1.withHour(0).withMinute(0).withSecond(0);
        a2 = a2.withHour(23).withMinute(59).withSecond(59);

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

    public static LocalTime convertToLocalTime(String tijd) {
        if (!tijd.contains(":") || tijd.length() > 5 ) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }

        int uur;
        try {
            uur = Integer.parseInt(tijd.substring(0, tijd.indexOf(":")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");

        }
        int minuten;
        try {
            minuten = Integer.parseInt(tijd.substring(tijd.indexOf(":") + 1, tijd.length()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }

        if (uur < 0 || uur > 23 || minuten < 0 || minuten > 59) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }

        LocalTime time = LocalTime.of(uur, minuten);
        return LocalTime.of(uur, minuten);
    }

}
