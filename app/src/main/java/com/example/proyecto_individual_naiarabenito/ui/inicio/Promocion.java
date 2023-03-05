
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.inicio;


/* ########################################### CLASE PROMOCION #########################################
    *) Descripción:
        La función de esta clase es guardar la información de las promociones.

    *) Tipo: Class
*/
public class Promocion {

    // Foto del producto de la orden
    private int img_id;

// __________________________________________ Constructor __________________________________________

    public Promocion(int img_id) {
        this.img_id = img_id;
    }

// _______________________________________ Getters y Setters _______________________________________

    public int getImg_id() {
        return img_id;
    }

// _________________________________________________________________________________________________

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}