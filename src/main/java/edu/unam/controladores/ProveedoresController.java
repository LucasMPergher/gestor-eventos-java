package edu.unam.controladores;

import java.io.IOException;

import edu.unam.App;
import edu.unam.modelo.Proveedor;
import edu.unam.servicios.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProveedoresController {

    @FXML
    private TableView<Proveedor> tablaProveedores;

    @FXML
    private TableColumn<Proveedor, String> idProveedor;

    @FXML
    private TableColumn<Proveedor, String> apellidos;

    @FXML
    private TableColumn<Proveedor, String> nombres;

    @FXML
    private Button botonNuevo;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonVolver;

    @FXML
    private Label labelIdProveedor;

    @FXML
    private DatePicker fechaNacimiento;

    @FXML
    private TextField textoApellidos;

    @FXML
    private TextField textoNombres;

    // agregado a mano (no definido en el archivo FXML)
    private Servicio servicio;

    // agregado a mano (no definido en el archivo FXML)
    @FXML
    private void initialize() {
        // una forma simple de pasar el servicio
        // no digo sea correcta pero es funcional
        servicio = App.getServicio();
        // inicialización de la tabla
        idProveedor.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
        nombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        apellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        // defino el controlador de selección de la tabla
        tablaProveedores.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());
        try {
            // cargo los datos
            tablaProveedores.getItems().addAll(servicio.listarProveedores());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al iniciar", e.getMessage());
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        var proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
        if (proveedor != null) {
            try {
                servicio.borrarProveedor(proveedor.getIdProveedor());
            } catch (Exception e) {
                Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar", e.getMessage());
            }
            limpiar();
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        var proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
        try {
            if (proveedor != null) {
                servicio.modificarProveedor(proveedor.getIdProveedor(), textoNombres.getText(),
                        textoApellidos.getText(), fechaNacimiento.getValue());
            } else {
                servicio.insertarProveedor(textoNombres.getText(), textoApellidos.getText(),
                        fechaNacimiento.getValue());
            }
            // limpio si no hay errores, porque consideran hago eso?
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
        var proveedor = tablaProveedores.getSelectionModel().getSelectedItem();
        if (proveedor != null) {
            labelIdProveedor.setText(proveedor.getIdProveedor().toString());
            textoApellidos.setText(proveedor.getApellidos());
            textoNombres.setText(proveedor.getNombres());
            fechaNacimiento.setValue(proveedor.getFechaNacimiento());
        }
    }

    // agregado a mano (no definido en el FXML)
    private void limpiar() {
        labelIdProveedor.setText("");
        textoApellidos.setText("");
        textoNombres.setText("");
        fechaNacimiento.setValue(null);
        tablaProveedores.getItems().clear();
        try {
            tablaProveedores.getItems().addAll(servicio.listarProveedores());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al obtener datos", e.getMessage());
        }
        tablaProveedores.getSelectionModel().clearSelection();
    }

}
