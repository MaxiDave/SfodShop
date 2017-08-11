package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FXMLLoginControler implements Initializable {
    
    @FXML
    private Label loginTitle;
    
    @FXML
    private Label loginInfo;
    
    @FXML
    private Label loginError;
    
    @FXML
    private TextField loginUser;
    
    @FXML
    private PasswordField loginPswd;
            
    @FXML
    private Button loginButton;
    
    private Connection conexio;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        Stage stage; 
        Parent root;
        try {
            loginInfo.setVisible(true);
            //get reference to the button's stage
            stage=(Stage) loginButton.getScene().getWindow();
            
            conexio= SQL.connectar(loginUser.getText(), loginPswd.getText());
                
            //load up OTHER FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDatabase.fxml"));
            FXMLDatabaseControler controler = new FXMLDatabaseControler(conexio);
            loader.setController(controler);
            root = loader.load();
                
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex);
            loginError.setVisible(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginInfo.setVisible(false);
        loginError.setVisible(false);
    }    
}