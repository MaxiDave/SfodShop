package sfod;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLPopupControler implements Initializable {
    
    private final ObservableList<Producte> productes;
    private Producte seleccionat;

    @FXML
    private TableView cerca;
    
    @FXML
    private TableColumn cercaCodi;
    
    @FXML
    private TableColumn cercaDesc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cercaCodi.setCellValueFactory(
            new PropertyValueFactory<Producte,String>("codi")
        );
        cercaDesc.setCellValueFactory(
            new PropertyValueFactory<Producte,String>("descripcio")
        );
        cerca.setItems(productes);
        
        cerca.setRowFactory( tv -> {
            TableRow<Producte> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    seleccionat= row.getItem();
                    Stage stage = (Stage) cerca.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });
    }    
    
    public FXMLPopupControler(ObservableList<Producte> llista){
        productes= llista;
    }
    
    public Producte getProducte(){
        return seleccionat;
    }
}