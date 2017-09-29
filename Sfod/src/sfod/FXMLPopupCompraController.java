package sfod;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;


public class FXMLPopupCompraController implements Initializable {
    
    private final ObservableList<Compra> elements;
    private Compra seleccionada;

    @FXML
    private TableView taulaCompres;
    
    @FXML
    private TableColumn columnaFactura;
    
    @FXML
    private TableColumn columnaProveidor;
    
    @FXML
    private TableColumn columnaVenedor;
    
    @FXML
    private TableColumn columnaImport;
    
    @FXML
    private TableColumn columnaDataCompra;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->taulaCompres.getSelectionModel().select(0));
        
        taulaCompres.setItems(elements);
        
        taulaCompres.setRowFactory( tv -> {
            TableRow<Compra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    seleccionada= row.getItem();
                    Stage stage = (Stage) taulaCompres.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });
        
        columnaFactura.setStyle("-fx-alignment: center; -fx-background-color: #b2cfff;");
        columnaProveidor.setStyle("-fx-background-color: #abe6fc;");
        columnaVenedor.setStyle("-fx-background-color: #abe6fc;");
        columnaImport.setStyle("-fx-background-color: #abe6fc;");
        columnaDataCompra.setStyle("-fx-background-color: #abe6fc;");

        columnaFactura.setCellValueFactory(
                new PropertyValueFactory<Compra,Integer>("num")
        );
        columnaFactura.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<Compra, Integer>() {

                        @Override
                        public void updateItem(Integer item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextAlignment(TextAlignment.CENTER);
                                // Get fancy and change color based on data
                                // if(item.contains("@")) this.setTextFill(Color.BLUEVIOLET);
                                setText(item.toString());
                            }
                        }
                    };
                }
            });
        
        columnaProveidor.setCellValueFactory(
                new PropertyValueFactory<Compra,String>("proveidor")
        );
        columnaProveidor.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<Compra, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setTextAlignment(TextAlignment.CENTER);
                            // Get fancy and change color based on data
                            // if(item.contains("@")) this.setTextFill(Color.BLUEVIOLET);
                            setText(item);
                        }
                    }
                };
            }
        });

        columnaVenedor.setCellValueFactory(
                new PropertyValueFactory<Compra,String>("venedor")
        );
        columnaVenedor.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<Compra, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextAlignment(TextAlignment.CENTER);
                                // Get fancy and change color based on data
                                // if(item.contains("@")) this.setTextFill(Color.BLUEVIOLET);
                                setText(item);
                            }
                        }
                    };
                }
            });
        
        columnaImport.setCellValueFactory(
                new PropertyValueFactory<Compra,Double>("import")
        );
        columnaImport.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<Compra, Double>() {

                        @Override
                        public void updateItem(Double item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextAlignment(TextAlignment.CENTER);
                                // Get fancy and change color based on data
                                // if(item.contains("@")) this.setTextFill(Color.BLUEVIOLET);
                                setText(item.toString());
                            }
                        }
                    };
                }
            });
        
        columnaDataCompra.setCellValueFactory(
                new PropertyValueFactory<Compra,String>("data")
        );
        columnaDataCompra.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<Compra, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextAlignment(TextAlignment.CENTER);
                                // Get fancy and change color based on data
                                // if(item.contains("@")) this.setTextFill(Color.BLUEVIOLET);
                                setText(item);
                            }
                        }
                    };
                }
            });
    }    
    
    @FXML
    public void eventKeyEnter(KeyEvent event){
        if (!taulaCompres.getSelectionModel().isEmpty() && event.getCode() == KeyCode.ENTER) {
            seleccionada= (Compra) taulaCompres.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) taulaCompres.getScene().getWindow();
            stage.close();
        }
    }
    
    public FXMLPopupCompraController(ObservableList<Compra> llista){
        elements= llista;
    }
    
    public Compra getCompra(){
        return seleccionada;
    }
}
