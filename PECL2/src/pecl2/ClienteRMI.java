package pecl2;

import java.rmi.*;

// Cliente que funciona bajo RMI usando la interfaz InterfacePeticion
public class ClienteRMI
{
    private InterfacePeticion obj;
    
    public ClienteRMI()
    {
        try
        {
            obj = (InterfacePeticion) Naming.lookup("//127.0.0.1/ObjetoPeticion"); // Localiza el objeto distribuido
        }
        catch (Exception e)
        {
            System.out.println("Excepción : " + e.getMessage());
            e.printStackTrace();
        }   
    }
    
    public void detener()
    {
        try
        {
            obj.detener();
        }
        catch (Exception e)
        {
            System.out.println("Excepción : " + e.getMessage());
            e.printStackTrace();
        } 
    }
    
    public void reanudar()
    {
        try
        {
            obj.reanudar();
        }
        catch (Exception e)
        {
            System.out.println("Excepción : " + e.getMessage());
            e.printStackTrace();
        } 
    }
}
