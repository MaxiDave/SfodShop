package sfod;

public class LiniaCompra {
    private String codiProducte;
    private Double PVC;
    private Double descompte;
    private Double PFVC;
    private Integer unitats;
    private Double PT;
    
    public LiniaCompra(){
        
    }
    
    public void setCodiProducte(String codi){
        codiProducte= codi;
    }
    
    public LiniaCompra(String codiProducte, Double PVC, Integer unitats, Double descompte){
        this.codiProducte= codiProducte;
        this.PVC= PVC;
        this.unitats= unitats;
        this.descompte= descompte;
        this.PFVC= PVC-(PVC*descompte/100);
        this.PT= PFVC*unitats;
    }
    
    public void modificarPVC(Double nouPVC){
        this.PVC= nouPVC;
        this.PFVC= PVC-(PVC*descompte/100);
        this.PT= PFVC*unitats;
    }
    
    public void modificarDescompte(Double nouDescompte){
        this.descompte= nouDescompte;
        this.PFVC= PVC-(PVC*nouDescompte/100);
        this.PT= PFVC*unitats;
    }
    
    public void modificarUnitats(Integer novesUnitats){
        this.unitats= novesUnitats;
        this.PT= PFVC*novesUnitats;
    }
    
    public String getCodiProducte(){
        return codiProducte;
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
}