package com.github.ksewen.yorozuya.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * @author ksewen
 * @date 24.09.2023 22:44
 */
class GzipUtilsTest {

  @Test
  void compressAndDecompress() throws IOException {
    final String text = "wait_for_compress";
    byte[] compressed = GzipUtils.compress(text);
    assertThat(compressed).isNotNull().isNotEmpty();
    byte[] decompressed = GzipUtils.decompress(compressed);
    assertThat(decompressed).isNotNull().isNotEmpty();
    String result = new String(decompressed);
    assertThat(result).isNotNull().isEqualTo(text);
  }

  @Test
  void compressWithNullSource() throws IOException {
    byte[] result = GzipUtils.compress(null);
    assertThat(result).isNull();
  }

  @Test
  void decompressWithNullBytes() throws IOException {
    byte[] result = GzipUtils.decompress(null);
    assertThat(result).isNull();
  }

  @Test
  void decompressWithEmptyBytes() throws IOException {
    byte[] result = GzipUtils.decompress(new byte[] {});
    assertThat(result).isNull();
  }
}
