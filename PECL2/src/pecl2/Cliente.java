package pecl2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

// Cliente que envia mensajes a través de Socket al servidor
public class Cliente
{
    private Socket socket;
    private DataOutputStream salida;
    
    public Cliente()
    {
        try
        {
            socket = new Socket(InetAddress.getLocalHost(), 5000);   // Creamos el socket para conectarnos al puerto 5000 del servidor
            salida = new DataOutputStream(socket.getOutputStream()); // Creamos el canal de salida
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void enviar(String mensaje)
    {
        try
        {
            salida.writeUTF(mensaje); // Enviamos un mensaje al servidor
            System.out.println("Mi mensaje es: " + mensaje);
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void cerrar()
    {
        try
        {
            socket.close(); // Cerramos la conexión
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
