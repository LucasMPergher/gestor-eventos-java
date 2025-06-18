package edu.unam.controladores;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// clase para mostrar alertas
// se puede usar para mostrar mensajes de error, advertencia, información, etc.
// ubicada en controladores porque se encuentra relacionada con la pantalla
// se puede crear un paquete de utilidades para ubicar clases de este tipo
public class Alerta {
    
    // método para mostrar una alerta
    // es estático porque no necesito crear un objeto de esta clase para usarlo
    // internamente si crea un objeto Alert()
    public static void mostrarAlerta(AlertType tipo, String titulo, String cabecera, String mensaje) {
        // mostramos una alerta
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(cabecera);
        a.setContentText(mensaje);
        a.show();
    }

}
