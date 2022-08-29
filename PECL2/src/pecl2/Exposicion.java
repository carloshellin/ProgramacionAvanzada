package pecl2;
import java.util.concurrent.Semaphore;
import javax.swing.JTextField;

// Exposicion con las listas de espera y dentro, usa semáforos.
public class Exposicion
{
    private int aforo;
    private ListaThreads colaEspera, dentro;
    private Semaphore semaforo;
    private Paso paso;

    public Exposicion(int aforo, JTextField tfEsperan, JTextField tfDentro, Paso paso)
    {
        this.aforo=aforo;
        semaforo=new Semaphore(aforo,true);
        colaEspera=new ListaThreads(tfEsperan);
        dentro=new ListaThreads(tfDentro);
        this.paso = paso;
    }
    
    public void entrar(Visitante v)
    {
        paso.mirarAbierto(); // Mira si esta abierto la exposición
        colaEspera.meter(v);
        try
        {
            semaforo.acquire();
        } catch(InterruptedException e){ }
        paso.mirarAbierto(); // Mira si esta abierto la exposición
        colaEspera.sacar(v);
        dentro.meter(v);
    }

    public void salir(Visitante v)
    {
        dentro.sacar(v);
        semaforo.release();
    }
    
    public void mirar(Visitante v)
    {
        try
        {
            Thread.sleep(2000+(int)(3000*Math.random()));
        } catch (InterruptedException e){ }
    }  
}
