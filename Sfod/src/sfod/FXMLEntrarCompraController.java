package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    private Button cancelar;
    
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
        Integer novesRefs= Integer.parseInt(referenciesTotals.getText())+1;
        referenciesTotals.setText(novesRefs.toString());
    }
    
    void decRef(){
        Integer novesRefs= Integer.parseInt(referenciesTotals.getText())-1;
        referenciesTotals.setText(novesRefs.toString());
    }
    
    void actualitzaImportTotal(){
        Double total= 0.00;
        for(LiniaCompra aux : contingut) total+= aux.getPT();
        importTotal.setText(total.toString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Càrrega d'informació de Venedors i Proveidors per tal de poder fer la cerca
            mapaProveidors= SQL.carregarProveidors(conexio);
            mapaVenedors= SQL.carregarVenedors(conexio);
            configuraComboProveidors();
            configuraComboVenedors();
            
            //Es carregen les imatges
            Image cancelButon = new Image(getClass().getResourceAsStream("cancelar.png"));
            cancelar.setGraphic(new ImageView(cancelButon));
            Image guardarButon = new Image(getClass().getResourceAsStream("guardar.png"));
            confirmar.setGraphic(new ImageView(guardarButon));
            
            cancelar.setVisible(false);
            confirmar.setVisible(false);
        
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
            columnaProducte.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaProducte.setOnEditCommit(
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
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
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
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaProducte);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                                    }
                                    else{
                                        incRef();
                                        clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                        liniaActual.setCodiProducte(aux.getPrincipal());
                                        liniaActual.setDescripcio(aux.getSecundari());
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPVC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompte);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitats);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaProducte);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                                        actualitzaImportTotal();
                                    }
                                }
                                else throw new Exception();
                            } catch (Exception ex) {
                                if(!liniaActual.isEmpty()) decRef();
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
                                clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaProducte);
                                actualitzaImportTotal();
                            }
                        }
                        else requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                    }
                }
            );
            
            columnaDescripcio.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,String>("descripcio")
            );
            columnaDescripcio.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaDescripcio.setOnEditCommit(
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
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
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
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
                                    }
                                    else{
                                        incRef();
                                        clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                        liniaActual.setCodiProducte(aux.getPrincipal());
                                        liniaActual.setDescripcio(aux.getSecundari());
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompte);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVC);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitats);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                        requestSelect(t.getTableView(), t.getTablePosition(), columnaProducte);
                                        requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
                                        actualitzaImportTotal();
                                    }
                                }
                                else throw new Exception();
                            } catch (Exception ex) {
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Atenció!", "No s'ha trobat cap referència");
                                if(!liniaActual.isEmpty()){
                                    decRef();
                                    clearLine(t.getTableView(), t.getTablePosition(), liniaActual);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompte);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPVC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVC);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaUnitats);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaProducte);
                                    actualitzaImportTotal();
                                }
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescripcio);
                            }
                        }
                        else requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
                    }
                }
            );
            
            columnaPVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PVC")
            );
            columnaPVC.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
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
            columnaPVC.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Double> t) {
                        try{
                            Double nouPVC= t.getNewValue();
                            LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                            if(t.getNewValue().equals(t.getOldValue())) requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompte);
                            else if(liniaActual.valida()){
                                liniaActual.setPVC(nouPVC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPVC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompte);
                                actualitzaImportTotal();
                            }
                            else{
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre massa gran");
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
                            }
                        } catch (NullPointerException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaPVC);
                        }
                    }
                }
            );
            
            columnaDescompte.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("Descompte")
            );
            columnaDescompte.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
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
            columnaDescompte.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Double> t) {
                        try {
                            LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                            Double nouDescompte= t.getNewValue();
                            if(nouDescompte < 0 || nouDescompte > 100) throw new NumberFormatException();
                            else if(liniaActual.valida()){
                                liniaActual.setDescompte(nouDescompte);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaDescompte);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPFVC);
                                requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitats);
                                actualitzaImportTotal();
                            }
                            else{
                                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompte);
                            }
                        } catch (NullPointerException | NumberFormatException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaDescompte);
                        }
                    }
                }
            );
            
            columnaPFVC.setCellValueFactory(
                    new PropertyValueFactory<LiniaCompra,Double>("PFVC")
            );

            columnaUnitats.setCellValueFactory(
                    new PropertyValueFactory<Compra,Integer>("Unitats")
            );
            columnaUnitats.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>(){
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
            columnaUnitats.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<LiniaCompra, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<LiniaCompra, Integer> t) {
                        LiniaCompra liniaActual= ((LiniaCompra) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        try {
                            Integer novesUnitats= t.getNewValue();
                            if(novesUnitats < 0) throw new NumberFormatException();
                            if(novesUnitats > 1000 && !PopupAlerta.mostrarConfirmacio("Atenció!", "Has introduït una quantitat molt gran. Estàs segur?")){
                                requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitats);
                            }
                            else{
                                if(liniaActual.isEmpty()){
                                    requestFocus(t.getTableView(), t.getTablePosition(), columnaProducte);
                                }
                                else{
                                    liniaActual.setUnitats(novesUnitats);
                                    requestSelect(t.getTableView(), t.getTablePosition(), columnaPF);
                                    
                                    if(contingut.size() == (t.getTablePosition().getRow()+1)) contingut.add(new LiniaCompra());
                                    requestFocusSeguent(t.getTableView(), t.getTablePosition(), columnaProducte);
                                    actualitzaImportTotal();
                                }
                            }
                        } catch (NullPointerException ex) {
                            PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Error 404", "Nombre introduït invàlid");
                            requestFocus(t.getTableView(), t.getTablePosition(), columnaUnitats);
                        }
                    }
                }
            );
            
            columnaPF.setCellValueFactory(
                    new PropertyValueFactory<Compra,Double>("PT")
            );
            
            venedor.setVisibleRowCount(5);
            proveidor.setVisibleRowCount(5);
            
            numCompra.setEditable(false);
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
        try {
            Compra nova= new Compra((String)proveidor.getSelectionModel().getSelectedItem(), (String)venedor.getSelectionModel().getSelectedItem(), LocalDate.now());
            for(LiniaCompra linia : contingut) if(!linia.isEmpty()) nova.afegirLiniaCompra(linia);
            
            long numInsCompra= SQL.obtenirNumInsCompra(conexio);
            nova.setNum(numInsCompra);
            
            numCompra.setText(String.valueOf(numInsCompra));
            if(PopupAlerta.mostrarConfirmacio("Atenció", "Vol entrar aquesta compra amb ref. '"+numInsCompra+"'?")){
                SQL.afegirCompra(conexio, nova);
                PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Acció Completada", "S'ha donat d'alta la compra correctament");
                cancelarCompra();
            }
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Connexió", ex.getMessage());
        }
    }
    
    private void cancelarCompra(){
        numCompra.clear();
        cancelar.setVisible(false);
        confirmar.setVisible(false);
        taulaLiniesCompra.setVisible(false);
        contingut.clear();
        contingut.add(new LiniaCompra());
        venedor.setDisable(false);
        venedor.getSelectionModel().clearSelection();
        proveidor.getSelectionModel().clearSelection();
        proveidor.setDisable(false);
        referenciesTotals.setText("0");
        importTotal.setText("0.00");
    }
    
    @FXML
    private void accioCancelarCompra(ActionEvent event) {
        cancelarCompra();
    }
    
    private void desbloquejarCamps(){
        venedor.setDisable(true);
        proveidor.setDisable(true);
        cancelar.setVisible(true);
        confirmar.setVisible(true);
        taulaLiniesCompra.setVisible(true);
        taulaLiniesCompra.requestFocus();
        taulaLiniesCompra.getSelectionModel().select(0);
        TableColumn tC= (TableColumn)taulaLiniesCompra.getColumns().get(0);
        Platform.runLater(()->taulaLiniesCompra.edit(0, tC));
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