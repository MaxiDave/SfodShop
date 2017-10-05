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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLSQLICController implements Initializable {

    @FXML
    private TextArea sqlComanda;
    
    @FXML
    private Button sortir;
    
    @FXML
    private Button executar;
    
    private Connection conexio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image okButton = new Image(getClass().getResourceAsStream("guardar.png"));
        executar.setGraphic(new ImageView(okButton));
        Image sortirButton = new Image(getClass().getResourceAsStream("sortir.png"));
        sortir.setGraphic(new ImageView(sortirButton));
    }

    public FXMLSQLICController(Connection conn){
        conexio= conn;
    }    

    @FXML
    private void accioExecutar(ActionEvent event){
        try {
            Statement stmt = conexio.createStatement();
            stmt.executeUpdate(sqlComanda.getText());
            PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Comanda Executada Correctament", "");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "ERROR 404", ex.getMessage());
        }
    }
    
    @FXML
    private void accioSortir(ActionEvent event){
        Stage stage= (Stage)sqlComanda.getScene().getWindow();
        stage.close();
    }
    
}
