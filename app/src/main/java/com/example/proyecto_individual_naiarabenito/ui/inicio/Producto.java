
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;


/* ######################################## CLASE PRODUCTO #########################################
    *) Descripción:
        La función de esta clase es guardar la información de los productos ofrece la aplicación.

    *) Tipo: Class
*/
public class Producto {

// ___________________________________________ Variables ___________________________________________
    public String nombre;           // Nombre del producto
    public String descripcion;      // Descripción del producto
    public int img_id;              // Id de la foto del producto
    public int cantidad;            // Cantidad del producto
    public double precio;           // Precio del producto

// __________________________________________ Constructor __________________________________________
    public Producto(String nombre, String descripcion, int img_id, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.img_id = img_id;
        this.precio = precio;
    }

// _________________________________________________________________________________________________
    public Producto(String nombre, String descripcion, int img_id, int cantidad, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.img_id = img_id;
        this.cantidad = cantidad;
        this.precio = precio;
    }

// _______________________________________ Getters y Setters _______________________________________
    public String getNombre() {
        return nombre;
    }

// _________________________________________________________________________________________________

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

// _________________________________________________________________________________________________

    public String getDescripcion() {
        return descripcion;
    }

// _________________________________________________________________________________________________

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

// _________________________________________________________________________________________________

    public int getImg_id() {
        return img_id;
    }

// _________________________________________________________________________________________________

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

// _________________________________________________________________________________________________

    public int getCantidad() {
        return cantidad;
    }

// _________________________________________________________________________________________________

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

// _________________________________________________________________________________________________

    public double getPrecio() {
        return precio;
    }

// _________________________________________________________________________________________________

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}