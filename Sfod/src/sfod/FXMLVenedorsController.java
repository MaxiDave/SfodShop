package sfod;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLVenedorsController implements Initializable {

    @FXML
    private TextField buscarVenedor;
    
    @FXML
    private TextField nomCompletVenedor;
    
    @FXML
    private TextField telefonVenedor;
    
    @FXML
    private TextField emailVenedor;
    
    @FXML
    private TextField codiPaisVenedor;
    
    @FXML
    private TextField nomPaisVenedor;
    
    @FXML
    private TextField direccioVenedor;
    
    @FXML
    private TextField poblacioVenedor;
    
    @FXML
    private TextField codiPostalVenedor;
    
    @FXML
    private TextField provinciaVenedor;
    
    @FXML
    private ChoiceBox tractamentVenedor;
    
    @FXML
    private TextArea informacioAddicionalVenedor;
    
    @FXML
    private Button guardarVenedor;
    
    @FXML
    private Button cancelarVenedor;
    
    @FXML
    private Button eliminarVenedor;

    private final Connection conexio;
    
    @FXML
    private AnchorPane panell;
    
    private AnchorPane panellPare;
    private ObservableList<String> opcionsTractamentVenedor= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //S'inicialitza el tamany del panell
        panell.setPrefHeight(panellPare.getHeight()*0.95);
        panell.setPrefWidth(panellPare.getWidth()*0.99);
        Platform.runLater(()->panell.getScene().getWindow().setX(panellPare.getLayoutX()));
        Platform.runLater(()->panell.getScene().getWindow().setY(panellPare.getLayoutY()+25));
        
        //Es carregen les imatges
        Image cancelButon = new Image(getClass().getResourceAsStream("cancelar.png"));
        cancelarVenedor.setGraphic(new ImageView(cancelButon));
        Image guardarButon = new Image(getClass().getResourceAsStream("guardar.png"));
        guardarVenedor.setGraphic(new ImageView(guardarButon));
        Image eliminarButon = new Image(getClass().getResourceAsStream("eliminar.png"));
        eliminarVenedor.setGraphic(new ImageView(eliminarButon));
        
        opcionsTractamentVenedor.add("Sr.");
        opcionsTractamentVenedor.add("Sra.");
        tractamentVenedor.setItems(opcionsTractamentVenedor);
    }    
    
    public FXMLVenedorsController(Connection conn, AnchorPane pare){
        conexio= conn;
        panellPare= pare;
    }
    
    private void desocultarCamps(){
        nomCompletVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        nomCompletVenedor.setEditable(true);
        tractamentVenedor.setDisable(false);
        telefonVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        telefonVenedor.setEditable(true);
        emailVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        emailVenedor.setEditable(true);
        direccioVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        direccioVenedor.setEditable(true);
        codiPostalVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        codiPostalVenedor.setEditable(true);
        poblacioVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        poblacioVenedor.setEditable(true);
        provinciaVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        provinciaVenedor.setEditable(true);
        codiPaisVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        codiPaisVenedor.setEditable(true);
        nomPaisVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        nomPaisVenedor.setEditable(true);
        informacioAddicionalVenedor.setStyle("-fx-border-color: #EBD298; -fx-background-color: #F7E5BA; -fx-border-radius: 4");
        informacioAddicionalVenedor.setEditable(true);
                
        buscarVenedor.setEditable(false);
        buscarVenedor.setStyle("-fx-border-color: #CCC7BA; -fx-background-color: #CCC7BA; -fx-border-radius: 4");
        
        nomCompletVenedor.requestFocus();
        guardarVenedor.setDisable(false);
    }
    
    private void desocultarCamps(Venedor aux){
        nomCompletVenedor.setText(aux.getNomComplet());
        if(aux.getTractament().equals("Sr.")) tractamentVenedor.getSelectionModel().select(0);
        else tractamentVenedor.getSelectionModel().select(1);
        telefonVenedor.setText(aux.getTelefon().toString());
        emailVenedor.setText(aux.getEmail());
        direccioVenedor.setText(aux.getDireccio());
        codiPostalVenedor.setText(aux.getCodiPostal().toString());
        poblacioVenedor.setText(aux.getPoblacio());
        provinciaVenedor.setText(aux.getProvincia());
        codiPaisVenedor.setText(aux.getCodiPais());
        nomPaisVenedor.setText(aux.getNomPais());
        informacioAddicionalVenedor.setText(aux.getInformacioAddicional());
        eliminarVenedor.setDisable(false);
        desocultarCamps();
    }
    
    private ElementCercable mostraPopup(List<ElementCercable> list) throws IOException{
        ObservableList<ElementCercable> listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"));
        FXMLPopupControler controler= new FXMLPopupControler(listObs, "Número", "Nom Complet");
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
    
    @FXML
    public void accioBuscarVenedor(KeyEvent event){
        try{
            if(event.getCode() == KeyCode.ENTER){
                String codi= buscarVenedor.getText();
                List<ElementCercable> list= SQL.seleccionaVenedorsCercables(conexio, codi);
                ElementCercable aux;
                if(list.isEmpty()){
                    if(codi.equals("*")) throw new Exception("no");
                    else throw new Exception("si");
                }
                else if(list.size() > 1) aux= mostraPopup(list);
                else aux= list.get(0);
                if(aux != null){
                    Venedor ven= SQL.seleccionaVenedor(conexio, aux.getPrincipal());
                    buscarVenedor.setText(ven.getNum().toString());
                    desocultarCamps(ven);
                }
                else buscarVenedor.clear();
            }
        } catch(NumberFormatException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error al cercar Venedor", "Introdueixi un nombre vàlid");
        } catch (Exception ex) {
                if(ex.getMessage().equals("si")){
                    if(PopupAlerta.mostrarConfirmacio("Venedor no trobat", "Vols donar d'alta al venedor número \""+buscarVenedor.getText()+"\"?")) desocultarCamps();
                    else buscarVenedor.clear();
                }
                else{
                    PopupAlerta.mostraAlerta(Alert.AlertType.WARNING, "No hi ha venedors", "No s'ha trobat cap referència");
                    buscarVenedor.clear();
                }
        }
    }
    
    @FXML
    private void accioGuardarVenedor(ActionEvent event){
        try{
            Venedor ven= new Venedor(Integer.parseInt(buscarVenedor.getText()), nomCompletVenedor.getText(), (String)tractamentVenedor.getSelectionModel().getSelectedItem(),
                Integer.parseInt(telefonVenedor.getText()), emailVenedor.getText(), direccioVenedor.getText(), Integer.parseInt(codiPostalVenedor.getText()), 
                poblacioVenedor.getText(), provinciaVenedor.getText(), codiPaisVenedor.getText(), nomPaisVenedor.getText(), informacioAddicionalVenedor.getText());
            if(PopupAlerta.mostrarConfirmacio("Confirmar acció", "Vols salvar els canvis?")){
                if(SQL.existeixVenedor(conexio, ven.getNum())) SQL.actualitzar(conexio, ven);
                else SQL.afegir(conexio, ven);
                cancelarVenedor();
            }
        } catch(NumberFormatException e){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Alguns camps està omplerts incorrectament, si us plau revisa'ls");
        } catch (SQLException ex) {
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor 404", ex.getMessage());
        } catch(NullPointerException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "No ha seleccionat el tractament del Venedor");
        } catch(Exception ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", "Algún dels camps introduits és massa llarg");
        }
    }
    
    private void cancelarVenedor(){
        tractamentVenedor.setDisable(true);
        tractamentVenedor.getSelectionModel().clearSelection();
        guardarVenedor.setDisable(true);
        eliminarVenedor.setDisable(true);
        
        buscarVenedor.clear();
        buscarVenedor.setEditable(true);
        buscarVenedor.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #BDBAB3; -fx-border-radius: 4");
        
        nomCompletVenedor.clear();
        nomCompletVenedor.setEditable(false);
        nomCompletVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        telefonVenedor.clear();
        telefonVenedor.setEditable(false);
        telefonVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        emailVenedor.clear();
        emailVenedor.setEditable(false);
        emailVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        direccioVenedor.clear();
        direccioVenedor.setEditable(false);
        direccioVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        codiPostalVenedor.clear();
        codiPostalVenedor.setEditable(false);
        codiPostalVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        poblacioVenedor.clear();
        poblacioVenedor.setEditable(false);
        poblacioVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        provinciaVenedor.clear();
        provinciaVenedor.setEditable(false);
        provinciaVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        codiPaisVenedor.clear();
        codiPaisVenedor.setEditable(false);
        codiPaisVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        nomPaisVenedor.clear();
        nomPaisVenedor.setEditable(false);
        nomPaisVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        informacioAddicionalVenedor.clear();
        informacioAddicionalVenedor.setEditable(false);
        informacioAddicionalVenedor.setStyle("-fx-background-color: #CCC7BA; -fx-border-color: #CCC7BA; -fx-border-radius: 4");
        
        buscarVenedor.requestFocus();
    }
    
    @FXML
    public void accioCancelarVenedor(ActionEvent event){
        cancelarVenedor();
    }
    
    @FXML
    public void accioEliminarVenedor(ActionEvent event){
        try{
            String sexeArticle;
            String sexeVenedor;
            if(tractamentVenedor.getSelectionModel().getSelectedItem().equals("Sr.")){
                sexeArticle= "el";
                sexeVenedor= "venedor";
            }
            else{
                sexeArticle= "la";
                sexeVenedor= "venedora";
            }
            if(PopupAlerta.mostrarConfirmacio("Eliminar Venedor/a", "Segur que vols eliminar "+sexeArticle+" "+sexeVenedor+" \""+buscarVenedor.getText()+": "+nomCompletVenedor.getText()+"\" ?")){
                SQL.eliminarVenedor(conexio, Integer.parseInt(buscarVenedor.getText()));
                cancelarVenedor();
                PopupAlerta.mostrarConfirmacio("Eliminar Venedor/a", "S'ha eliminat "+sexeArticle+" "+sexeVenedor+" correctament");
            }
        }
        catch(SQLException ex){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error de Servidor 404", ex.getMessage());
        }
    }
}