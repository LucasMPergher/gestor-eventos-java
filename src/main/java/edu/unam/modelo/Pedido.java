package edu.unam.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID idPedido = UUID.randomUUID();
    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();
    @ManyToOne
    @JoinColumn(nullable = false)
    private Proveedor proveedor;
    // En este caso se puede usar una relacion unidereccional, 
    // ya que no se necesita (desde mi punto de vista) conocer
    // desde itemPedido a que pedido pertenece.
    // El problema que hacer una relacion unidireccional usando
    // este formato genera una tabla adicional    
    // esto se soluciona agregado una anotación @JoinColumn
    // definiendo el nombre de la columna que hace de clave foranea
    // igualmente esto es ineficiente, ya que se ejecutan primero los inserts
    // sin la clave foranea en la tabla detalle (ItemPedido) y luego se actualiza
    // la clave foranea

    //    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    //    @JoinColumn(name = "pedido_id")

    // Soluciones posibles:
    // - hacer la relación bidireccional (se usa en el ejemplo)
    // - o usar @ElementCollection donde ItemPedido es un @Embeddable

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<ItemPedido> items = new ArrayList<>();
    // los estados se crean en estado abierto
    @Column(nullable = false)
    private EstadoPedido estadoPedido = EstadoPedido.ABIERTO;

    protected Pedido() {
    }

    public Pedido(Proveedor proveedor) {
        if (proveedor == null) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }
        
        this.proveedor = proveedor;
    }

    public UUID getIdPedido() {
        return idPedido;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public List<ItemPedido> getItems() {
        return List.copyOf(items);
    }

    public EstadoPedido getEstado() {
        return estadoPedido;
    }

    public void agregarItem(Producto producto, int cantidad) {
        if (estadoPedido.equals(EstadoPedido.CERRADO)) {
            throw new IllegalStateException("No se pueden agregar items a un pedido cerrado");
        }

        if (estadoPedido.equals(EstadoPedido.CANCELADO)) {
            throw new IllegalStateException("No se pueden agregar items a un pedido cancelado");
        }

        items.add(new ItemPedido(producto, this, cantidad));
    }

    public void quitarItem(ItemPedido item) {
        if (estadoPedido.equals(EstadoPedido.CERRADO)) {
            throw new IllegalStateException("No se pueden quitar items a un pedido cerrado");
        }

        if (estadoPedido.equals(EstadoPedido.CANCELADO)) {
            throw new IllegalStateException("No se pueden quitar items a un pedido cancelado");
        }

        items.remove(item);
    }

    public void cerrarPedido() {
        this.estadoPedido = EstadoPedido.CERRADO;
    }

    public void cancelarPedido() {
        this.estadoPedido = EstadoPedido.CANCELADO;
    }

}
