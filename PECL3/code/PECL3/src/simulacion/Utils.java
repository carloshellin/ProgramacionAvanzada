package simulacion;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils
{
    // Devuelve un entero N al azar al que a <= N <= b
    public static int azar(int a, int b)
    {
        return a + (int)((b - a) * Math.random());
    }
    
    // Devuelve un entero N al azar al que a <= N <= b en double
    public static double azar(double a, double b)
    {
        return a + (b - a) * Math.random();
    }
    
    public static void dormir_ms(int milisegundos)
    {
        try
        {
            sleep(milisegundos);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void dormir_ms(double milisegundos)
    {
        try
        {
            sleep((long) milisegundos);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void dormir(int segundos)
    {
        dormir_ms(segundos * 1000);
    }
    
    public static void dormir(double segundos)
    {
        dormir_ms(segundos * 1000);
    }
    
    // Espera entre a milisegundos y b milisegundos
    public static void dormir_ms(int ams, int bms)
    {
        dormir_ms(azar(ams, bms));
    }
    
    // Espera entre a segundos y b segundos
    public static void dormir(int as, int bs)
    {
        dormir(azar(as, bs));
    }
    
    // Espera entre a segundos y b segundos en double
    public static void dormir(double as, double bs)
    {
        dormir(azar(as, bs));
    }
}
