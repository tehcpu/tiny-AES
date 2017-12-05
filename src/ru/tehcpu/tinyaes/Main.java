package ru.tehcpu.tinyaes;

import ru.tehcpu.tinyaes.core.AES;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static AES cipher;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please, pass the text");
            return;
        }

        String text = args[0];
        byte[] key = (args.length == 2) ? args[1].getBytes() : getKey();

        // Input prepare stuff
        text = fillBlock(text);
        byte[] inputText = text.getBytes();

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
}
