package simulacion.actividades;

import javax.swing.JTextField;
import simulacion.Lista;
import simulacion.Paso;
import simulacion.Usuario;
import static simulacion.Utils.dormir;
import simulacion.Zona;

public class PiscinaNinos extends Zona
{
    private final Lista esperaAdultos;
    
    public PiscinaNinos(JTextField tfColaAfuera, JTextField tfColaDentro, 
            JTextField tfColaAdultos, JTextField tfMonitor, Paso paso )
    {
        // En la PISCINA DE NIÑOS, hay una capacidad máxima de 15 usuarios. 
        // Los niños de 1 a 5 deben ir acompañados de un adulto y los usuarios 
        // de 11 años en adelante no pueden acceder. Un usuario permanece en la
        // piscina de niños entre 1 y 3 segundos. Los menores acompañados
        // utilizan dos espacios de la capacidad de la piscina, dado que van
        // acompañados. Los acompañantes de los niños de entre 6 y 10 años que
        // entren en la actividad solos, deberán esperar a que el niño
        // correspondiente termine su actividad. El monitor de esta piscina
        // comprobará las edades de los usuarios antes de dejarles pasar,
        // tardando un periodo de entre 1 y 1,5 segundos.
        super(15, tfColaAfuera, tfColaDentro, 3, tfMonitor, paso);
        
        this.esperaAdultos = new Lista(tfColaAdultos);
    }

    @Override
    public boolean accion(Usuario usuario)
    {
        boolean realizada = false;
        if (usuario.getEdad() < 11)
        {
            dormir(2, 5);
            realizada = true;
        }
        return realizada;
    }
    
    @Override
    public void salir(Usuario usuario)
    {
        int edad = usuario.getEdad();
        
        if (edad >= 6 && edad <= 10)
        {
            esperaAdultos.extraer(usuario.getAcompanante());
        }
        
        super.salir(usuario);
    }
	@Override
    public void entrar(Usuario usuario) {
        usuario.setUbicacion("Piscina Niños");
        super.entrar(usuario); 
    }
    

    @Override
    public Usuario comprobarEdad()
    {
        Usuario usuario = super.comprobarEdad();
        
        if (usuario != null)
        {
            int edad = usuario.getEdad();
            Usuario acompanante = usuario.getAcompanante();
        
            // Los usuarios de 11 años en adelante no pueden acceder
            if (edad >= 11)
            {
                extraer(usuario, acompanante);
            }
            else
            {
                extraer(usuario, acompanante);
                if (edad >= 6 && edad <= 10)
                {
                    acompanante = null;
                    // Los acompañantes de los niños de entre 6 y 10 años que
                    // entren en la actividad solos, deberán esperar a que el
                    // niño correspondiente termine su actividad
                    esperaAdultos.insertar(usuario.getAcompanante());
                }
                
                insertar(usuario, acompanante);
            }
            
            dormir(1.0f, 1.5f);
        }
        
        return usuario;
    }
}
