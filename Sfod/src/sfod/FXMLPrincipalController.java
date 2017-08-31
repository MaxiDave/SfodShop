package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLPrincipalController implements Initializable {
    
    private Connection conexio;
    
    @FXML
    private AnchorPane panell;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void accioObrirProductes(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLProductes.fxml"));
        FXMLProductesController controller= new FXMLProductesController(panell, conexio);
        loader.setController(controller);
        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Manteniment de Productes");
        inputStage.showAndWait();
    }
    
    public FXMLPrincipalController(Connection conn){
        conexio= conn;
    }
}
