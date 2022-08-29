/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import simulacion.Parque;
import simulacion.Parque.Actividad;
import simulacion.Usuario;
import simulacion.Zona;
import simulacion.actividades.PiscinaGrande;
import simulacion.actividades.PiscinaNinos;
import simulacion.actividades.PiscinaOlas;
import simulacion.actividades.Tobogan;
import simulacion.actividades.Tumbonas;
import simulacion.actividades.Vestuario;

 
public class Servidor extends Thread
{
    
   private ServerSocket servidor;
   private Socket conexion; 
   private DataInputStream entrada;
   private DataOutputStream salida;
   ExecutorService executor = Executors.newFixedThreadPool(10);
   private final Parque parque; 
   private final Vestuario vestuario;
   private final PiscinaOlas piscinaOlas;
   private final PiscinaNinos piscinaNinos;
   private final Tumbonas tumbonas;
   private final PiscinaGrande piscinaGrande ;
   private final Tobogan toboganA;
   private final Tobogan toboganB;
   private final Tobogan toboganC;
   private final Zona [] zonas;
   
   public enum MensajeControl{
        UBICACION, MENORES, TOBOGANES, AFORO
   }
   
   public Servidor(Parque parque) throws IOException
   {
        servidor = new ServerSocket(5000); //Creamos un ServerSocket en el Puerto 5000
        this.parque = parque;
        vestuario = parque.getVestuario();
        zonas =parque.getZonas();
        piscinaOlas = (PiscinaOlas) zonas[Actividad.PISCINAOLAS.ordinal()];
        piscinaNinos = (PiscinaNinos) zonas[Actividad.PISCINANINOS.ordinal()];
        tumbonas = (Tumbonas) zonas[Actividad.TUMBONAS.ordinal()];
        piscinaGrande = (PiscinaGrande) zonas[Actividad.PISCINAGRANDE.ordinal()];
        toboganA = (Tobogan) zonas[Actividad.TOBOGANA.ordinal()];
        toboganB = (Tobogan) zonas[Actividad.TOBOGANB.ordinal()];
        toboganC = (Tobogan) zonas[Actividad.TOBOGANC.ordinal()];
   }
   
   public void run()
   {
        while(true)
        {
            try 
            {
                conexion = servidor.accept();
                
                Runnable r = new Runnable()
                {
                    public void run()                    
                    {
                        try 
                        {
                            entrada = new DataInputStream(conexion.getInputStream()); //Abrimos los canales de E/S
                            
                            while(!conexion.isClosed())
                            {
                                String mensaje = entrada.readUTF();
                                tratarMensaje(conexion, mensaje);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                executor.execute(r);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void tratarMensaje(Socket conexion, String mensaje) throws IOException
    {
        salida = new DataOutputStream(conexion.getOutputStream());
        
        String [] mens = mensaje.split(",");
        int totalUsuarioToboganes = 0;
        Usuario usuario;
        switch(MensajeControl.values()[Integer.parseInt(mens[0])])
        {
            case UBICACION:
                usuario = parque.getListaUsuarios().buscarUsuario(Integer.parseInt(mens[1]));
                if(usuario != null){
                    salida.writeUTF("0,"+usuario.mostrarIdentificador()+","+ usuario.getUbicacion()+","+usuario.getNumActividadesHechas());
                }else{
                    salida.writeUTF("0,"+"No encontrado" +","+" "+","+" ");
                }
                break;
            case MENORES:
                salida.writeUTF("1,"+parque.getNumMenores());
                break;
            case TOBOGANES: 
                salida.writeUTF("2," + toboganA.getNumUsuario() + "," + 
                                toboganB.getNumUsuario() + "," + toboganC.getNumUsuario());
                break;
            case AFORO:
                totalUsuarioToboganes = (toboganA.getNumUsuario() + toboganB.getNumUsuario() + toboganC.getNumUsuario());
                
                salida.writeUTF("3," + vestuario.getDentro().tamano() + "," + piscinaOlas.getDentro().tamano() + "," +
                                    piscinaNinos.getDentro().tamano() + "," + tumbonas.getDentro().tamano() + ","  + totalUsuarioToboganes + 
                                    "," + piscinaGrande.getDentro().tamano());
                break;
        }
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
                        executor.shutdown(); 
                        System.err.println("El executor no terminó");
                    }
                }
            } 
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt(); // Preserva el flag de interrupt
            }
            servidor.close(); // Y cerramos la conexión 
        }
        catch (IOException e) {}
     }
}
