package edu.unam.modelo;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID idProducto = UUID.randomUUID();
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    // para borrado lógico
    @Column(nullable = false)
    private boolean baja = false;

    protected Producto() {
    }

    public Producto(String nombre) {
        // validaciones
        nombre = nombre.trim().toUpperCase();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres");
        }
        // asignaciones
        this.nombre = nombre;
    }

    public UUID getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        // validaciones
        nombre = nombre.trim().toUpperCase();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres");
        }

        this.nombre = nombre;
    }

    public boolean isBaja() {
        return baja;
    }

    // porque consideran uso este formato de setter?
    public void setBaja() {
        this.baja = true;
    }

    public String toString() {
        // saque el id de producto, quedaba muy largo
        return String.format("%s", nombre);
    }
}
