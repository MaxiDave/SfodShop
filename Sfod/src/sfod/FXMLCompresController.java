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
import javafx.event.EventHandler;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class FXMLCompresController implements Initializable {
    
    @FXML
    private TextField numCompra;
    
    @FXML
    private TextField nReferencies;
    
    @FXML
    private TextField importTotal;
    
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
    private Button cancelar;
    
    @FXML
    private Button confirmar;
    
    @FXML
    private TableView taulaLiniesCompra;
    
    @FXML
    private TableView taulaEntrarCompra;
    
    @FXML
    private TableColumn columnaProducte;
    
    @FXML
    private TableColumn columnaProducteC;
    
    @FXML
    private TableColumn columnaDescripcio;
    
    @FXML
    private TableColumn columnaDescripcioC;
    
    @FXML
    private TableColumn columnaPVC;
    
    @FXML
    private TableColumn columnaPVCC;
    
    @FXML
    private TableColumn columnaDescompte;
    
    @FXML
    private TableColumn columnaDescompteC;
    
    @FXML
    private TableColumn columnaPFVC;
    
    @FXML
    private TableColumn columnaPFVCC;
    
    @FXML
    private TableColumn columnaUnitats;
    
    @FXML
    private TableColumn columnaUnitatsC;
    
    @FXML
    private TableColumn columnaPF;
    
    @FXML
    private TableColumn columnaPFC;
    
    @FXML
    private Label labelVenedor;
    
    @FXML
    private TextField ven;
    
    @FXML
    private Label labelProveidor;
    
    @FXML
    private TextField prov;
    
    @FXML
    private TextField buscarVenedor;
    
    @FXML
    private TextField venEntrar;
    
    @FXML
    private TextField buscarProveidor;
    
    @FXML
    private TextField provEntrar;
    
    private Connection conexio;
    private AnchorPane panellPare;
    private Map<Integer, String> mapaProveidors;
    private Map<Integer, String> mapaVenedors;
    private ObservableList<LiniaCompra> contingut= FXCollections.observableArrayList();
    
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
    
    private void requestFocus(TableView<LiniaCompra> tV, TablePosition origen, TableColumn col){
        tV.requestFocus();
        tV.getSelectionModel().select(origen.getRow(), col);
        TablePosition nova= tV.getFocusModel().getFocusedCell();
        Platform.runLater(()->tV.edit(nova.getRow(), nova.getTableColumn()));
    }
    
    private void requestSelect(TableView<LiniaCompra> tV, TablePosition origen, TableColumn col){
        tV.requestFocus();
        tV.getSelectionModel().select(origen.getRow(), col);
        TablePosition nova= tV.getFocusModel().getFocusedCell();
        tV.edit(nova.getRow(), nova.getTableColumn());
    }
    
    private void requestFocusSeguent(TableView<LiniaCompra> tV, TablePosition origen, TableColumn col){
        tV.requestFocus();
        tV.getSelectionModel().select(origen.getRow(), col);
        TablePosition nova= tV.getFocusModel().getFocusedCell();
        Platform.runLater(()->tV.edit(nova.getRow()+1, nova.getTableColumn()));
    }
    
    private void clearLine(TableView t, TablePosition tP, LiniaCompra linia){
        linia.clear();
        requestSelect(t, tP, columnaProducte);
        requestSelect(t, tP, columnaPVC);
        requestSelect(t, tP, columnaDescompte);
        requestSelect(t, tP, columnaPFVC);
        requestSelect(t, tP, columnaUnitats);
        requestSelect(t, tP, columnaPF);
        requestSelect(t, tP, columnaDescripcio);
    }
    
    void incRef(){
        Integer novesRefs= Integer.parseInt(nReferencies.getText())+1;
        nReferencies.setText(novesRefs.toString());
    }
    
    void decRef(){
        Integer novesRefs= Integer.parseInt(nReferencies.getText())-1;
        nReferencies.setText(novesRefs.toString());
    }
    
    void actualitzaImportTotal(){
        Double total= 0.00;
        for(LiniaCompra aux : contingut) total+= aux.getPT();
        importTotal.setText(total.toString());
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
            
            //Es carregen les imatges
            Image cancelButon = new Image(getClass().getResourceAsStream("cancelar.png"));
            cancelar.setGraphic(new ImageView(cancelButon));
            Image buscarButton = new Image(getClass().getResourceAsStream("buscar.png"));
            cercar.setGraphic(new ImageView(buscarButton));
            Image guardarButton = new Image(getClass().getResourceAsStream("guardar.png"));
            confirmar.setGraphic(new ImageView(guardarButton));

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
            columnaProducteC.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
            columnaDescripcioC.setStyle("-fx-background-color: #abe6fc;");
            columnaPVCC.setStyle("-fx-background-color: #abe6fc;");
            columnaDescompteC.setStyle("-fx-background-color: #abe6fc;");
            columnaPFVCC.setStyle("-fx-background-color: #abe6fc;");
            columnaUnitatsC.setStyle("-fx-background-color: #abe6fc;");
            columnaPFC.setStyle("-fx-background-color: #abe6fc;");
            
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
            
            columnaProducteC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("codiProducte")
            );
            columnaProducteC.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaProducteC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, String> t) {
                        LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        if(t.getNewValue().isEmpty()){
                            if(!liniaActual.isEmpty()){
                                decRef();
                                clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                actualitzaImportTotal();
                            }
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                        }
                        else if(!t.getNewValue().equals(t.getOldValue())){
                            try {
                                List<ElementCercable> list= SQL.seleccionaProductesCercables(conexio, "codi", t.getNewValue());
                                ElementCercable aux;
                                if(list.isEmpty()) throw new Exception();
                                else if(list.size() > 1) aux= mostraPopup(list);
                                else aux= list.get(0);
                                if(aux != null){
                                    if(aux.getPrincipal().equals(liniaActual.getCodiProducte())){
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                                    }
                                    else{
                                        incRef();
                                        clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                        liniaActual.setCodiProducte(aux.getPrincipal());
                                        liniaActual.setDescripcio(aux.getSecundari());
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVCC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                                        actualitzaImportTotal();
                                    }
                                }
                                else throw new Exception();
                            } catch (Exception ex) {
                                if(!liniaActual.isEmpty()) decRef();
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
                                clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                actualitzaImportTotal();
                            }
                        }
                        else requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                    }
                }
            );
            
            columnaDescripcioC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("descripcio")
            );
            columnaDescripcioC.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaDescripcioC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, String> t) {
                        LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        if(t.getNewValue().isEmpty()){
                            if(!liniaActual.isEmpty()){
                                decRef();
                                clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                actualitzaImportTotal();
                            }
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                        }
                        else if(!t.getNewValue().equals(t.getOldValue())){
                            try {
                                List<ElementCercable> list= SQL.seleccionaProductesCercables(conexio, "descripcio", t.getNewValue());
                                ElementCercable aux;
                                if(list.isEmpty()) throw new Exception();
                                else if(list.size() > 1) aux= mostraPopup(list);
                                else aux= list.get(0);
                                if(aux != null){
                                    if(aux.getPrincipal().equals(liniaActual.getCodiProducte())){
                                        liniaActual.setDescripcio(aux.getSecundari());
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                                    }
                                    else{
                                        incRef();
                                        clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                        liniaActual.setCodiProducte(aux.getPrincipal());
                                        liniaActual.setDescripcio(aux.getSecundari());
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVCC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                                        actualitzaImportTotal();
                                    }
                                }
                                else throw new Exception();
                            } catch (Exception ex) {
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Atenció!", "No s'ha trobat cap referència");
                                if(!liniaActual.isEmpty()){
                                    decRef();
                                    clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPVCC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVCC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                    actualitzaImportTotal();
                                }
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcioC);
                            }
                        }
                        else requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                    }
                }
            );
            
            columnaPVCC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PVC")
            );
            columnaPVCC.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
                @Override
                public String toString(Double i) {
                    if (i == null) {
                        return "";
                    } else {
                        return i.toString();
                    }
                }

                @Override
                public Double fromString(String string) {
                    if (string.trim().length() == 0) {
                        return null;
                    } else {
                        try {
                            return Double.valueOf(string);
                        } catch (NumberFormatException nfe) {
                            return null;
                        } 
                    }
                }
            }));
            columnaPVCC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Double> t) {
                        try{
                            Double nouPVC= t.getNewValue();
                            LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                            if(t.getNewValue().equals(t.getOldValue())) requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                            else if(liniaActual.valida()){
                                liniaActual.setPVC(nouPVC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPVCC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVCC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                                actualitzaImportTotal();
                            }
                            else{
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre massa gran");
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                            }
                        } catch (NullPointerException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaPVCC);
                        }
                    }
                }
            );
            
            columnaDescompteC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("Descompte")
            );
            columnaDescompteC.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
                @Override
                public String toString(Double i) {
                    if (i == null) {
                        return "";
                    } else {
                        return i.toString();
                    }
                }

                @Override
                public Double fromString(String string) {
                    if (string.trim().length() == 0) {
                        return null;
                    } else {
                        try {
                            return Double.valueOf(string);
                        } catch (NumberFormatException nfe) {
                            return null;
                        } 
                    }
                }
            }));
            columnaDescompteC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Double> t) {
                        try {
                            LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                            Double nouDescompte= t.getNewValue();
                            if(nouDescompte < 0 || nouDescompte > 100) throw new NumberFormatException();
                            else if(liniaActual.valida()){
                                liniaActual.setDescompte(nouDescompte);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVCC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                                actualitzaImportTotal();
                            }
                            else{
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                            }
                        } catch (NullPointerException | NumberFormatException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompteC);
                        }
                    }
                }
            );
            
            columnaPFVCC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PFVC")
            );

            columnaUnitatsC.setCellValueFactory(
                    new PropertyValueFactory<Compra,Integer>("Unitats")
            );
            columnaUnitatsC.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>(){
                @Override
                public String toString(Integer i) {
                    if (i == null) {
                        return "";
                    } else {
                        return i.toString();
                    }
                }

                @Override
                public Integer fromString(String string) {
                    if (string.trim().length() == 0) {
                        return null;
                    } else {
                        try {
                            return Integer.valueOf(string);
                        } catch (NumberFormatException nfe) {
                            return null;
                        } 
                    }
                }
            }));
            columnaUnitatsC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Integer> t) {
                        LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        try {
                            Integer novesUnitats= t.getNewValue();
                            if(novesUnitats < 0) throw new NumberFormatException();
                            if(novesUnitats > 1000 && !PopupAlerta.mostrarConfirmacio("Atenció!", "Has introduït una quantitat molt gran. Estàs segur?")){
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                            }
                            else{
                                if(liniaActual.isEmpty()){
                                    requestFocus(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                }
                                else{
                                    liniaActual.setUnitats(novesUnitats);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPFC);
                                    
                                    if(contingut.size() == (t.getTablePosition().getRow()+1)) contingut.add(new LiniaCompra());
                                    requestFocusSeguent(t.getTableView(), t.getTablePosition(), columnaProducteC);
                                    actualitzaImportTotal();
                                }
                            }
                        } catch (NullPointerException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitatsC);
                        }
                    }
                }
            );
            
            columnaPFC.setCellValueFactory(
                    new PropertyValueFactory<Compra,Double>("PT")
            );
            
            taulaLiniesCompra.setItems(contingut);
            taulaEntrarCompra.setItems(contingut);
            
            nReferencies.setVisible(false);
            importTotal.setVisible(false);
            labelNReferencies.setVisible(false);
            labelImportTotal.setVisible(false);
            euroSymbol.setVisible(false);
            
            Platform.runLater(()->buscarVenedor.requestFocus());
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", "Comprovi la seva connexió a Internet");
        }
    }
    
    private ElementCercable mostraPopup(List<ElementCercable> list) throws IOException{
        ObservableList<ElementCercable> listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"));
        FXMLPopupControler controler= new FXMLPopupControler(listObs, "Codi", "Descripció");
        loader.setController(controler);
        Parent newScene;
        newScene = loader.load();

        Stage inputStage = new Stage();
        inputStage.initModality(Modality.WINDOW_MODAL);
        inputStage.initOwner(importTotal.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getElementCercable();
    }
    
    private Compra mostraPopup(ObservableList<Compra> list) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopupCompra.fxml"));
        FXMLPopupCompraController controler= new FXMLPopupCompraController(list);
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
        buscarVenedor.setVisible(false);
        buscarProveidor.setVisible(false);
        venEntrar.setVisible(false);
        provEntrar.setVisible(false);
        ven.setVisible(true);
        prov.setVisible(true);
        nReferencies.setVisible(true);
        importTotal.setVisible(true);
        labelNReferencies.setVisible(true);
        labelImportTotal.setVisible(true);
        euroSymbol.setVisible(true);
        taulaLiniesCompra.setVisible(true);
    }
    
    private void configuraCamps(Compra compra){
        numCompra.setText(String.valueOf(compra.getNum()));
        nReferencies.setText(compra.getNumRefs().toString());
        importTotal.setText(compra.getImportTotal().toString());
        ven.setText(compra.getVenedor());
        prov.setText(compra.getProveidor());
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
                SQL.obtenirLiniesCompra(conexio, aux);
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
    
    private void cancelar(){
        buscarVenedor.clear();
        buscarProveidor.clear();
        venEntrar.clear();
        provEntrar.clear();
        confirmar.setVisible(false);
        venEntrar.setDisable(false);
        provEntrar.setDisable(false);
        buscarProveidor.setDisable(false);
        buscarVenedor.setDisable(false);
        buscarVenedor.setVisible(true);
        buscarProveidor.setVisible(true);
        venEntrar.setVisible(true);
        provEntrar.setVisible(true);
        
        ven.setVisible(false);
        prov.setVisible(false);
        nReferencies.setVisible(false);
        importTotal.setVisible(false);
        labelNReferencies.setVisible(false);
        labelImportTotal.setVisible(false);
        euroSymbol.setVisible(false);
        taulaLiniesCompra.setVisible(false);
        taulaEntrarCompra.setVisible(false);
        importTotal.clear();
        nReferencies.clear();
        numCompra.clear();
        prov.clear();
        ven.clear();
        contingut.clear();
        
        venedor.getSelectionModel().clearSelection();
        proveidor.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void accioCancelar(ActionEvent event){
        cancelar();
    }
    
    private ElementCercable mostraPopup(List<ElementCercable> list, String opt) throws IOException{
        ObservableList<ElementCercable> listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"));
        FXMLPopupControler controler;
        if(opt.equals("v")) controler= new FXMLPopupControler(listObs, "Número", "Nom Complet");
        else controler= new FXMLPopupControler(listObs, "Número", "Nom");
        loader.setController(controler);
        Parent newScene;
        newScene = loader.load();

        Stage inputStage = new Stage();
        inputStage.initModality(Modality.WINDOW_MODAL);
        inputStage.initOwner(buscarVenedor.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getElementCercable();
    }
    
    private void desbloquejarCamps(){
        venEntrar.setDisable(true);
        provEntrar.setDisable(true);
        buscarProveidor.setDisable(true);
        buscarVenedor.setDisable(true);
        cancelar.setVisible(true);
        confirmar.setVisible(true);
        euroSymbol.setVisible(true);
        
        labelNReferencies.setVisible(true);
        labelImportTotal.setVisible(true);
        
        contingut.add(new LiniaCompra());
        taulaEntrarCompra.setVisible(true);
        taulaEntrarCompra.requestFocus();
        taulaEntrarCompra.getSelectionModel().select(0);
        TableColumn tC= (TableColumn)taulaEntrarCompra.getColumns().get(0);
        Platform.runLater(()->taulaEntrarCompra.edit(0, tC));
        nReferencies.setText("0");
        importTotal.setText("0.00");
        nReferencies.setVisible(true);
        importTotal.setVisible(true);
    }
    
    private void entrarCompra(){
        Integer numVen= Integer.parseInt(buscarVenedor.getText());
        Integer numProv= Integer.parseInt(buscarProveidor.getText());
        desbloquejarCamps();
    }
    
    @FXML
    private void accioBuscarVenedor(KeyEvent event){
        try{
            if(event.getCode() == KeyCode.ENTER){
                String codi= buscarVenedor.getText();
                List<ElementCercable> list= SQL.seleccionaVenedorsCercables(conexio, codi);
                ElementCercable aux;
                if(list.isEmpty()) throw new Exception();
                else if(list.size() > 1) aux= mostraPopup(list, "v");
                else aux= list.get(0);
                if(aux != null){
                    buscarVenedor.setText(aux.getPrincipal().toString());
                    venEntrar.setText(aux.getSecundari().toString());
                    if(provEntrar.getText().isEmpty()) buscarProveidor.requestFocus();
                    else entrarCompra();
                }
                else buscarVenedor.clear();
            }
        } catch(NumberFormatException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error al cercar Venedor", "Introdueixi un nombre vàlid");
        } catch (Exception ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "No existeix venedor", "No s'ha trobat cap referència");
            buscarVenedor.clear();
        }
    }
    
    @FXML
    private void accioBuscarProveidor(KeyEvent event){
        try{
            if(event.getCode() == KeyCode.ENTER){
                String codi= buscarProveidor.getText();
                List<ElementCercable> list= SQL.seleccionaProveidorsCercables(conexio, codi);
                ElementCercable aux;
                if(list.isEmpty()) throw new Exception();
                else if(list.size() > 1) aux= mostraPopup(list, "p");
                else aux= list.get(0);
                if(aux != null){
                    buscarProveidor.setText(aux.getPrincipal().toString());
                    provEntrar.setText(aux.getSecundari().toString());
                    if(venEntrar.getText().isEmpty()) buscarVenedor.requestFocus();
                    else entrarCompra();
                }
                else buscarVenedor.clear();
            }
        } catch(NumberFormatException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error al cercar Venedor", "Introdueixi un nombre vàlid");
        } catch (Exception ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "No existeix venedor", "No s'ha trobat cap referència");
            buscarVenedor.clear();
        }
    }
    
    @FXML
    private void accioGuardar(ActionEvent event){
        try {
            Compra nova= new Compra(buscarProveidor.getText()+". "+provEntrar.getText(), buscarVenedor.getText()+". "+venEntrar.getText(), LocalDate.now());
            for(LiniaCompra linia : contingut) if(!linia.isEmpty()) nova.afegirLiniaCompra(linia);
            if(nova.teLiniesCompra()){
                long numInsCompra= SQL.obtenirNumInsCompra(conexio);
                nova.setNum(numInsCompra);

                numCompra.setText(String.valueOf(numInsCompra));
                if(PopupAlerta.mostrarConfirmacio("Atenció", "Vol entrar aquesta compra amb ref. '"+numInsCompra+"'?")){
                    SQL.afegirCompra(conexio, nova);
                    PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Acció Completada", "S'ha donat d'alta la compra correctament");
                    cancelar();
                }
            }
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", ex.getMessage());
        }
    }
}