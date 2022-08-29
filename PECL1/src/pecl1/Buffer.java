package pecl1;

import java.util.concurrent.locks.*;

public class Buffer
{
    private int[] capacidad = new int[15];
    private int posicionEscritura = 0;
    private int disponible = 0;
    private Lock cerrojo = new ReentrantLock();
    private Condition productores = cerrojo.newCondition();
    private Condition consumidores = cerrojo.newCondition();
    
    public void introducir(int numero)
    {
        try
        {
            cerrojo.lock();
            while (disponible >= capacidad.length)
            {
                try
                {
                    productores.await();
                } catch (Exception e) {}
            }
            
            if (posicionEscritura >= capacidad.length)
            {
                posicionEscritura = 0;
            }
            
            
            capacidad[posicionEscritura++] = numero;
            disponible++;
            
            consumidores.signal();
            
            if (disponible < capacidad.length)
            {
                productores.signal();
            }
        }
        finally
        {
            cerrojo.unlock();
        }
    }
    
    public int extraer()
    {
        int numero = 0;
        try
        {
            cerrojo.lock();
            while (disponible == 0)
            {
                try
                {
                    consumidores.await();
                } catch (Exception e) {}
            }
            
            int siguiente = posicionEscritura - disponible;
            
            if (siguiente < 0)
            {
                siguiente += capacidad.length;
            }
            
            
            numero = capacidad[siguiente];
            disponible--;
            
            productores.signal();
            
            if (disponible != 0)
            {
                consumidores.signal();
            }
        }
        finally
        {
            cerrojo.unlock();
        }
        
        return numero;
    }
}