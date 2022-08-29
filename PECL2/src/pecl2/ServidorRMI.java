package pecl2;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// ServidorRMI que registra el objeto
// El rmiregistry se arranca en el mismo ordenador en el que se ejecuta ProgPrincipal
// por lo que no es necesario ejecutar rmiregistry.exe y tampoco necesitamos modificar CLASSPATH
public class ServidorRMI 
{
    public ServidorRMI(Paso paso)
    {
        try
        {
            // Para mantener un máximo de 10 conexiones simultáneamente en RMI se podría utilizar la siguiente
            // opción comentada, pero debido a que provoca excepciones aleatorias en ModControl, se deja comentada
            // por el buen funcionamiento del programa
            //System.getProperties().put("sun.rmi.transport.tcp.maxConnectionThreads", "10");
            
            PeticionRMI obj = new PeticionRMI(paso); //Crea una instancia del objeto a registrar
            Registry registry = LocateRegistry.createRegistry(1099); // rmiregistry local en el puerto 1099
            Naming.rebind("//localhost/ObjetoPeticion",obj);   // rebind sólo funciona sobre una url del equipo local
            System.out.println("El Objeto Peticion se ha quedado registrado");
        }
        catch (Exception e)
        {
            System.out.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
