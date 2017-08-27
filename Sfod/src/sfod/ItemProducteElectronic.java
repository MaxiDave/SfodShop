package sfod;

public class ItemProducteElectronic {
    private final String titol;
    private String info;
    
    public ItemProducteElectronic(String titol, String info){
        this.titol= titol;
        this.info= info;
    }
    
    public String getTitol(){
        return titol;
    }
    
    public String getInfo(){
        return info;
    }
    
    public void setInfo(String info){
        this.info= info;
    }
    
    @Override
    public String toString(){
        return info;
    }
}