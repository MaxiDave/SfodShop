package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE Productes (
            codiElem varchar(10) not null,  
            eban int,
            stock int,
            PRIMARY KEY ( codiElem ) 
    )
    ALTER TABLE Productes ADD FOREIGN KEY (codiElem) REFERENCES ElemsVendibles(codi) 
 
*/

public class Producte extends ElemVendible{
    private final Integer EBAN;
    private final Integer stock;
    
    public Producte(String codi, String descripcio, Integer EBAN, Integer stock) {
        super(codi, descripcio);
        this.EBAN= EBAN;
        this.stock= stock;
    }
    
    public Integer getEBAN(){
        return EBAN;
    }
    
    public Integer getStock(){
        return stock;
    }
    
    public Boolean valid(){
        return(super.valid());
    }
}
