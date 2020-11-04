package net.cpollet.gallery.domain.picture.values;

import lombok.NonNull;
import lombok.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Value
public class Bytes {
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    @NonNull byte[] bytes;

    public String toString() {
        return String.format("[%d bytes data]", bytes.length);
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String toHex() {
        char[] result = new char[2 * bytes.length];
        int j = 0;
        for (byte b : bytes) {
            result[j++] = HEX[(0xF0 & b) >>> 4];
            result[j++] = HEX[(0x0F & b)];
        }
        return String.valueOf(result);
    }

    public int size() {
        return bytes.length;
    }

    public Bytes hash() {
        try {
            return new Bytes(MessageDigest.getInstance("SHA3-256").digest(bytes));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }
}
