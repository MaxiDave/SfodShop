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
            any int,
            PRIMARY KEY ( codi ) 
    )

*/

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
    cac[12]= any
*/
public class ProducteElectronic extends Producte{
    private List<String> cac= new ArrayList<>();
    private String marca;
    
    public ProducteElectronic(Integer EBAN, String codi, String descripcio, List cac, String marca){
        super(codi, EBAN, descripcio);
        this.cac= cac;
        this.marca= marca;
    }
}
