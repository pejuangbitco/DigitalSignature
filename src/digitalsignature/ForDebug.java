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
public class ForDebug {
    public static void main(String args[]) {
        int penanda = 5;
        ElGamal el = new ElGamal();
        el.enkripsi("aHc", penanda);
        
        System.out.println("P: "+el.getBilanganPrima());
        System.out.println("A: "+el.getA());
        System.out.println("chiper: "+el.getChiperString());
        
        int[] chiper = el.getChiper();
        int P = el.getBilanganPrima();
        int a = el.getA();
        System.out.println("dekrips: "+el.dekripsi(chiper, penanda, P, a));
    }
}
