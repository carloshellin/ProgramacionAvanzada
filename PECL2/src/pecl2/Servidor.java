package pecl2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Servidor que funciona bajo Socket, usa hilo para no interrumpir la interfaz de la Exposición
public class Servidor extends Thread {
    private Paso paso;
    private ServerSocket serverSocket;
    private int num = 0;
    ExecutorService executor = Executors.newFixedThreadPool(10); // Permitir la conexión de hasta un máximo de 10 clientes simultáneamente
                    
    public Servidor(Paso paso)
    {
        this.paso = paso;
        try
        {
            serverSocket = new ServerSocket(5000); // Creamos un ServerSocket en el puerto 5000
        }
        catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void run()
    {
        while (true)
        {
            aceptar();
        }
    }
    
    public void aceptar()
    {
        try
        {
            Socket conexion = serverSocket.accept(); // Esperamos una conexión
            
            num++;
            System.out.println("Conexión nº "+num+" desde: "+conexion.getInetAddress());
            
            Runnable r = new Runnable()
            {
                public void run()                    
                {
                    peticion(conexion);
                }
            };
            executor.execute(r);
        }
        catch (IOException e) { }
    }
     
     public void peticion(Socket conexion)
     {
        try
        {    
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());  // Abrimos el cansl de entrada
            int numActual = num;

            while (!conexion.isClosed()) // Si la conexión no ha sido cerrada, continuar
            {                   
                String mensaje = entrada.readUTF(); //Leemos el mensaje del cliente
                
                System.out.println("Conexión nº "+numActual+". Mensaje recibido: "+mensaje);
                
                if (mensaje.contains("detener"))
                {
                    paso.cerrar();    // Cerramos el paso a los visitantes
                }
                else if (mensaje.contains("reanudar"))
                {
                    paso.abrir();    // Abrimos el paso a los visitantes
                }
                else if (mensaje.contains("cerrar"))
                {
                    num--; // Se cierra la conexión, ahora num se le resta 1
                }
            }
        }
        catch (IOException e) {} 
     }
     
     public void cerrar()
     {
        executor.shutdown(); // Finaliza gradualmente
        try
        {
            try
            {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) // Espera durante 1 minuto
                {
                    executor.shutdownNow(); // Fuerza terminar las tareas
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) // Espera otro minuto
                    {
                        System.err.println("El executor no terminó");
                    }
                }
            } 
            catch (InterruptedException ex)
            {
                executor.shutdown(); // Recancela el executor
                Thread.currentThread().interrupt(); // Preserva el flag de interrupt
            }
            
            
            serverSocket.close(); // Y cerramos la conexión 
        }
        catch (IOException e) {}
     }
}
