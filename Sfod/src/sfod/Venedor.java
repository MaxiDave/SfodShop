package sfod;

/*
    ESTRUCTURA SQL
    
    CREATE TABLE venedor (
            num int(3) not null, 
            nomComplet varchar(50),
            tractament varchar(4),
            telefon int(15),
            email varchar(35),
            direccio varchar(40),
            codiPostal int(5),
            poblacio varchar(20),
            provincia varchar(20),
            codiPais varchar(2),
            nomPais varchar(20),
            informacioAddicional varchar(100),
            PRIMARY KEY ( num ) 
    )
*/

public class Venedor {
    private final Integer num;
    private final String nomComplet;
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
    
    public Venedor(Integer num, String nomComplet, String tractament, Integer telefon, String email, String direccio, Integer codiPostal, String poblacio, String provincia, String codiPais, String nomPais, String informacioAddicional){
        this.num= num;
        this.nomComplet= nomComplet;
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
    
    public String getNomComplet(){
        return nomComplet;
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
    
    public boolean venedorValid(){
        return(valid(num,3) && valid(nomComplet,50) && valid(tractament, 4) && email.length() <= 35 && valid(telefon, 15) && valid(direccio, 40) && valid(provincia, 20) && valid(codiPostal, 5) && valid(poblacio, 20) && valid(codiPais, 2) && valid(nomPais, 20) && informacioAddicional.length() <= 100);
    }
}
    