package aes;

import aes.Extensions.Extensions;

public class AES {
    private static final int BLOCK_SIZE = 16;

    public static int[] encriptar(int[] simpleText, String password) {
        int[] pkcsText = Extensions.pkcs7(simpleText, BLOCK_SIZE);
        return BlockCipher.encriptarArray(pkcsText, password);
    }











}
