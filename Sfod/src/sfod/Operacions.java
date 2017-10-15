package sfod;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class Operacions {
    
    public static Boolean valid(String camp, Integer espai){
        return !camp.isEmpty() && camp.length() <= espai;
    }
    
    public static Boolean valid(Double camp, Integer espai){
        long valor= 1;
        Boolean continuar= true;
        Integer i=0;
        while(continuar && i<espai){
            valor*= 10;
            i++;
            continuar= camp >= valor;
        }
        return !continuar;
    }
    
    private static TablePosition novaRequest(TableView tV, TablePosition origen, TableColumn col){
        tV.requestFocus();
        tV.getSelectionModel().select(origen.getRow(), col);
        TablePosition nova= tV.getFocusModel().getFocusedCell();
        return nova;
    }
    
    public static void requestFocus(TableView tV, TablePosition origen, TableColumn col){
        TablePosition nova= novaRequest(tV, origen, col);
        Platform.runLater(()->tV.edit(nova.getRow(), nova.getTableColumn()));
    }
    
    public static void requestSelect(TableView tV, TablePosition origen, TableColumn col){
        TablePosition nova= novaRequest(tV, origen, col);
        tV.edit(nova.getRow(), nova.getTableColumn());
    }
    
    public static void requestFocusSeguent(TableView tV, TablePosition origen, TableColumn col){
        TablePosition nova= novaRequest(tV, origen, col);
        Platform.runLater(()->tV.edit(nova.getRow()+1, nova.getTableColumn()));
    }
    
    public static ElementCercable mostraPopupEC(List list, Window finestra, String nomPrincipal, String nomSecundari) throws IOException{
        ObservableList listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(Operacions.class.getResource("FXMLPopup.fxml"));
        FXMLPopupControler controler= new FXMLPopupControler(listObs, nomPrincipal, nomSecundari);
        loader.setController(controler);
        Parent newScene;
        newScene = loader.load();

        Stage inputStage = new Stage();
        inputStage.initModality(Modality.WINDOW_MODAL);
        inputStage.initOwner(finestra);
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(Operacions.class.getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getElementCercable();
    }
    
    public static Compra mostraPopupCompra(List list, Window finestra) throws IOException{
        ObservableList listObs= FXCollections.observableArrayList();
        listObs.addAll(list);
        FXMLLoader loader = new FXMLLoader(Operacions.class.getResource("FXMLPopupCompra.fxml"));
        FXMLPopupCompraController controler= new FXMLPopupCompraController(listObs);
        loader.setController(controler);
        Parent newScene;
        newScene = loader.load();

        Stage inputStage = new Stage();
        inputStage.initModality(Modality.WINDOW_MODAL);
        inputStage.initOwner(finestra);
        inputStage.setScene(new Scene(newScene));
        inputStage.getIcons().add(new Image(Operacions.class.getResourceAsStream("logo.png")));
        inputStage.setTitle("Buscador");
        inputStage.showAndWait();
        return controler.getCompra();
    }
}