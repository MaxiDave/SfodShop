//sfod

/**
 * @file ElemVendible.java
 * @author David Martínez, MaxiDave13
 * @version 1.0 Alpha
 * @date 9-2017
 * @warning --
 * @brief Classe ElemVendible: Classe que conté un element vendible, és a dir, algo que es pot vendre amb codi i descripció
 * @copyright Public License
 */
package sfod;

/*
    ESTRUCTURA SQL

    CREATE TABLE ElemsVendibles (
            codi varchar(10) not null, 
            descripcio varchar(100),
            PRIMARY KEY ( codi ) 
    )

*/

/**
 * DESCRIPCIÓ GENERAL
 * @brief Representa un element que pot ser venut
 */

public class ElemVendible {
    
    //ATRIBUTS-------------------------------------------------------------------------
    private final String codi;
    private final String descripcio;
    
    //MÈTODES PÚBLICS------------------------------------------------------------------
    /**
     * @pre --
     * @post Constructor per defecte. Crea un ElemVendible amb un codi i una descripció
     * @param codi
     * @param descripcio 
     */
    public ElemVendible(String codi, String descripcio) {
        this.codi= codi;
        this.descripcio= descripcio;
    }
    
    /**
     * @pre --
     * @post Retorna el codi de l'ElemVendible
     * @return codi 
     */
    public String getCodi(){
        return codi;
    }
    
    /**
     * @pre --
     * @post Retorna la descripció de l'ElemVendible
     * @return descripcio 
     */
    public String getDescripcio(){
        return descripcio;
    }
    
    /**
     * @pre --
     * @post Retorna cert si l'ElemVendible és vàlid, és a dir, que els camps no superin la longitud màxima i el codi no sigui buit
     * @return valid 
     */
    public Boolean valid(){
        return(Operacions.valid(codi, 10) && descripcio.length() <= 100);
    }
}