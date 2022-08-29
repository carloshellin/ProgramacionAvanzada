package simulacion;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Paso
{
    private boolean cerrado=false;
    private final Lock cerrojo =  new ReentrantLock();
    private final Condition parar = cerrojo.newCondition();
 
    public void mirar()
    {
        try
        {
            cerrojo.lock();
            while(cerrado)
            {
                try
                {
                    parar.await();
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Paso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        finally
        {
            cerrojo.unlock();
        }
    }

    public void abrir()
    {
        try
        {
            cerrojo.lock();
            // Se cambia la condición por la que otros hilos
            // podrían estar esperando
            cerrado = false;
            parar.signalAll();
        }
        finally
        {
            cerrojo.unlock();
        }
    }

    public void cerrar()
    {
        try
        {
            cerrojo.lock();
            cerrado = true;
        }
        finally
        {
            cerrojo.unlock();
        }
    }
} 