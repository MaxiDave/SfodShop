package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class FXMLSQLICController implements Initializable {

    @FXML
    private TextArea sqlComanda;
    
    private Connection conexio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public FXMLSQLICController(Connection conn){
        conexio= conn;
    }    

    @FXML
    private void accioExecutar(ActionEvent event){
        try {
            Statement stmt = conexio.createStatement();
            stmt.executeUpdate(sqlComanda.getText());
            PopupAlerta.mostraAlerta(Alert.AlertType.CONFIRMATION, "Comanda Executada Correctament", "");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "ERROR 404", ex.getMessage());
        }
    }
    
}
