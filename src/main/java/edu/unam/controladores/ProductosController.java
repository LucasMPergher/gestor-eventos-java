package edu.unam.controladores;

import java.io.IOException;

import edu.unam.App;
import edu.unam.modelo.Producto;
import edu.unam.servicios.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductosController {

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonNuevo;

    @FXML
    private Button botonVolver;

    @FXML
    private TableColumn<Producto, String> idProducto;

    @FXML
    private TableColumn<Producto, String> nombre;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private Label labelIdProducto;

    @FXML
    private TextField textoNombre;

    // agregado a mano (no definido en el archivo FXML)
    private Servicio servicio;

    // agregado a mano (no definido en el archivo FXML)
    @FXML
    private void initialize() {

        // una forma simple de pasar el servicio
        // no digo sea correcta pero es funcional
        servicio = App.getServicio();
        // inicialización de la tabla
        idProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        // defino el controlador de selección de la tabla
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());
        try {
            // cargo los datos
            tablaProductos.getItems().addAll(servicio.listaProductos());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al iniciar", e.getMessage());
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        var producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            try {
                servicio.borrarProducto(producto.getIdProducto());
            } catch (Exception e) {
                Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar", e.getMessage());
            }
            limpiar();
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        var producto = tablaProductos.getSelectionModel().getSelectedItem();
        try {
            if (producto != null) {
                servicio.modificarProducto(producto.getIdProducto(), textoNombre.getText());
            } else {
                servicio.insertarProducto(textoNombre.getText());
            }
            limpiar();
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void nuevo(ActionEvent event) {
        limpiar();
    }

    @FXML
    private void volver(ActionEvent event) throws IOException {
        // volver a la pantalla principal
        App.setRoot("principal");
    }

    // agregado a mano (no definido en el FXML)
    private void cargarDatos() {
        var producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            labelIdProducto.setText(producto.getIdProducto().toString());
            textoNombre.setText(producto.getNombre());
        }
    }

    // agregado a mano (no definido en el FXML)
    private void limpiar() {
        labelIdProducto.setText("");
        textoNombre.setText("");
        tablaProductos.getItems().clear();
        // manejo de excepciones al obtener datos
        // piensen cuando pueden suceder errores en este caso
        // si miran no manejo errores en listaProductos(), una alternativa es manejarlos ahi
        // para mostrar un mensaje más claro al usuario final
        try {
            tablaProductos.getItems().addAll(servicio.listaProductos());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al obtener datos", e.getMessage());
        }
        tablaProductos.getSelectionModel().clearSelection();
    }

}
