/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JTextField;

        
/**
 *
 * @author dferr
 */
public class Cliente 
{
    private Socket cliente; 
    private DataInputStream entrada;
    private DataOutputStream salida;
    private JTextField [] arraycUbicacion;
    private JTextField []arraycNumMenores;
    private JTextField []arraycToboganes;
    private JTextField [] arraycAforo;
    
    public Cliente(JTextField [] arraycUbicacion,JTextField []arraycNumMenores,JTextField []arraycToboganes,JTextField [] arraycAforo) throws IOException
    {
        cliente = new Socket(InetAddress.getLocalHost(),5000);
        
        this.arraycUbicacion = arraycUbicacion;
        this.arraycNumMenores = arraycNumMenores;
        this.arraycToboganes = arraycToboganes;
        this.arraycAforo = arraycAforo;
        
        Thread thConexion = new Thread(new Comunicacion());
	thConexion.start();
    }
    
    public enum MensajeControl{
        UBICACION, MENORES, TOBOGANES, AFORO
    }
    
    private class Comunicacion implements Runnable
    {
        @Override
        public void run() 
        {
            try 
            {
                entrada = new DataInputStream(cliente.getInputStream());
                salida = new DataOutputStream(cliente.getOutputStream());
                String mensaje;
                while(true)
                {
                    mensaje = entrada.readUTF();
                    recibirMensaje(mensaje);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    public void enviarMensaje(String mensaje) throws IOException
    {
        salida.writeUTF(mensaje);
    }
    
    public void escribirDatosControl(JTextField [] tf, String [] datos)
    {
        for(int i = 0; i < tf.length;i++)
        {
            tf[i].setText(datos[i+1]);
        }
    }
    
    public void recibirMensaje(String mensaje)
    {
        String [] arrayM = devolverMensajeArray(mensaje);
        switch(MensajeControl.values()[Integer.parseInt(arrayM[0])]){
            case UBICACION:
                escribirDatosControl(arraycUbicacion, arrayM);
                break;
            case MENORES: 
                escribirDatosControl(arraycNumMenores, arrayM);
                break;
            case TOBOGANES: 
                escribirDatosControl(arraycToboganes, arrayM);
                break;
            case AFORO:
                escribirDatosControl(arraycAforo, arrayM);
                break;
            default: 
                break;
        }
    }

    public String [] devolverMensajeArray(String mensaje)
    {
        String [] arrayMensaje = null;
        return arrayMensaje = mensaje.split(",");
    }
    
}
