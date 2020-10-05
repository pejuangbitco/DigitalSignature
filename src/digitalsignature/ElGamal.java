/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalsignature;

import java.math.BigInteger;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author CodeForLife
 */
public class ElGamal {
    private int P;
    private int g;
    private int b;
    private int a;
    private BigInteger Pb;
    private BigInteger gb;
    private BigInteger bb;
    private BigInteger ab;
    private int penanda_tangan;    
    private boolean perbandingan_msg_digest;
    
    private ArrayList<Integer> chiper_list;
    public int getBilanganPrima() 
    {
        return this.P;
    }
    
    public boolean getPerbandinganMessageDigest()
    {
        return this.perbandingan_msg_digest;
    }
    
    public String getChiperString()
    {
        return this.chiper_list.toString();
    } 
    
    public int[] getChiper()
    {
        int[] chiper = new int[this.chiper_list.size()];
        for(int i=0; i<chiper.length; i++) {
            chiper[i] = this.chiper_list.get(i).intValue();
        }
        return chiper;
    } 
    
    public ElGamal()
    {
        Random int_random = new Random();                
        
        this.P = getPrime();
        this.g       = int_random.nextInt(this.P-2);
        this.a       = int_random.nextInt(this.P-2);
        this.Pb = BigInteger.valueOf(this.P);
        this.gb = BigInteger.valueOf(this.g);
        this.ab = BigInteger.valueOf(this.a);
        this.bb  = gb.modPow(ab, Pb);
        this.chiper_list = new ArrayList<Integer>();
    }
    
    public void enkripsi(String msg_digest, int penanda_tangan)
    {
        Random int_random = new Random();    
        char msg[] = msg_digest.toCharArray();
        for(int i=0; i<msg.length ;i++) {    
            int r[] = new int[penanda_tangan];            
            for(int j=0; j<r.length; j++) {
                r[j] = int_random.nextInt(this.P-2);
            }
            //calc C1,..., Cn
            BigInteger[] c1 = chiperC1(r);
            BigInteger c2 = chiperC2(msg[i], r);
            this.dekripsi(r, c1, c2, Pb, a);
        }
    }
    
    public BigInteger[] chiperC1(int r[])
    {   
        BigInteger[] c1 = new BigInteger[r.length];
        for(int i=0; i<r.length; i++) {
            c1[i] = this.gb.modPow(BigInteger.valueOf(r[i]), this.Pb);
            this.chiper_list.add(c1[i].intValue());
        }
        
        return c1;
    }
    
    public BigInteger chiperC2(int m, int r[])
    {        
        BigInteger tmp = new BigInteger("1");
        
        tmp = tmp.multiply(BigInteger.valueOf(m));
        for(int i=0; i<r.length; i++) {
            tmp = tmp.multiply(this.bb.pow(r[i]));            
        }
        
        BigInteger c2 = tmp.mod(this.Pb);
        this.chiper_list.add(c2.intValue());
        return c2;
    }
    
    public void dekripsi(int[] r, BigInteger[] c1, BigInteger c2, BigInteger Pb, int a)
    {
        BigInteger tmp = new BigInteger("1");
        tmp = tmp.multiply(c2);
        for(int i=0; i<r.length; i++) {
            tmp = tmp.multiply(c1[i].pow(this.P-1-a));            
        }
        
        BigInteger M = tmp.mod(Pb);
        System.out.println("dekrip: "+M.toString());
    }
    
    public String dekripsi(int[] chiper, int jumlah_penanda, int P, int a)
    {
        String md5 = "";
        BigInteger tmp = new BigInteger("1");
        BigInteger Pb = new BigInteger(Integer.toString(P));
        int stepper = 0;
        for(int i=0; i<chiper.length; i++) {
            BigInteger chiper_b = new BigInteger(Integer.toString(chiper[i]));
            if(stepper==jumlah_penanda) {
                stepper=0;
                tmp = tmp.multiply(chiper_b);
                BigInteger ascii = tmp.mod(Pb);
                tmp = new BigInteger("1");
                md5 += (char)ascii.intValue();
                System.out.println("hit: "+i);
            } else {
                stepper++;
                tmp = tmp.multiply(chiper_b.pow(P-1-a));
            }
        }
        return md5;
    }
    
    public int getPrime()
    {
        Random rand = new Random();
        int num;
        int counter;
        
        num     = rand.nextInt(1000)+500;
        counter = 0;
        
        for(int i=2; i<num; i++) {
            if(num%i==0) {
                i = 1;
                num = rand.nextInt(1000)+500;
            }
        }
        
        return num;
    }
    
    public int getG()
    {
        return this.g;
    }
    
    public int getB()
    {
        return this.bb.intValue();
    }
    
    public int getA()
    {
        return this.a;
    }
        
}
