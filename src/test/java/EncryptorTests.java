import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {Launcher.main(args);});
        assertNotEquals("", e.getMessage());
    }

    @Test
    void noKey() {
       String[] args = new String[] {"input.txt"};
       IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {Launcher.main(args);});
       assertNotEquals("", e.getMessage());
    }
}
