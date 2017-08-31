package sfod;

/*
    ESTRUCTURA SQL
    
    CREATE TABLE venedor (
            num varchar(3) not null, 
            nom varchar(15), 
            cognom1 varchar(15),
            cognom2 varchar(15),
            telefon varchar(15),
            email varchar(35),
            PRIMARY KEY ( num ) 
    )
*/

public class Venedor {
    private String num;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String telefon;
    private String email;
    private String direccio;
    private String provincia;
    private String codiPais;
    private String nomPais;
    private String informacioAddicional;
    
    public Venedor(String num, String nom, String cognom1, String cognom2, String telefon, String email, String direccio, String provincia, String codiPais, String nomPais, String informacioAddicional){
        this.num= num;
        this.nom= nom;
        this.cognom1= cognom1;
        this.cognom2= cognom2;
        this.telefon= telefon;
        this.email= email;
        this.direccio= direccio;
        this.provincia= provincia;
        this.codiPais= codiPais;
        this.nomPais= nomPais;
        this.informacioAddicional= informacioAddicional;
    }
    
    public String getNum(){
        return num;
    }
    
    public String getNom(){
        return nom;
    }
    
    public String getCognom1(){
        return cognom1;
    }
    
    public String getCognom2(){
        return cognom2;
    }
    
    public String getTelefon(){
        return telefon;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getDireccio(){
        return direccio;
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
    
    public boolean valid(String camp, Integer espai){
        return !camp.isEmpty() && camp.length() <= espai;
    }
    
    public boolean venedorValid(){
        return(valid(num,3) && valid(nom,15) && valid(cognom1,15) && valid(cognom2,15) && email.length() <= 35 && telefon.length() <= 15);
    }
}
    