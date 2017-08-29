package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE proveidor (
            num varchar(3) not null, 
            nom varchar(20), 
            especialitat varchar(15),
            email varchar(35),
            tempsEntrega varchar(15),
            PRIMARY KEY ( num ) 
    )

*/

public class Proveidor {
    private String num;
    private String nom;
    private String especialitat;
    private String email;
    private String tempsEntrega;
    
    public Proveidor(String num, String nom, String especialitat, String email, String tempsEntrega){
        this.num= num;
        this.nom= nom;
        this.especialitat= especialitat;
        this.email= email;
        this.tempsEntrega= tempsEntrega;
    }
    
    public String getNum(){
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
    
    public void setNum(String num){
        this.num= num;
    }
    
    public void setNom(String nom){
        this.nom= nom;
    }
    
    public void setEspecialitat(String especialitat){
        this.especialitat= especialitat;
    }
    
    public void setEmail(String email){
        this.email= email;
    }
    
    public void setTempsEntrega(String tempsEntrega){
        this.tempsEntrega= tempsEntrega;
    }
    
    public boolean valid(String camp, Integer espai){
        return !camp.isEmpty() && camp.length() <= espai;
    }
    
    public boolean proveidorValid(){
        return(valid(num,3) && valid(nom,20) && especialitat.length() <= 15 && email.length() <= 35 && tempsEntrega.length() <= 15);
    }
}

