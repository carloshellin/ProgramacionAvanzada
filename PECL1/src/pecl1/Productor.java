package pecl1;

public class Productor extends Thread
{
    private String prefijo;
    private int n = 80;
    private Buffer buffer;
    
    public Productor(String prefijo, Buffer buffer)
    {
        this.prefijo = prefijo;
        this.buffer = buffer;
    }
    
    @Override
    public void run()
    {
        while (n > 0)
        {
            try
            {
                sleep(200 + (int) (600 * Math.random()));  // Espera entre 0.2 y 0,8 seg.
            } catch(InterruptedException e) {}
            
            int numero = (int) (Math.random() * 20);
            
            buffer.introducir(numero);
            System.out.println(prefijo + " genera " + numero);
            
            n--;
        }
    }
}
