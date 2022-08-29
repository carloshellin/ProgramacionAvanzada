package simulacion.actividades;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import simulacion.Paso;
import simulacion.Usuario;
import static simulacion.Utils.dormir;
import simulacion.Zona;

public class PiscinaOlas extends Zona
{ 
    private final Semaphore semaforoPareja;
    private Usuario pareja;
    
    public PiscinaOlas(JTextField tfColaAfuera, JTextField tfColaDentro,
            JTextField tfMonitor, Paso paso)
    {
        // En la PISCINA DE OLAS, hay una capacidad máxima de 20 usuarios.
        // Los niños de 1 a 5 años no pueden acceder a esta piscina, los de 6
        // a 10 años deben ir acompañados de un adulto y los demás usuarios
        // pueden acceder sin limitaciones. En esta piscina deben ir por parejas
        // por lo que, si hay un niño con acompañante, puede pasar directamente
        // a disfrutarla; pero si un usuario va solo, debe esperar a
        // otro usuario para poder entrar a la piscina. Los menores acompañados
        // utilizan dos espacios de la capacidad de la piscina, dado que van
        // acompañados. Un usuario permanece en la piscina de olas entre 2 y 5
        // segundos. El monitor de esta piscina comprueba, en primer lugar, que
        // estén por parejas (tarda 1 segundo) antes de dejarles pasar.    
        super(20, tfColaAfuera, tfColaDentro, 2, tfMonitor, paso);
        
        this.semaforoPareja = new Semaphore(1);
        this.semaforoPareja.tryAcquire(); // Semaforo binario inicialmente a 0
        this.pareja = null;
    }
    
    private void esperarPareja(Usuario usuario)
    {
        if (usuario.getAcompanante() == null)
        {
            if (pareja == null)
            {
                pareja = usuario;
                try
                {
                    semaforoPareja.acquire();
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(PiscinaOlas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                pareja.setPareja(usuario);
                pareja = null;
                semaforoPareja.release();
            }
        }
    }

    @Override
    public void entrar(Usuario usuario) 
    {         
		usuario.setUbicacion("Piscina Olas");
        super.entrar(usuario);
        esperarPareja(usuario);
            
        try
        {
            getSemaforoBinario().acquire();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(PiscinaOlas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean accion(Usuario usuario)
    {
        boolean realizada = false;
        int edad = usuario.getEdad();
        if (!(edad >= 1 && edad <= 5))
        {
            dormir(2, 5);
            realizada = true;
        }
        
        return realizada;
    }

    @Override
    public void salir(Usuario usuario)
    {
        super.salir(usuario);
        usuario.setPareja(null); 
    }
    
    @Override
    public Usuario comprobarEdad()
    {
        Usuario usuario = super.comprobarEdad();
        
        if (usuario != null)
        {
            int edad = usuario.getEdad();
            Usuario acompanante = usuario.getAcompanante();
            Usuario pareja = usuario.getPareja();

            if (edad >= 1 && edad <= 5)
            {
                extraer(usuario, acompanante);
            }
            else if (usuario.necesitaAcompanante())
            {
                extraer(usuario, acompanante);
                insertar(usuario, acompanante);
            }
            else if (pareja != null)
            {
                extraer(usuario, pareja);
                insertar(usuario, pareja);
            }
            
            dormir(1);
        }
        
        return usuario;
    }
}
