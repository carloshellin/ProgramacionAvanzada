package simulacion.actividades;

import javax.swing.JTextField;
import simulacion.Lista;
import simulacion.Paso;
import simulacion.Usuario;
import static simulacion.Utils.azar;
import static simulacion.Utils.dormir;
import simulacion.Zona;

public class PiscinaGrande extends Zona
{  
    public PiscinaGrande(JTextField tfColaAfuera, JTextField tfColaDentro,
            JTextField tfMonitor, Paso paso)
    {
        // En la PISCINA GRANDE, hay una capacidad máxima de 50 usuarios.
        // No hay restricciones de acceso por edad, pero si se alcanza el
        // máximo de capacidad, no podrán entrar nuevos usuarios, ni tampoco
        // por los TOBOGANES, ya que llegan a la misma piscina. Los acompañante
        // de niños consumen un hueco. El monitor vigilará el acceso a la
        // piscina. Tardará 0,5 segundo en comprobar si el usuario puede entrar
        // en la piscina. En el caso de no poder acceder por el exceso de aforo,
        // el monitor, aleatoriamente, elegirá sacar a alguien de la piscina,
        // lo cual le llevará entre 0,5 y 1 segundos.
        // En el caso de usuarios con acompañante ambos saldrán de la piscina
        super(50, tfColaAfuera, tfColaDentro, 5, tfMonitor, paso);
    }

    @Override
    public boolean accion(Usuario usuario)
    {
        dormir(3, 5);
        
        return super.accion(usuario);
    }
	
	@Override
    public void entrar(Usuario usuario) {
        usuario.setUbicacion("Piscina Grande");
        super.entrar(usuario); 
    }

    @Override
    public Usuario comprobarEdad()
    {
        Usuario usuario = super.comprobarEdad();
        
        if (usuario != null)
        {
            Usuario acompanante = usuario.getAcompanante();

            extraer(usuario, acompanante);
            insertar(usuario, acompanante);
            
            dormir(0.5);
        }
        
        Lista dentro = getDentro();
        int dentroTamano =  dentro.tamano();

        if (dentroTamano >= getCapacidad())
        {
            int index = azar(0, (dentroTamano - 1));
            usuario = dentro.obtener(index);
            
            dentro.extraer(usuario);
            dentro.extraer(usuario.getAcompanante());

            dormir(0.5, 1);
        }
        
        return usuario;
    }    
}
