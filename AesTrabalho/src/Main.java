//Ricardo Berndt
//Lorhan Melo


import aes.AES;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

import java.io.File;


public class Main {

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
      
        //pegar arquivo do usuário
        System.out.println("Digite o caminho completo para o arquivo a ser criptografado ():");
        String inputPath = scanner.nextLine();
        File fileToEncrypt = new File(inputPath);


        // pegar chave de cripto
        System.out.println("Digite a chave (bytes separados por ','):");
        String password = scanner.nextLine();

      
        // pegar diretorio de saída 
        System.out.println("Digite o caminho completo de saída para o arquivo criptografado ():");
        String outputPath = scanner.nextLine();
        File outputFile = new File(outputPath);

        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(fileToEncrypt.getPath())) {
             bytes = fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo de entrada");
        }


        int[] encriptado = AES.encriptar(byteToIntArray(bytes), password);


        try (FileOutputStream fos = new FileOutputStream(outputFile.getPath())) {
            fos.write(intToByteArray(encriptado));
        }
         catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo de saida");
        }
    }





       static int[] byteToIntArray(byte[] array) {
        return IntStream.range(0, array.length).map(i -> array[i]).toArray();
        }

        static byte[] intToByteArray(int[] array) {
            byte[] newArray = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = (byte) array[i];
            }
            return newArray;
        }
}
