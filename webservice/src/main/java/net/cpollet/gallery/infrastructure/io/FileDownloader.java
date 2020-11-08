package net.cpollet.gallery.infrastructure.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
    private final int connectTimeoutMs;
    private final int readTimeoutMs;

    public FileDownloader(int connectTimeoutMs, int readTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
    }

    public byte[] getBytesFromUrl(String url) {
        try {
            return getBytesFromUrlThrowingException(url);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private byte[] getBytesFromUrlThrowingException(String url) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = openConnection(url).getInputStream()) {
            readAllBytes(inputStream, outputStream);
        }

        return outputStream.toByteArray();
    }

    private void readAllBytes(InputStream inputStream, ByteArrayOutputStream outputStream) throws IOException {
        byte[] byteChunk = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(byteChunk)) > 0) {
            outputStream.write(byteChunk, 0, bytesRead);
        }
    }

    private URLConnection openConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setConnectTimeout(connectTimeoutMs);
        connection.setReadTimeout(readTimeoutMs);
        connection.connect();
        return connection;
    }
}
