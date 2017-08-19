package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE producte (
            codi varchar(10) not null, 
            descripcio varchar(100), 
            eban int, 
            PRIMARY KEY ( codi ) 
    )

*/

public class Producte {
    private Integer EBAN;
    private String codi;
    private String descripcio;
    
    public Producte(String codi, Integer EBAN, String descripcio) {
        this.EBAN= EBAN;
        this.codi= codi;
        this.descripcio= descripcio;
    }
    
    public String getCodi(){
        return codi;
    }
    
    public Integer getEBAN(){
        return EBAN;
    }
    
    public String getDescripcio(){
        return descripcio;
    }
}
