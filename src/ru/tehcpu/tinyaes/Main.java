package ru.tehcpu.tinyaes;

import ru.tehcpu.tinyaes.core.AES;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    private static AES cipher;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please, pass the text");
            return;
        }

        javaCryptoTest();
        keySensitiveTest();

        String text = args[0];
        byte[] key = (args.length == 2) ? args[1].getBytes() : getKey();

        // Input prepare stuff
        text = fillBlock(text);
        byte[] inputText = text.getBytes();

        System.out.println("\n~~~ mainTest ~~~\n");

        // ECB test
        cipher = new AES(key);

        double startTime = System.currentTimeMillis();
        for (int i=0; i < 100000; i++) cipher.ECB_decrypt(cipher.ECB_encrypt(inputText));
        double endTime = System.currentTimeMillis();
        System.out.println("ECB | "+(endTime-startTime)/1000.0 + " secs");

        // CBC test
        byte[] iv = "c8IKDNGsbioSCfxWa6KT8A84SrlMwOUH".getBytes();
        cipher = new AES(key, iv);

        double startTimeCBC = System.currentTimeMillis();
        for (int i=0; i < 100000; i++) cipher.CBC_decrypt(cipher.CBC_encrypt(inputText));
        double endTimeCBC = System.currentTimeMillis();
        System.out.println("CBC | "+(endTimeCBC-startTimeCBC)/1000.0 + " secs");
    }

    private static String fillBlock(String text) {
        int spaceNum = text.getBytes().length%16==0?0:16-text.getBytes().length%16;
        for (int i = 0; i<spaceNum; i++) text += " ";
        return text;
    }

    private static byte[] getKey() {
        String key = "";
        for (int i=0; i < 2; i++) key += Long.toHexString(Double.doubleToLongBits(Math.random()));
        return key.getBytes();
    }

    // some tests / demonstration

    private static void keySensitiveTest() {
        System.out.println("\n~~~ keySensitiveTest ~~~\n");
        byte[] inputText = "very secret message".getBytes();
        byte[] iv = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        byte[] key;

        // 128
        System.out.println("128 bit");
        key = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        cipher = new AES(key);
        System.out.println("ECB result --> " + new String(cipher.ECB_decrypt(cipher.ECB_encrypt(inputText))));
        cipher = new AES(key, iv);
        System.out.println("CBC result --> " + new String(cipher.CBC_decrypt(cipher.CBC_encrypt(inputText))));

        // 192
        System.out.println("192 bit");
        key = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        cipher = new AES(key);
        System.out.println("ECB result --> " + new String(cipher.ECB_decrypt(cipher.ECB_encrypt(inputText))));
        cipher = new AES(key, iv);
        System.out.println("CBC result --> " + new String(cipher.CBC_decrypt(cipher.CBC_encrypt(inputText))));

        // 256
        System.out.println("256 bit");
        key = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        cipher = new AES(key);
        System.out.println("ECB result --> " + new String(cipher.ECB_decrypt(cipher.ECB_encrypt(inputText))));
        cipher = new AES(key, iv);
        System.out.println("CBC result --> " + new String(cipher.CBC_decrypt(cipher.CBC_encrypt(inputText))));
    }

    private static void javaCryptoTest() {
        System.out.println("\n~~~ javaCryptoTest ~~~\n");
        System.out.println("AES 128 CBC");

        byte[] inputText = "very secret message".getBytes();
        byte[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        byte[] iv = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        cipher = new AES(key, iv);

        System.out.println(javaCryptoDecrypt(key, iv, cipher.CBC_encrypt(inputText)));
        System.out.println(new String(cipher.CBC_decrypt(javaCryptoEncrypt(key, iv, fillBlock(new String(inputText)).getBytes()))));

        // and so on.. ECB/CBC, different key sizes.. Keep in mind:
    }

    // javax.crypto methods part

    private static byte[] javaCryptoEncrypt(byte[] key, byte[] initVector, byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value);

            return encrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String javaCryptoDecrypt(byte[] key, byte[] initVector, byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
