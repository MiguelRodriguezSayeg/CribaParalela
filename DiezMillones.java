package numprim;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiezMillones {
    static int n = 10000000;
    static int nb = 100000000;
    static Object myObj = new Object();
    static int numeros[] = new int[n+1];
    static int counter=0;
    static boolean[] esPrimo = new boolean[nb+1];
    private static class Worker extends Thread{
		int miLimInf;
		int miLimSup;
		
		public Worker(int limInf, int limSup){
			miLimInf = limInf;
			miLimSup = limSup;
		}
		public void run(){
                    int c;
                    int contador=0;
                    for(c=miLimInf;c<miLimSup;c++){
                    if(esPrimo[numeros[c]]==true){
                        contador++;
                    }
                    }
                    synchronized(myObj){
                        counter+=contador;
                    }
                }
	}
    public static void main(String[] args) {
        int d,c;
        Random rand=new Random();
        int m = (int) Math.pow(10, 10 - 1);
    
        for ( d = 0 ; d < n ; d++ )
            numeros[d] = (int)rand.nextInt((10000000 - 1));
        for (int i = 2; i <= n; i++) {
            esPrimo[i] = true;
        }
        double start=System.currentTimeMillis();
        for (int multiplicador = 2; multiplicador*multiplicador <= nb; multiplicador++) {
            if (esPrimo[multiplicador]) {
                for (int j = multiplicador; multiplicador*j <= nb; j++) {
                    esPrimo[multiplicador*j] = false;
                }
            }
        }
        int proc = Runtime.getRuntime().availableProcessors();
        Thread[] hilos = new Worker[proc + 1];
        int valdos=0;
        int value=0;
         
         for(int x = 0; x < (hilos.length-1); x++){
            valdos=value+(n/proc);
            hilos[x]=new Worker(value,valdos);
            System.out.println("Creando hilo con val ini:"+value+" y val final "+valdos);
            hilos[x].start();
            value=valdos;
        }
         for (int x = 0; x < (hilos.length-1); x++) {
            try {
                hilos[x].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiezMillones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         double end=System.currentTimeMillis();
        System.out.println("Cantidad de números primos: "+counter);
        System.out.println("Tiempo tardado:"+(end-start)/1000F);
    }
}
