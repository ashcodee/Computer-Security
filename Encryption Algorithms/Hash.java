import java.io.*;
import java.security.*;;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Hash {

    public static void main(String[] args) {

        File smallFile = new File("1K.txt");
        File largeFile = new File("10M.txt");
        Long startTime,endTime;
        int totalTime=0;

        System.out.println("\n-----------------------------Hashing Algorithms---------------------------\n"); 

        // Computing hash values for 1 Kb file and measure time
        startTime = System.nanoTime();
        computeHash("1K.txt", "SHA-256");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA-256 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time to hash message with SHA-256 algorithm = " + (endTime - startTime)/1024 + " nanoseconds");
        totalTime+=endTime - startTime;

        startTime = System.nanoTime();
        computeHash("1K.txt", "SHA-512");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA-512 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to hash message with SHA-512 algorithm = " + (endTime - startTime)/1024 + " nanoseconds");
        totalTime+=endTime - startTime;
        
        startTime = System.nanoTime();
        computeHash("1K.txt", "SHA3-256");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA3-256 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to hash message with SHA3-256 algorithm = " + (endTime - startTime)/1024 + " nanoseconds");
        totalTime+=endTime - startTime;
        
        

        // Computing hash values for 10 Mb file and measure time
        startTime = System.nanoTime();
        computeHash("10M.txt", "SHA-256");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA-256 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to hash message with SHA-256 algorithm = " + (endTime - startTime)/(10*1024*1024) + " nanoseconds");
        totalTime+=endTime - startTime;
        
        startTime = System.nanoTime();
        computeHash("10M.txt", "SHA-512");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA-512 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to to hash message with SHA-512 algorithm = " + (endTime - startTime)/(10*1024*1024) + " nanoseconds");
        totalTime+=endTime - startTime;
        
        startTime = System.nanoTime();
        computeHash("10M.txt", "SHA3-256");
        endTime = System.nanoTime();
        System.out.println("Time to hash message with SHA3-256 algorithm = " + (endTime - startTime) + " nanoseconds");
        System.out.println("Per byte time  to hash message with SHA3-256 algorithm = " + (endTime - startTime)/(10*1024*1024) + " nanoseconds");
        totalTime+=endTime - startTime;
        System.out.println("Total time taken to hash both files with multiple algorithms = " + totalTime + " nanoseconds");
    }

    //computeHash method computes the hash of files using different algorithms

    private static void computeHash(String fileName, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(Files.readAllBytes(Paths.get(fileName)));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String hash = sb.toString();
            System.out.println(algorithm + " hash value for " + fileName + ": " + hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
