/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author CodeForLife
 */
class MD5
{
 
    private static final int INIT_A = 0x67452301;
    private static final int INIT_B = (int)0xEFCDAB89L;
    private static final int INIT_C = (int)0x98BADCFEL;
    private static final int INIT_D = 0x10325476;

    private static final int[] TABLE_T = new int[64];
    static
    {
        for (int i = 0; i < 64; i++)
          TABLE_T[i] = (int)(long)((1L << 32) * Math.abs(Math.sin(i + 1)));
    }

    public static byte[] computeMD5(byte[] message)
    {
        //allocate byte array as blocks
        ByteBuffer blocksBuffer = ByteBuffer.allocate((((message.length + 8) / 64) + 1) * 64).order(ByteOrder.LITTLE_ENDIAN);
        //put byte msg to allocated byte array
        blocksBuffer.put(message);
        //put one byte, equals to bit 1000 0000 
        blocksBuffer.put((byte)128);
        //get length message and put to allocated byte array
        long messageLenBits = (long)message.length * 8;
        blocksBuffer.putLong(blocksBuffer.capacity() - 8, messageLenBits);
        //reset position to zero, byteBuffer padded 
        blocksBuffer.rewind();

        //assign 4 buffer MD5
        int a = INIT_A;
        int b = INIT_B;
        int c = INIT_C;
        int d = INIT_D;
        while (blocksBuffer.hasRemaining()) {
          // obtain a slice of the buffer from the current position,
          // and view it as an array of 32-bit ints
          IntBuffer blockBuffer = blocksBuffer.slice().order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
          int originalA = a;
          int originalB = b;
          int originalC = c;
          int originalD = d;

          //round 1
          int[] shift_bit = {
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22
          };
          for (int i=0; i<16; i++) {
            int f = (b & c) | (~b & d);
            int temp = b + Integer.rotateLeft(a + f + blockBuffer.get(i) + TABLE_T[i], shift_bit[i]);
            a = d;
            d = c;
            c = b;
            b = temp;
          }

          //round 2
          shift_bit = new int[]{
            5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20
          };
          int[] element_chunk = {
            1, 6, 11, 0, 5, 10, 15, 4, 9, 14, 3, 8, 13, 2, 7, 12  
          };
          for (int i=0; i<16; i++) {
            int f = (b & d) | (c & ~d);
            int temp = b + Integer.rotateLeft(a + f + blockBuffer.get(element_chunk[i]) + TABLE_T[i+16], shift_bit[i]);
            a = d;
            d = c;
            c = b;
            b = temp;
          }

          //round 3
          shift_bit = new int[]{
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23
          };
          element_chunk = new int[]{
            5, 8, 11, 14, 1, 4, 7, 10, 13, 0, 3, 6, 9, 12, 15, 2
          };
          for (int i=0; i<16; i++) {
            int f = b ^ c ^ d;
            int temp = b + Integer.rotateLeft(a + f + blockBuffer.get(element_chunk[i]) + TABLE_T[i+32], shift_bit[i]);
            a = d;
            d = c;
            c = b;
            b = temp;
          }

          //round 4
          shift_bit = new int[]{
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
          };
          element_chunk = new int[]{
            0, 7, 14, 5, 12, 3, 10, 1, 8, 15, 6, 13, 4, 11, 2, 9
          };
          for (int i=0; i<16; i++) {
            int f = c ^ (b | ~d);
            int temp = b + Integer.rotateLeft(a + f + blockBuffer.get(element_chunk[i]) + TABLE_T[i+48], shift_bit[i]);
            a = d;
            d = c;
            c = b;
            b = temp;
          }

          a += originalA;
          b += originalB;
          c += originalC;
          d += originalD;
          blocksBuffer.position(blocksBuffer.position() + 64);
        }

        ByteBuffer md5 = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
        for (int n : new int[]{a, b, c, d})
        {
          md5.putInt(n);
        }
        return md5.array();
    }

    public static String toHexString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
        {
          sb.append(String.format("%02X", b[i] & 0xFF));
        }
        return sb.toString();
    }
 
    public static String checkSum(String fileName)
    {
        byte[] hmd5 = null;
        try {
            Path path = Paths.get(fileName);
            byte[] fileByte = Files.readAllBytes(path);
            hmd5 = computeMD5(fileByte);
        } catch (IOException ex) {
            Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toHexString(hmd5);
    }
    
}