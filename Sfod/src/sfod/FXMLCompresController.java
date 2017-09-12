package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class FXMLCompresController implements Initializable {

    @FXML
    private AnchorPane panell;
    
    @FXML
    private ComboBox venedor;
    
    @FXML
    private ComboBox proveidor;
    
    @FXML
    private DatePicker dataInici;
    
    @FXML
    private DatePicker dataFi;
    
    @FXML
    private Button cercar;
    
    @FXML
    private TableView taulaCompres;
    
    @FXML
    private TableColumn columnaFactura;
    
    @FXML
    private TableColumn columnaProveidor;
    
    @FXML
    private TableColumn columnaVenedor;
    
    @FXML
    private TableColumn columnaImport;
    
    @FXML
    private TableColumn columnaDataCompra;
    
    private Connection conexio;
    private AnchorPane panellPare;
    private Map<Integer, String> mapaProveidors;
    private Map<Integer, String> mapaVenedors;
    
    public FXMLCompresController(Connection conn, AnchorPane pare){
        conexio= conn;
        panellPare= pare;
    }
    
    public void configuraComboProveidors(){
        ObservableList<String> contingut= FXCollections.observableArrayList();
        for(Map.Entry<Integer, String> entry: mapaProveidors.entrySet()){
            String resultat= entry.getKey().toString()+". "+entry.getValue();
            contingut.add(resultat);
        }
        proveidor.setItems(contingut);
    }
    
    public void configuraComboVenedors(){
        ObservableList<String> contingut= FXCollections.observableArrayList();
        for(Map.Entry<Integer, String> entry: mapaVenedors.entrySet()){
            String resultat= entry.getKey().toString()+". "+entry.getValue();
            contingut.add(resultat);
        }
        venedor.setItems(contingut);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //S'inicialitza el tamany del panell
            panell.setPrefHeight(panellPare.getHeight()*0.95);
            panell.setPrefWidth(panellPare.getWidth()*0.99);
            Platform.runLater(()->panell.getScene().getWindow().setX(panellPare.getLayoutX()));
            Platform.runLater(()->panell.getScene().getWindow().setY(panellPare.getLayoutY()+25));
            
            //Càrrega d'informació de Venedors i Proveidors per tal de poder fer la cerca
            mapaProveidors= SQL.carregarProveidors(conexio);
            mapaVenedors= SQL.carregarVenedors(conexio);
            configuraComboProveidors();
            configuraComboVenedors();
            
            taulaCompres.setVisible(false);
            columnaFactura.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
            columnaProveidor.setStyle("-fx-background-color: #abe6fc;");
            columnaVenedor.setStyle("-fx-background-color: #abe6fc;");
            columnaImport.setStyle("-fx-background-color: #abe6fc;");
            columnaDataCompra.setStyle("-fx-background-color: #abe6fc;");
            columnaFactura.setCellValueFactory(
                    new PropertyValueFactory<Compra,Integer>("num")
            );
            columnaProveidor.setCellValueFactory(
                    new PropertyValueFactory<Compra,String>("proveidor")
            );
            columnaVenedor.setCellValueFactory(
                    new PropertyValueFactory<Compra,String>("venedor")
            );
            columnaImport.setCellValueFactory(
                    new PropertyValueFactory<Compra,Double>("import")
            );
            columnaDataCompra.setCellValueFactory(
                    new PropertyValueFactory<Compra,String>("data")
            );
            
            venedor.setVisibleRowCount(5);
            proveidor.setVisibleRowCount(5);
            
            dataInici.setValue(LocalDate.now().minusYears(2));
            dataFi.setValue(LocalDate.now());
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", "Comprovi la seva connexió a Internet");
        }
    }
    
    @FXML
    private void accioBuscarCompres(ActionEvent event){
        try {
            String infoVenedor= (String)venedor.getSelectionModel().getSelectedItem();
            String infoProveidor= (String)proveidor.getSelectionModel().getSelectedItem();
            LocalDate inici= dataInici.getValue();
            LocalDate fi= dataFi.getValue();
            ObservableList<Compra> llista= SQL.obtenirCompres(conexio, infoVenedor, infoProveidor, inici, fi);
            if(llista.isEmpty()) PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Atenció", "No s'ha trobat cap compra");
            else{
                taulaCompres.setItems(llista);
                taulaCompres.setVisible(true);
            }
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor", ex.getMessage());
        }
    }
}