package aes;


import aes.tables.Tabelas;
import aes.Extensions.Extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrizEstado {
    private final int[][] words = new int[4][4];

    private MatrizEstado(int[] text) {
        if (text.length != 16) {
            throw new IllegalArgumentException("Password must be 128 bits");
        }

        int index = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                words[j][i] = text[index++];
            }
        }
    }

    private MatrizEstado(int[] firstKey, int[] secondKey, int[] thirdKey, int[] fourthKey) {
        if (firstKey.length != 4 || secondKey.length != 4 || thirdKey.length != 4 || fourthKey.length != 4) {
            throw new IllegalArgumentException("Password must be 128 bits: all the words must have 32 bits");
        }

        for (int i = 0; i < 4; i++) {
            words[i][0] = firstKey[i];
            words[i][1] = secondKey[i];
            words[i][2] = thirdKey[i];
            words[i][3] = fourthKey[i];
        }
    }

    public static MatrizEstado deChave(String password) {
        return new MatrizEstado(Arrays.stream(password.split(",")).mapToInt(Integer::parseInt).toArray());
    }

    public static MatrizEstado deChave(int[] array) {
        return new MatrizEstado(array);
    }

    public static List<MatrizEstado> deTextoSimples(int[] simpleText) {
        List<MatrizEstado> result = new ArrayList<>();

        for (int i = 0; i < simpleText.length; i += 16) {
            result.add(new MatrizEstado(Arrays.copyOfRange(simpleText, i, i + 16)));
        }

        return result;
    }

    public static MatrizEstado deRoundKeys(int[] firstKey, int[] secondKey, int[] thirdKey, int[] fourthKey) {
        return new MatrizEstado(firstKey, secondKey, thirdKey, fourthKey);
    }

    private static int[] rotateRow(int[] row, int index) {
        for (int i = 0; i < index; i++) {
            row = Extensions.rotacionarArray(row);
        }
        return row;
    }

    

    public MatrizEstado subBytes() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                words[i][j] = Tabelas.PegarValorTabelaSbox(words[i][j]);
            }
        }
        return this;
    }

    public MatrizEstado shiftRows() {
        IntStream.range(0, 4).forEach(i -> words[i] = rotateRow(words[i], i));
        return this;
    }

    public MatrizEstado mixColumns() {
        List<int[]> wordsList = getPalavras();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                words[i][j] = calcularMixColumns(wordsList.get(j), Extensions.getMultiMatrixRow(i));
            }
        }

        return this;
    }

    public MatrizEstado applyRoundKey(MatrizEstado roundKey) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                words[i][j] ^= roundKey.words[i][j];
            }
        }
        return this;
    }

    public List<int[]> getLinhas() {
        return Arrays.stream(words, 0, 4).collect(Collectors.toList());
    }

    public List<int[]> getPalavras() {
        return IntStream.range(0, 4).mapToObj(i -> IntStream.range(0, 4).map(j -> words[j][i]).toArray()).collect(Collectors.toList());
    }

    public int[] toIntArray() {
        return getPalavras().stream().flatMapToInt(Arrays::stream).toArray();
    }

    private String formatHex(String hex) {
        return (hex.length() == 1 ? "0x0" : "0x").concat(hex);
    }

    @Override
    public String toString() {
        StringBuilder[] lines = new StringBuilder[4];
        IntStream.range(0, 4).forEach(i -> lines[i] = new StringBuilder());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                lines[j].append(formatHex(Integer.toHexString(words[j][i]))).append(" | ");
            }
        }

        return String.join("\n", lines);
    }



    private static int calcularMixColumns(int[] column, int[] row) {
        int result = calcularValorGalois(column[0], row[0]);

        for (int i = 1; i < 4; i++) {
            result ^= calcularValorGalois(column[i], row[i]);
        }

        return result;
    }

    private static int calcularValorGalois(int valueOne, int valueTwo) {
        if (valueOne == 0 || valueTwo == 0) return 0;
        if (valueOne == 1) return valueTwo;
        if (valueTwo == 1) return valueOne;

        int TabelaHResult = Tabelas.pegarValorTabelaH(valueOne) + Tabelas.pegarValorTabelaH(valueTwo);

        if (TabelaHResult > 0xFF) TabelaHResult -= 0xFF;

        return Tabelas.pegarValorTabelaE(TabelaHResult);
    }
}
