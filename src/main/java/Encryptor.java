import java.io.*;


public class Encryptor {

    private final String key;

    public Encryptor(String key) {
        this.key = key;
    }


    public int encrypt(InputStream in, OutputStream out) throws IOException {
        int count = 0;
        byte[] bufferedKey = this.key.getBytes();
        try (InputStreamReader reader = new InputStreamReader(in)) {
            try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
                int sym = reader.read();
                while (sym != -1) {
                    count++;
                    int currPos = count - bufferedKey.length*(count/bufferedKey.length);
                    writer.write(sym^bufferedKey[currPos]);
                    sym = reader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }
    }

    public int encrypt(String inputName, String outputName) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputName)) {
            try (FileOutputStream fos = new FileOutputStream(outputName)) {
                return encrypt(fis, fos);
            }
        }
    }
}
