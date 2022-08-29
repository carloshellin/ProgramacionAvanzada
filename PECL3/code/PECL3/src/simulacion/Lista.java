package simulacion;

import java.util.ArrayList;
import javax.swing.JTextField;

public class Lista
{
    private final ArrayList<Usuario> lista;
    private final JTextField tf;
    
    public Lista(JTextField tf)
    {
        this.tf = tf;
        this.lista = new ArrayList<>();
    }
    
    public Lista()
    {
        this(null);
    }

    public synchronized void insertar(Usuario usuario)
    {
        lista.add(usuario);
        imprimir();
    }
    
    public synchronized void extraer(Usuario usuario)
    {   
        lista.remove(usuario);
        imprimir();
    }
    
    public void limpiar()
    {
        for (int i = 0; i < tamano(); i++)
        {
            if (lista.get(i) == null)
            {
                lista.remove(i);
            }
        }
    }
    
    public Usuario obtener(int index)
    {
        return lista.get(index);
    }
    
    public Usuario primero()
    {
        return lista.isEmpty() ? null : lista.get(0);
    }
    
    public int tamano()
    {
        return lista.size();
    }
    
    public boolean contiene(Usuario usuario)
    {
        return lista.contains(usuario);
    }
    
    public void imprimir()
    {
        if (tf != null)
        {
            String mensaje = "";
            
            for (int i = 0; i < tamano(); i++)
            {
                Usuario usuario = obtener(i);
                if (usuario != null)
                {
                    mensaje = mensaje + usuario.mostrarIdentificador() + " ";
                }
            }
            
            tf.setText(mensaje);   
        }
    }
    
    public Usuario buscarUsuario(int id)
    {
        Usuario usuario = null;
        boolean encontrado = false;
        for(int i = 0;i< tamano() && !encontrado; i++)
        {
            if(obtener(i).getIdentificador() == id){
                usuario = obtener(i);
                encontrado = true;
            }
        }
        return usuario;
    }
}
