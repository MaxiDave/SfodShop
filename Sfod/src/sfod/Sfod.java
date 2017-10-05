//sfod

/**
 * @file: Sfod.java
 * @author: David Martínez, MaxiDave13
 * @version: 1.0 Alpha
 * @date: 9-2017
 * @warning: --
 * @brief: Classe Sfod: Extén la classe Application per tal d'inicialitzar totes les estructures i cridar a la càrrega del Login
 * @copyright: Public License
 */

package sfod;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * DESCRIPCIÓ GENERAL 
 * @brief: Classe principal que s'encarrega d'iniciar l'aplicació 
 */
public class Sfod extends Application {
    //ATRIBUTS-------------------------------------------------------------------------
    
    //MÈTODES PÚBLICS------------------------------------------------------------------
    
    /**
     * @pre --
     * @post Sobreescriptura del mètode start que s'encarrega de carregar el FXML del Login
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Sfod");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        stage.setResizable(false);
        stage.show();
    }
    
    /**
     * @pre --
     * @post Mètode principal main que arrenca l'aplicació
     */
    public static void main(String[] args) {
        launch(args);
    }
}