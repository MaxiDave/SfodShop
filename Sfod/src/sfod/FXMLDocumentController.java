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
    private Button sqlExecutar;
    
    @FXML
    private TextArea sqlComanda;
    
    private Connection conexio;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, SQLException {
        Stage stage; 
        Parent root;
        if(event.getSource()==loginButton){
            try {
                //get reference to the button's stage
                stage=(Stage) loginButton.getScene().getWindow();
                conexio= SQL.connectar(loginUser.getText(), loginPswd.getText());
                
                //load up OTHER FXML document
                root = FXMLLoader.load(getClass().getResource("FXML2.fxml"));
                
                //create a new scene with root and set the stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex);
                loginError.setVisible(true);
            }
        }
        else if(event.getSource()==btn2){
            stage=(Stage) btn2.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            Statement stmt = conexio.createStatement();
            System.out.println(sqlComanda.getText());
            ResultSet rs = stmt.executeQuery(sqlComanda.getPromptText());
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}