/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

/**
 *
 * @author CodeForLife
 */
public class SHA256 {
    public static String checkSum(String fileName){
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(fileName);

            byte[] data = new byte[1024];
            int read = 0; 
            while ((read = fis.read(data)) != -1) {
                sha256.update(data, 0, read);
            };
            byte[] hashBytes = sha256.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
              sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            String fileHash = sb.toString();

            return fileHash;
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
            return "null";
        }
    }
}
