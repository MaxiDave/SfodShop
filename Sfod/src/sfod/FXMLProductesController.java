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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    private TextArea descBuscar;
    
    @FXML
    private TextField ebanBuscar;
    
    @FXML
    private Button guardar;
    
    @FXML
    private Button cancelar;
    
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
        
        tipusProducte.getSelectionModel().selectedIndexProperty().addListener(new
                ChangeListener<Number>(){
                    public void changed(ObservableValue ov,
                            Number value, Number new_value){
                        if(new_value.intValue() == 0){
                            dataElectronic.addAll(carregaSpecs());
                            taulaSpecs.setItems(dataElectronic);
                            taulaSpecs.setVisible(true);
                        }
                        else taulaSpecs.setVisible(false);
                    }
                }
            );
        
        infoTipusProducte.setVisible(false);
        tipusProducte.setVisible(false);
        labelTipus.setVisible(false);
        opcionsTipus.add("Electrònic");
        opcionsTipus.add("Varis");
        tipusProducte.setItems(opcionsTipus);
        taulaSpecs.setVisible(false);
        
        specTitol.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
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
    
    private void desocultarCamps(Producte aux){
        if(aux != null){
            descBuscar.setText(aux.getDescripcio());
            ebanBuscar.setText(aux.getEBAN().toString());
        }
        else{
            labelTipus.setVisible(true);
            tipusProducte.setVisible(true);
        }
        descBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        descBuscar.setEditable(true);
                
        ebanBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        ebanBuscar.setEditable(true);
                
        codiBuscar.setEditable(false);
        codiBuscar.setStyle("-fx-border-color: #CCC7BA; -fx-background-color: #CCC7BA; -fx-border-radius: 4");
                
        guardar.setDisable(false);
    }
    
    private Producte mostraPopup(List<Producte> list) throws IOException{
        ObservableList<Producte> listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"));
        FXMLPopupControler controler= new FXMLPopupControler(listObs);
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
        return controler.getProducte();
    }
    
    @FXML
    private void accioBuscarDescripcioProducte(KeyEvent event){
        if(codiBuscar.isEditable() && event.getCode() == KeyCode.ENTER){
            dataElectronic.clear();
            String desc= descBuscar.getText();
            try{
                List<Producte> list= SQL.seleccionaProductes(conexio, "descripcio", desc);
                Producte aux;
                if(list.isEmpty()) throw new Exception();
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                if(aux != null){
                    codiBuscar.setText(aux.getCodi());
                    desocultarCamps(aux);
                    if(aux instanceof ProducteElectronic){
                        infoTipusProducte.setText("PRODUCTE ELECTRONIC");
                        ProducteElectronic auxElect=(ProducteElectronic)aux;
                        Iterator<ItemProducteElectronic> it= auxElect.getItemsIterator();
                        while(it.hasNext()) dataElectronic.add(it.next());
                        taulaSpecs.setItems(dataElectronic);
                        taulaSpecs.setVisible(true);
                    }
                    else infoTipusProducte.setText("PRODUCTE VARIS");
                    infoTipusProducte.setVisible(true);
                }
                else descBuscar.clear();
            } catch (Exception ex) {
                PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
                descBuscar.clear();
            }
        }
        else if(event.getCode() == KeyCode.TAB){
            if(ebanBuscar.isEditable()){
                ebanBuscar.requestFocus();
                descBuscar.setText(descBuscar.getText().substring(0, descBuscar.getText().length()));
            }
            else if(codiBuscar.isEditable()){
                codiBuscar.requestFocus();
                descBuscar.setText(descBuscar.getText().substring(0, descBuscar.getText().length()));
            }
        }
    }
    
    @FXML
    private void accioBuscarCodiProducte(KeyEvent event){
        dataElectronic.clear();
        if(event.getCode() == KeyCode.ENTER){
            String codi= codiBuscar.getText();
            try {
                List<Producte> list= SQL.seleccionaProductes(conexio, "codi", codi);
                Producte aux;
                if(list.isEmpty()){
                    if(codi.startsWith("?") || codi.endsWith("?")) throw new Exception("no");
                    else throw new Exception("si");
                }
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                if(aux != null){
                    aux= SQL.seleccionaProducte(conexio, aux.getCodi());
                    codiBuscar.setText(aux.getCodi());
                    desocultarCamps(aux);
                    if(aux instanceof ProducteElectronic){
                        infoTipusProducte.setText("PRODUCTE ELECTRONIC");
                        ProducteElectronic auxElect=(ProducteElectronic)aux;
                        Iterator<ItemProducteElectronic> it= auxElect.getItemsIterator();
                        while(it.hasNext()) dataElectronic.add(it.next());
                        taulaSpecs.setItems(dataElectronic);
                        taulaSpecs.setVisible(true);
                    }
                    else infoTipusProducte.setText("PRODUCTE VARIS");
                    infoTipusProducte.setVisible(true);
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
    
    @FXML
    private void accioGuardar(ActionEvent event){
        try{
            String novaDesc= descBuscar.getText();
            String nouEban= ebanBuscar.getText();
            if(novaDesc.length()>100) PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Descripció massa llarga, revisa-la");
            else{
                Integer intEban;
                if(nouEban.isEmpty()) intEban= 0;
                else intEban= Integer.parseInt(nouEban);
                Producte aux;
                if(SQL.existeixProducte(conexio, codiBuscar.getText())){
                    if(infoTipusProducte.getText().equals("SERVEI")){
                        aux= new Producte(codiBuscar.getText(), intEban, novaDesc);
                    }
                    else aux= new ProducteElectronic(codiBuscar.getText(), intEban, novaDesc, dataElectronic);
                }
                else{
                    if(tipusProducte.getSelectionModel().getSelectedItem().equals("Electrònic")){
                        aux= new ProducteElectronic(codiBuscar.getText(), intEban, novaDesc, dataElectronic);
                    }
                    else aux= new Producte(codiBuscar.getText(), intEban, novaDesc);
                }
                if(PopupAlerta.mostrarConfirmacio("Confirmar acció", "Vols salvar els canvis?")){
                    if(SQL.existeixProducte(conexio, aux.getCodi())) SQL.actualitzar(conexio, aux);
                    else SQL.afegir(conexio, aux);
                    cancelar();
                }
            }
        } catch(NumberFormatException e){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Codi de barres invàlid, revisa'l");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor 404", ex.getMessage());
        } catch(NullPointerException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Tipus de Producte invàlid");
        }
    }
    
    private void cancelar(){
        infoTipusProducte.setVisible(false);
        labelTipus.setVisible(false);
        tipusProducte.setVisible(false);
        tipusProducte.getSelectionModel().clearSelection();
        guardar.setDisable(true);
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
        
        codiBuscar.requestFocus();
    }
    
    @FXML
    private void accioCancelar(ActionEvent event){
        cancelar();
    }
}
