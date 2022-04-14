import org.junit.jupiter.api.Test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class EncryptorTests {

    @Test
    void encDec() throws IOException {
        Encryptor enc = new Encryptor("1F");
        int tmp = enc.encrypt("input.txt", "output.txt");
        tmp = enc.encrypt("output.txt", "result.txt");
        byte[] res = Files.readAllBytes(Path.of("result.txt"));
        byte[] exp = Files.readAllBytes(Path.of("input.txt"));
        assertArrayEquals(exp, res);

    }

    @Test
    void longKey() throws IOException {
        Encryptor enc = new Encryptor("1F24A51");
        int tmp = enc.encrypt("test1.txt", "test2.txt");
        tmp = enc.encrypt("test2.txt", "test3.txt");
        byte[] res = Files.readAllBytes(Path.of("test3.txt"));
        byte[] exp = Files.readAllBytes(Path.of("test1.txt"));
        assertArrayEquals(exp, res);
    }

    @Test
    void notHexadecimalKey() {
        String[] args = new String[] {"-c", "16", "input.txt"};
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Launcher.main(args));
        assertNotEquals("", e.getMessage());
    }

    @Test
    void noKey() {
       String[] args = new String[] {"input.txt"};
       IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {Launcher.main(args);});
       assertNotEquals("", e.getMessage());
    }

    @Test
    void randomTest() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("randomTest.txt")) {
            int size = (int) (10 + Math.random() * 1000);
            for (int i = 0; i < size; i ++) {
                int randByte = (int) (32 + Math.random()*94);
                fos.write(randByte);
            }
        }
        java.util.List<Character> chars = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2', '3', '4', '5');
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < (int) (1 + Math.random()*100); i++) {
            key.append(chars.get((int) (Math.random() * chars.size())));
        }
        Encryptor enc = new Encryptor(key.toString());
        int tmp = enc.encrypt("randomTest.txt", "randomTestOut.txt");
        tmp = enc.encrypt("randomTestOut.txt", "finalOut.txt");
        byte[] res = Files.readAllBytes(Path.of("finalOut.txt"));
        byte[] exp = Files.readAllBytes(Path.of("randomTest.txt"));
        assertArrayEquals(exp, res);
    }
}
