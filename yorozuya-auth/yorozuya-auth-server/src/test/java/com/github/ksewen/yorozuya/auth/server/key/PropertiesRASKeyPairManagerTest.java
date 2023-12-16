package com.github.ksewen.yorozuya.auth.server.key;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.github.ksewen.yorozuya.auth.server.configuration.TokenAuthenticationProperties;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 29.11.2023 17:14
 */
@SpringBootTest(classes = PropertiesRSAKeyPairManager.class)
public class PropertiesRASKeyPairManagerTest {

  @Autowired private PropertiesRSAKeyPairManager keyPairManager;

  @MockBean private TokenAuthenticationProperties properties;

  public static final String PUBLIC_KEY_FOR_TEST =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5gLkjIomXr91YSp352WT"
          + "jamhP1Q6DQN9rWYlZp3zoFljtkBYFgGa4Ur3VNPbGqn0kEL3EGNraoufIx+YGE2n"
          + "v82f0V8OP+1r76mmqnxXDUDVmPOrktGr0XsS8K2maQinNUKR6m92ChowdTaNQ8Ou"
          + "XYQb1z27n/pjGmKAIf+Nkelph46O1uTQs2okO582bVLF9rmZ1LVmE+H4pEhsYnpW"
          + "VGkPrtc+cCuTTBr9nZapimRdeuwktgb8jDVYsTPgOI2tG2m3iGStkqpydSrh02W1"
          + "8e9uLgBMSy7G7CbwnrmSOg2ahoOA7GdOsz14fQhpiJ9XhoBprNeXoW7TCll5XCOq"
          + "hQIDAQAB";

  public static String PRIVATE_KEY_FOR_TEST =
      "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDmAuSMiiZev3Vh"
          + "KnfnZZONqaE/VDoNA32tZiVmnfOgWWO2QFgWAZrhSvdU09saqfSQQvcQY2tqi58j"
          + "H5gYTae/zZ/RXw4/7WvvqaaqfFcNQNWY86uS0avRexLwraZpCKc1QpHqb3YKGjB1"
          + "No1Dw65dhBvXPbuf+mMaYoAh/42R6WmHjo7W5NCzaiQ7nzZtUsX2uZnUtWYT4fik"
          + "SGxielZUaQ+u1z5wK5NMGv2dlqmKZF167CS2BvyMNVixM+A4ja0babeIZK2SqnJ1"
          + "KuHTZbXx724uAExLLsbsJvCeuZI6DZqGg4DsZ06zPXh9CGmIn1eGgGms15ehbtMK"
          + "WXlcI6qFAgMBAAECggEAMxmxbQfEPoiKq2uj9+S5Q1/MUyWDQCDmav+mj4XH+nfh"
          + "jzv8VedFYYgmKtJmeyYHD4XRHfL4ilM6p7L9N2kJO7OTLhDQOaOltzkZrgax0F/3"
          + "FGQOWi/63C9bpdzue6dQfqSrhkn9D4GCAoh8GfH/awdOqCMu6/vUyKfZj7QW37Nv"
          + "pyDnxVjdy4hI0Fg3/YRMRX/yim8Np5ZN16QE5Lm5+xSLcA1GE2eu/B1/vrY7fuHX"
          + "5m3CTonYz78QtBZ/V/Q/kTQF+MVoet4QEoK0R1dDiZaKp4+CN8dl7HEKeAun8Bw3"
          + "rVdV/JytJXlzmA4I7QSKVgRa3HJtKJ3RQcID41YwAQKBgQD/kP9nSHrnm2YLQUEl"
          + "VheNmCg7Y0CINRg21HxQ1SwupOOeUUvrK+muFQtKCyTdxVYn8ysb8AbXz+jPeMNi"
          + "rKlTHIaMDHEfAuba8Vq6BdkDAaS9IKJzuig4r3FFP3wxWo6771Iw0UbzBh3dYF/a"
          + "hsUIN2pvCn8Q0HiDxag+1BbnoQKBgQDmZsuoSlLJgj9hIAvGcMGEtz/tK6lUwFYK"
          + "JZEXdbLjzaX2ESdZcZbPxKBfPjmiU3MVUIQdp/eTIAdS3tp8us8e0qg/g6DTwtlX"
          + "pT3AGd2HCbbsuaLg4MBELs11wwANHVAIns468AmuWJyiHlFVJH/gkKf3zo9/BshH"
          + "9iWYZdVIZQKBgQD/FzTHW+JJoaP2YfjrkoZJ7/Fgbc4w5oY33FoFuPGz0SPdl5nm"
          + "r9s8xoaRLpRqxBkeISsLZMb/zOe9hvXHXT5Z1eQ6kASthpD1QuljOokE5jQQDy33"
          + "w314Ly4ph5eoD5Le5Z8tWQavdpCCsjqa/eBXNjEHivuPxfY02Sr5I2uFYQKBgDkT"
          + "YAIpn0aSAHdkA+a9ygLBjpGHTmRTXT0YP1ArdQxfxmwlpbTPoceIcx4FASwINisi"
          + "jgf2syr8NTQ/Rqq9cE+3abyKNA4hZ8wBgEGVZr5sFqef+QxyQM7jNTyl+N5G06u/"
          + "A2GwUwWgp/S1Q/qulwsfaqzFu+CA4p6JCJkvKN3NAoGBANHVnI48r2kcj8Ubzr5+"
          + "sexTS4N9uS4087w3pDJBELi7KbGNC6EoAZ48HWSQV2D83ltEj2FptfpZhh49x1/I"
          + "85f74Nma0tcI+KhQA622B2Hehp3lfFhRnMu/zgPHVwHhAAwDBB5yBKqVEgsO2p5+"
          + "SETlMLE0U7Xb9KP7cJXBiyCW";

  @Test
  void getAccessTokenKeyPair() {
    when(this.properties.getAccessPublicKey()).thenReturn(PUBLIC_KEY_FOR_TEST);
    when(this.properties.getAccessPrivateKey()).thenReturn(PRIVATE_KEY_FOR_TEST);

    KeyPair keyPair = this.keyPairManager.getAccessTokenKeyPair();
    assertThat(keyPair).isNotNull();
  }

  @Test
  void getAccessTokenKeyPairWithInvalidPublicKey() {
    when(this.properties.getAccessPublicKey()).thenReturn("public");
    when(this.properties.getAccessPrivateKey()).thenReturn(PRIVATE_KEY_FOR_TEST);

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> this.keyPairManager.getAccessTokenKeyPair());
    assertThat(exception).isNotNull().matches(e -> e.getCause() instanceof InvalidKeySpecException);
  }

  @Test
  void getAccessTokenKeyPairWithInvalidPrivateKey() {
    when(this.properties.getAccessPublicKey()).thenReturn(PUBLIC_KEY_FOR_TEST);
    when(this.properties.getAccessPrivateKey()).thenReturn("private");

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> this.keyPairManager.getAccessTokenKeyPair());
    assertThat(exception).isNotNull().matches(e -> e.getCause() instanceof InvalidKeySpecException);
  }

  @Test
  void getRefreshTokenKeyPair() {
    when(this.properties.getRefreshPublicKey()).thenReturn(PUBLIC_KEY_FOR_TEST);
    when(this.properties.getRefreshPrivateKey()).thenReturn(PRIVATE_KEY_FOR_TEST);

    KeyPair keyPair = this.keyPairManager.getRefreshTokenKeyPair();
    assertThat(keyPair).isNotNull();
  }

  @Test
  void getRefreshTokenKeyPairWithInvalidPublicKey() {
    when(this.properties.getRefreshPublicKey()).thenReturn("public");
    when(this.properties.getRefreshPrivateKey()).thenReturn(PRIVATE_KEY_FOR_TEST);

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> this.keyPairManager.getRefreshTokenKeyPair());
    assertThat(exception).isNotNull().matches(e -> e.getCause() instanceof InvalidKeySpecException);
  }

  @Test
  void getRefreshTokenKeyPairWithInvalidPrivateKey() {
    when(this.properties.getRefreshPublicKey()).thenReturn(PUBLIC_KEY_FOR_TEST);
    when(this.properties.getRefreshPrivateKey()).thenReturn("private");

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> this.keyPairManager.getRefreshTokenKeyPair());
    assertThat(exception).isNotNull().matches(e -> e.getCause() instanceof InvalidKeySpecException);
  }
}
