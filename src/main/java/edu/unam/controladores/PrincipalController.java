package edu.unam.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import edu.unam.App;

public class PrincipalController {

    @FXML
    private Button botonPedidos;
    @FXML
    private Button botonProductos;
    @FXML
    private Button botonProveedores;

    @FXML
    private void mostrarPedidos(ActionEvent event) throws IOException {
        App.setRoot("pedidos");
    }

    @FXML
    private void mostrarProductos(ActionEvent event) throws IOException {
        App.setRoot("productos");
    }

    @FXML
    private void mostrarProveedores(ActionEvent event) throws IOException {
        App.setRoot("proveedores");
    }

}



