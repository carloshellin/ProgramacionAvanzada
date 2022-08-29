package simulacion;

public class Persona extends Thread
{
    final int id;
    private final int edad;
    
    public Persona(int id, int edad)
    {
        this.id = id;
        this.edad = edad;
        
        // Para depurar mejor con muchos hilos
        setName("ID" + id + "-" + edad);
    }
    
    public int getIdentificador()
    {
        return id;
    }
    
    public int getEdad()
    {
        return edad;
    }
}
