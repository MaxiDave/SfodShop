package sfod;

public abstract class Operacions {
    
    public static Boolean valid(String camp, Integer espai){
        return !camp.isEmpty() && camp.length() <= espai;
    }
    
    public static Boolean valid(Double camp, Integer espai){
        long valor= 1;
        Boolean continuar= true;
        Integer i=0;
        while(continuar && i<espai){
            valor*= 10;
            i++;
            continuar= camp >= valor;
        }
        return !continuar;
    }
}
