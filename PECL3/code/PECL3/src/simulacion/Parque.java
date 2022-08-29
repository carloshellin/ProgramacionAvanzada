package simulacion;

import interfaz.Control.ColasDentro;
import interfaz.Control.ColasAfuera;
import interfaz.Control.Monitores;
import javax.swing.JTextField;
import simulacion.actividades.PiscinaGrande;
import simulacion.actividades.PiscinaNinos;
import simulacion.actividades.PiscinaOlas;
import simulacion.actividades.Tobogan;
import simulacion.actividades.Tobogan.Tipo;
import simulacion.actividades.Tumbonas;
import simulacion.actividades.Vestuario;

public class Parque extends Zona
{
    // A pesar de ser un atributo se deja en public cualquier enum
    // en todo el proyecto porque con enum no hay getters o setters
    public enum Actividad
    {
        PISCINAOLAS, PISCINANINOS, TUMBONAS, PISCINAGRANDE,
        TOBOGANA, TOBOGANB, TOBOGANC;
    }

    private final Vestuario vestuario;
    private final Zona actividades[];  
    private int numMenores;
	private final Lista listaUsuarios;
    
    public Parque(JTextField tfColasAfuera[], JTextField tfColasDentro[],
            JTextField tfMonitores[], Paso paso)
    {
        // Tiene una única entrada con una cola.
        // La capacidad del parque es de 100 usuarios.
        // El parque tiene un VESTUARIO de uso obligatorio a la entrada,
        // y 5 zonas de actividades: Las actividades son: PISCINA DE OLAS,
        // PISCINA DE NIÑOS, TUMBONAS, PISCINA GRANDE y TOBOGANES   
        super(100, tfColasAfuera[ColasAfuera.PARQUE.ordinal()], paso);
        this.numMenores = 0;
        this.listaUsuarios = new Lista();
        
        this.vestuario = new Vestuario(
                tfColasAfuera[ColasAfuera.VESTUARIOS.ordinal()], 
                tfColasDentro[ColasDentro.VESTUARIOS.ordinal()],
                tfMonitores[Monitores.VESTUARIOS.ordinal()], paso);   
        
        PiscinaGrande piscinaGrande = 
                new PiscinaGrande(tfColasAfuera[ColasAfuera.PISCINAGRANDE.ordinal()],
                    tfColasDentro[ColasDentro.PISCINAGRANDE.ordinal()],
                    tfMonitores[Monitores.PISCINAGRANDE.ordinal()], paso);
        
        this.actividades = new Zona[] {
            new PiscinaOlas(tfColasAfuera[ColasAfuera.PISCINAOLAS.ordinal()],
                    tfColasDentro[ColasDentro.PISCINAOLAS.ordinal()],
                    tfMonitores[Monitores.PISCINAOLAS.ordinal()], paso),
            
            new PiscinaNinos(tfColasAfuera[ColasAfuera.PISCINANINOS.ordinal()],
                    tfColasDentro[ColasDentro.PISCINANINOS.ordinal()],
                    tfColasDentro[ColasDentro.ESPERADULTOS.ordinal()],
                    tfMonitores[Monitores.PISCINANINOS.ordinal()], paso),
            
            new Tumbonas(tfColasAfuera[ColasAfuera.TUMBONAS.ordinal()],
                    tfColasDentro[ColasDentro.TUMBONAS.ordinal()],
                    tfMonitores[Monitores.TUMBONAS.ordinal()], paso), 
            
            piscinaGrande,
            
            new Tobogan(Tipo.A, piscinaGrande, 6,
                    tfColasAfuera[ColasAfuera.TOBOGANA.ordinal()],
                    tfColasDentro[ColasDentro.TOBOGANA.ordinal()],
                    tfMonitores[Monitores.TOBOGANA.ordinal()], paso),
            
            new Tobogan(Tipo.B, piscinaGrande, 7,
                    tfColasAfuera[ColasAfuera.TOBOGANB.ordinal()],
                    tfColasDentro[ColasDentro.TOBOGANB.ordinal()],
                    tfMonitores[Monitores.TOBOGANB.ordinal()], paso),
            
            new Tobogan(Tipo.C, piscinaGrande, 8,
                    tfColasAfuera[ColasAfuera.TOBOGANC.ordinal()],
                    tfColasDentro[ColasDentro.TOBOGANC.ordinal()],
                    tfMonitores[Monitores.TOBOGANC.ordinal()], paso)};
    }

    @Override
    public void entrar(Usuario usuario)
    {
	listaUsuarios.insertar(usuario);
        super.entrar(usuario);
        // - Una vez que se accede al parque, el usuario debe pasar
        //   obligatoriamente por el vestuario para luego ir a las distintas
        //   actividades.
        vestuario.entrar(usuario);
        //listaUsuarios.insertar(usuario);
        if(!usuario.esAdulto()) numMenores++;
        vestuario.salir(usuario);
    }
	public Zona [] getZonas()
    {
        return actividades; 
    }

    @Override
    public void salir(Usuario usuario)
    {
        vestuario.entrar(usuario);
        vestuario.salir(usuario);   
        usuario.setUbicacion("Terminó");
        super.salir(usuario);
        
        if(!usuario.esAdulto()) numMenores--;
    }    

    public boolean realizaActividad(Usuario usuario, Actividad actividad)
    {
        usuario.setZonaActual(actividades[actividad.ordinal()]);
        actividades[actividad.ordinal()].entrar(usuario);
        boolean realizada = actividades[actividad.ordinal()].accion(usuario);
        actividades[actividad.ordinal()].salir(usuario);
        
        // Si ha realizado con existo alguna actividad en el tobogán, se le mete
        // directamente a la piscina grande
        if (realizada && (actividad == Actividad.TOBOGANA
                || actividad == Actividad.TOBOGANB
                || actividad == Actividad.TOBOGANC))
        {
            int piscinaGrande = Actividad.PISCINAGRANDE.ordinal();
            actividades[piscinaGrande].
                    insertar(usuario, usuario.getAcompanante());
            actividades[piscinaGrande].accion(usuario);
            actividades[piscinaGrande].salir(usuario);
        }
       
        return realizada;
    }
        
    public int getNumMenores()
    {
        return numMenores;
    }

    public Lista getListaUsuarios()
    {
        return listaUsuarios;
    }
    
    public Vestuario getVestuario()
    {
        return vestuario;
    }
    
}
