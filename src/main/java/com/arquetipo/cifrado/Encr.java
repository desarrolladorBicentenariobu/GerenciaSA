package com.arquetipo.cifrado;

/**
* @author Jesus Cardenas
*/
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Component
public class Encr {
    // 16 byte long secret key!
    private byte[] key = new byte[] { 
                                 1,  2,  3,  4, 
                                 5,  6,  7,  8, 
                                 9, 10, 11, 12, 
                                13, 14, 15, 16
                         };
    
    public String encr(String message) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] encrypted = c.doFinal(message.getBytes("UTF8"));

        BASE64Encoder b64 = new BASE64Encoder();
        return b64.encode(encrypted).replaceAll("\r\n", "");
    }

    public String dencr(String encrypted) throws Exception {
        BASE64Decoder b64 = new BASE64Decoder();
        byte[] rawEnc = b64.decodeBuffer(encrypted);

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] raw = c.doFinal(rawEnc);
        return new String(raw, "UTF8"); 
    }

  
}

