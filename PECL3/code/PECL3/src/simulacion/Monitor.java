package simulacion;

import static simulacion.Utils.azar;
import static simulacion.Utils.dormir_ms;

// El parque acuático tendrá 8 monitores (modelados como hilos) que controlan la
// edad de los usuarios que quieren acceder a cada actividad: uno para el
// vestuario, uno para la piscina de olas, otro para la piscina de niños, otro
// para las tumbonas, tres para los toboganes y uno para la piscina grande.
public class Monitor extends Persona
{
    private final Zona zona;
    private Usuario usuario;
    
    public Monitor(int id, Zona zona)
    {
        super(id, azar(18, 50));
        
        this.zona = zona;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            zona.comprobarEdad();
            
            // Para que no este siempre en ejecución
            // y los 8 hilos usando la CPU en un while true,
            // se duerme unos 16 ms
            dormir_ms(16);
        }
    }
    
    // Para mostrarlo en el control de acceso de cada actividad.
    public String mostrarIdentificador()
    {        
        int id = getIdentificador();
        int edad = getEdad();
            
        return "ID" + id + "-" + edad;     
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }
    
    public Usuario getUsuario()
    {
        return usuario;
    }
}
