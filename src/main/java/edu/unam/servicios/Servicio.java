package edu.unam.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.unam.modelo.EstadoPedido;
import edu.unam.modelo.ItemPedido;
import edu.unam.modelo.Pedido;
import edu.unam.modelo.Producto;
import edu.unam.modelo.Proveedor;
import edu.unam.repositorios.Repositorio;

/*
 * Se usa solamente una clase de servicio dado que es un proyecto simple. 
 * En proyectos más grandes se pueden usar varias clases de servicios 
 * (lo mismo aplica a repositorios donde se suelen usar un repositorio por 
 * cada clase que maneja datos).
 * 
 * El uso de varios servicios y repositorios lo vamos a tratar en POO2 
 * 
 */

public class Servicio {
    
    private Repositorio repositorio;

    public Servicio(Repositorio p) {
        this.repositorio = p;
    }

    // Listados

    /** Se obtienen solo los proveedores activos
    * @return List<Proveedor> listado de proveedores activos
    */
    public List<Proveedor> listarProveedores() {
        var proveedores = this.repositorio.buscarTodos(Proveedor.class);
        var listado = new ArrayList<Proveedor>();
        for (var proveedor : proveedores) {
            if (proveedor.isBaja() == false) {
                listado.add(proveedor);
            }
        }
        return listado;
    }

    /** Se obtienen los pedidos abiertos
     * @return List<Pedido> listado de pedidos abiertos
     */
    public List<Pedido> listarPedidosAbiertos() {
        var pedidos = this.repositorio.buscarTodos(Pedido.class);
        var listado = new ArrayList<Pedido>();
        for (var pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.ABIERTO) {
                listado.add(pedido);
            }
        }
        return listado;
    }

    /** Se obtienen todos los pedidos
     * @return List<Pedido> listado de pedidos
     */
    public List<Pedido> listarPedidos() {
        return this.repositorio.buscarTodos(Pedido.class);
    }

    /** Se obtienen todos los productos
     * @return List<Producto> listado de productos activos
     */
    public List<Producto> listaProductos() {
        var productos = this.repositorio.buscarTodos(Producto.class);
        var listado = new ArrayList<Producto>();
        for (var producto : productos) {
            if (producto.isBaja() == false) {
                listado.add(producto);
            }
        }
        return listado;
    }

    // Búsquedas

    /** Retorna un proveedor de acuerdo a su identificador
     * @param id Identificador del proveedor
     * @return Proveedor proveedor encontrado o null si no existe
     */
    public Proveedor buscarProveedor(String id) {
        return this.repositorio.buscar(Proveedor.class, id);
    }

    /** Retorna un pedido de acuerdo a su identificador
     * @param uuid Identificador del pedido
     * @return Pedido pedido encontrado o null si no existe
     */
    public Pedido buscarPedido(UUID uuid) {
        return this.repositorio.buscar(Pedido.class, uuid);
    }

    /** Retorna un producto de acuerdo a su identificador
     * @param id Identificador del producto
     * @return Producto producto encontrado o null si no existe
     */
    public Producto buscarProducto(String id) {
        return this.repositorio.buscar(Producto.class, id);
    }

    // Inserciones

    // como el manejo de errores se da en el modelo aquí debo manejar excepciones
    // para descartar una transacción y no dejar la base de datos en un estado inconsistente

    /** Inserta un nuevo proveedor
     * @param nombres Nombres del proveedor
     * @param apellidos Apellidos del proveedor
     * @param fechaNacimiento Fecha de nacimiento del proveedor
     * */
    public void insertarProveedor(String nombres, String apellidos, LocalDate fechaNacimiento) {
        try {
            this.repositorio.iniciarTransaccion();
            var proveedor = new Proveedor(nombres, apellidos, fechaNacimiento);
            this.repositorio.insertar(proveedor);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            // lanzo nuevamente la excepción para que sea manejada en la capa superior
            throw e;
        }
    }

    /** Inserta un nuevo producto
     * @param nombre Nombre del producto
     */
    public void insertarProducto(String nombre) {
        try {
            this.repositorio.iniciarTransaccion();
            var producto = new Producto(nombre);
            this.repositorio.insertar(producto);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            // lanzo nuevamente la excepción para que sea manejada en la capa superior
            throw e;
        }

    }

    /** Inserta un nuevo pedido
     * @param proveedor Proveedor asociado al pedido
     */
    public void insertarPedido(Proveedor proveedor) {
        try {
            this.repositorio.iniciarTransaccion();
            var pedido = new Pedido(proveedor);
            this.repositorio.insertar(pedido);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            // lanzo nuevamente la excepción para que sea manejada en la capa superior
            throw e;
        }
    }

    /** Agrega un nuevo item a un pedido
     * @param idPedido identificador del Pedido al que se le agrega el item
     * @param producto Producto a agregar
     * @param cantidad Cantidad del producto a agregar
     */
    public void agregarItem (UUID idPedido, Producto producto, int cantidad) {
        try {
            this.repositorio.iniciarTransaccion();
            // se busca en el repositorio para verificar el estado actual del pedido
            var pedido = this.repositorio.buscar(Pedido.class, idPedido);
            // Pregunta: se puede hacer lo mismo para validar si el producto se encuentra de baja?
            // Respuesta: es una alternativa factible
            pedido.agregarItem(producto, cantidad);
            this.repositorio.modificar(pedido);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }

    /** Elimina un item de un pedido
     * @param pedido Pedido al que se le quita el item
     * @param item Item a quitar
     */
    public void eliminarItem (Pedido pedido, ItemPedido item) {
        try {
            this.repositorio.iniciarTransaccion();
            pedido.quitarItem(item);
            this.repositorio.modificar(pedido);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }

    /** Cierra un pedido
     * 
     * @param pedido Pedido a cerrar
     */
    public void cerrarPedido (Pedido pedido) {
        try {
            this.repositorio.iniciarTransaccion();
            pedido.cerrarPedido();
            this.repositorio.modificar(pedido);
            this.repositorio.confirmarTransaccion();
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }
    
    // Modificaciones (no se modifica un pedido)

    /** Modifica un proveedor
     * @param idProveedor Identificador del proveedor
     * @param nombres Nuevos nombres del proveedor
     * @param apellidos Nuevos apellidos del proveedor
     * @param fechaNacimiento Nueva fecha de nacimiento del proveedor
     */
    public void modificarProveedor(UUID idProveedor, String nombres, String apellidos, LocalDate fechaNacimiento) {
        try {
            this.repositorio.iniciarTransaccion();
            var proveedor = this.repositorio.buscar(Proveedor.class, idProveedor);
            if (proveedor != null) {
                proveedor.setNombres(nombres);
                proveedor.setApellidos(apellidos);
                proveedor.setFechaNacimiento(fechaNacimiento);
                this.repositorio.modificar(proveedor);
                this.repositorio.confirmarTransaccion();
            } else {
                this.repositorio.descartarTransaccion();
            }                
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }

    /** Modifica un producto
     * @param idProducto Identificador del producto
     * @param nombre Nuevo nombre del producto
     */
    public void modificarProducto (UUID idProducto, String nombre) {
        try {
            this.repositorio.iniciarTransaccion();
            var producto = this.repositorio.buscar(Producto.class, idProducto);
            if (producto != null) {
                producto.setNombre(nombre);
                this.repositorio.modificar(producto);
                this.repositorio.confirmarTransaccion();
            } else {
                this.repositorio.descartarTransaccion();
            }                
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }

    // borrado (conviene hacer un borrado lógico en lugar de físico)
    // eso implica agregar un campo para poder "borrar" los datos
    // los datos "borrados" no se retornan en los listados (salvo se aclare), 
    // solo en las búsquedas individuales 

    /** Borra (da de baja) un proveedor
    * @param idProveedor Identificador del proveedor a dar de baja
    */
    public void borrarProveedor(UUID idProveedor) {
        try {
            this.repositorio.iniciarTransaccion();
            var proveedor = this.repositorio.buscar(Proveedor.class, idProveedor);
            // se controla que exista el proveedor y que no se encuentre de baja
            if (proveedor != null && proveedor.isBaja() == false) {
                proveedor.setBaja();
                // Pregunta: no deben cancelarse los pedidos abiertos de dicho proveedor
                this.repositorio.modificar(proveedor);
                this.repositorio.confirmarTransaccion();
            } else {
                this.repositorio.descartarTransaccion();
            }                
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    } 

    /** Borra (da de baja) un producto
     * @param idProducto Identificador del producto a dar de baja
     */
    public void borrarProducto (UUID idProducto) {
        try {
            this.repositorio.iniciarTransaccion();
            var producto = this.repositorio.buscar(Producto.class, idProducto);
            // se controla que exista el producto y que no se encuentre de baja
            if (producto != null && producto.isBaja() == false) {
                producto.setBaja();
                // Pregunta: que hacer con productos que se dan de baja en pedidos abiertos
                this.repositorio.modificar(producto);
                this.repositorio.confirmarTransaccion();
            } else {
                this.repositorio.descartarTransaccion();
            }
        } catch (Exception e) {
            this.repositorio.descartarTransaccion();
            throw e;
        }
    }
    
}

