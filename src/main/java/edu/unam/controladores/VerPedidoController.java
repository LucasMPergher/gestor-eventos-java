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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VerPedidoController {
    
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
    private Button botonVolver;

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

        try {
            pedido = servicio.buscarPedido(pedido.getIdPedido());
            tablaItemsPedido.getItems().addAll(pedido.getItems());
        } catch (Exception e) {
            Alerta.mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar pedido", e.getMessage());
        }
    }

    @FXML
    private void volver(ActionEvent event) throws IOException {
        App.setRoot("pedidos");
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

}

