package edu.unam.controladores;

import java.io.IOException;

import edu.unam.App;
import edu.unam.modelo.EstadoPedido;
import edu.unam.modelo.Pedido;
import edu.unam.modelo.Proveedor;
import edu.unam.servicios.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PedidosController {

    @FXML
    private TableView<Pedido> tablaPedidos;

    @FXML
    private TableColumn<Pedido, String> idPedido;

    @FXML
    private TableColumn<Pedido, Proveedor> proveedor;


    @FXML
    private TableColumn<Pedido, EstadoPedido> estado;

    @FXML
    private Button botonNuevo;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonVer;

    @FXML
    private Button botonEditar;

    @FXML
    private Button botonVolver;

    @FXML
    private Label labelIdPedido;

    @FXML
    private DatePicker fecha;

    @FXML
    private ComboBox<Proveedor> comboProveedores;

    @FXML
    private Label labelEstado;

    private Servicio servicio;

    // agregado a mano (no definido en el archivo FXML)
    @FXML
    private void initialize() {
        try {
            // una forma simple de pasar el servicio
            // no digo sea correcta pero es funcional
            servicio = App.getServicio();
            // inicialización de la tabla
            idPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
            proveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
            estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            // defino el controlador de selección de la tabla
            tablaPedidos.getSelectionModel().selectedItemProperty().addListener(e -> cargarDatos());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al iniciar", e.getMessage());
        }
        // en limpiar se encuentra la lógica de carga de datos para cargar un nuevo pedido
        // cambia esta pantalla respecto a los otros ABM
        limpiar();
    }


    @FXML
    private void nuevo(ActionEvent event) {
        limpiar();
    }

    @FXML
    void guardar(ActionEvent event) {
        var pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedido == null) {
            try {
                servicio.insertarPedido(comboProveedores.getValue());
            } catch (Exception e) {
                Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al guardar", e.getMessage());
            }
        }
        limpiar();
    }


    @FXML
    void ver(ActionEvent event) throws IOException {
        var pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        var fxmlLoader = App.setRoot("itemPedidosVer");
        VerPedidoController controlador = fxmlLoader.<VerPedidoController>getController();
        controlador.setPedido(pedido);
        // realizo esto porque si uso initialize() genera error porque no se inicializa el pedido
        // cuando se activa la pantalla de itemPedidos
        controlador.cargarInicialmente();
    }

    @FXML
    void editar(ActionEvent event) throws IOException {
        var pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        var fxmlLoader = App.setRoot("itemPedidos");
        CargarPedidoController controlador = fxmlLoader.<CargarPedidoController>getController();
        controlador.setPedido(pedido);
        // realizo esto porque si uso initialize() genera error porque no se inicializa el pedido
        // cuando se activa la pantalla de itemPedidos
        controlador.cargarInicialmente();
    }

    @FXML
    private void volver(ActionEvent event) throws IOException {
        // volver a la pantalla principal
        App.setRoot("principal");
    }

    // agregado a mano (no definido en el FXML)
    private void cargarDatos() {
        var pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedido != null) {
            labelIdPedido.setText(pedido.getIdPedido().toString());
            fecha.setValue(pedido.getFecha());
            comboProveedores.setValue(pedido.getProveedor());
            labelEstado.setText(pedido.getEstado().toString());
            // lógica de la pantalla
            comboProveedores.setDisable(true);
            botonNuevo.setDisable(false);
            if (pedido.getEstado() == EstadoPedido.ABIERTO) {
                botonGuardar.setDisable(true);
                botonEditar.setDisable(false);
                botonVer.setDisable(true);
            } else {
                botonGuardar.setDisable(true);
                botonEditar.setDisable(true);
                botonVer.setDisable(false);
            }
        }
    }

    // agregado a mano (no definido en el FXML)
    private void limpiar() {
        labelIdPedido.setText("");
        fecha.setValue(null);
        labelEstado.setText("");
        
        botonNuevo.setDisable(true);
        botonGuardar.setDisable(false);
        botonEditar.setDisable(true);
        botonVer.setDisable(true);

        comboProveedores.setDisable(false);
        comboProveedores.getItems().clear();
        tablaPedidos.getItems().clear();

        try {
            comboProveedores.getItems().addAll(servicio.listarProveedores());
            tablaPedidos.getItems().addAll(servicio.listarPedidos());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al obtener datos", e.getMessage());
        }

        comboProveedores.setValue(null);
        tablaPedidos.getSelectionModel().clearSelection();
    }
}
