package com.example.proyecto_individual_naiarabenito.ui.cesta;

public class Orden {

    public int id;
    public String nombreProd;
    public double precioProd;
    public int imagenProd;
    public int cantidadProd;
    public String emailUsuario;

    public Orden(String nombreProd, double precioProd, int imagenProd, int cantidadProd, String emailUsuario) {
        this.nombreProd = nombreProd;
        this.precioProd = precioProd;
        this.imagenProd = imagenProd;
        this.cantidadProd = cantidadProd;
        this.emailUsuario = emailUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Orden() {}

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public double getPrecioProd() {
        return precioProd;
    }

    public void setPrecioProd(double precioProd) {
        this.precioProd = precioProd;
    }

    public int getImagenProd() {
        return imagenProd;
    }

    public void setImagenProd(int imagenProd) {
        this.imagenProd = imagenProd;
    }

    public int getCantidadProd() {
        return cantidadProd;
    }

    public void setCantidadProd(int cantidadProd) {
        this.cantidadProd = cantidadProd;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}