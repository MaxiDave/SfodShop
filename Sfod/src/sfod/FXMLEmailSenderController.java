package sfod;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class FXMLEmailSenderController implements Initializable {
    
    private final String signatura= "<html>------------------ <p><b>Sfod SL.</b></p><i><p>sfodshop@gmail.com</p><p>maxidave13@gmail.com - David</p></i></html>";

    @FXML
    private TextField assumpte;
    
    @FXML
    private ComboBox receptor;
    
    @FXML
    private TextField adjunts;
    
    @FXML
    private HTMLEditor cos;
    
    @FXML
    private Button enviar;
    
    @FXML 
    private Button navega;
    
    @FXML
    private Button esborra;
    
    private final Properties prop= new Properties();
    private Session sessio;
    private String textAdjunts= new String();
    private List<File> llistaAdjunts= new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Image enviarButton = new Image(getClass().getResourceAsStream("enviar.png"));
        enviar.setGraphic(new ImageView(enviarButton));
        
        prop.put("mail.smtp.host", "smtp.gmail.com");
	prop.put("mail.smtp.starttls.enable", "true");		
        prop.put("mail.smtp.port", "25");
	prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
 
	sessio= Session.getInstance(prop,
            new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("sfodshop@gmail.com", "Monster13");
            }});
    }
    
    private static void afegirAdjunt(Multipart multipart, File file) throws MessagingException{
        DataSource source = new FileDataSource(file.getAbsolutePath());
        BodyPart messageBodyPart = new MimeBodyPart();        
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }
    
    private void afegirAdjunts(Multipart multiPart) throws MessagingException{
        for(File aux : llistaAdjunts) afegirAdjunt(multiPart, aux);
    }
    
    private void enviarMissatge(String to){
        try{
            Multipart multiPart= new MimeMultipart();
            MimeMessage missatge = new MimeMessage(sessio);
            
            missatge.setFrom(new InternetAddress("sfodshop@gmail.com"));
            missatge.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            missatge.setSubject(assumpte.getText());

            BodyPart text= new MimeBodyPart();
            text.setContent(cos.getHtmlText()+signatura, "text/html; charset=UTF-8");
            multiPart.addBodyPart(text);
            missatge.setContent(multiPart);
            afegirAdjunts(multiPart);
            Transport.send(missatge);

            PopupAlerta.mostraAlerta(Alert.AlertType.INFORMATION, "Acció completada", "Email enviat correctament");
            
            Stage actual= (Stage)esborra.getScene().getWindow();
            actual.close();
        }catch (MessagingException me){
            PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404", me.getMessage());
	}
    }

    @FXML
    private void accioEnviar(ActionEvent event) {
        String to= (String)receptor.getSelectionModel().getSelectedItem();
        if(to.isEmpty()) PopupAlerta.mostraAlerta(Alert.AlertType.ERROR, "Error 404: Camp invàlid", "Si us plau, introdueix un receptor");
        else{
            if(assumpte.getText().isEmpty() && PopupAlerta.mostrarConfirmacio("Atenció", "Vols enviar un missatge sense assumpte?")){
                enviarMissatge(to);
            }
            else if(cos.getHtmlText().isEmpty() && PopupAlerta.mostrarConfirmacio("Atenció", "Vols enviar un missatge sense cos?")){
                enviarMissatge(to);
            }
            else enviarMissatge(to);
        }
    }
    
    private void actualitzarTextAdjunts(List<File> llistaFitxers){
        if(!llistaFitxers.isEmpty()){
            for(File aux : llistaFitxers) textAdjunts+= aux.getName()+";";
        }
    }
    
    @FXML
    private void accioNavegar(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tria fitxers per a adjuntar");
        List<File> nousAdjunts= fileChooser.showOpenMultipleDialog(navega.getScene().getWindow());
        llistaAdjunts.addAll(nousAdjunts);
        actualitzarTextAdjunts(nousAdjunts);
        adjunts.setText(textAdjunts);
    }
    
    @FXML
    private void accioEsborrar(ActionEvent event){
        llistaAdjunts.clear();
        textAdjunts= "";
        adjunts.clear();
    }
}