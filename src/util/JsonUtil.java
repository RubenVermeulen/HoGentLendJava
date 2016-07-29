package util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

public class JsonUtil {
    public static String getJson(String url) {
        try {
            return IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            return null;
        }
    }
}
