package work1.project1.package1.other;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String getMd5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hash = no.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        //System.out.println(messageDigest.length+"   "+hashtext);
        return hash;
    }

}
