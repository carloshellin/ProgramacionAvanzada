package simulacion;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import simulacion.actividades.PiscinaNinos;
import simulacion.actividades.PiscinaOlas;
import simulacion.actividades.Tumbonas;

public class Zona
{
    private int capacidad;
    private int usuarios;
    private Semaphore semaforo;
    private final Semaphore semaforoBinario;
    private final Lista colaEntrada;
    private final Lista dentro;
    private Monitor monitor;
    private Paso paso; 
    
    public Zona(int capacidad, JTextField tfColaEntrada, 
            JTextField tfColaDentro, int id, JTextField tfMonitor, Paso paso)
    {
        this.capacidad = capacidad;
        this.usuarios = 0;
        this.semaforo = new Semaphore(capacidad, true);
        this.semaforoBinario = new Semaphore(1, true);
        this.colaEntrada = new Lista(tfColaEntrada);
        this.dentro = new Lista(tfColaDentro);
        this.monitor = null;
        this.paso = paso;
        if (id > 0)
        {
            this.monitor = new Monitor(id, this);
            tfMonitor.setText(monitor.mostrarIdentificador());
            monitor.start();
        }
        semaforoBinario.tryAcquire(); // Dejamos el semaforo binario a 0
    }
        
    public Zona(int capacidad, JTextField tf, Paso paso)
    {
       this(capacidad, tf, null, 0, null,paso);
    }
    
    public void extraer(Usuario usuario, Usuario acompanante)
    {
        paso.mirar();
        colaEntrada.extraer(usuario);
        paso.mirar();
        colaEntrada.extraer(acompanante);
        paso.mirar();
    }
    
    public void extraerTodo(Usuario usuario, Usuario acompanante)
    {
        colaEntrada.extraer(usuario);
        colaEntrada.extraer(acompanante);
        dentro.extraer(usuario);
        dentro.extraer(acompanante);
    }
    
    public void insertar(Usuario usuario, Usuario acompanante)
    {
        paso.mirar();
        dentro.insertar(usuario);
        paso.mirar();
        if (acompanante != null) dentro.insertar(acompanante);
        paso.mirar();
    }
    
    public void entrar(Usuario usuario, Semaphore semaforo, int permisos)
    {
        paso.mirar();
        Usuario acompanante = usuario.getAcompanante();
        
        paso.mirar();
        
        // - Cada vez que el usuario intente entrar a una actividad que esté
        //   llena esperará en una cola de espera.
        colaEntrada.insertar(usuario);
        
        paso.mirar();
        if (usuario.necesitaAcompanante()
                && !(this instanceof PiscinaNinos))
        {
            paso.mirar();
            colaEntrada.insertar(acompanante);
            paso.mirar();
        }
        
        try
        {
            paso.mirar();
            semaforo.acquire();
            paso.mirar();
            
            if (this instanceof Parque)
            {
                // En el parque entran 
                colaEntrada.extraer(usuario);
                paso.mirar();
                dentro.insertar(usuario);
                paso.mirar();
                if (usuario.necesitaAcompanante())
                {
                    colaEntrada.extraer(acompanante);
                    paso.mirar();
                    dentro.insertar(acompanante);
                    paso.mirar();
                }
            }
            else if (!(this instanceof PiscinaOlas))
            {
                // Los usuarios antes de pasar a una actividad se paran en el
                // semáforo binario en 0, y lo pone a 1 el monitor
                paso.mirar();
                semaforoBinario.acquire();
                paso.mirar();
                if (this instanceof Tumbonas)
                {
                    monitor.setUsuario(usuario);
                    paso.mirar();
                }
            }
        } 
        catch (InterruptedException ex)
        {
            Logger.getLogger(Zona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void entrar(Usuario usuario)
    {
        paso.mirar();
        if (usuario.necesitaAcompanante())
        {
            entrar(usuario, semaforo, 2);
            paso.mirar();
        }
        else
        {
            entrar(usuario, semaforo, 1);
            paso.mirar();
        }
    }
    
    public void entrar(Usuario usuario, int permisos)
    {
        paso.mirar();
        entrar(usuario, semaforo, permisos);
        paso.mirar();
    }
    
    public boolean accion(Usuario usuario)
    {
        return true;
    }
    
    public void salir(Usuario usuario, Semaphore semaforo, int permisos)
    {
        paso.mirar();
        dentro.extraer(usuario);
        paso.mirar();
        if (usuario.necesitaAcompanante())
        {
            dentro.extraer(usuario.getAcompanante());
            paso.mirar();
        }
        semaforo.release(permisos);
        paso.mirar();
    }
    
    public void salir(Usuario usuario)
    {
        paso.mirar();
        if (usuario.necesitaAcompanante())
        {
            salir(usuario, semaforo, 2);
            paso.mirar();
        }
        else
        {
            salir(usuario, semaforo, 1);
            paso.mirar();
        }
    }
    
    public void salir(Usuario usuario, int permisos)
    {
        paso.mirar();
        salir(usuario, semaforo, permisos);
        paso.mirar();
    }
    
    public void limpiar(Usuario usuario, Lista cola)
    {
        // Con 5000 hilos es necesario revisar que no quede nadie
        // por ahí "colgado"
        if (usuario != null)
        {
            Zona zona = usuario.getZonaActual();
            if (zona != null && this != zona || !usuario.isAlive())
            {
                extraerTodo(usuario, usuario.getAcompanante());
            }
        }
        else
        {
            cola.limpiar();
        }
    }
    
    public Usuario comprobarEdad()
    {
        paso.mirar();
        Usuario usuario = colaEntrada.primero();
        Usuario usuarioDentro = dentro.primero();
        
        limpiar(usuario, colaEntrada);
        limpiar(usuarioDentro, dentro);
        
        if (usuario != null)
        {
            paso.mirar();
            if (semaforoBinario.availablePermits() == 0)
            {
                semaforoBinario.release();
            }
            if (this instanceof Tumbonas && dentro.tamano() == (capacidad - 1))
            {
                usuario = monitor.getUsuario();  
                paso.mirar();
            }
        }

        if (dentro.tamano() == capacidad)
        {
            paso.mirar();
            usuario = null;
            paso.mirar();
        }        
        
        return usuario;
    }
    
    public int getCapacidad()
    {
        return capacidad;
    }
    
    public int getUsuarios()
    {
        return usuarios;
    }
    
    public Lista getColaEntrada()
    {
        return colaEntrada;
    }
    
    public Lista getDentro()
    {
        return dentro;
    }
    
    public Monitor getMonitor()
    {
        return monitor;
    }
    
    public void setSemaforo(Semaphore semaforo)
    {
        this.semaforo = semaforo;
    }
    
    public Semaphore getSemaforo()
    {
        return semaforo;
    }
    
    public Semaphore getSemaforoBinario()
    {
        return semaforoBinario;
    }
}
