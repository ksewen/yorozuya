package com.github.ksewen.yorozuya.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 23.09.2023 10:16
 */
@Slf4j
public class GzipUtils {

  public static byte[] compress(String source) throws IOException {
    if (source == null || source.isEmpty()) {
      log.debug("the string to be compressed is null.");
      return null;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    GZIPOutputStream gzip = new GZIPOutputStream(out);
    gzip.write(source.getBytes(Charset.forName("UTF-8")));
    gzip.close();
    return out.toByteArray();
  }

  public static byte[] decompress(byte[] bytes) throws IOException {
    if (bytes == null || bytes.length == 0) {
      log.debug("the byte array to be decompressed is null.");
      return null;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
    GZIPInputStream ungzip = new GZIPInputStream(in);
    byte[] buffer = new byte[256];
    int n;
    while ((n = ungzip.read(buffer)) >= 0) {
      out.write(buffer, 0, n);
    }
    return out.toByteArray();
  }
}
