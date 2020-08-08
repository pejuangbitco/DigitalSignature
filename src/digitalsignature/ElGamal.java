/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author CodeForLife
 */
public class ElGamal {
    private int bilangan_prima;
    private int penanda_tangan;    
    private boolean perbandingan_msg_digest;
    
    public int getBilanganPrima() 
    {
        return this.bilangan_prima;
    }
    
    public boolean getPerbandinganMessageDigest()
    {
        return this.perbandingan_msg_digest;
    }
    
    public ElGamal(String msg_digest, int penanda_tangan)
    {
        Random int_random = new Random();                
        char msg[] = msg_digest.toCharArray();
        this.bilangan_prima = getPrime();
        this.perbandingan_msg_digest = true;
        
        int g       = int_random.nextInt(this.bilangan_prima-2);
        int a       = int_random.nextInt(this.bilangan_prima-2);
        int r[] = new int[penanda_tangan];
        System.out.println("jumlah: "+r.length);
        for(int j=0; j<r.length; j++) {
            r[j] = int_random.nextInt(this.bilangan_prima-2);
        }
        
        for(int i=0; i<msg.length ;i++) {    
            boolean check_valid = hitung(msg[i], this.bilangan_prima, g, a, r);
            if(check_valid==false) {
                this.perbandingan_msg_digest = false;
                break;
            }
        }
    }
    
    public boolean hitung(int m, int P, int g, int a, int r[])
    {        
        BigInteger Pb = BigInteger.valueOf(P);
        BigInteger gb = BigInteger.valueOf(g);
        BigInteger ab = BigInteger.valueOf(a);
        BigInteger b  = gb.modPow(ab, Pb);
//        System.out.println("P:"+Pb+" g:"+gb+" a:"+BigInteger.valueOf(a)+" b:"+b);
        
        BigInteger c1[] = new BigInteger[r.length];
        for(int i=0; i<r.length; i++) {
            c1[i] = gb.modPow(BigInteger.valueOf(r[i]), Pb);
//            System.out.println("c1"+(i+1)+":"+c1[i]);
        }
        
        //c2
        BigInteger tmp = new BigInteger("1");
        
        tmp = tmp.multiply(BigInteger.valueOf(m));
        for(int i=0; i<r.length; i++) {
            tmp = tmp.multiply(b.pow(r[i]));            
        }
        
        BigInteger c2 = tmp.mod(Pb);
//        System.out.println("c2:"+c2);
        
        //dekripsi
        tmp = new BigInteger("1");
        tmp = tmp.multiply(c2);
        for(int i=0; i<r.length; i++) {
            tmp = tmp.multiply(c1[i].pow(P-1-a));            
        }
        
        BigInteger M = tmp.mod(Pb);
        System.out.println("msg:"+m+" "+"dekripsi:"+M);
//        System.out.println("----------------------------------");
        if(BigInteger.valueOf(m).compareTo(M) == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getPrime()
    {
        Random rand = new Random();
        int num;
        int counter;
        
        num     = rand.nextInt(50000)+500;
        counter = 0;
        
        for(int i=2; i<num; i++) {
            if(num%i==0) {
                i = 1;
                num = rand.nextInt(50000)+500;
            }
        }
        
        return num;
    }
}
