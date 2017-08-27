package sfod;

import java.util.Optional;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class PopupAlerta {
    
    public static EventHandler<KeyEvent> fireOnEnter = event -> {
    if (KeyCode.ENTER.equals(event.getCode()) 
            && event.getTarget() instanceof Button) {
        ((Button) event.getTarget()).fire();
        }
    };
    
    public static void mostraAlerta(AlertType tipusAlerta, String header, String content){
        Alert alerta= new Alert(tipusAlerta);
        alerta.setTitle("Sfod");
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        alerta.showAndWait();
    }
    
    public static Boolean mostrarConfirmacio(String header, String content){
        Alert alerta= new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Sfod");
        alerta.setHeaderText(header);
        alerta.setContentText(content);
        Button okButton = (Button) alerta.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDefaultButton(false);
        DialogPane dialeg = alerta.getDialogPane();
        dialeg.getButtonTypes().stream().map(dialeg::lookupButton)
            .forEach(button -> button.addEventHandler(
                KeyEvent.KEY_PRESSED,
                fireOnEnter
            ));
        Optional<ButtonType> resultat = alerta.showAndWait();
        return resultat.get() == ButtonType.OK;
    }
}
