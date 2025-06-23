package edu.unam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.Servicio;
import jakarta.persistence.Persistence;

/**
 * Ejemplo de aplicación usando JavaFX y JPA
 * Recomiendo recorran el código, vean cómo se estructura y lean los comentarios
 * Cualquier duda o comentario a su disposición para ayudarles
 * 
 * El código es mejorable en varios aspectos, pero es un buen punto de partida
 * Todo aquel que encuentre un error o tenga una sugerencia, por favor hágame
 * saber
 * 
 * Mejoras a realizar en ventanas:
 * - no tiene titulo, icono, ni tamaño fijo
 * - todas usan la misma escena
 * 
 * Existe un error que he comentado en el código, si lo solucionan por favor me
 * avisan
 * 
 * Si llego a este punto, felicidades!! El proyecto debe iniciarse desde
 * AppLauncher.java
 * En esa clase se explica por qué se creó y cómo se usa y que hacer si quiere
 * usar el sistema de módulos de java
 */
public class App extends Application {

    private static Scene scene;
    private static Servicio servicio;

    @Override
    public void start(Stage stage) throws IOException {
        // creación del manejador de la conexión
        var emf = Persistence.createEntityManagerFactory("eventos");
        // crea el servicio y repositorio
        servicio = new Servicio(new Repositorio(emf));

        // carga la escena principal
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("principal.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static Servicio getServicio() {
        return servicio;
    }

    // carga un archivo FXML
    // retorna el FXMLLoader para poder acceder a los controladores
    // ver por ejemplo: void editar(ActionEvent event) en PedidosController.java
    public static FXMLLoader setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch(args);
    }

}