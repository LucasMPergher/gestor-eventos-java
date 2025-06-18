package edu.unam.controladores;

import java.io.IOException;

import edu.unam.App;
import edu.unam.modelo.ItemPedido;
import edu.unam.modelo.Pedido;
import edu.unam.modelo.Producto;
import edu.unam.servicios.Servicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CargarPedidoController {

    @FXML
    private Label labelIdPedido;

    @FXML
    private Label labelProveedor;

    @FXML
    private TableView<ItemPedido> tablaItemsPedido;


    @FXML
    private TableColumn<ItemPedido, Long> columnaId;

    @FXML
    private TableColumn<ItemPedido, Producto> columnaProducto;

    @FXML
    private TableColumn<ItemPedido, Integer> columnaCantidad;

    @FXML
    private Button botonAgregar;

    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonCerrarPedido;

    @FXML
    private Button botonVolver;


    @FXML
    private ComboBox<Producto> comboProductos;

    @FXML
    private TextField textoCantidad;

    private Servicio servicio;

    private Pedido pedido;

    // vean que no uso initialize()
    public void cargarInicialmente() {    
        servicio = App.getServicio();
        labelIdPedido.setText(pedido.getIdPedido().toString());
        labelProveedor.setText(pedido.getProveedor().toString());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // fijense que en este caso pido a la base de datos nuevamente el pedido
        // Una alternativa es usar pedido.getItems() para cargar los items si pedir el pedido a la base de datos
        // y cargar o borrar los items en el arreglo de items del pedido para luego al final persistir el pedido 
        // con todos sus items y no uno a uno
        //
        // piensen en los pro y contras de cada opción

        try {
            pedido = servicio.buscarPedido(pedido.getIdPedido());
            tablaItemsPedido.getItems().addAll(pedido.getItems());
            comboProductos.getItems().addAll(servicio.listaProductos());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar pedido", e.getMessage());
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        try {
            var cantidad = Integer.parseInt(textoCantidad.getText()); 
            servicio.agregarItem(pedido.getIdPedido(), comboProductos.getValue(), cantidad);
        // por comodidad en todos los casos uso la clase Exception
        // podemos decir que no es la mejor práctica
        // por ejemplo en este caso es mejor mostrar un mejor mensaje de error al usuario
        // en el caso de que la cantidad no sea un número
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al agregar item", e.getMessage());
        }
        actualizar();
    }

    @FXML
    private void cerrarPedido(ActionEvent event) {
        try {
            servicio.cerrarPedido(pedido);
            // si se cierra el pedido no debería poderse agregar o eliminar items
            // entonces salgo de la pantalla de carga
            App.setRoot("pedidos");
            // se puede poner un mensaje informativo, pero eso deben analizar en cada desarrollo
            // de acuerdo a lo solicitado por el usuario, en este "proyecto" solo muestro mensajes de error 
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al cerrar pedido", e.getMessage());
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        var item = tablaItemsPedido.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
            servicio.eliminarItem(pedido, item);
            } catch (Exception e) {
                Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al eliminar item", e.getMessage());
            }
            actualizar();
        }
    }

    @FXML
    private void volver(ActionEvent event) throws IOException {
        App.setRoot("pedidos");
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    private void actualizar() {
        textoCantidad.setText("");
        comboProductos.setValue(null);
        // aca puedo pedir que traiga nuevamente los productos
        // pensar pro y contras de hacerlo

        tablaItemsPedido.getItems().clear();
        try {
            // porque no pido los items al servicio?
            // que beneficio tendría pedir los items al servicio en este caso?
            // que desventaja tendría pedir los items al servicio en este caso?
            // piensen en los pro y contras de cada opción

            // prueben de agregar varios items de Pedido y luego 
            // un item de Pedido sin producto y cantidad, que sucede?
            tablaItemsPedido.getItems().addAll(pedido.getItems());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar items", e.getMessage());
        }
    }
}
