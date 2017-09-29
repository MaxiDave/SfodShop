package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLCompresController implements Initializable {
    
    @FXML
    private TextField numCompra;
    
    @FXML
    private TextField nReferencies;
    
    @FXML
    private TextField importTotal;
    
    @FXML
    private Label labelNumCompra;
    
    @FXML
    private Label labelNReferencies;
    
    @FXML
    private Label labelImportTotal;
    
    @FXML
    private Label euroSymbol;

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
    private TableView taulaLiniesCompra;
    
    @FXML
    private TableColumn columnaProducte;
    
    @FXML
    private TableColumn columnaDescripcio;
    
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
    private AnchorPane panellPare;
    private Map<Integer, String> mapaProveidors;
    private Map<Integer, String> mapaVenedors;
    private ObservableList<LiniaCompra> contingut;
    
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

            venedor.setVisibleRowCount(5);
            proveidor.setVisibleRowCount(5);
            
            dataInici.setValue(LocalDate.now().minusYears(2));
            dataFi.setValue(LocalDate.now());
            
            taulaLiniesCompra.setVisible(false);
            columnaProducte.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
            columnaDescripcio.setStyle("-fx-background-color: #abe6fc;");
            columnaPVC.setStyle("-fx-background-color: #abe6fc;");
            columnaDescompte.setStyle("-fx-background-color: #abe6fc;");
            columnaPFVC.setStyle("-fx-background-color: #abe6fc;");
            columnaUnitats.setStyle("-fx-background-color: #abe6fc;");
            columnaPF.setStyle("-fx-background-color: #abe6fc;");
            
            columnaProducte.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("codiProducte")
            );
            columnaDescripcio.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("descripcio")
            );
            columnaPVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PVC")
            );
            columnaDescompte.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("descompte")
            );
            columnaPFVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PFVC")
            );
            columnaUnitats.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Integer>("unitats")
            );
            columnaPF.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PT")
            );
            
            taulaLiniesCompra.setItems(contingut);
            
            numCompra.setVisible(false);
            nReferencies.setVisible(false);
            importTotal.setVisible(false);
            labelNumCompra.setVisible(false);
            labelNReferencies.setVisible(false);
            labelImportTotal.setVisible(false);
            euroSymbol.setVisible(false);
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", "Comprovi la seva connexió a Internet");
        }
    }
    
    private Compra mostraPopup(List<Compra> list) throws IOException{
        ObservableList<Compra> listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopupCompra.fxml"));
        FXMLPopupCompraController controler= new FXMLPopupCompraController(listObs);
        loader.setController(controler);
        Parent newScene;
        newScene = loader.load();

        Stage inputStage = new Stage();
        inputStage.initModality(Modality.WINDOW_MODAL);
        inputStage.initOwner(venedor.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getCompra();
    }
    
    private void mostraCamps(){
        numCompra.setVisible(true);
        nReferencies.setVisible(true);
        importTotal.setVisible(true);
        labelNumCompra.setVisible(true);
        labelNReferencies.setVisible(true);
        labelImportTotal.setVisible(true);
        euroSymbol.setVisible(true);
        taulaLiniesCompra.setVisible(true);
    }
    
    private void configuraCamps(Compra compra){
        numCompra.setText(String.valueOf(compra.getNum()));
        nReferencies.setText(compra.getNumRefs().toString());
        importTotal.setText(compra.getImportTotal().toString());
        
        Iterator<LiniaCompra> it= compra.getIteradorLC();
        while(it.hasNext()){
            LiniaCompra lC= it.next();
            contingut.add(lC);
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
            Compra aux;
            if(llista.isEmpty()) throw new Exception(); 
            else if(llista.size() > 1) aux= mostraPopup(llista);
            else aux= llista.get(0);
            if(aux != null){
                configuraCamps(aux);
                mostraCamps();
            }
            else throw new Exception();
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor", ex.getMessage());
        } catch(Exception ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Atenció", "No s'ha trobat cap compra");
        }
    }
}