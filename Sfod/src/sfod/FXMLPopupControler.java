package sfod;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLPopupControler implements Initializable {
    
    private final ObservableList<ElementCercable> elements;
    private final String nomPrincipal;
    private final String nomSecundari;
    private ElementCercable seleccionat;

    @FXML
    private TableView resultatCerca;
    
    @FXML
    private TableColumn cercaPrincipal;
    
    @FXML
    private TableColumn cercaSecundari;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cercaPrincipal.setText(nomPrincipal);
        cercaSecundari.setText(nomSecundari);
        
        Platform.runLater(()->resultatCerca.getSelectionModel().select(0));
        cercaPrincipal.setCellValueFactory(
            new PropertyValueFactory<ElementCercable,String>("principal")
        );
        cercaSecundari.setCellValueFactory(
            new PropertyValueFactory<ElementCercable,String>("secundari")
        );
        resultatCerca.setItems(elements);
        
        resultatCerca.setRowFactory( tv -> {
            TableRow<ElementCercable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    seleccionat= row.getItem();
                    Stage stage = (Stage) resultatCerca.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });
    }

    @FXML
    public void eventKeyEnter(KeyEvent event){
        if (!resultatCerca.getSelectionModel().isEmpty() && event.getCode() == KeyCode.ENTER) {
            seleccionat= (ElementCercable) resultatCerca.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) resultatCerca.getScene().getWindow();
            stage.close();
        }
    }
    
    public FXMLPopupControler(ObservableList<ElementCercable> llista, String nomPrincipal, String nomSecundari){
        elements= llista;
        this.nomPrincipal= nomPrincipal;
        this.nomSecundari= nomSecundari;
    }
    
    public ElementCercable getElementCercable(){
        return seleccionat;
    }
}