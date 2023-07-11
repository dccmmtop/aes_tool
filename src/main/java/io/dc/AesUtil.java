package io.dc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String IV = "jsuhf763kish7yd9"; // 16 bytes IV
    private static final int KEY_SIZE = 128;
    private static final String CHARSET = "UTF-8";

    private static final String EN_FILE_FLAG = ".gpg";



    public static void writeFile(String filename, String text) throws IOException {
        // 使用 FileWriter 写入文件
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(filename)), CHARSET)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void aesEn(String key, String filename) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String plaintext = readFile(filename);
        System.out.println(plaintext);
        key = String.format("%-16s", key);
        System.out.println(key);
        System.out.println(key.length());
        byte[] iv = IV.getBytes(CHARSET);

        // Generate a new AES key
        byte[] keyBytes = key.getBytes(CHARSET);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the cipher with the key and IV
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));

        // Encrypt the plaintext
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(CHARSET));

        // Return the Base64-encoded ciphertext
        plaintext = Base64.getEncoder().encodeToString(ciphertext);
        writeFile(filename + EN_FILE_FLAG, plaintext);
    }

    public static void aesDe(String key, String filename) throws Exception {
        if(!filename.endsWith(EN_FILE_FLAG)){
            System.out.println("不是加密文件，跳过");
            return;
        }
        String encrypted = readFile(filename);
        key = String.format("%-16s", key);
        byte[] iv = IV.getBytes(CHARSET);

        // Generate the AES key from the key bytes
        byte[] keyBytes = key.getBytes(CHARSET);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the cipher with the key and IV
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));

        // Decrypt the ciphertext
        byte[] ciphertext = Base64.getDecoder().decode(encrypted);
        byte[] plaintext = cipher.doFinal(ciphertext);

        writeFile(filename.replace(EN_FILE_FLAG,""), new String(plaintext, CHARSET));
    }

    public static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        // 使用 BufferedReader 读取文件
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filename)),CHARSET));
        String line = null;
        while((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        bufferedReader.close();

        return content.toString().trim();
    }

    public static void main(String[] args) throws Exception {
    }
}