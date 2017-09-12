package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class FXMLEntrarCompraController implements Initializable {
    
    @FXML
    private TextField numCompra;
    
    @FXML
    private TextField referenciesTotals;
    
    @FXML
    private TextField importTotal;

    @FXML
    private ComboBox venedor;
    
    @FXML
    private ComboBox proveidor;
    
    @FXML
    private Button confirmar;
    
    @FXML
    private TableView taulaLiniesCompra;
    
    @FXML
    private TableColumn columnaProducte;
    
    @FXML
    private TableColumn columnaPVC;
    
    @FXML
    private TableColumn columnaDescompte;
    
    @FXML
    private TableColumn columnaPFVC;
    
    @FXML
    private TableColumn columnaUnitats;
    
    @FXML
    private TableColumn columnaPF;
    
    private Connection conexio;
    private Map<Integer, String> mapaProveidors;
    private Map<Integer, String> mapaVenedors;
    private ObservableList<LiniaCompra> contingut;
    
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
    
    public FXMLEntrarCompraController(Connection conn){
        conexio= conn;
        contingut= FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Càrrega d'informació de Venedors i Proveidors per tal de poder fer la cerca
            mapaProveidors= SQL.carregarProveidors(conexio);
            mapaVenedors= SQL.carregarVenedors(conexio);
            configuraComboProveidors();
            configuraComboVenedors();
            
            taulaLiniesCompra.setVisible(false);
            columnaProducte.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
            columnaPVC.setStyle("-fx-background-color: #abe6fc;");
            columnaDescompte.setStyle("-fx-background-color: #abe6fc;");
            columnaPFVC.setStyle("-fx-background-color: #abe6fc;");
            columnaUnitats.setStyle("-fx-background-color: #abe6fc;");
            columnaPF.setStyle("-fx-background-color: #abe6fc;");
            
            columnaProducte.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("codiProducte")
            );
            columnaProducte.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaProducte.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, String> t) {
                        ((LiniaCompra) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setCodiProducte(t.getNewValue());
                    }
                }
            );
            columnaPVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PVC")
            );
            columnaPVC.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaDescompte.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("Descompte")
            );
            columnaDescompte.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaPFVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PFVC")
            );
            columnaPFVC.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaUnitats.setCellValueFactory(
                    new PropertyValueFactory<Compra,Double>("Unitats")
            );
            columnaUnitats.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaPF.setCellValueFactory(
                    new PropertyValueFactory<Compra,Double>("PT")
            );
            columnaPF.setCellFactory(TextFieldTableCell.forTableColumn());
            
            venedor.setVisibleRowCount(5);
            proveidor.setVisibleRowCount(5);
            
            numCompra.setEditable(false);
            confirmar.setVisible(false);
            referenciesTotals.setEditable(false);
            importTotal.setEditable(false);
            
            contingut.add(new LiniaCompra());
            taulaLiniesCompra.setItems(contingut);
            
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", "Comprovi la seva connexió a Internet");
        }
    }    

    @FXML
    private void accioConfirmarCompra(ActionEvent event) {
        
    }
    
    private void desbloquejarCamps(){
        taulaLiniesCompra.setVisible(true);
        referenciesTotals.setText("0");
        importTotal.setText("0.00");
    }
    
    @FXML
    private void accioDesbloquejarCamps(ActionEvent event){
        if(!taulaLiniesCompra.isVisible()){
            String infoVenedor= (String)venedor.getSelectionModel().getSelectedItem();
            String infoProveidor= (String)proveidor.getSelectionModel().getSelectedItem();
            if(infoVenedor != null && infoProveidor != null) desbloquejarCamps();
        }
    }
}