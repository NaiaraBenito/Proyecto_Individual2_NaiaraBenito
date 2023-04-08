
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

/* ################################# INTERFAZ ACTUALIZAR CESTA #####################################
    *) Descripción:
        La función de esta interfaz es ofrecer una conexión entre ListAdapter_Ordenes y
        CestaFragment.

    *) Tipo: Interface
*/

public interface InterfazActualizarCesta {

// ____________________________________________ Métodos ____________________________________________
    void notificarCambios();
    void notificarBorrado(int pos);
}
