package channel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

public class HelperUtil {

    public static byte[] returnFileStream() {

        File file = new File("src", "invoice.pdf");
        if (file.exists()) {

            byte[] inFileBytes = new byte[0];
            try {
                byte[] bytes = loadFile(file);
                byte[] pdf = Files.readAllBytes(file.toPath());
                byte[] encoded = Base64.getEncoder().encode(pdf);
                return encoded;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @SuppressWarnings("resource")
    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

}
