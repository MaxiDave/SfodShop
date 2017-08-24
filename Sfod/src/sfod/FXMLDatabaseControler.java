package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLDatabaseControler implements Initializable {
    
    private ObservableList<Producte> data = FXCollections.observableArrayList();
    private ObservableList<ItemProducteElectronic> dataElectronic= FXCollections.observableArrayList();
    
    @FXML
    private Label sqlError;
    
    @FXML
    private TextArea sqlComanda;
    
    @FXML
    private TableView taula;

    @FXML
    private TableColumn codi;
    
    @FXML
    private TableColumn descripcio;
    
    @FXML
    private TableColumn EBAN;
    
    @FXML
    private TableView specs;
    
    @FXML
    private TableColumn specTitol;
    
    @FXML
    private TableColumn specInfo;
    
    @FXML
    private TextField afegirCodi;
    
    @FXML
    private TextField afegirEBAN;
    
    @FXML
    private TextField afegirDesc;
    
    @FXML
    private TextField codiBuscar;
    
    @FXML
    private TextField descBuscar;
    
    @FXML
    private TextField ebanBuscar;
    
    @FXML
    private Button guardar;
    
    private final Connection conexio;
    
    public FXMLDatabaseControler(Connection con){
        conexio= con;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sqlError.setText("");
        
        codi.setCellValueFactory(
            new PropertyValueFactory<Producte,String>("codi")
        );
        descripcio.setCellValueFactory(
            new PropertyValueFactory<Producte,String>("descripcio")
        );
        EBAN.setCellValueFactory(
            new PropertyValueFactory<Producte,Integer>("EBAN")
        );
        
        specTitol.setCellValueFactory(
            new PropertyValueFactory<ItemProducteElectronic,String>("titol")
        );
        specInfo.setCellValueFactory(
            new PropertyValueFactory<ItemProducteElectronic,String>("info")
        );
        
        try {
            Statement stmt = conexio.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM producte");
            
            while(rs.next()){
                String code= rs.getString("codi");
                String des= rs.getString("descripcio");
                Integer barres= rs.getInt("EBAN");
                
                Producte aux= new Producte(code, barres, des);
                data.add(aux);
            }
        } catch (SQLException ex) {
            
        }
        
        taula.setItems(data);
        
    }
    
    private void desocultarCamps(Producte aux){
        if(aux != null){
            descBuscar.setText(aux.getDescripcio());
            ebanBuscar.setText(aux.getEBAN().toString());
        }
        
        descBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        descBuscar.setEditable(true);
                
        ebanBuscar.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        ebanBuscar.setEditable(true);
                
        codiBuscar.setEditable(false);
        codiBuscar.setStyle("-fx-border-color: #CCC7BA; -fx-background-color: #CCC7BA; -fx-border-radius: 4");
                
        guardar.setDisable(false);
    }
    @FXML
    private void buscarProducte(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            String codi= codiBuscar.getText();
            try {
                Producte aux= SQL.selecionaProducte(conexio, codi);
                desocultarCamps(aux);
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Sfod");
                alert.setHeaderText("Producte no trobat");
                alert.setContentText("Vols donar d'alta la referència \""+codi+"\"?");
               
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    desocultarCamps(null);
                } else {
                    codiBuscar.clear();
                }
            }
        }
    }
    
    private void mostraAvis(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Sfod");
        alert.setHeaderText("Error 404");
        alert.setContentText("Descripció massa llarga, revisa-la");
        alert.showAndWait();
    }
    
    @FXML
    private void guardar(ActionEvent event){
        try{
            String novaDesc= descBuscar.getText();
            String nouEban= ebanBuscar.getText();
            if(novaDesc.length()>100) mostraAvis();
            Integer intEban;
            if(nouEban.isEmpty()) intEban= 0;
            else intEban= Integer.parseInt(nouEban);
            
            Producte aux= new Producte(codiBuscar.getText(), intEban, novaDesc);
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Sfod");
            alert.setHeaderText("Confirmar acció");
            alert.setContentText("Vols salvar els canvis?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                SQL.actualitzar(conexio, aux);
                cancelar();
            } else {
                
            }
        } catch(NumberFormatException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sfod");
            alert.setHeaderText("Error 404");
            alert.setContentText("Codi de barres invàlid, revisa'l");
            alert.showAndWait();
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sfod");
            alert.setHeaderText("Error 404");
            alert.setContentText("Sembla que hi ha un problema de connexió al servidor");
            alert.showAndWait();
        }
    }
    
    private void cancelar(){
        guardar.setDisable(true);
        
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
    private void cancelar(ActionEvent event){
        cancelar();
    }

    @FXML
    private void insertarProducte(ActionEvent event) throws SQLException{
        Producte aux= new Producte(afegirCodi.getText(), Integer.parseInt(afegirEBAN.getText()), afegirDesc.getText());
        data.add(aux);
        afegirCodi.clear();
        afegirEBAN.clear();
        afegirDesc.clear();
        
        SQL.afegir(conexio, aux);
    }
    
    @FXML
    private void executarComanda(ActionEvent event){
        try {
            Stage stage;
            Parent root;
            Statement stmt = conexio.createStatement();
            stmt.executeUpdate(sqlComanda.getText());
            sqlError.setText("Comanda executada correctament");
        } catch (SQLException ex) {
            sqlError.setText(ex.getMessage());
        }
        sqlError.setVisible(true);
    }
}
