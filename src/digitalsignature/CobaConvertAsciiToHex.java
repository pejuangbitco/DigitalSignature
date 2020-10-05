/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 *
 * @author CodeForLife
 */
public class CobaConvertAsciiToHex {
    public static void main(String[] args) {
       
      String ascii = "COBA";      //dibawah 448
//      String ascii = "COBA AKSJDAKSDJWIJDKADJKWAJWKDJAKWJAKSJDAKSDJAKSDJAKSDJSKK";  //diatas 448, dibawah 512
//      String ascii = "COBA AKSJDAKSDJWIJDKADJKWAJWKDJAKWJAKSJDAKSDJAKSDJAKSDJSKK AKSJDAKSDJWIJDKADJKWAJWKDJAKWJAKSJDAKSDJAKSDJAKSDJSKK AKSJDAKSDJWIJDKADJKWAJWKDJAKWJAKSJDAKSDJAKSDJAKSDJSKK";              
      byte[] asc = ascii.getBytes();
      
      byte[] asc_after_padd = add_padding(asc);
      
      System.out.println("length SEBELUM: "+asc.length*8);
      System.out.println("length SESUDAH: "+asc_after_padd.length*8);
      
      int satu = 152;
      byte satu1 = (byte)satu;
      System.out.println("byte 152: "+ Byte.toUnsignedLong(satu1));      
      IntToByteArray(584);
      
      //for menampilkan byte ke bentuk bit
//      for(byte ascb : asc_after_padd) {
//          int bin_ascb = Byte.toUnsignedInt(ascb);
//          System.out.println("bin: "+Integer.toBinaryString(bin_ascb));
//      }
//      try {
//        String text = new String(asc, "UTF-8");
//        System.out.println("byte: " + text);
//      } catch (IOException e) {
//            e.printStackTrace();
//        }
      
      
      // Step-1 - Convert ASCII string to char array
//      char[] ch = ascii.toCharArray();
//      
//      // Step-2 Iterate over char array and cast each element to Integer.
//      StringBuilder builder = new StringBuilder();
//
//      for (char c : ch) {
//         int i = (int) c;
//          System.out.println("bit: " + Integer.toBinaryString(i));
//          System.out.println("panjang: " + Integer.toBinaryString(i).length());
//         // Step-3 Convert integer value to hex using toHexString() method.
//         builder.append(Integer.toHexString(i).toUpperCase());
//      }
//
//      System.out.println("ASCII = " + ascii);
//      System.out.println("Hex = " + builder.toString());
   }
    
    

public static byte[] IntToByteArray( int data ) {    
    byte[] result = new byte[8];
    
//    result[0] = (byte) ((data & 0xFF) >> 56);
//    result[1] = (byte) ((data & 0x00FF) >> 48);
//    result[2] = (byte) ((data & 0x0000FF) >> 40);
//    result[3] = (byte) ((data & 0x000000FF) >> 32);
//    result[4] = (byte) ((data & 0x00000000FF) >> 24);
//    result[5] = (byte) ((data & 0x0000000000FF) >> 16);
//    result[6] = (byte) ((data & 0x000000000000FF) >> 8);
//    result[7] = (byte) ((data & 0x00000000000000FF) >> 0);
//    result = ByteBuffer.allocate(8).putInt(data).array();
    
    BigInteger tes = new BigInteger("5884");
    result = tes.toByteArray();
    System.out.println("binary: " + tes.toString(2) + " length:"+result.length);
    
    byte[] panjang_pesan = new byte[8];
    System.out.println(" panjang:::: "+panjang_pesan.length);
    int counter = 0;
    for(int i=7; i>=0; i--) {
        if(counter==result.length) {
            panjang_pesan[i] = (byte) 0;
        } else {
            panjang_pesan[i] = result[counter];
            counter++;
        }
    }
    for(byte ascb : panjang_pesan) {
          int bin_ascb = Byte.toUnsignedInt(ascb);
          System.out.println("bin: "+Integer.toBinaryString(bin_ascb)+" int: "+bin_ascb);
      }
//    
//    BigInteger bitStr = new BigInteger(result);
    
    return result;        
}


    
    public static byte[] add_padding(byte[] asc) {
        //menambahkan padding bit, sampai panjang pesan kongruen dgn 448 mod 512
        //return berapa panjang bit yg harus di tambahkan.
        int panjang_pesan = asc.length;        
        int bit448 = 56; //56 byte atau 448 bit
        int bit512 = 64;
        int byte_tambahan = 0;
        
        byte[] tmp_result = new byte[bit512];
        int tmp_panjang_pesan = panjang_pesan%bit512;
        
        if(tmp_panjang_pesan > bit448) {
            byte_tambahan = tmp_panjang_pesan-bit448;
        } else {
            byte_tambahan = bit448-tmp_panjang_pesan;
        }
        
        if(panjang_pesan/bit512 == 0 && panjang_pesan>bit448) {
            byte_tambahan = (panjang_pesan+bit512-byte_tambahan) - panjang_pesan;
        }
        
        for(int i=0; i<byte_tambahan; i++) {
            if(i==0)
                tmp_result[i] = (byte)128;
            else
                tmp_result[i] = (byte)0;
        }
        System.out.println("tambahan: "+byte_tambahan*8+ " length: "+tmp_result.length);
        byte[] result = new byte[panjang_pesan+byte_tambahan];
        System.arraycopy(asc, 0, result, 0, panjang_pesan);
        System.arraycopy(tmp_result, 0, result, panjang_pesan, byte_tambahan);
        return result;
    }
}
