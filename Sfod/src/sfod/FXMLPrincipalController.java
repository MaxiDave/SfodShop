package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLPrincipalController implements Initializable {
    
    private Connection conexio;
    
    @FXML
    private AnchorPane panell;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image fons= new Image(getClass().getResourceAsStream("SfodLogo.PNG"));
        BackgroundSize backgroundSize = new BackgroundSize(200, 300, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(fons, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        panell.setBackground(background);
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
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    @FXML
    public void accioObrirSQLIC(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSQLIC.fxml"));
        FXMLSQLICController controller= new FXMLSQLICController(conexio);
        loader.setController(controller);
        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("SQL Interaction Center");
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    @FXML
    public void accioObrirVenedorsProveidors(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLVenedorsProveidors.fxml"));
        FXMLVenedorsProveidorsController controller= new FXMLVenedorsProveidorsController(conexio, panell);
        loader.setController(controller);
        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Manteniment de Venedors & Proveïdors");
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    @FXML
    public void accioObrirVeureCompres(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCompres.fxml"));
        FXMLCompresController controller= new FXMLCompresController(conexio, panell);
        loader.setController(controller);
        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Llista de Compres");
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    @FXML
    public void accioObrirEntrarCompra(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEntrarCompra.fxml"));
        FXMLEntrarCompraController controller= new FXMLEntrarCompraController(conexio);
        loader.setController(controller);
        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Entrar Compra");
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    @FXML
    public void accioObrirEmailSender(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEmailSender.fxml"));

        Parent newScene;
        newScene = loader.load();
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.NONE);
        inputStage.initOwner(panell.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Email Sender");
        inputStage.setResizable(false);
        inputStage.showAndWait();
    }
    
    public FXMLPrincipalController(Connection conn){
        conexio= conn;
    }
}