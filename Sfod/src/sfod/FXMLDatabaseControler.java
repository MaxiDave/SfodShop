package sfod;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FXMLDatabaseControler implements Initializable {
    
    @FXML
    private Label sqlError;
    
    @FXML
    private TextArea sqlComanda;

    private final Connection conexio;
    
    public FXMLDatabaseControler(Connection con){
        conexio= con;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sqlError.setText("");
    }    

    @FXML
    private void handleButtonAction(ActionEvent event){
        try {
            Stage stage;
            Parent root;
            Statement stmt = conexio.createStatement();
            System.out.println(sqlComanda.getText());
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
            sqlError.setVisible(false);
        } catch (SQLException ex) {
            sqlError.setText(ex.getMessage());
            sqlError.setVisible(true);
        }
    }
}
