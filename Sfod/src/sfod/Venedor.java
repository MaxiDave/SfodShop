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
    
    public Venedor(String num, String nom, String cognom1, String cognom2, String telefon, String email){
        this.num= num;
        this.nom= nom;
        this.cognom1= cognom1;
        this.cognom2= cognom2;
        this.telefon= telefon;
        this.email= email;
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
    
    public void setNum(String num){
        this.num= num;
    }
    
    public void setNom(String nom){
        this.nom= nom;
    }
    
    public void setCognom1(String cognom1){
        this.cognom1= cognom1;
    }
    
    public void setCognom2(String cognom2){
        this.cognom2= cognom2;
    }
    
    public void setTelefon(String telefon){
        this.telefon= telefon;
    }
    
    public void setEmail(String email){
        this.email= email;
    }
    
    public boolean venedorValid(){
        return(num.length() <= 3 && nom.length() <= 15 && cognom1.length() <= 15 && cognom2.length() <= 15 && email.length() <= 35 && telefon.length() <= 15);
    }
}
