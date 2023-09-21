import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

class RSA3072 {


    private static final int KEY_SIZE = 3072;
    public static void main(String[] args) throws Exception {
        
        System.out.println("\n-----------------------------RSA Mode for key size 3072---------------------------\n"); 
        // Generating RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEY_SIZE);
        long startTime = System.nanoTime();
        KeyPair keyPair = keyGen.generateKeyPair();
        long endTime = System.nanoTime();
        System.out.println("Time taken to generate RSA key of size "+ KEY_SIZE + " bits : " + (endTime - startTime) + " nanoseconds");
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Encrypting the 1Kb file with RSA algorithm
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        FileInputStream in = new FileInputStream("1K.txt");
        FileOutputStream out = new FileOutputStream("1KB.txt_EncRSA");

        byte[] buf = new byte[190];
        int len;
        startTime = System.nanoTime();
        while ((len = in.read(buf)) != -1) {
            byte[] encrypted = cipher.doFinal(buf, 0, len);
            out.write(encrypted);
        }
        endTime = System.nanoTime();
        System.out.println("Time to encrypt 1Kb file with RSA = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Encryption speed per byte with RSA = " + (double)(1024)/(endTime - startTime) + " bytes/nanoseconds");
        in.close();
        out.close();

        // Decrypting the 1Kb file with RSA algorithm
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        in = new FileInputStream("1KB.txt_EncRSA");
        out = new FileOutputStream("1KB.txt_DecRSA");

        buf = new byte[384];
        startTime = System.nanoTime();
        while ((len = in.read(buf)) != -1) {
            byte[] decrypted = cipher.doFinal(buf, 0, len);
            out.write(decrypted);
        }
        endTime = System.nanoTime();
        System.out.println("Time to decrypt 1Kb file with RSA = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Decryption speed per byte with RSA = " + (double)(1024)/(endTime - startTime) + " bytes/nanoseconds");
        in.close();
        out.close();

        // Encrypting the 1Mb file with RSA algorithm
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        in = new FileInputStream("1M.txt");
        out = new FileOutputStream("1MB.txt_EncRSA");

        buf = new byte[190];
        startTime = System.nanoTime();
        while ((len = in.read(buf)) != -1) {
            byte[] encrypted = cipher.doFinal(buf, 0, len);
            out.write(encrypted);
        }
        endTime = System.nanoTime();
        System.out.println("Time to encrypt 1Mb file with RSA = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Encryption speed per byte with RSA = " + (double)(1024*1024)/(endTime - startTime) + " bytes/nanoseconds");
        in.close();
        out.close();

        // Decrypting the 1Mb file with RSA algorithm
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        in = new FileInputStream("1MB.txt_EncRSA");
        out = new FileOutputStream("1MB.txt_DecRSA");

        buf = new byte[384];
        startTime = System.nanoTime();
        while ((len = in.read(buf)) != -1) {
            byte[] decrypted = cipher.doFinal(buf, 0, len);
            out.write(decrypted);
        }
        endTime = System.nanoTime();
        System.out.println("Time to decrypt 1Mb file with RSA = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Decryption speed per byte with RSA = " + (double)(1024*1024)/(endTime - startTime) + " bytes/nanoseconds");
        in.close();
        out.close();
    }
}