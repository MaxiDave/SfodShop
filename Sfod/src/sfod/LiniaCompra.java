package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE LiniesCompra (
        numC int not null,
        codiElem varchar(10) not null,
        PVC double(6,2),
        desc double(4,2),
        PFVC double(6,2),
        unitats int,
        PT double,
        PRIMARY KEY ( numC, codiElem )
    )
    ALTER TABLE LiniesCompra ADD FOREIGN KEY (numC) REFERENCES Compres(numC) 
    ALTER TABLE LiniesCompra ADD FOREIGN KEY (codiElem) REFERENCES ElemsVendibles(codi) 
*/

public class LiniaCompra {
    private String codiProducte;
    private String descripcio;
    private Double PVC;
    private Double descompte;
    private Double PFVC= 0.00;
    private Integer unitats;
    private Double PT= 0.00;
    private Boolean empty;
    
    public LiniaCompra(){
        empty= true;
        this.codiProducte= "";
        this.descripcio= "";
        this.PVC= 0.00;
        this.descompte= 0.00;
        this.unitats= 0;
    }
    
    public LiniaCompra(String codiProducte, String descripcio, Double PVC, Integer unitats, Double descompte){
        empty= false;
        this.codiProducte= codiProducte;
        this.descripcio= descripcio;
        this.PVC= PVC;
        this.unitats= unitats;
        this.descompte= descompte;
        this.PFVC= PVC-(PVC*descompte/100);
        this.PT= PFVC*unitats;
    }
    
    public void clear(){
        empty= true;
        codiProducte= "";
        descripcio= "";
        PVC= 0.00;
        descompte= 0.00;
        PFVC= 0.00;
        unitats= 0;
        PT= 0.00;
    }
    
    public Boolean valida(){
        return(Operacions.valid(codiProducte, 10) && descripcio.length() <= 35 && Operacions.valid(PVC, 4) && Operacions.valid(descompte, 2) && Operacions.valid(PFVC, 4) && Operacions.valid(PT, 12));
    }
    
    public Boolean isEmpty(){
        return empty;
    }
    
    public String getCodiProducte(){
        return codiProducte;
    }
    
    public String getDescripcio(){
        return descripcio;
    }
    
    public Double getPVC(){
        return PVC;
    }
    
    public Double getDescompte(){
        return descompte;
    }
    
    public Integer getUnitats(){
        return unitats;
    }
    
    public Double getPFVC(){
        return PFVC;
    }
    
    public Double getPT(){
        return PT;
    }
    
    public void setCodiProducte(String codi){
        if(!codi.isEmpty()){
            if(empty) empty= false;
            codiProducte= codi;
        }
    }
    
    public void setDescripcio(String descripcio){
        if(!descripcio.isEmpty()){
            if(empty) empty= false;
            this.descripcio= descripcio;
        }
    }
    
    public void setPVC(Double nouPVC){
        if(nouPVC != 0){
            if(empty) empty= false;
            this.PVC= nouPVC;
            this.PFVC= PVC-(PVC*(descompte/100));
            this.PT= PFVC*unitats;
        }
    }
    
    public void setDescompte(Double nouDescompte){
        if(nouDescompte != 0){
            if(empty) empty= false;
            this.descompte= nouDescompte;
            this.PFVC= PVC-(PVC*(nouDescompte/100));
            this.PT= PFVC*unitats;
        }
    }
    
    public void setUnitats(Integer novesUnitats){
        if(novesUnitats != 0){
            if(empty) empty= false;
            this.unitats= novesUnitats;
            this.PT= PFVC*novesUnitats;
        }
    }
}