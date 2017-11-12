package ru.tehcpu.tinyaes;

import ru.tehcpu.tinyaes.core.AES;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please, pass the text");
            return;
        }

        String text = args[0];
        byte[] key = (args.length == 2) ? args[1].getBytes() : getKey();

        // Input prepare stuff
        text = fillBlock(text);

        byte[] ciphertext = encrypt(text.getBytes(), key);
        System.out.println("Encrypted text: "+new String(ciphertext)+" | Key: "+new String(key));

        Scanner reader = new Scanner(System.in);
        System.out.print("Decrypt? [y/n] (y): ");
        String decision = reader.nextLine();
        if (decision.equals("y") || decision.equals("")) {
            byte[] plaintext = decrypt(ciphertext, key);
            System.out.println("Plaintext: "+new String(plaintext));
        } else {
            System.out.println("Bye");
        }
    }

    private static byte[] encrypt(byte[] text, byte[] key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AES Cipher = new AES(key);
        for (int i = 0; i < text.length; i+=16) {
            try {
                out.write(Cipher.encrypt(Arrays.copyOfRange(text, i, i + 16)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    private static byte[] decrypt(byte[] text, byte[] key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AES Cipher = new AES(key);
        for (int i = 0; i < text.length; i+=16) {
            try {
                out.write(Cipher.decrypt(Arrays.copyOfRange(text, i, i + 16)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
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
