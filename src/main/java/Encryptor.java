import java.io.*;
import java.util.Arrays;

public class Encryptor {

    private final String key;

    public Encryptor(String key) {
        this.key = key;
    }

    private byte[] getFullKey(String key, int len) {
        byte[] buff = key.getBytes();
        byte[] tmp = key.getBytes();
        byte[] res = key.getBytes();
        while (buff.length < len) {
            res = Arrays.copyOf(buff, buff.length + tmp.length);
            System.arraycopy(tmp, 0, res, buff.length, tmp.length);
            buff = res;
        }
        byte[] fullKey = Arrays.copyOfRange(res, 0, len);
        return fullKey;
    }

    public int encrypt(InputStream in, OutputStream out) throws IOException {
        int count = 0;
        byte[] bufferedInput = in.readAllBytes();
        byte[] bufferedKey = this.getFullKey(key, bufferedInput.length);
        try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
            for (int i = 0; i < bufferedInput.length; i++) {
                int encrypted = bufferedInput[i] ^ bufferedKey[i];
                writer.write(encrypted);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int encrypt(String inputName, String outputName) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputName)) {
            try (FileOutputStream fos = new FileOutputStream(outputName)) {
                return encrypt(fis, fos);
            }
        }
    }
}
