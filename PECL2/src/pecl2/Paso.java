package pecl2;

// La clase Paso define variable booleana cerrado
// que es comprobada por un proceso.
// Si vale false(abierto) el proceso puede continuar. Si es true(cerrado) el proceso se detiene
public class Paso
{
    private boolean cerrado = false;
    private boolean abierto = true;
    
    public synchronized void mirar()
    {
        while (cerrado)
        {
           try
           {
               wait();
           }
           catch (InterruptedException ex) { }   
        }
    }
    
    public synchronized void abrir()
    {
        cerrado = false;
        notifyAll();
    }
    
    public synchronized void cerrar()
    {
        cerrado = true;
    }
    
        
    public synchronized void mirarAbierto()
    {
        while (!abierto)
        {
           try
           {
               wait();
           }
           catch (InterruptedException ex) { }   
        }
    }
    
    public synchronized void abrirExpo()
    {
        abierto = true;
        notifyAll();
    }
    
    public synchronized void cerrarExpo()
    {
        abierto = false;
    }
} 