//Ricardo Berndt
//Lorhan Melo


package aes;

import aes.Extensions.Extensions;

public class AES {
    private static final int TAMANHO_BLOCO = 16;

    public static int[] encriptar(int[] textoSimples, String key) {
        int[] pkcsText = Extensions.pkcs7(textoSimples,TAMANHO_BLOCO);
        return BlockCipher.encriptarArray(pkcsText, key);
    }
}
