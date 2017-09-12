package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE proveidor (
            num int(3) not null, 
            nom varchar(20), 
            especialitat varchar(50),
            email varchar(35),
            tempsEntrega varchar(25),
            informacioAddicional varchar(100),
            PRIMARY KEY ( num ) 
    )

*/

public class Proveidor {
    private final Integer num;
    private final String nom;
    private final String especialitat;
    private final String email;
    private final String tempsEntrega;
    private final String informacioAddicional;
    
    public Proveidor(Integer num, String nom, String especialitat, String email, String tempsEntrega, String informacioAddicional){
        this.num= num;
        this.nom= nom;
        this.especialitat= especialitat;
        this.email= email;
        this.tempsEntrega= tempsEntrega;
        this.informacioAddicional= informacioAddicional;
    }
    
    public Integer getNum(){
        return num;
    }
    
    public String getNom(){
        return nom;
    }
    
    public String getEspecialitat(){
        return especialitat;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getTempsEntrega(){
        return tempsEntrega;
    }
    
    public String getInformacioAddicional(){
        return informacioAddicional;
    }
    
    public boolean valid(String camp, Integer espai){
        return !camp.isEmpty() && camp.length() <= espai;
    }
    
    public boolean valid(Integer camp, Integer espai){
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
    
    public boolean proveidorValid(){
        return(valid(num,3) && valid(nom,20) && especialitat.length() <= 50 && email.length() <= 35 && tempsEntrega.length() <= 25 && informacioAddicional.length() <= 100);
    }
}

