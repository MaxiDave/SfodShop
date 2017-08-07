package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label loginTitle;
    
    @FXML
    private Label loginError;
    
    @FXML
    private TextField loginUser;
    
    @FXML
    private PasswordField loginPswd;
            
    @FXML
    private Button loginButton;
    
    @FXML
    private Button btn2;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage; 
        Parent root;
        if(event.getSource()==loginButton){
            try {
                //get reference to the button's stage
                stage=(Stage) loginButton.getScene().getWindow();
                Connection conexio= SQL.connectar(loginUser.getText(), loginPswd.getText());
                //load up OTHER FXML document
                root = FXMLLoader.load(getClass().getResource("FXML2.fxml"));
                
                //create a new scene with root and set the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | ClassNotFoundException ex) {
                loginError.setVisible(true);
            }
        }
        else{
            stage=(Stage) btn2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}