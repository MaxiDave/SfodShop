package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE producte (
            codi varchar(10) not null, 
            descripcio varchar(100), 
            eban int,
            esElectronic varchar(1),
            PRIMARY KEY ( codi ) 
    )

*/

public class Producte {
    private final Integer EBAN;
    private final String codi;
    private final String descripcio;
    
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
