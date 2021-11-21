package dominio;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {

    public SecretKey keyGenerator() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("DES").generateKey();
    }

    public String encrypt(SecretKey secretKey, String toEncrypt) throws Exception {
        Cipher encrypt = Cipher.getInstance("DES");
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bytesToEncrypt = toEncrypt.getBytes(StandardCharsets.UTF_8);
        byte[] bytesEncrypted = encrypt.doFinal(bytesToEncrypt);
        bytesEncrypted = Base64.getEncoder().encode(bytesEncrypted);
        return new String(bytesEncrypted);
    }

    public String decrypt(SecretKey secretKey, String toDecrypt) throws Exception {
        Cipher decrypt = Cipher.getInstance("DES");
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] bytesToDecrypt = Base64.getDecoder().decode(toDecrypt.getBytes());
        byte[] bytesDecrypted = decrypt.doFinal(bytesToDecrypt);
        return new String(bytesDecrypted);
    }
}
