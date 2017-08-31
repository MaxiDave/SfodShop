package sfod;

public class ElementCercable {
    private final String principal;
    private final String secundari;
    
    public ElementCercable(String principal, String secundari){
        this.principal= principal;
        this.secundari= secundari;
    }
    
    public String getPrincipal(){
        return principal;
    }
    
    public String getSecundari(){
        return secundari;
    }
}
