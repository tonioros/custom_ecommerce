package org.antonioxocoy.cecommerce.securitytest;

import org.antonioxocoy.cecommerce.security.encription.EncryptService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestEncryptService {

    @Autowired
    private EncryptService encryptService;

    @Test
    public void testEncryptCorrect() {
        String plainText = "Hello World 321!!";
        String encryptedText = null;
        try {
            encryptedText = this.encryptService.encrypt(plainText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotEquals(plainText, encryptedText);
    }

    @Test
    public void testEncryptAndDecryptSuccess() throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException,
            InvalidKeyException {
        String plainText = "Hello World 321!!";
        String encryptedText = null;
        String decryptedText = null;
        encryptedText = this.encryptService.encrypt(plainText);
        decryptedText = this.encryptService.decrypt(encryptedText);
        Assert.assertNotEquals(plainText, encryptedText);
        Assert.assertEquals(plainText, decryptedText);
    }

    @Test
    public void testNotDecryptARandomText()  {
        String plainText = "@#$%@asdfa0789erwewerq#$%@$%we3245o23u452@##$%@";
        String base64 = Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
        String decryptedText = null;
        try {
            decryptedText = this.encryptService.decrypt(base64);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        Assert.assertNull(decryptedText);
    }
}
