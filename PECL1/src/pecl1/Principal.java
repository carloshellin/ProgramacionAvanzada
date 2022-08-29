package pecl1;

public class Principal 
{
    public static void main(String[] s)
    {
        Buffer buffer = new Buffer();
        Productor A = new Productor("A", buffer);
        Productor B = new Productor("B", buffer);
        Productor C = new Productor("C", buffer);
        Consumidor jose = new Consumidor("José", buffer);
        Consumidor ana = new Consumidor("Ana", buffer);
        Consumidor maria = new Consumidor("María", buffer);
        
        A.start();
        B.start();
        C.start();
        
        jose.start();
        ana.start();
        maria.start();
    }
}