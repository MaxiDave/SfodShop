package sfod;

public class ItemProducteElectronic {
    private String titol;
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
}
