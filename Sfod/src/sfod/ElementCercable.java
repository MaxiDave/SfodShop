//sfod

/**
 * @file ElementCercable.java 
 * @author David Martínez, MaxiDave13
 * @version 1.0 Alpha
 * @date 9-2017
 * @warning --
 * @brief Classe ElementCercable: Conté dos camps String que serveix per a informar a l'usuari en un recuadre de cerca
 * @copyright Public License
 */
package sfod;

/**
 * DESCRIPCIÓ GENERAL
 * @brief Conté informació clau per a identificar algun element sense tota la seva informació
 */

public class ElementCercable {
    
    //ATRIBUTS-------------------------------------------------------------------------
    private final String principal;
    private final String secundari;
    
    //MÈTODES PÚBLICS------------------------------------------------------------------
    /**
     * @pre --
     * @post Constructor per defecte. Crea un ElementCercable amb els dos strings principal i secundari
     * @param principal
     * @param secundari 
     */
    public ElementCercable(String principal, String secundari){
        this.principal= principal;
        this.secundari= secundari;
    }
    
    /**
     * @pre --
     * @post Retorna l'string principal de l'ElementCercable
     * @return String principal 
     */
    public String getPrincipal(){
        return principal;
    }
    
    /**
     * @pre --
     * @post Retorna l'string secundari de l'ElementCercable
     * @return String secundari 
     */
    public String getSecundari(){
        return secundari;
    }
}