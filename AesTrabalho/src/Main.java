import aes.AES;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import java.io.File;


public class Main {

private static final File PROJECT_DIR = new File(".");
private static final JFileChooser FILE_CHOOSER = new JFileChooser();
    public static void main(String[] args) {

        //pegar arquivo do usuário
        FILE_CHOOSER.setCurrentDirectory(PROJECT_DIR);
        FILE_CHOOSER.setDialogTitle("Selecione um arquivo para criptografar");
        FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FILE_CHOOSER.showOpenDialog(null);
        File fileToEncrypt = FILE_CHOOSER.getSelectedFile();

        // pegar chave de cripto
        String password = JOptionPane.showInputDialog("Insira a chave de criptografia do arquivo (128 bits):");
        while (password.split(",").length != 16) {
            JOptionPane.showMessageDialog(null, "Tamanho da chave errado: a chave deve conter 16 caracteres.");
            password = JOptionPane.showInputDialog("Insira a chave de criptografia do arquivo (128 bits):");
        }

        // pegar diretorio de saída 
        FILE_CHOOSER.setCurrentDirectory(PROJECT_DIR);
        FILE_CHOOSER.setDialogTitle("Selecione a pasta de destino para salvar o arquivo.");
        FILE_CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        FILE_CHOOSER.showSaveDialog(null);
        String path = FILE_CHOOSER.getSelectedFile().getPath();

        //Novo nome do arquivo de saída
        String fileName = JOptionPane.showInputDialog("Insira o nome do arquivo:");
        fileName = fileName.contains(".") ? fileName : fileName.concat(".txt");

        int[] result = AES.encriptar(Extensions.byteToIntArray(LerConteudoArquivo(fileToEncrypt.getPath())), password);

        boolean fileSave = SalvarConteudoArquivo(path + "\\" + fileName, Extensions.intToByteArray(result));

        JOptionPane.showMessageDialog(null, fileSave ? "Arquivo Salvo com sucesso" : "Não foi possível salvar o arquivo");
    }





    public static boolean SalvarConteudoArquivo(String path, byte[] content) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(content);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static byte[] LerConteudoArquivo(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error when trying to read the selected file");
        }
    }
}
