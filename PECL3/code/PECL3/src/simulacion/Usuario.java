package simulacion;

import simulacion.Parque.Actividad;
import static simulacion.Utils.azar;

// Los usuarios deberán ser modelados como hilos.
public class Usuario extends Persona
{
    private Usuario acompanante;
    
    // Para la piscina de olas y poder diferenciarlo del acompañanate
    private Usuario pareja;
    
    private final Parque parque;
    private int actividadesHechas;
    private int actividades;
    
    // Para comprobar que no se quede en una zona cuando están realizando otra
    private Zona zonaActual;
    
    // Para la interfaz de remoto
    private String ubicacion;
    
    public Usuario(int id, Parque parque)
    {
        // Antes de iniciar sus actividades, cada usuario necesita establecer un
        // ID numérico y la edad (por ejemplo: ID1-17, ID2-23, etc.),
        // entre 1 y 50 años, para usarla en el control de
        // acceso de cada actividad.
        super(id, azar(1, 50));
        
        this.acompanante = null;
        this.parque = parque;
        this.actividadesHechas = 0; 
        this.ubicacion = "desconocida";
        
        // Si el usuario es un niño de 1 a 10 años,
        // en el  momento de la creación de éste, se creará automáticamente
        // un usuario acompañante con una edad mayor de 18 años, y en ambos
        // usuarios se concatenará el ID numérico del usuario
        // asociado(ID3-9-4, ID4-23-3).
        if (necesitaAcompanante())
        {
            acompanante = new Usuario(id + 1, parque, this);
        }
    }
    
    public Usuario(int id, Parque parque, Usuario acompanante)
    {
        super(id, azar(18, 50));

        this.parque = parque;
        this.acompanante = acompanante;
    }

    @Override
    public void run()
    {
        // - El usuario que llega al parque intenta acceder al mismo, y se
        //   coloca en la cola si está lleno.

        parque.entrar(this);

        // - El usuario estará en el parque realizando entre 5 y 15 actividades.
        for (actividades = azar(5, 15); actividades != 0; actividades--)
        {

            Actividad actividad = elegirActividad();
            if (!parque.realizaActividad(this, actividad))
            {
                actividades++;
            }else{
                actividadesHechas++;
            }
        }
        // - Una vez terminadas las actividades, el usuario pasará otra vez por
        //   el vestuario y abandonará el parque.
        parque.salir(this);

    }
    
    private Actividad elegirActividad()
    {
        int valor = azar(Actividad.PISCINAOLAS.ordinal(),
                         Actividad.TOBOGANC.ordinal() + 1);
        
        return Actividad.values()[valor];
    }
    
    public final boolean necesitaAcompanante()
    {
        int edad = getEdad();
        return (edad >= 1 && edad <= 10);
    }
     
    public String getUbicacion()
    {
        return ubicacion;
    }
    
    public void setUbicacion(String ubi)
    {
       ubicacion = ubi; 
    }

    public int getNumActividadesHechas()
    {
        return actividadesHechas;
    }
    
    public void aumentarNumActividadesHechas()
    {
        actividadesHechas++;
    }
    
    public boolean esAdulto()
    {
        int edad = getEdad();
        return edad >= 18;
    }
    
    public void setPareja(Usuario pareja)
    {
        this.pareja = pareja;
    }
    
    public Usuario getPareja()
    {
        return pareja;
    }
    
    public Usuario getAcompanante()
    {
        return acompanante;
    }
    
    // Para mostrarlo en el control de acceso de cada actividad.
    public String mostrarIdentificador()
    {        
        int id = getIdentificador();
        int edad = getEdad();
        
        // Si tiene acompañante, en ambos usuarios se concatenará el ID numérico
        // del usuario asociado(ID3-9-4, ID4-23-3).
        String concatenar = acompanante != null ? "-" + 
                acompanante.getIdentificador() : "";
        
        return "ID" + id + "-" + edad + concatenar;     
    }

    public void setZonaActual(Zona zonaActual)
    {
        this.zonaActual = zonaActual;
    }
    
    public Zona getZonaActual()
    {
        return zonaActual;
    }
}
