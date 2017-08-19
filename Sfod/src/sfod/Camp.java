package sfod;

public class Camp {
    private String nom;
    private String tipus;
    private Integer espai;
    private Boolean potSerNull;
    private Boolean isPrimaryKey;
    private Boolean isForeignKey;
    
    public Camp(String nom, String tipus, Integer espai, Boolean potSerNull, Boolean isPrimaryKey, Boolean isForeignKey){
        this.nom= nom;
        this.tipus= tipus;
        this.espai= espai;
        this.potSerNull= potSerNull;
        this.isPrimaryKey= isPrimaryKey;
        this.isForeignKey= isForeignKey;
    }
    
    public String obtNom(){
        return nom;
    }
    
    public String obtTipus(){
        return tipus;
    }
    
    public Boolean teEspai(){
        return espai==null || espai==0;
    }
    
    public Integer obtEspai(){
        return espai;
    }
    
    public Boolean potSerNull(){
        return potSerNull;
    }
    
    public Boolean isPrimaryKey(){
        return isPrimaryKey;
    }
    
    public Boolean isForeignKey(){
        return isForeignKey;
    }
}
