package sfod;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FXMLEmailSenderController implements Initializable {

    @FXML
    private TextField assumpte;
    
    @FXML
    private TextField receptor;
    
    @FXML
    private TextField adjunts;
    
    @FXML
    private TextArea cos;
    
    @FXML
    private Button enviar;
    
    private final Properties prop= new Properties();
    private Session sessio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prop.put("mail.smtp.host", "smtp.gmail.com");
	prop.put("mail.smtp.starttls.enable", "true");		
        prop.put("mail.smtp.port", "25");
	prop.put("mail.smtp.auth", "true");
 
	sessio= Session.getInstance(prop,
            new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("sfodshop@gmail.com", "Monster13");
            }});
        
        sessio.setDebug(true);
    }    
    
    private void enviarMissatge(){
        try{
            MimeMessage missatge = new MimeMessage(sessio);
            missatge.setFrom(new InternetAddress("sfodshop@gmail.com"));
            missatge.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor.getText()));
            missatge.setSubject(assumpte.getText());
            missatge.setText(cos.getText());
            Transport.send(missatge);
            
            PopupAlerta.mostrarConfirmacio("Acció completada", "Email enviat correctament");
        }catch (MessagingException me){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", me.getMessage());
	}
    }

    @FXML
    private void accioEnviar(ActionEvent event) {
        if(receptor.getText().isEmpty()) PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404: Camp invàlid", "Si us plau, introdueix un receptor");
        else{
            if(assumpte.getText().isEmpty() && PopupAlerta.mostrarConfirmacio("Atenció", "Vols enviar un missatge sense assumpte?")){
                enviarMissatge();
            }
            else if(cos.getText().isEmpty() && PopupAlerta.mostrarConfirmacio("Atenció", "Vols enviar un missatge sense cos?")){
                enviarMissatge();
            }
            else enviarMissatge();
        }
    }
}