import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Launcher {

    @Option(name = "-c", usage = "Encryption key", forbids = {"-d"})
    private String encryptionKey;

    @Option(name = "-d", usage = "Decryption key", forbids = {"-c"})
    private String decryptionKey;

    @Argument(required = true, usage = "Input file name")
    private String inputFileName;

    @Option(name = "-o", usage = "Output file name")
    private String outputFileName;

    public static void main(String[] args) {
        new Launcher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        String key = "";
        try {
            parser.parseArgument(args);
            if (outputFileName == null)
                outputFileName = "New" + inputFileName;
            if (decryptionKey == null && encryptionKey == null) {
                System.err.println("Enter encryption or decryption key");
                parser.printUsage(System.err);
                throw new IllegalArgumentException();
            }
            key = Objects.requireNonNullElseGet(decryptionKey, () -> encryptionKey);
            if (!Pattern.matches("[1-5A-Fa-f]*", key)) {
                System.err.println("Key must be a hexadecimal number");
                throw new IllegalArgumentException();
            }
        }
        catch (CmdLineException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("INCORRECT INPUT!");
            System.err.println("java -jar [-c key] [-d key] inputname.txt [-o outputname.txt]");
            parser.printUsage(System.err);
        }

        Encryptor encryptor = new Encryptor(key);
        try {
            int res = encryptor.encrypt(inputFileName, outputFileName);
            System.out.println("Finished. Symbols handled: " + res);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}





