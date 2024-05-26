package aes;

import aes.Extensions.Extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeyExpansion {

    public static List<MatrizEstado> expandirChaves(MatrizEstado initialMatrix) {
        List<MatrizEstado> roundKeys = new ArrayList<>();
        roundKeys.add(initialMatrix);

        for (int i = 1; i < 11; i++) {
            MatrizEstado previousMatrix = roundKeys.get(i - 1);
            List<int[]> words = previousMatrix.getPalavras();

            int[] firstKey = expandirPrimeiraChave(words, i);
            int[] secondKey = Extensions.aplicarXor(firstKey, words.get(1));
            int[] thirdKey = Extensions.aplicarXor(secondKey, words.get(2));
            int[] fourthKey = Extensions.aplicarXor(thirdKey, words.get(3));

            roundKeys.add(MatrizEstado.deRoundKeys(firstKey, secondKey, thirdKey, fourthKey));
        }

        return roundKeys;
    }

    private static int[] expandirPrimeiraChave(List<int[]> previousMatrixWords, int index) {
        return Optional.of(previousMatrixWords.get(3))                            // (1) Copiar última palavra da round key anterior
                .map(Extensions::rotacionarArray)                                          // (2) Rotacionar bytes (RotWord)
                .map(Extensions::substituirPalavra)                                       // (3) Substituir bytes (SubWord)
                .map(w -> Extensions.aplicarXor(w, Extensions.getRoundConstant(index)))       // (4) Gerar RoundConstant // (5) XOR de (3) com (4)
                .map(w -> Extensions.aplicarXor(w, previousMatrixWords.get(0)))          // (6) XOR de (5) com 1ª palavra da roundkey anterior
                .orElseThrow(() -> new RuntimeException("Error when trying to expand the first key of the block"));
    }
}
