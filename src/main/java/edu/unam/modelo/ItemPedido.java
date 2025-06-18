package edu.unam.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_pedido")
public class ItemPedido {

    // a ItemPedido en clase le faltaba era definir un Id
    // en ese punto ya pueden saber como son las relaciones
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    // El @JoinColumn se usa para indicar que el campo en la base de datos
    // es no nulo, se puede definir también el nombre de la columna 
    @JoinColumn(nullable = false)
    private Producto producto;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Pedido pedido;
    @Column(nullable = false)
    private int cantidad;

    protected ItemPedido() {
    }

    // como se ha agregado Pedido se debió modificar el constructor 
    // recuerden que si no se quiere usar excepciones de java se puede
    // crear una propia excepción
    public ItemPedido(Producto producto, Pedido pedido, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        this.producto = producto;
        this.pedido = pedido;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String toString() {
        // no me interesa mostrar en un combo o lista el pedido
        return String.format("%s x %d", producto, cantidad);
    }

}
