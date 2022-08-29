package pecl2;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz que implementa PeticionRMI para el uso remoto del objeto
public interface InterfacePeticion extends Remote
{    
    public void detener() throws RemoteException;
    public void reanudar() throws RemoteException;
}
