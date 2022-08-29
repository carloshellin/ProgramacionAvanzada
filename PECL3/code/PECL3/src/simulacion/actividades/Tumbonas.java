package simulacion.actividades;

import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import simulacion.Lista;
import simulacion.Paso;
import simulacion.Usuario;
import static simulacion.Utils.dormir;
import static simulacion.Utils.dormir_ms;
import simulacion.Zona;

public class Tumbonas extends Zona
{    
    public Tumbonas(JTextField tfColaAfuera, JTextField tfColaDentro,
            JTextField tfMonitor, Paso paso)
    {
        // En las TUMBONAS, hay una capacidad máxima de 20 usuarios. Solo los 
        // usuarios de 15 años o más pueden acceder. Cada vez que una tumbona 
        // queda libre, todos los que quieren acceder lo intentan y compiten
        // para ver quién es el primero. Un usuario permanece en las tumbonas
        // entre 2 y 4 segundos. El monitor de las tumbonas comprobará que no
        // entren menores de 15 años, tardando entre 500 y 900 milisegundos.
        
        super(20, tfColaAfuera, tfColaDentro, 4, tfMonitor, paso);
        
        // Semaforo injusto para las tumbonas
        setSemaforo(new Semaphore(20, false));
    }
 
    @Override
    public boolean accion(Usuario usuario)
    {
        boolean realizada = false;
        
        if (usuario.getEdad() >= 15)
        {
            dormir(2, 4);
            realizada = true;
        }
        
        return realizada;
    }    
	
	@Override
    public void entrar(Usuario usuario) {
        usuario.setUbicacion("Tumbonas");
        super.entrar(usuario); //To change body of generated methods, choose Tools | Templates.
    }
    

    @Override
    public void salir(Usuario usuario)
    {
        // Cada vez que una tumbona queda libre, todos los que quieren
        // acceder lo intentan y compiten para ver quién es el primero.
        Lista dentro = getDentro();
        int capacidad = getCapacidad();
        Semaphore semaforo = getSemaforo();
        if (dentro.tamano() >= capacidad)
        {
            semaforo.release();
        }
        
        super.salir(usuario);
    }

    @Override
    public Usuario comprobarEdad()
    {
        Usuario usuario = super.comprobarEdad();        
        
        if (usuario != null)
        {
            int edad = usuario.getEdad();
            Usuario acompanante = usuario.getAcompanante();
     
            // Solo los usuarios de 15 años o más pueden acceder
            if (edad < 15)
            {
                extraer(usuario, acompanante);
            }
            else
            {
                extraer(usuario, acompanante);                
                insertar(usuario, acompanante);
           }
           
            getMonitor().setUsuario(null);
           
            dormir_ms(500, 900);
        }
        
        return usuario;
    }
}