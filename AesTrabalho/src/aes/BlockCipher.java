package aes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockCipher {

    public static int[] encriptarArray(int[] simpleText, String key) {
        List<MatrizEstado> roundKeys = KeyExpansion.expandirChaves(MatrizEstado.deChave(key));
        List<MatrizEstado> textBlocks = MatrizEstado.deTextoSimples(simpleText);
        return encriptar(roundKeys, textBlocks);
    }

    private static int[] encriptar(List<MatrizEstado> roundKeys, List<MatrizEstado> textBlocks) {
        Stream<MatrizEstado> blockStream = textBlocks.stream().map(b -> b.applyRoundKey(roundKeys.get(0)));

        for (int i = 1; i < 10; i++) {
            MatrizEstado roundKey = roundKeys.get(i);
            blockStream = blockStream
                    .map(MatrizEstado::subBytes)
                    .map(MatrizEstado::shiftRows)
                    .map(MatrizEstado::mixColumns)
                    .map(b -> b.applyRoundKey(roundKey));
        }

        return blockStream
                .map(MatrizEstado::subBytes)
                .map(MatrizEstado::shiftRows)
                .map(b -> b.applyRoundKey(roundKeys.get(10)))
                .map(MatrizEstado::toIntArray)
                .flatMapToInt(Arrays::stream).toArray();
    }
}
