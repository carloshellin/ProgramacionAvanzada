package simulacion.actividades;

import java.util.concurrent.Semaphore;
import javax.swing.JTextField;
import simulacion.Lista;
import simulacion.Paso;
import simulacion.Zona;
import simulacion.Usuario;
import static simulacion.Utils.dormir;

public class Vestuario extends Zona
{
    private final Semaphore semaforoNinos;
    
    public Vestuario(JTextField tfColaAfuera, JTextField tfColaDentro,
            JTextField tfMonitor, Paso paso)
    {
        // En el VESTUARIO, tenemos una capacidad total de 30 usuarios:
        // 20 adultos y 10 niños. Como los niños de 1 a 10 años necesitan ir
        // acompañados, el acompañante consume un espacio de los 10 disponibles
        // para niños. Los niños de 11 a 17 años entran en la categoría de
        // niños, pero no necesitan acompañante. El tiempo en el vestuario,
        // tanto para entrar como para salir, que invierte cada usuario es
        // de 3 segundos.
        
        super(30, tfColaAfuera, tfColaDentro, 1, tfMonitor,paso);
          
        this.semaforoNinos = new Semaphore(10, true);
        setSemaforo(new Semaphore(20, true));
    }
    
    @Override
    public void entrar(Usuario usuario)
    {
        usuario.setZonaActual(this);
		usuario.setUbicacion("Vestuarios");
        
        if (usuario.esAdulto())
        {
            super.entrar(usuario);
        }
        else if (usuario.necesitaAcompanante())
        {
            entrar(usuario, semaforoNinos, 2);
        }
        else
        {
            entrar(usuario, semaforoNinos, 1);
        }
        dormir(3);
    }

    @Override
    public void salir(Usuario usuario)
    {    
        dormir(3);
                
        if (usuario.esAdulto())
        {
            super.salir(usuario);

        }
        else if (usuario.necesitaAcompanante())
        {
            salir(usuario, semaforoNinos, 2);
        }
        else
        {
            salir(usuario, semaforoNinos, 1);
        }
    }

    @Override
    public Usuario comprobarEdad()
    {
        Usuario usuario = super.comprobarEdad();
        
        // El monitor del VESTUARIO controlará la edad del
        // usuario y el acompañante asociado.
        // El monitor tardará 1 segundo en comprobar la edad de cada usuario.
        if (usuario != null)
        {
            Lista colaEntrada = getColaEntrada();
            Lista dentro = getDentro();

            colaEntrada.extraer(usuario);
            dentro.insertar(usuario);

            if (usuario.necesitaAcompanante())
            {
                Usuario acompanente = usuario.getAcompanante();
                colaEntrada.extraer(acompanente);
                dentro.insertar(acompanente);
            }

            dormir(1);
        }
        
        return usuario;
    }
      
}