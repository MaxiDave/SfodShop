package sfod;

import java.util.*;

/*
    ESTRUCTURA SQL

    CREATE TABLE ProductesElects (
            codiP varchar(10) not null,
            marca varchar(15),
            any varchar(4),
            disseny varchar(20),
            cpu varchar(20),
            gpu varchar(20),
            pantalla varchar(20),
            ram varchar(20),
            rom varchar(20),
            camara varchar(20),
            bateria varchar(10),
            sim varchar(20),
            nfc varchar(10),
            usb varchar(10),
            sensors varchar(40),
            PRIMARY KEY ( codiP ) 
    )
    ALTER TABLE ProductesElects ADD FOREIGN KEY (codiP) REFERENCES Productes(codiElem)

"marca","any","disseny","cpu","gpu","pantalla","ram","rom","camara","bateria","sim","nfc","usb","sensors"
*/

public class ProducteElectronic extends Producte{
    private List<ItemProducteElectronic> cac= new ArrayList<>();
    
    public ProducteElectronic(String codi, String descripcio, Integer EBAN, Integer stock, List<ItemProducteElectronic> cac){
        super(codi, descripcio, EBAN, stock);
        this.cac= cac;
    }
    
    public Boolean valid(){
        if(super.valid()){
            Integer[] valorsItems= new Integer[]{15, 4, 20, 20, 20, 20, 20, 20, 20, 10, 20, 2, 10, 40};
            Boolean seguir= true;
            int i= 0;
            while(seguir && i<=13){
                if(cac.get(i).getInfo().length() > valorsItems[i]) seguir= false;
                i++;
            }
            return seguir;
        }
        else return false;
    }
    
    public Iterator<ItemProducteElectronic> getItemsIterator(){
        return cac.iterator();
    }
}