package pecl1;

import java.util.concurrent.locks.*;

public class Consumidor extends Thread
{
    private String nombre;
    private Buffer buffer;
    private static int resultado = 0;
    private Lock cerrojo = new ReentrantLock();
    
    public Consumidor(String nombre, Buffer buffer)
    {
        this.nombre = nombre;
        this.buffer = buffer;
    }
    
    @Override
    public void run()
    {
        int numero = 0;
        while (true)
        {
            try
            {
                sleep(300 + (int) (400 * Math.random())); // Espera entre 0.3 y 0,7 seg.
            } catch(InterruptedException e){ }
            
            numero = buffer.extraer();
            
            try
            {
                cerrojo.lock();
                resultado += numero;
            }
            finally
            {
                cerrojo.unlock();
            }
            
            System.out.println(nombre + " ha leído " + numero + ", resultado: " + resultado);
        }
    }
}
