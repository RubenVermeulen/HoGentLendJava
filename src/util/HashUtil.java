package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class HashUtil {
    public static String getSha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            
            return DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
