import java.util.stream.IntStream;

public class Extensions {
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
