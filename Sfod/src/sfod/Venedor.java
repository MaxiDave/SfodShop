package sfod;

/*
    ESTRUCTURA SQL
    
    CREATE TABLE Venedors (
            numVen int(3) not null, 
            nom varchar(20),
            cog1 varchar(15),
            cog2 varchar(15),
            tractament varchar(4),
            telefon int(15),
            email varchar(35),
            direccio varchar(40),
            codiPostal int(5),
            poblacio varchar(20),
            provincia varchar(20),
            codiPais varchar(2),
            infoAdd varchar(100),
            PRIMARY KEY ( numVen ) 
    )

    ALTER TABLE Venedors ADD FOREIGN KEY (codiPais) REFERENCES paisos(codiPais) 
*/

public class Venedor {
    private final Integer num;
    private final String nom;
    private final String cog1;
    private final String cog2;
    private final String tractament;
    private final Integer telefon;
    private final String email;
    private final String direccio;
    private final Integer codiPostal;
    private final String poblacio;
    private final String provincia;
    private final String codiPais;
    private final String nomPais;
    private final String informacioAddicional;
    
    public Venedor(Integer num, String nom, String cog1, String cog2, String tractament, Integer telefon, String email, String direccio, Integer codiPostal, String poblacio, String provincia, String codiPais, String nomPais, String informacioAddicional){
        this.num= num;
        this.nom= nom;
        this.cog1= cog1;
        this.cog2= cog2;
        this.tractament= tractament;
        this.telefon= telefon;
        this.email= email;
        this.direccio= direccio;
        this.codiPostal= codiPostal;
        this.poblacio= poblacio;
        this.provincia= provincia;
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
    
    public String getCog1(){
        return cog1;
    }
    
    public String getCog2(){
        return cog2;
    }
    
    public String getTractament(){
        return tractament;
    }
    
    public Integer getTelefon(){
        return telefon;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getDireccio(){
        return direccio;
    }
    
    public Integer getCodiPostal(){
        return codiPostal;
    }
    
    public String getPoblacio(){
        return poblacio;
    }
    
    public String getProvincia(){
        return provincia;
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
        return(Operacions.valid(Double.valueOf(num),3) && Operacions.valid(nom,20) && Operacions.valid(cog1,15) && Operacions.valid(cog2,15) && 
                Operacions.valid(tractament, 4) && email.length() <= 35 && Operacions.valid(Double.valueOf(telefon), 15) && 
                Operacions.valid(direccio, 40) && Operacions.valid(provincia, 20) && Operacions.valid(Double.valueOf(codiPostal), 5) && 
                Operacions.valid(poblacio, 20) && Operacions.valid(codiPais, 2) && Operacions.valid(nomPais, 100) && informacioAddicional.length() <= 100);
    }
}
    