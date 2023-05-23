package org.antonioxocoy.cecommerce.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureUtil {
    public static final short BCRYPT_STENGETH = 10;
    public static String generateSafeToken(SecureRandom random) {
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }
}
