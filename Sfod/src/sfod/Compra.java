package sfod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
    ESTRUCTURA SQL

    CREATE TABLE compra (
        num int not null AUTO_INCREMENT,
        proveidor varchar(25),
        venedor varchar(55),
        importTotal double(5,2),
        data date,
        PRIMARY KEY ( num )
    )
    ALTER TABLE compra AUTO_INCREMENT = 17000000;
*/
public class Compra {
    private Integer num;
    private String proveidor;
    private String venedor;
    private Double importTotal;
    private List<LiniaCompra> items;
    private LocalDate data;
    
    public Compra(String proveidor, String venedor, LocalDate data){
        this.proveidor= proveidor;
        this.venedor= venedor;
        this.importTotal= (double)0;
        items= new ArrayList<>();
        this.data= data;
    }
    
    public void setNum(Integer num){
        this.num= num;
    }
    
    public void setImportTotal(Double importTotal){
        this.importTotal= importTotal;
    }
    
    public void afegirLiniaCompra(LiniaCompra item){
        items.add(item);
        importTotal+= item.getPT();
    }
    
    public void treureLiniaCompra(LiniaCompra item){
        importTotal-= item.getPT();
        items.remove(item);
    }
    
    public Integer getNum(){
        return num;
    }
    
    public String getProveidor(){
        return proveidor;
    }
    
    public String getVenedor(){
        return venedor;
    }
    
    public Double getImportTotal(){
        return importTotal;
    }
    
    public LocalDate getData(){
        return data;
    }
}
/*
CREATE TABLE `paisos` (
`id` int(11) NOT NULL auto_increment,
`codiPais` varchar(2) NOT NULL default '',
`nomPais` varchar(100) NOT NULL default '',
PRIMARY KEY (`id`)
*/