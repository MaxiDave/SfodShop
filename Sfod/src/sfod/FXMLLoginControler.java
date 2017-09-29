package sfod;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
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
    
    private Service serveiLogin= new procesLogin();
    private Boolean errorInternet= false;
    private Boolean errorServer= false;
    
    @FXML
    private AnchorPane panell;
    
    @FXML
    private TextField loginUser;
    
    @FXML
    private PasswordField loginPswd;
            
    @FXML
    private Button loginButton;
    
    @FXML
    private ProgressIndicator progressBar;
    
    private Connection conexio;
    
    @FXML
    private void accioIniciarSessio(ActionEvent event) throws IOException, SQLException {
        if(!serveiLogin.isRunning()){
            loginButton.setVisible(false);
            progressBar.setVisible(true);
            serveiLogin.start();
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
        
        serveiLogin.setOnSucceeded(e ->{
            if(errorInternet){
                //No hi ha internet
                loginPswd.clear();

                //Mostrar Error d'autenticació
                PopupAlerta.mostraAlerta(AlertType.ERROR, "Error de connexió", "No s'ha pogut connectar a internet");
            }
            else if(errorServer){
                //Contrasenya incorrecta o error de servidor
                loginPswd.clear();

                //Mostrar Error d'autenticació
                PopupAlerta.mostraAlerta(AlertType.ERROR, "Error d'autenticació", "Usuari i/o contrasenya incorrectes");
            }
            else{
                try {
                    Stage stage;
                    Parent root;
                    //Es carrega l'stage del botó
                    stage=(Stage) loginButton.getScene().getWindow();

                    //Càrrega del document FXML principal de l'aplicatiu
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
                    FXMLPrincipalController controlador = new FXMLPrincipalController(conexio);
                    loader.setController(controlador);
                    root = loader.load();

                    //Crear i mostrar la nova escena amb el FXML carregat
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.setTitle("Sfod - Business Management V1.0 Alpha");
                    stage.show();
                } catch (IOException ex) {
                    PopupAlerta.mostraAlerta(AlertType.ERROR, "Error d'autenticació", "Usuari i/o contrasenya incorrectes");
                }
            }
            progressBar.setVisible(false);
            loginButton.setVisible(true);
            serveiLogin.reset();
        });
    }

    private class procesLogin extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Runnable runner = new Runnable() {
                        public void run(){
                            try {
                                Socket socket= socket = new Socket("www.google.com", 80);
                                try {
                                    conexio= SQL.connectar(loginUser.getText(), loginPswd.getText());
                                } catch (SQLException | ClassNotFoundException ex) {
                                    errorServer= true;
                                }
                            } catch (IOException ex) {
                                errorInternet= true;
                            }
                        }
                    };
                    Thread t = new Thread(runner, "Execució de login");
                    t.start();
                    t.join();
                    return null;
                }
            };
        }
    }
}