package com.github.fashionbrot.tlv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author fashionbrot
 */
public class GzipUtil {

    public static byte[] compress(String input) throws IOException {
        if (input == null) {
            return null;
        }
        return compress(input.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] compress(byte[] input) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOS = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOS.write(input);
            gzipOS.finish(); // 完成写入压缩数据，但不关闭流
            return byteArrayOutputStream.toByteArray();
        }
    }

    public static String decompress(byte[] compressedBytes) throws IOException {
        return new String(decompressToByte(compressedBytes), StandardCharsets.UTF_8);
    }

    public static byte[] decompressToByte(byte[] compressedBytes) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedBytes);
             GZIPInputStream gzipIS = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }


}
