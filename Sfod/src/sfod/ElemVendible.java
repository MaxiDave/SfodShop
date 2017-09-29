package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE ElemsVendibles (
            codi varchar(10) not null, 
            descripcio varchar(100),
            PRIMARY KEY ( codi ) 
    )

*/

public class ElemVendible {
    private final String codi;
    private final String descripcio;
    
    public ElemVendible(String codi, String descripcio) {
        this.codi= codi;
        this.descripcio= descripcio;
    }
    
    public String getCodi(){
        return codi;
    }
    
    public String getDescripcio(){
        return descripcio;
    }
    
    public Boolean valid(){
        return(Operacions.valid(codi, 10) && descripcio.length() <= 100);
    }
}
