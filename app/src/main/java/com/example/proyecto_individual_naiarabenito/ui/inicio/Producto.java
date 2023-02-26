package com.example.proyecto_individual_naiarabenito.ui.inicio;

public class Producto {
    public String nombre;
    public String descripcion;
    public int img_id;

    public Producto(String nombre, String descripcion, int img_id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.img_id = img_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}
