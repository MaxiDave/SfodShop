package sfod;

import java.util.*;

/*
    ESTRUCTURA SQL

    CREATE TABLE producteElectronic (
            codi varchar(10) not null, 
            disseny varchar(20),
            cpu varchar(20),
            gpu varchar(20),
            pantalla varchar(20),
            ram varchar(20),
            rom varchar(20),
            camara varchar(20),
            bateria varchar(10),
            sim varchar(20),
            nfc varchar(2),
            usb varchar(10),
            sensors varchar(30),
            any varchar(4),
            PRIMARY KEY ( codi ) 
    )

*/

public class ProducteElectronic extends Producte{
    private List<ItemProducteElectronic> cac= new ArrayList<>();
    
    public ProducteElectronic(String codi, Integer EBAN, String descripcio, List<ItemProducteElectronic> cac){
        super(codi, EBAN, descripcio);
        this.cac= cac;
    }
    
    public Iterator<ItemProducteElectronic> getItemsIterator(){
        return cac.iterator();
    }
}