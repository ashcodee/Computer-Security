import java.lang.System;
import java.security.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DSA {
    private static final int KEY_SIZE = 2048; // KEY SIZE = 2048 bits
    public static void main(String[] args) throws Exception {
        System.out.println("\n-----------------------------DSA Mode for key size 2048---------------------------\n"); 

        long startTime,endTime;
        // Generating a 2048-bit DSA key
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(KEY_SIZE, new SecureRandom());
        startTime = System.nanoTime();
        KeyPair keyPair = keyGen.generateKeyPair();
        endTime = System.nanoTime();
        System.out.println("Time taken to generate DSA key of size "+ KEY_SIZE + " bits : " + (endTime - startTime) + " nanoseconds");
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Signing the "1K.txt" file with Digital Signature
        byte[] byte1K = Files.readAllBytes(Paths.get("1K.txt"));
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        startTime = System.nanoTime();
        signature.update(byte1K);
        byte[] signature1K = signature.sign();
        endTime = System.nanoTime();
        System.out.println("Time taken to sign the message of size 1Kb with Digital Signature : " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to sign the message of size 1Kb with Digital Signature = " + (endTime - startTime)/(1024) + " nanoseconds");

        // Verifying the signature of the "1K.txt" file
        signature.initVerify(publicKey);
        startTime = System.nanoTime();
        signature.update(byte1K);
        endTime = System.nanoTime();
        boolean verified1 = signature.verify(signature1K);
        System.out.println("Signature for 1K.txt verified: " + verified1);
        System.out.println("Time taken to verify the message of size 1Kb with Digital Signature : " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to verify the message of size 1Kb with Digital Signature = " + (endTime - startTime)/1024 + " nanoseconds");

        // Signing the "10M.txt" file with Digital Signature
        byte[] byte10M = Files.readAllBytes(Paths.get("10M.txt"));
        startTime = System.nanoTime();
        signature.initSign(privateKey);
        signature.update(byte10M);
        byte[] signature10M = signature.sign();
        endTime = System.nanoTime();
        System.out.println("Time taken to sign the message of size 10Mb with Digital Signature : " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to sign the message of size 1Kb with Digital Signature = " + (endTime - startTime)/(10*1024*1024) + " nanoseconds");

        // Verifying the signature of the "10M.txt" file
        signature.initVerify(publicKey);
        startTime = System.nanoTime();
        signature.update(byte10M);
        endTime = System.nanoTime();
        boolean verified2 = signature.verify(signature10M);
        System.out.println("Signature for 10M.txt verified: " + verified2);
        System.out.println("Per byte time  to verify the message of size 1Kb with Digital Signature = " + (endTime - startTime)/(10*1024*1024) + " nanoseconds");
        System.out.println("Time taken to verify the message of size 10Mb with Digital Signature : " + (endTime - startTime) + " nanoseconds");
    }
}