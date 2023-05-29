package org.antonioxocoy.cecommerce.security.encription;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class EncryptService {

    @Value("${security.encrypt.alg}")
    private String algorithm;
    @Value("${security.encrypt.type}")
    private String algorithmCipher;
    @Value("${security.encrypt.key}")
    private String password;
    @Value("${security.encrypt.salt}")
    private String salt;
    private IvParameterSpec iv;

    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(algorithmCipher);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), getIv());
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(algorithmCipher);
        cipher.init(Cipher.DECRYPT_MODE, getKey(), getIv());
        byte[] planText = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(planText);
    }

    public SecretKey getKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65526, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public IvParameterSpec getIv() {
        if (this.iv == null) {
            this.iv = generateIv();
        }
        return this.iv;
    }

    public IvParameterSpec generateIv() {
         SecureRandom rnd = new SecureRandom();
        return new IvParameterSpec(rnd.generateSeed(16));
    }

}
