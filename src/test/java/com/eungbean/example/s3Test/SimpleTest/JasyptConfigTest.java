package com.eungbean.example.s3Test.SimpleTest;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JasyptConfigTest {

    private final String SECRET_KEY = "awss3";
    private final String ALGORITHM = "PBEWithMD5AndDES";


//    @Test
    void string_encryption() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(SECRET_KEY);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String originalString = "AKIA6ODU3LBC44UNDE6I";

        // 암호화
        String encryptedString = encryptor.encrypt(originalString);
        System.out.println("Encrypted String ::: ENC(" + encryptedString + ")");

        // 복호화
        String decryptedString = encryptor.decrypt(encryptedString);
        System.out.println("Decrypted String ::: " + decryptedString);

        assertEquals(originalString, decryptedString);
    }
}

