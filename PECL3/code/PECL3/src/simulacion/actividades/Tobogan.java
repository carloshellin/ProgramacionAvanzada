package simulacion.actividades;

import javax.swing.JTextField;
import simulacion.Paso;
import simulacion.Usuario;
import static simulacion.Utils.dormir;
import static simulacion.Utils.dormir_ms;
import simulacion.Zona;


public class Tobogan extends Zona
{
    public enum Tipo
    {
        A, B, C
    };
    
    private final Tipo tipo;
    private final PiscinaGrande piscinaGrande;
	private int numUsuarios;
    
    public Tobogan(Tipo tipo, PiscinaGrande piscinaGrande, int id, 
            JTextField tfColaAfuera, JTextField tfColaDentro,
            JTextField tfMonitor, Paso paso)
    {
        // En la actividad de TOBOGANES tenemos tres toboganes, A, B y C,
        // con las siguientes restricciones de edad: A para niños de 11 a
        // 14 años, B para niños de 15 a 17 años y C para usuarios de 18 años 
        // y más. El usuario tarda un tiempo entre 2 y 3 segundos en tirarse
        // por el tobogán. Habrá un monitor en cada tobogán para controlar la
        // edad de cada uno, tardando entre 400 y 500 milisegundos.
        // Cada tobogán tiene una única cola de acceso de tipo FIFO

        super(1, tfColaAfuera, tfColaDentro, id, tfMonitor, paso);
        
        this.tipo = tipo;
        this.piscinaGrande = piscinaGrande;        
        this.numUsuarios = 0;
    }

	@Override
    public void entrar(Usuario usuario) {
        switch(tipo)
        {
            case A:
                usuario.setUbicacion("ToboganA");
                break;
            case B:
                usuario.setUbicacion("ToboganB");
                break;
            case C:
                usuario.setUbicacion("ToboganC");
                break;
            default: 
                break;
        }
        super.entrar(usuario); 
    }
	
    @Override
    public boolean accion(Usuario usuario)
    {
        boolean realizada = false;
        int edad = usuario.getEdad();
        
        switch (tipo)
        {
            case A:
            {
                if (edad >= 11 && edad <= 14)
                {
                    realizada = true;
					numUsuarios++;
                }
            } break;

            case B:
            {
                if (edad >= 15 && edad <= 17)
                {
                    realizada = true;
					numUsuarios++;
                }
            } break;

            case C:
            {
                if (edad >= 18)
                {
                    realizada = true;
					numUsuarios++;
                }
            } break;
        }
        
        if (realizada)
        {
            dormir(2, 3);
        }
        
        return realizada;
    }
    
    @Override
    public Usuario comprobarEdad() 
    {
        Usuario usuario = null;
        
        if (piscinaGrande.getDentro().tamano() < (piscinaGrande.getCapacidad()))
        {
            usuario = super.comprobarEdad();        

            if (usuario != null)
            {
                int edad = usuario.getEdad();
                Usuario acompanante = usuario.getAcompanante();


                extraer(usuario, acompanante);

                switch (tipo)
                {
                    case A:
                    {
                        if (edad >= 11 && edad <= 14)
                        {
                            insertar(usuario, acompanante);
                        }
                    } break;

                    case B:
                    {
                        if (edad >= 15 && edad <= 17)
                        {
                            insertar(usuario, acompanante);
    
                        }
                    } break;

                    case C:
                    {
                        if (edad >= 18)
                        {
                            insertar(usuario, acompanante);
                        }
                    } break;
                }
                
                dormir_ms(400, 500);
            }
        }
    
        return usuario;
    } 
	public int getNumUsuario()
    {
        return numUsuarios;
    }
    
    
}
