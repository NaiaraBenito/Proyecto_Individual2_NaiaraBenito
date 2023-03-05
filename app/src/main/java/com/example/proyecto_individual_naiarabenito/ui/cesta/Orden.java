
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;


/* ########################################### CLASE ORDEN #########################################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del carrito.

    *) Tipo: Class
*/
public class Orden {

// ___________________________________________ Variables ___________________________________________
    private int id;                 // Id de la orden
    private String nombreProd;      // Nombre del producto de la orden
    private double precioProd;      // Precio por unidad del producto de la orden
    private int imagenProd;         // Foto del producto de la orden
    private int cantidadProd;       // Cantidad del producto de la orden
    private String emailUsuario;    // Email del usuario de la orden

// __________________________________________ Constructor __________________________________________
    public Orden(String nombreProd, double precioProd, int imagenProd, int cantidadProd, String emailUsuario) {
        this.nombreProd = nombreProd;
        this.precioProd = precioProd;
        this.imagenProd = imagenProd;
        this.cantidadProd = cantidadProd;
        this.emailUsuario = emailUsuario;
    }

// _________________________________________________________________________________________________

    public Orden() {}

// _______________________________________ Getters y Setters _______________________________________

    public int getId() {
        return id;
    }

// _________________________________________________________________________________________________

    public void setId(int pId) {
        this.id = pId;
    }

// _________________________________________________________________________________________________

    public String getNombreProd() {
        return nombreProd;
    }

// _________________________________________________________________________________________________

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

// _________________________________________________________________________________________________

    public double getPrecioProd() {
        return precioProd;
    }

// _________________________________________________________________________________________________

    public void setPrecioProd(double precioProd) {
        this.precioProd = precioProd;
    }

// _________________________________________________________________________________________________

    public int getImagenProd() {
        return imagenProd;
    }

// _________________________________________________________________________________________________

    public void setImagenProd(int imagenProd) {
        this.imagenProd = imagenProd;
    }

// _________________________________________________________________________________________________

    public int getCantidadProd() {
        return cantidadProd;
    }

// _________________________________________________________________________________________________

    public void setCantidadProd(int cantidadProd) {
        this.cantidadProd = cantidadProd;
    }

// _________________________________________________________________________________________________

    public String getEmailUsuario() {
        return emailUsuario;
    }

// _________________________________________________________________________________________________

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}