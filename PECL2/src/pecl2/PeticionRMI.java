package pecl2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// La clase PeticionRMI usada como objeto remoto en RMI
public class PeticionRMI extends UnicastRemoteObject implements InterfacePeticion
{
    private Paso paso;
   
    public PeticionRMI(Paso paso) throws RemoteException
    {
        this.paso = paso;
    }
    
    @Override
    public void reanudar() throws RemoteException
    {
        paso.abrir();
    }
    
    @Override
    public void detener() throws RemoteException
    {
        paso.cerrar();
    }
} 
