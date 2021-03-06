package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceBox;
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

public class FXMLProductesController implements Initializable {

    @FXML
    private AnchorPane panell;
    
    @FXML
    private TableView taulaSpecs;
    
    @FXML
    private TableColumn specTitol;
    
    @FXML
    private TableColumn specInfo;
    
    @FXML
    private TextField codiBuscar;
    
    @FXML
    private TextField descBuscar;
    
    @FXML
    private TextField ebanBuscar;
    
    @FXML
    private TextField stock;
    
    @FXML
    private Button guardar;
    
    @FXML
    private Button cancelar;
    
    @FXML
    private Button eliminar;
    
    @FXML
    private Label labelTipus;
    
    @FXML
    private ChoiceBox tipusProducte;
    
    @FXML
    private Label infoTipusProducte;
    
    private AnchorPane panellPare;
    
    private Connection conexio;
    
    private ObservableList<ItemProducteElectronic> dataElectronic= FXCollections.observableArrayList();
    
    private ObservableList<String> opcionsTipus= FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //S'inicialitza el tamany del panell
        panell.setPrefHeight(panellPare.getHeight()*0.95);
        panell.setPrefWidth(panellPare.getWidth()*0.99);
        Platform.runLater(()->panell.getScene().getWindow().setX(panellPare.getLayoutX()));
        Platform.runLater(()->panell.getScene().getWindow().setY(panellPare.getLayoutY()+25));
        
        //Es carregen les imatges
        Image cancelButon = new Image(getClass().getResourceAsStream("cancelar.png"));
        cancelar.setGraphic(new ImageView(cancelButon));
        Image guardarButon = new Image(getClass().getResourceAsStream("guardar.png"));
        guardar.setGraphic(new ImageView(guardarButon));
        Image eliminarButon = new Image(getClass().getResourceAsStream("eliminar.png"));
        eliminar.setGraphic(new ImageView(eliminarButon));
        
        tipusProducte.getSelectionModel().selectedIndexProperty().addListener(new
                ChangeListener<Number>(){
                    public void changed(ObservableValue ov,
                            Number value, Number new_value){
                        if(new_value.intValue() == 0){
                            if(dataElectronic.isEmpty()) dataElectronic.addAll(carregaSpecs());
                            taulaSpecs.setItems(dataElectronic);
                            taulaSpecs.setVisible(true);
                            ebanBuscar.setEditable(true);
                            ebanBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
                        }
                        else{
                            dataElectronic.clear();
                            dataElectronic.addAll(carregaSpecs());
                            taulaSpecs.setVisible(false);
                            if(new_value.intValue() == 2){
                                ebanBuscar.setEditable(false);
                                ebanBuscar.clear();
                                ebanBuscar.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
                            }
                            else{
                                ebanBuscar.setEditable(true);
                                ebanBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
                            }
                        }
                    }
                }
            );
        
        infoTipusProducte.setVisible(false);
        tipusProducte.setVisible(false);
        labelTipus.setVisible(false);
        opcionsTipus.add("Electrònic");
        opcionsTipus.add("Varis");
        opcionsTipus.add("Servei");
        tipusProducte.setItems(opcionsTipus);
        taulaSpecs.setVisible(false);
        
        specTitol.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
        specInfo.setStyle("-fx-background-color: #abe6fc;");
        
        specTitol.setCellValueFactory(
                new PropertyValueFactory<ItemProducteElectronic,String>("titol")
        );
        specInfo.setCellValueFactory(
                new PropertyValueFactory<ItemProducteElectronic,String>("info")
        );
        specInfo.setCellFactory(TextFieldTableCell.forTableColumn());
        specInfo.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ItemProducteElectronic, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ItemProducteElectronic, String> t) {
                        ((ItemProducteElectronic) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setInfo(t.getNewValue());
                        TablePosition pos = t.getTablePosition();
                        if(pos.getRow() < (taulaSpecs.getItems().size()-1)) {
                            t.getTableView().requestFocus();
                            t.getTableView().getSelectionModel().select(pos.getRow()+1, pos.getTableColumn());
                            TablePosition tp= t.getTableView().getFocusModel().getFocusedCell();
                            Platform.runLater(()->t.getTableView().edit(tp.getRow(), tp.getTableColumn()));
                        }
                        else guardar.requestFocus();
                    }
                }
        );
    }   
    
    FXMLProductesController(AnchorPane pare, Connection conn){
        panellPare= pare;
        conexio= conn;
    }
    
    private List<ItemProducteElectronic> carregaSpecs(){
        List llistaAux= new ArrayList<>();
        llistaAux.add(new ItemProducteElectronic("Marca",""));
        llistaAux.add(new ItemProducteElectronic("Any",""));
        llistaAux.add(new ItemProducteElectronic("Disseny",""));
        llistaAux.add(new ItemProducteElectronic("CPU",""));
        llistaAux.add(new ItemProducteElectronic("GPU",""));
        llistaAux.add(new ItemProducteElectronic("Pantalla",""));
        llistaAux.add(new ItemProducteElectronic("RAM",""));
        llistaAux.add(new ItemProducteElectronic("ROM",""));
        llistaAux.add(new ItemProducteElectronic("Càmara",""));
        llistaAux.add(new ItemProducteElectronic("Bateria",""));
        llistaAux.add(new ItemProducteElectronic("SIM",""));
        llistaAux.add(new ItemProducteElectronic("NFC",""));
        llistaAux.add(new ItemProducteElectronic("USB",""));
        llistaAux.add(new ItemProducteElectronic("Sensors",""));
        return llistaAux;
    }
    
    private void desocultarCamps(ElemVendible eV){
        if(eV != null){
            descBuscar.setText(eV.getDescripcio());
            if(eV instanceof Producte){
                if(eV instanceof ProducteElectronic) tipusProducte.getSelectionModel().select(0);
                else tipusProducte.getSelectionModel().select(1);
                Integer stock= ((Producte)eV).getStock();
                this.stock.setText(stock.toString());
                Integer eban= ((Producte)eV).getEBAN();
                if(eban == null) ebanBuscar.setText("");
                else ebanBuscar.setText(((Producte)eV).getEBAN().toString());
                ebanBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
                ebanBuscar.setEditable(true);
            }
            else{
                tipusProducte.getSelectionModel().select(2);
                ebanBuscar.setEditable(false);
            }
            eliminar.setDisable(false);
            dataElectronic.clear();
        }
        else{
            labelTipus.setVisible(true);
            tipusProducte.setVisible(true);
        }
        
        descBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        descBuscar.setEditable(true);
        descBuscar.requestFocus();
        codiBuscar.setEditable(false);
        codiBuscar.setStyle("-fx-border-color: #CCC7BA; -fx-background-color: #CCC7BA; -fx-border-radius: 4");
                
        guardar.setDisable(false);
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
        inputStage.initOwner(codiBuscar.getScene().getWindow());
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getElementCercable();
    }
    
    @FXML
    private void accioBuscarDescripcioProducte(KeyEvent event){
        if(codiBuscar.isEditable() && event.getCode() == KeyCode.ENTER){
            dataElectronic.clear();
            String desc= descBuscar.getText();
            try{
                List<ElementCercable> list= SQL.seleccionaProductesCercables(conexio, "descripcio", desc);
                ElementCercable aux;
                if(list.isEmpty()) throw new Exception();
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                if(aux != null){
                    ElemVendible eV= SQL.seleccionaElemVendible(conexio, aux.getPrincipal());
                    codiBuscar.setText(eV.getCodi());
                    desocultarCamps(eV);
                    if(eV instanceof ProducteElectronic){
                        infoTipusProducte.setText("PRODUCTE ELECTRONIC");
                        ProducteElectronic auxElect=(ProducteElectronic)eV;
                        Iterator<ItemProducteElectronic> it= auxElect.getItemsIterator();
                        while(it.hasNext()) dataElectronic.add(it.next());
                        taulaSpecs.setItems(dataElectronic);
                        taulaSpecs.setVisible(true);
                    }
                    else if(eV instanceof Producte) infoTipusProducte.setText("PRODUCTE VARIS");
                    else infoTipusProducte.setText("SERVEI");
                    infoTipusProducte.setVisible(true);
                }
                else descBuscar.clear();
                descBuscar.requestFocus();
            } catch (Exception ex) {
                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
                descBuscar.clear();
            }
        }
    }
    
    @FXML
    private void accioBuscarCodiProducte(KeyEvent event){
        dataElectronic.clear();
        if(event.getCode() == KeyCode.ENTER){
            String codi= codiBuscar.getText();
            try {
                List<ElementCercable> list= SQL.seleccionaProductesCercables(conexio, "codi", codi);
                ElementCercable aux;
                if(list.isEmpty()){
                    if(codi.startsWith("?") || codi.endsWith("?")) throw new Exception("no");
                    else throw new Exception("si");
                }
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                if(aux != null){
                    ElemVendible eV= SQL.seleccionaElemVendible(conexio, aux.getPrincipal());
                    codiBuscar.setText(eV.getCodi());
                    desocultarCamps(eV);
                    if(eV instanceof ProducteElectronic){
                        infoTipusProducte.setText("PRODUCTE ELECTRONIC");
                        ProducteElectronic auxElect=(ProducteElectronic)eV;
                        Iterator<ItemProducteElectronic> it= auxElect.getItemsIterator();
                        while(it.hasNext()) dataElectronic.add(it.next());
                        taulaSpecs.setItems(dataElectronic);
                        taulaSpecs.setVisible(true);
                    }
                    else if(eV instanceof Producte) infoTipusProducte.setText("PRODUCTE VARIS");
                    else infoTipusProducte.setText("SERVEI");
                    infoTipusProducte.setVisible(true);
                    descBuscar.requestFocus();
                }
                else codiBuscar.clear();
            } catch (Exception ex) {
                if(ex.getMessage().equals("si")){
                    if(PopupAlerta.mostrarConfirmacio("Producte no trobat", "Vols donar d'alta la referència \""+codi+"\"?")) desocultarCamps(null);
                    else codiBuscar.clear();
                }
                else{
                    PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
                    codiBuscar.clear();
                }
            }
        }
    }
    
    private void guardarElemVendible(ElemVendible eV) throws SQLException{
        if(PopupAlerta.mostrarConfirmacio("Confirmar acció", "Vols salvar els canvis?")){
            if(!SQL.existeixElementVendible(conexio, eV.getCodi())) SQL.afegirElemVendible(conexio, eV);
            else SQL.actualitzarElemVendible(conexio, eV);
        }
    }
    
    @FXML
    private void accioGuardar(ActionEvent event){
        try{
            String tipusP= (String)tipusProducte.getSelectionModel().getSelectedItem();
            String novaDesc= descBuscar.getText();
            String codi= codiBuscar.getText();
            if(tipusP.equals("Electrònic")){
                Integer eban;
                if(ebanBuscar.getText().isEmpty()) eban= null; 
                else eban= Integer.parseInt(ebanBuscar.getText());
                ProducteElectronic pE= new ProducteElectronic(codi, novaDesc, eban, 0, dataElectronic);
                guardarElemVendible(pE);
            }
            else if(tipusP.equals("Varis")){
                Integer eban;
                if(ebanBuscar.getText().isEmpty()) eban= null; 
                else eban= Integer.parseInt(ebanBuscar.getText());
                Producte prod= new Producte(codi, novaDesc, eban, 0);
                guardarElemVendible(prod);
            }
            else{
                ElemVendible eV= new ElemVendible(codi, novaDesc);
                guardarElemVendible(eV);
            }
            PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Acció Completada", "S'ha guardat el producte correctament");
            cancelar();
        } catch(NumberFormatException e){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Codi de barres invàlid, revisa'l");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Revisi els camps");
        } catch(NullPointerException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Si us plau, marqui de quin tipus és el producte");
        }
    }
    
    private void cancelar(){
        infoTipusProducte.setVisible(false);
        labelTipus.setVisible(false);
        tipusProducte.setVisible(false);
        tipusProducte.getSelectionModel().clearSelection();
        guardar.setDisable(true);
        eliminar.setDisable(true);
        taulaSpecs.setVisible(false);
        dataElectronic.clear();
        dataElectronic.addAll(carregaSpecs());
        taulaSpecs.setItems(dataElectronic);
        
        codiBuscar.clear();
        codiBuscar.setEditable(true);
        codiBuscar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDBAB3; -fx-border-radius: 4");
        
        descBuscar.clear();
        descBuscar.setEditable(true);
        descBuscar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDBAB3; -fx-border-radius: 4");
        
        ebanBuscar.clear();
        ebanBuscar.setEditable(false);
        ebanBuscar.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        stock.clear();
        
        codiBuscar.requestFocus();
    }
    
    @FXML
    private void accioCancelar(ActionEvent event){
        cancelar();
    }
    
    @FXML
    private void accioEliminar(ActionEvent event){
        if(PopupAlerta.mostrarConfirmacio("Atenció!", "Segur que vols eliminar el producte '"+codiBuscar.getText()+"'?")){
            try {
                SQL.eliminarElemVendible(conexio, codiBuscar.getText(), (String)tipusProducte.getSelectionModel().getSelectedItem());
                PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Confirmació", "S'ha eliminat el producte correctament");
                cancelar();
            } catch (SQLException ex) {
                PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor 404", ex.getMessage());
            }
        }
    }
}
