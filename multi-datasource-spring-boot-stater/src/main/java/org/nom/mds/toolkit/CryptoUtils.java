package org.nom.mds.toolkit;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class CryptoUtils {

    private static final StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
    private static final String ALGORITHM = "PBEWithHMACSHA512AndAES_256";
    private static final String DEFAULT_DBK = "MY_DB_K";

    static {
        enc.setAlgorithm(ALGORITHM);
        String key = System.getenv(DEFAULT_DBK);
        if (key == null || key.isEmpty()) {
            key = DEFAULT_DBK;
        }
        enc.setPassword(key);
        enc.setIvGenerator(new RandomIvGenerator());
    }

    private CryptoUtils() {
    }

    public static String encrypt(String plain) {
        return enc.encrypt(plain);
    }

    public static StringEncryptor getEnc() {
        return enc;
    }

}
