//sfod

/**
 * @file Compra.java
 * @author David Martínez, MaxiDave13
 * @version 1.0 Alpha
 * @date 9-2017
 * @warning --
 * @brief Classe Compra: Classe que conté una compra feta per un venedor a un proveidor amb les seves línies de comanda
 * @copyright Public License
 */

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
        PRIMARY KEY ( numC ),
        FOREIGN KEY (numProv) REFERENCES Proveidors(numProv),
        FOREIGN KEY (numVen) REFERENCES Venedors(numVen)
    )
*/

/**
 * DESCRIPCIÓ GENERAL 
 * @brief Representa una compra de productes per un venedor a un proveidor
 */

public class Compra {
    
    //ATRIBUTS-------------------------------------------------------------------------
    private long num;
    private String proveidor;
    private String venedor;
    private Double importTotal;
    private List<LiniaCompra> items;
    private LocalDate data;
    
    //MÈTODES PÚBLICS------------------------------------------------------------------
    /**
     * @pre --
     * @post Constructor d'una compra a partir d'un proveidor, un venedor i una data
     */
    public Compra(String proveidor, String venedor, LocalDate data){
        this.proveidor= proveidor;
        this.venedor= venedor;
        this.importTotal= (double)0;
        items= new ArrayList<>();
        this.data= data;
    }
    
    /**
     * @pre --
     * @post Retorna cert si la compra és vàlida
     */
    public Boolean valida(){
        return(Operacions.valid(proveidor, 25) && Operacions.valid(venedor, 55));
    }
    
    /**
     * @pre --
     * @post Retorna cert si la compra conté línies de compra
     */
    public Boolean teLiniesCompra(){
        return !items.isEmpty();
    }
    
    /**
     * @pre --
     * @post Afegeix un número a la compra
     */
    public void setNum(long num){
        this.num= num;
    }
    
    /**
     * @pre --
     * @post Afegeix l'import total a la compra
     */
    public void setImportTotal(Double importTotal){
        this.importTotal= importTotal;
    }
    
    /**
     * @pre --
     * @post Afegeix la línia compra "item" a la llista de LiniaCompra de Compra
     */
    public void afegirLiniaCompra(LiniaCompra item){
        items.add(item);
        importTotal+= item.getPT();
    }
    
    /**
     * @pre --
     * @post Elimina la LiniaCompra "item" de la llista de línies de compra de Compra
     */
    public void treureLiniaCompra(LiniaCompra item){
        importTotal-= item.getPT();
        items.remove(item);
    }
    
    /**
     * @pre --
     * @post Retorna el número de compra de Compra
     */
    public long getNum(){
        return num;
    }
    
    /**
     * @pre --
     * @post Retorna el número del proveïdor de la Compra
     */
    public Integer getNumProv(){
        return Integer.parseInt(proveidor.substring(0, 1));
    }
    
    /**
     * @pre --
     * @post Retorna el proveidor de la Compra
     */
    public String getProveidor(){
        return proveidor;
    }
    
    /**
     * @pre --
     * @post Retorna el número de venedor que ha realitzat la Compra
     */
    public Integer getNumVen(){
        return Integer.parseInt(venedor.substring(0, 1));
    }
    
    /**
     * @pre --
     * @post Retorna el venedor que ha realitzat la Compra
     */
    public String getVenedor(){
        return venedor;
    }
    
    /**
     * @pre --
     * @post Retorna l'import total de la Compra
     */
    public Double getImportTotal(){
        return importTotal;
    }
    
    /**
     * @pre --
     * @post Retorna la data en què s'ha realitzat la compra en format LocalDate
     */
    public LocalDate getData(){
        return data;
    }
    
    /**
     * @pre --
     * @post Retorna el número de referències (LiniaCompra) de la Compra
     */
    public Integer getNumRefs(){
        return items.size();
    }
    
    /**
     * @pre --
     * @post Retorna un iterador a les LiniaCompra de Compra
     */
    public Iterator<LiniaCompra> getIteradorLC(){
        return items.iterator();
    }
}
/*
    ESTRUCTURA SQL PAISOS

    CREATE TABLE Paisos (
        codi varchar(2) NOT NULL default '',
        nomPais varchar(100) NOT NULL default '',
        PRIMARY KEY (codi)
    )
*/