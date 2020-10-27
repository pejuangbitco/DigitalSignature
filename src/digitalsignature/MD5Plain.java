/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

/**
 *
 * @author CodeForLife
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
 
class MD5Plain
{
 
  private static final int INIT_A = 0x67452301;
  private static final int INIT_B = (int)0xEFCDAB89L;
  private static final int INIT_C = (int)0x98BADCFEL;
  private static final int INIT_D = 0x10325476;
 
  private static final int[] SHIFT_AMTS = {
    7, 12, 17, 22,
    5,  9, 14, 20,
    4, 11, 16, 23,
    6, 10, 15, 21
  };
 
  private static final int[] TABLE_T = new int[64];
  static
  {
    for (int i = 0; i < 64; i++)
      TABLE_T[i] = (int)(long)((1L << 32) * Math.abs(Math.sin(i + 1)));
  }
 
  public static byte[] computeMD5(byte[] message)
  {
    ByteBuffer padded = ByteBuffer.allocate((((message.length + 8) / 64) + 1) * 64).order(ByteOrder.LITTLE_ENDIAN);
    padded.put(message);
    padded.put((byte)0x80);
    long messageLenBits = (long)message.length * 8;
    padded.putLong(padded.capacity() - 8, messageLenBits);
 
    padded.rewind();
 
    int a = INIT_A;
    int b = INIT_B;
    int c = INIT_C;
    int d = INIT_D;
    while (padded.hasRemaining()) {
      // obtain a slice of the buffer from the current position,
      // and view it as an array of 32-bit ints
      IntBuffer chunk = padded.slice().order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
      int penyanggaA = a;
      int penyanggaB = b;
      int penyanggaC = c;
      int penyanggaD = d;
      for (int j = 0; j < 64; j++)
      {
        int div16 = j >>> 4;
        int f = 0;
        int bufferIndex = j;
        switch (div16)
        {
          case 0:
            f = (b & c) | (~b & d);
            break;
 
          case 1:
            f = (b & d) | (c & ~d);
            bufferIndex = (bufferIndex * 5 + 1) & 0x0F;
            break;
 
          case 2:
            f = b ^ c ^ d;
            bufferIndex = (bufferIndex * 3 + 5) & 0x0F;
            break;
 
          case 3:
            f = c ^ (b | ~d);
            bufferIndex = (bufferIndex * 7) & 0x0F;
            break;
        }
        int temp = b + Integer.rotateLeft(a + f + chunk.get(bufferIndex) + TABLE_T[j], SHIFT_AMTS[(div16 << 2) | (j & 3)]);
        a = d;
        d = c;
        c = b;
        b = temp;
      }
 
      a += penyanggaA;
      b += penyanggaB;
      c += penyanggaC;
      d += penyanggaD;
      padded.position(padded.position() + 64);
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
        Logger.getLogger(MD5Plain.class.getName()).log(Level.SEVERE, null, ex);
    }
    return toHexString(hmd5);
  }
 
}
