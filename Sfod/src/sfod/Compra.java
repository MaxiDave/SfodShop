package sfod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    ESTRUCTURA SQL

    CREATE TABLE Compres (
        numC int not null,
        numProv int(3),
        numVen int(3),
        impTotal double,
        data date,
        PRIMARY KEY ( numC )
    )

    ALTER TABLE Compres ADD FOREIGN KEY (numProv) REFERENCES Proveidors(numProv)
    ALTER TABLE Compres ADD FOREIGN KEY (numVen) REFERENCES Venedors(numVen) 
*/
public class Compra {
    private long num;
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
    
    public Boolean valida(){
        return(Operacions.valid(proveidor, 25) && Operacions.valid(venedor, 55));
    }
    
    public void setNum(long num){
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
    
    public long getNum(){
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
    
    public Integer getNumRefs(){
        return items.size();
    }
    
    public Iterator<LiniaCompra> getIteradorLC(){
        return items.iterator();
    }
}
/*
CREATE TABLE Paisos (
    codi varchar(2) NOT NULL default '',
    nomPais varchar(100) NOT NULL default '',
    PRIMARY KEY (codi)
)
*/