package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class FXMLLoginControler implements Initializable {
    
    @FXML
    private AnchorPane panell;
    
    @FXML
    private TextField loginUser;
    
    @FXML
    private PasswordField loginPswd;
            
    @FXML
    private Button loginButton;
    
    private Connection conexio;
    
    @FXML
    private void accioIniciarSessio(ActionEvent event) throws IOException, SQLException {
        Stage stage; 
        Parent root;
        try {
            //Es carrega l'stage del botó
            stage=(Stage) loginButton.getScene().getWindow();
            
            //Connexió amb la BDD
            conexio= SQL.connectar(loginUser.getText(), loginPswd.getText());
                
            //Càrrega del document FXML principal de l'aplicatiu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
            FXMLPrincipalController controlador = new FXMLPrincipalController(conexio);
            loader.setController(controlador);
            root = loader.load();
                
            //Crear i mostrar la nova escena amb el FXML carregat
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            
        } catch (SQLException | ClassNotFoundException ex) {
            //Contrasenya incorrecta o error de connexió
            loginPswd.clear();
            
            //Mostrar Error d'autenticació
            PopupAlerta.mostraAlerta(AlertType.ERROR, "Error d'autenticació", "Usuari i/o contrasenya incorrectes");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Demanar focus al TextField loginUser
        Platform.runLater(()->loginUser.requestFocus());
        
        Image fons= new Image(getClass().getResourceAsStream("login.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(200, 300, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(fons, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        panell.setBackground(background);
    }    
}