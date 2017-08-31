package sfod;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLVenedorsController implements Initializable {

    @FXML
    private TextField venedorBuscar;
    
    @FXML
    private TextField nomCompletVenedor;
    
    @FXML
    private TextField telefonVenedor;
    
    @FXML
    private TextField emailVenedor;
    
    @FXML
    private TextField codiPaisVenedor;
    
    @FXML
    private TextField nomPaisVenedor;
    
    @FXML
    private TextField direccioVenedor;
    
    @FXML
    private TextField poblacioVenedor;
    
    @FXML
    private TextField provinciaVenedor;
    
    @FXML
    private ChoiceBox<?> tractament;
    
    @FXML
    private TextArea informacioAddicionalVenedor;
    
    @FXML
    private Button guardarVenedor;
    @FXML
    private Button cancelarVenedor;

    private Connection conexio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public FXMLVenedorsController(Connection conn){
        conexio= conn;
    }
    
}
