package ru.tehcpu.tinyaes;

import ru.tehcpu.tinyaes.core.AES;

public class Main {
    private static void encrypt(String text, String key) {
        AES Cipher = new AES(key.getBytes());
        byte[] out = Cipher.encrypt(text.getBytes());
        System.out.println(new String(Cipher.decrypt(out)));
    }
    public static void main(String[] args) {
	// write your code here
        encrypt("qweqweqweqweqwe1", "lSAjoFac0lvyBzGVGysTDHzbiEwtXw4J");
    }
}
