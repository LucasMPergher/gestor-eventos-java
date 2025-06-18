package edu.unam.modelo;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    // se debe usar para que tome como tipo de datos UUID
    @Column(columnDefinition = "UUID")
    private UUID idProveedor = UUID.randomUUID();
    @Column(name = "nombres", length = 50, nullable = false)
    private String nombres;
    @Column(name = "apellidos", length = 50, nullable = false)
    private String apellidos;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    // para borrado lógico
    @Column(nullable = false)
    private boolean baja = false;
    
    protected Proveedor() {
    }

    public Proveedor(String nombres, String apellidos, LocalDate fechaNacimiento) {
        // validaciones
        nombres = nombres.trim().toUpperCase();
        apellidos = apellidos.trim().toUpperCase();

        if (nombres.isEmpty() || apellidos.isEmpty()) {
            throw new IllegalArgumentException("Los nombres y apellidos no pueden estar vacíos");
        }

        if (nombres.length() > 50 || apellidos.length() > 50) {
            throw new IllegalArgumentException("Los nombres y apellidos no pueden tener más de 50 caracteres");
        }

        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }

        // asignaciones
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public UUID getIdProveedor() {
        return idProveedor;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        nombres = nombres.trim().toUpperCase();

        if (nombres.isEmpty()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacíos");
        }

        if (nombres.length() > 50) {
            throw new IllegalArgumentException("Los nombres no pueden tener más de 50 caracteres");
        }
        
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        apellidos = apellidos.trim().toUpperCase();

        if (apellidos.isEmpty()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos");
        }

        if (apellidos.length() > 50) {
            throw new IllegalArgumentException("Los apellidos no pueden tener más de 50 caracteres");
        }

        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja() {
        this.baja = true;
    }

    public String toString() {
        return String.format("%s %s", nombres, apellidos);
    }

}
