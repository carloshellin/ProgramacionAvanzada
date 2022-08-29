package pecl2;

// Visitantes que usan como hilos, antes de realizar alguna acción mira si tienen paso
public class Visitante extends Thread
{
    Exposicion expo;
    private Paso paso;
    
    public Visitante(int id, Exposicion expo, Paso paso)
    {
        super.setName(String.valueOf(id));
        this.expo = expo;
        this.paso = paso;
    }
    
    public void run()
    {      
        try
        {
            sleep((int)(3000*Math.random()));
        } catch (InterruptedException e){ }

        paso.mirar(); // Mira si tiene paso
        
        expo.entrar(this); //Entra en la exposición, si hay hueco; y sino espera en la cola
        paso.mirarAbierto(); // Mira si esta abierto la exposición
        
        paso.mirar(); // Mira si tiene paso
        expo.mirar(this); //Está un tiempo dentro de la exposición
        
        paso.mirar(); // Mira si tiene paso
        expo.salir(this); //Sale de la exposición
    }
}
