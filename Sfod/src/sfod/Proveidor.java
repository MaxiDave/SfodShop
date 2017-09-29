package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE Proveidors (
            numProv int(3) not null, 
            nomProv varchar(20), 
            esp varchar(50),
            email varchar(40),
            tempsEntr varchar(25),
            infoAdd varchar(100),
            codiPais varchar(2),
            PRIMARY KEY ( numProv ) 
    )
    ALTER TABLE Proveidors ADD FOREIGN KEY (codiPais) REFERENCES paisos(codiPais) 

*/

public class Proveidor {
    private final Integer num;
    private final String nom;
    private final String especialitat;
    private final String email;
    private final String tempsEntrega;
    private final String codiPais;
    private final String nomPais;
    private final String informacioAddicional;
    
    public Proveidor(Integer num, String nom, String especialitat, String email, String tempsEntrega, String codiPais, String nomPais, String informacioAddicional){
        this.num= num;
        this.nom= nom;
        this.especialitat= especialitat;
        this.email= email;
        this.tempsEntrega= tempsEntrega;
        this.codiPais= codiPais;
        this.nomPais= nomPais;
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
    
    public String getCodiPais(){
        return codiPais;
    }
    
    public String getNomPais(){
        return nomPais;
    }
    
    public String getInformacioAddicional(){
        return informacioAddicional;
    }

    public boolean valid(){
        return(Operacions.valid(Double.valueOf(num),3) && Operacions.valid(nom,20) && especialitat.length() <= 50 && 
                email.length() <= 35 && tempsEntrega.length() <= 25 && Operacions.valid(codiPais, 2) && 
                Operacions.valid(nomPais, 100) && informacioAddicional.length() <= 100);
    }
}

