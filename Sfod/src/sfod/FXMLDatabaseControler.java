package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDatabaseControler implements Initializable {
    
    private ObservableList<ItemProducteElectronic> dataElectronic= FXCollections.observableArrayList();
    private ObservableList<String> opcionsTipus= FXCollections.observableArrayList();
    private ObservableList<Venedor> venedorsLlista;
    private ObservableList<Proveidor> proveidorsLlista;
    
    @FXML
    private TextArea sqlComanda;
    
    @FXML
    private TableView specs;
    
    @FXML
    private TableColumn specTitol;
    
    @FXML
    private TableColumn specInfo;
    
    @FXML
    private TableView venedorsTaula;
    
    @FXML
    private TableColumn venedorsNum;
    
    @FXML
    private TableColumn venedorsNom;
    
    @FXML 
    private TableColumn venedorsCognom1;
    
    @FXML
    private TableColumn venedorsCognom2;
    
    @FXML
    private TableColumn venedorsTelefon;
    
    @FXML
    private TableColumn venedorsEmail;
    
    @FXML
    private TableView proveidorsTaula;
    
    @FXML
    private TableColumn proveidorsNum;
    
    @FXML
    private TableColumn proveidorsNom;
    
    @FXML
    private TableColumn proveidorsEspecialitat;
    
    @FXML
    private TableColumn proveidorsEmail;
    
    @FXML
    private TableColumn proveidorsTempsEntrega;
    
    @FXML
    private TextField codiBuscar;
    
    @FXML
    private TextField descBuscar;
    
    @FXML
    private TextField ebanBuscar;
    
    @FXML
    private Button guardar;
    
    @FXML
    private Label labelTipus;
    
    @FXML
    private ChoiceBox tipusProducte;
    
    @FXML
    private Label infoTipusProducte;
    
    @FXML
    private TextField venedorsAfegirNum;
    
    @FXML
    private TextField venedorsAfegirNom;
    
    @FXML
    private TextField venedorsAfegirCognom1;
    
    @FXML
    private TextField venedorsAfegirCognom2;
    
    @FXML
    private TextField venedorsAfegirTelefon;
    
    @FXML
    private TextField venedorsAfegirEmail;
    
    @FXML
    private TextField proveidorsAfegirNum;
    
    @FXML
    private TextField proveidorsAfegirNom;
    
    @FXML
    private TextField proveidorsAfegirEspecialitat;
    
    @FXML
    private TextField proveidorsAfegirEmail;
    
    @FXML
    private TextField proveidorsAfegirTempsEntrega;
    
    private final Connection conexio;
    
    public FXMLDatabaseControler(Connection con){
        conexio= con;
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            tipusProducte.getSelectionModel().selectedIndexProperty().addListener(new
                    ChangeListener<Number>(){
                        public void changed(ObservableValue ov,
                                Number value, Number new_value){
                            if(new_value.intValue() == 0){
                                dataElectronic.addAll(carregaSpecs());
                                specs.setItems(dataElectronic);
                                specs.setVisible(true);
                            }
                            else specs.setVisible(false);
                        }
                    }
            );
            
            infoTipusProducte.setVisible(false);
            tipusProducte.setVisible(false);
            labelTipus.setVisible(false);
            opcionsTipus.add("Electrònic");
            opcionsTipus.add("Servei");
            tipusProducte.setItems(opcionsTipus);
            specs.setVisible(false);
            
            specTitol.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
            specTitol.setCellValueFactory(
                    new PropertyValueFactory<ItemProducteElectronic,String>("titol")
            );
            specInfo.setCellValueFactory(
                    new PropertyValueFactory<ItemProducteElectronic,String>("info")
            );
            specInfo.setCellFactory(TextFieldTableCell.forTableColumn());
            specInfo.setOnEditCommit(
                    new EventHandler<CellEditEvent<ItemProducteElectronic, String>>() {
                        @Override
                        public void handle(CellEditEvent<ItemProducteElectronic, String> t) {
                            ((ItemProducteElectronic) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    ).setInfo(t.getNewValue());
                        }
                    }
            );
            
            venedorsNum.setStyle("-fx-background-color: #b2cfff;");
            venedorsNum.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("num")
            );
            
            venedorsNom.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("nom")
            );
            
            venedorsCognom1.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("cognom1")
            );
            
            venedorsCognom2.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("cognom2")
            );
            
            venedorsTelefon.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("telefon")
            );
            venedorsTelefon.setCellFactory(TextFieldTableCell.forTableColumn());
            venedorsTelefon.setOnEditCommit(
                    new EventHandler<CellEditEvent<Venedor, String>>() {
                        @Override
                        public void handle(CellEditEvent<Venedor, String> t) {
                            Venedor aux= ((Venedor) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    );
                            aux.setTelefon(t.getNewValue());
                            try {
                                SQL.actualitzarTelefonVenedor(conexio,aux);
                            } catch (SQLException ex) {
                                
                            }
                        }
                    }
            );
            
            venedorsEmail.setCellValueFactory(
                    new PropertyValueFactory<Venedor,String>("email")
            );
            venedorsEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            venedorsEmail.setOnEditCommit(
                    new EventHandler<CellEditEvent<Venedor, String>>() {
                        @Override
                        public void handle(CellEditEvent<Venedor, String> t) {
                            Venedor aux= ((Venedor) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    );
                            aux.setEmail(t.getNewValue());
                            try {
                                SQL.actualitzarEmailVenedor(conexio,aux);
                            } catch (SQLException ex) {
                                
                            }
                        }
                    }
            );
            
            proveidorsNum.setStyle("-fx-background-color: #b2cfff;");
            proveidorsNum.setCellValueFactory(
                    new PropertyValueFactory<Proveidor,String>("num")
            );
            
            proveidorsNom.setCellValueFactory(
                    new PropertyValueFactory<Proveidor,String>("nom")
            );
            
            proveidorsEspecialitat.setCellValueFactory(
                    new PropertyValueFactory<Proveidor,String>("especialitat")
            );
            proveidorsEspecialitat.setCellFactory(TextFieldTableCell.forTableColumn());
            proveidorsEspecialitat.setOnEditCommit(
                    new EventHandler<CellEditEvent<Proveidor, String>>() {
                        @Override
                        public void handle(CellEditEvent<Proveidor, String> t) {
                            Proveidor aux= ((Proveidor) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    );
                            aux.setEspecialitat(t.getNewValue());
                            try {
                                SQL.actualitzarEspecialitatProveidor(conexio,aux);
                            } catch (SQLException ex) {
                                
                            }
                        }
                    }
            );
            
            proveidorsEmail.setCellValueFactory(
                    new PropertyValueFactory<Proveidor,String>("email")
            );
            proveidorsEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            proveidorsEmail.setOnEditCommit(
                    new EventHandler<CellEditEvent<Proveidor, String>>() {
                        @Override
                        public void handle(CellEditEvent<Proveidor, String> t) {
                            Proveidor aux= ((Proveidor) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    );
                            aux.setEmail(t.getNewValue());
                            try {
                                SQL.actualitzarEmailProveidor(conexio,aux);
                            } catch (SQLException ex) {
                                
                            }
                        }
                    }
            );
            
            proveidorsTempsEntrega.setCellValueFactory(
                    new PropertyValueFactory<Proveidor,String>("tempsEntrega")
            );
            proveidorsTempsEntrega.setCellFactory(TextFieldTableCell.forTableColumn());
            proveidorsTempsEntrega.setOnEditCommit(
                    new EventHandler<CellEditEvent<Proveidor, String>>() {
                        @Override
                        public void handle(CellEditEvent<Proveidor, String> t) {
                            Proveidor aux= ((Proveidor) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                                    );
                            aux.setTempsEntrega(t.getNewValue());
                            try {
                                SQL.actualitzarTempsEntregaProveidor(conexio,aux);
                            } catch (SQLException ex) {
                                
                            }
                        }
                    }
            );
            venedorsLlista= SQL.carregarVenedors(conexio);
            proveidorsLlista= SQL.carregarProveidors(conexio);
            venedorsTaula.setItems(venedorsLlista);
            proveidorsTaula.setItems(proveidorsLlista);
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(AlertType.ERROR, "Error 404", "Format incorrecte");
        }
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
        inputStage.showAndWait();
        return controler.getProducte();
    }
    
    @FXML
    private void buscarProducte(KeyEvent event){
        dataElectronic.clear();
        if(event.getCode() == KeyCode.ENTER){
            String codi= codiBuscar.getText();
            try {
                List<Producte> list= SQL.selecionaProducte(conexio, codi);
                Producte aux;
                if(list.isEmpty()){
                    if(codi.startsWith("?") || codi.endsWith("?")) throw new Exception("no");
                    else throw new Exception("si");
                }
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                codiBuscar.setText(aux.getCodi());
                desocultarCamps(aux);
                if(aux instanceof ProducteElectronic){
                    infoTipusProducte.setText("PRODUCTE ELECTRONIC");
                    ProducteElectronic auxElect=(ProducteElectronic)aux;
                    Iterator<ItemProducteElectronic> it= auxElect.getItemsIterator();
                    while(it.hasNext()) dataElectronic.add(it.next());
                    specs.setItems(dataElectronic);
                    specs.setVisible(true);
                }
                else infoTipusProducte.setText("SERVEI");
                infoTipusProducte.setVisible(true);
            } catch (Exception ex) {
                if(ex.getMessage().equals("si")){
                    if(PopupAlerta.mostrarConfirmacio("Producte no trobat", "Vols donar d'alta la referència \""+codi+"\"?")) desocultarCamps(null);
                    else codiBuscar.clear();
                }
                else{
                    PopupAlerta.mostraAlerta(AlertType.WARNING, "Producte no trobat", "No s'ha trobat cap referència");
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
            if(novaDesc.length()>100) PopupAlerta.mostraAlerta(AlertType.ERROR, "Error 404", "Descripció massa llarga, revisa-la");
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
            PopupAlerta.mostraAlerta(AlertType.ERROR, "Error 404", "Codi de barres invàlid, revisa'l");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(AlertType.ERROR, "Error de Servidor 404", ex.getMessage());
        } catch(NullPointerException ex){
            PopupAlerta.mostraAlerta(AlertType.ERROR, "Error 404", "Tipus de Producte invàlid");
        }
    }
    
    private void cancelar(){
        infoTipusProducte.setVisible(false);
        labelTipus.setVisible(false);
        tipusProducte.setVisible(false);
        tipusProducte.getSelectionModel().clearSelection();
        guardar.setDisable(true);
        specs.setVisible(false);
        dataElectronic.clear();
        dataElectronic.addAll(carregaSpecs());
        specs.setItems(dataElectronic);
        
        codiBuscar.clear();
        codiBuscar.setEditable(true);
        codiBuscar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDBAB3; -fx-border-radius: 4");
        
        descBuscar.clear();
        descBuscar.setEditable(false);
        descBuscar.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        ebanBuscar.clear();
        ebanBuscar.setEditable(false);
        ebanBuscar.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
    }
    
    @FXML
    private void accioCancelar(ActionEvent event){
        cancelar();
    }
    
    @FXML
    private void accioExecutar(ActionEvent event){
        try {
            Statement stmt = conexio.createStatement();
            stmt.executeUpdate(sqlComanda.getText());
            PopupAlerta.mostraAlerta(AlertType.CONFIRMATION, "Comanda Executada Correctament", "");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(AlertType.ERROR, "ERROR 404", ex.getMessage());
        }
    }
    
    @FXML
    private void accioAfegirVenedor(ActionEvent event) throws SQLException{
        String num= venedorsAfegirNum.getText();
        String nom= venedorsAfegirNom.getText(); 
        String cognom1= venedorsAfegirCognom1.getText(); 
        String cognom2= venedorsAfegirCognom2.getText(); 
        String telefon= venedorsAfegirTelefon.getText(); 
        String email= venedorsAfegirEmail.getText();
        Venedor aux= new Venedor(num, nom, cognom1, cognom2, telefon, email);
        venedorsLlista.add(aux);
        venedorsTaula.setItems(venedorsLlista);
        
        SQL.afegir(conexio, aux);
        venedorsAfegirNum.clear();
        venedorsAfegirNom.clear();
        venedorsAfegirCognom1.clear();
        venedorsAfegirCognom2.clear();
        venedorsAfegirTelefon.clear();
        venedorsAfegirEmail.clear();
    }
    
        @FXML
    private void accioAfegirProveidor(ActionEvent event) throws SQLException{
        String num= proveidorsAfegirNum.getText();
        String nom= proveidorsAfegirNom.getText(); 
        String especialitat= proveidorsAfegirEspecialitat.getText(); 
        String email= proveidorsAfegirEmail.getText(); 
        String tempsEntrega= proveidorsAfegirTempsEntrega.getText(); 
        Proveidor aux= new Proveidor(num, nom, especialitat, email, tempsEntrega);
        
        if(aux.proveidorValid()){
            proveidorsLlista.add(aux);
            proveidorsTaula.setItems(proveidorsLlista);
            SQL.afegir(conexio, aux);
            proveidorsAfegirNum.clear();
            proveidorsAfegirNom.clear();
            proveidorsAfegirEspecialitat.clear();
            proveidorsAfegirEmail.clear();
            proveidorsAfegirTempsEntrega.clear();
        }
    }
}