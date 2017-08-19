package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLDatabaseControler implements Initializable {
    
    private ObservableList<Producte> data = FXCollections.observableArrayList();
    
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
    private TextField afegirCodi;
    
    @FXML
    private TextField afegirEBAN;
    
    @FXML
    private TextField afegirDesc;
    
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
            /*
            ResultSet rs = stmt.executeQuery(sqlComanda.getText());
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
            }
            */
            stmt.executeUpdate(sqlComanda.getText());
            sqlError.setText("Comanda executada correctament");
        } catch (SQLException ex) {
            sqlError.setText(ex.getMessage());
        }
        sqlError.setVisible(true);
    }
}
