package sfodshop;

import java.util.*;

/*  LLEGENDA CARACTERÍSTIQUES
    
    cac[0]= disseny
    cac[1]= cpu
    cac[2]= gpu
    cac[3]= pantalla
    cac[4]= ram
    cac[5]= rom
    cac[6]= camara
    cac[7]= bateria
    cac[8]= sim
    cac[9]= nfc
    cac[10]= usb
    cac[11]= sensors
*/
public class Smartphone extends Producte{
    private List<String> cac= new ArrayList<>();
    
    public Smartphone(Integer EBAN, String codi, String descripcio, List cac){
        super(EBAN, codi, descripcio);
        this.cac= cac;
    }
}
