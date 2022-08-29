package simulacion;

import javax.swing.JTextField;
import static simulacion.Utils.dormir_ms;

// Simulador es un hilo para que la interfaz no quede bloqueada
public class Simulador extends Thread
{
    // El número de usuarios que el sistema genera es de 5.000.
    private final int totalUsuarios = 5000;
    private final int totalMonitores = 8;
    private final Parque parque;
    private final Paso paso;
    
    public Simulador(JTextField tfColasAfuera[], JTextField tfColasDentro[],
            JTextField tfMonitores[], Paso paso)
    {
        this.paso = paso;
        this.parque = new Parque(tfColasAfuera, tfColasDentro, tfMonitores, paso);
    }
    
    private void generarUsuarios()
    {
        // El sistema generará usuarios con una periodicidad de
        // entre 400 y 700 milisegundos.
        // Del ID 1 al 8 son para los monitores y el resto, del 9 al 5009,
        // para los 5000 usuarios
        for (int i = totalMonitores; i <= (totalUsuarios + totalMonitores); i++)
        {
            Usuario usuario = new Usuario(i + 1, parque);
            if (usuario.necesitaAcompanante()) i++;
            usuario.start();
            dormir_ms(400, 700);
        }
    }

    @Override
    public void run()
    {
        generarUsuarios();
    }    
    
    public Parque getParque()
    {
        return parque;
    }
}
