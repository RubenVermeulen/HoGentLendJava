package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javafx.scene.image.Image;

public class ImageUtil {

    private static Image defaultImage = null;

    public static byte[] imageFileToByteArray(File file) {
        try {
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("De foto is veel te groot. (Max 2.14GB)");
            }
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IllegalArgumentException("De gegeven foto kan niet volledig worden gelezen.");
            }
            is.close();
            return bytes;
        } catch (IOException e) {
            throw new IllegalArgumentException("Het lukte niet om de gegeven foto in te lezen.");
        }
    }

    public static byte[] imageFileToByteArray(String fotoUrl) {
        if (fotoUrl == null || fotoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("De foto locatie mag niet leeg zijn.");
        }
        return imageFileToByteArray(new File(fotoUrl));
    }

    public static Image byteArrayToImage(byte[] byteArray) {
        if (byteArray == null) {
            return defaultImage != null ? defaultImage
                    : (defaultImage = new Image(ImageUtil.class.getResourceAsStream("/images/default-placeholder.png")));
        }
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public static File getResourceAsFile(String resourcePath) {
        try {
            InputStream in = ImageUtil.class.getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".jpg");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
