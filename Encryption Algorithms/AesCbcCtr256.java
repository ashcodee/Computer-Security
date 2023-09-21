import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AesCbcCtr256 {

    private static final int KEY_SIZE = 256;
    private static final String CBCMode = "AES/CBC/PKCS5Padding";
    private static final String CTRMode = "AES/CTR/PKCS5Padding";
    private static final int BUFFER_SIZE = 1024 * 1024; // Buffer size to write data in 1 Mb chunks

    public static void main(String[] args) throws Exception {
        System.out.println("\n-----------------------------CBC/CTR Mode for AES key size 256---------------------------\n");
        
            File smallFile=null;
            File largeFile=null;
            long startTime,endTime;
            startTime = System.nanoTime();
            Key key = generateAESKey256();
            endTime = System.nanoTime();
            System.out.println("Time taken to generate AES key of size "+ KEY_SIZE + " bits : " + (endTime - startTime) + " nanoseconds");
            try {
                smallFile = new File("1K.txt");
                largeFile = new File("10M.txt");
            } catch (Exception e) {
                System.out.println("Error getting file: " + e.getMessage());
                System.exit(1);
            }
            System.out.println("Small file size in Kb:"+ " " + smallFile.length()/1024);
            System.out.println("Large file size in Kb:"+ " " + largeFile.length()/1024);
            System.out.println("Key Size ---- 256 bits -----");
            calculateTime(CBCMode,smallFile,largeFile,key);
            calculateTime(CTRMode,smallFile,largeFile,key);
        
        
    }

    private static void calculateTime(final String mode,File smallFile,File largeFile,Key key) throws Exception {
        long megaByte10 = 10*1024*1024;
        int kilobyte1 = 1024;
        long startTime,endTime;
        String modename = mode.equals("AES/CBC/PKCS5Padding") ? "CBC" : "CTR";
        if(modename=="CBC")
        System.out.println("\n-----------------------------CBC Mode for key size 256---------------------------\n");
        else
        System.out.println("\n-----------------------------CTR Mode for key size 256---------------------------\n"); 
        // Measure the runtime of encrypting the small file
        startTime = System.nanoTime();
        encryptFile(smallFile, key, mode);
        endTime = System.nanoTime();
        System.out.println("Time to encrypt 1Kb file with "+modename+" = " + (double)(endTime - startTime) + " nanoseconds");
        System.out.println("Encryption speed per byte with "+modename+" = " + (double)kilobyte1/((endTime - startTime)) + " bytes/nanoseconds");
        startTime = System.nanoTime();
        encryptFile(largeFile, key, mode);
        endTime = System.nanoTime();
        System.out.println("Time to encrypt 10Mb file with "+modename+" = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Encryption speed per byte with "+modename+" = " + (double)megaByte10/((endTime - startTime)) + " bytes/nanoseconds");

        // Measure the runtime of decrypting the large file
        startTime = System.nanoTime();
        decryptFile(smallFile, key, mode);
        endTime = System.nanoTime();
        System.out.println("Time to decrypt 1Kb file with "+modename+" = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Decryption speed per byte with "+modename+" = " + (double)kilobyte1/(endTime - startTime) + " bytes/nanoseconds");
        startTime = System.nanoTime();        
        decryptFile(largeFile, key, mode);
        endTime = System.nanoTime();
        System.out.println("Time to decrypt 10Mb file with "+modename+" = " + (double)(endTime - startTime) + " nanoseconds");
        System.out.println("Decryption speed per byte with "+modename+" = " + (double)megaByte10/((endTime - startTime)) + " bytes/nanoseconds");
    }

    // Generating a 256-bit AES key
    private static Key generateAESKey256() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }


    private static void encryptFile(File file, Key key, String mode) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        try (FileInputStream in = new FileInputStream(file);
             FileOutputStream out = new FileOutputStream(file.getAbsolutePath() + ".encrypted" + (mode.equals("AES/CBC/PKCS5Padding") ? "CBC" : "CTR"))) {
            byte[] buffer = new byte[cipher.getOutputSize(BUFFER_SIZE)];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(cipher.update(buffer, 0, bytesRead));
            }
            out.write(cipher.doFinal());
        }
    }

    private static void decryptFile(File file, Key key, String mode) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        String modename = mode.equals("AES/CBC/PKCS5Padding") ? "CBC" : "CTR";
        try (FileInputStream in = new FileInputStream(file.getAbsolutePath() + ".encrypted" + modename);
             FileOutputStream out = new FileOutputStream(file.getAbsolutePath() + ".decrypted" + modename)) {
            byte[] buffer = new byte[cipher.getOutputSize(BUFFER_SIZE)];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(cipher.update(buffer, 0, bytesRead));
            }
            out.write(cipher.doFinal());
        }
    }
}
