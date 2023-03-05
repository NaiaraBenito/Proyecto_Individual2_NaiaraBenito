
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.ui.cesta;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/* ############################### CLASE CONFIRMAR_BORRADO_ORDEN_DIALOG ############################
    *) Descripción:
        La función de esta clase es mostrar y gestionar la configuración del dialogo de confirmación
        de compra.

    *) Tipo: DialogFragment
*/
public class ConfirmarBorradoOrdenDialog extends DialogFragment {

// ___________________________________________ Interfaz ____________________________________________
    private ConfirmarBorradoOrdenDialog.ListenerDialogo listener;   // Listener del Diálogo

    // Interfaz para gestionar la acción confirmar la eliminación del producto
    public interface ListenerDialogo{
        void setPositiveButton(String produto, int position);
    }

    // Asignar valor al listener del Diálogo
    @Override
    public void onAttach(@NonNull Activity context) {
        super.onAttach(context);
        try{
            // Inicializar el listener
            listener = (ListenerDialogo) this.context;
        }catch (ClassCastException e){  // En caso de excepción
            // Notificar error
            throw new ClassCastException(this.toString() + " debe implementar ListenerDialogo");
        }
    }

// ___________________________________________ Variables ___________________________________________
    private final String producto;    // Nombre del producto que se desea eliminar
    private final int position;       // Posición en la cesta del producto que se desea eliminar
    private final Context context;    // Contexto del Activity Cesta Fragment

// __________________________________________ Constructor __________________________________________
    public ConfirmarBorradoOrdenDialog(String pProducto, int pPosition, Context pContext){
        this.producto = pProducto;
        this.position = pPosition;
        this.context = pContext;
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreateDialog:
    ----------------------
        *) Parámetros (Input):
                1) (Bundle) savedInstanceState: Contiene el diseño predeterminado del Diálogo.
        *) Parámetro (Output):
                (Dialog) dialogo: Contiene el objeto Diálogo de confirmación.
        *) Descripción:
                Este método se ejecuta cuando el usuario se intenta eliminar un producto de la cesta.
*/
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Crear el Diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¡Espera!");   // Asignar título
        builder.setMessage("¿Seguro que deseas eliminar el producto de la cesta?")  // Asignar texto
                .setPositiveButton("Seguro", new DialogInterface.OnClickListener() {    // Asignar botón de confirmación
                    public void onClick(DialogInterface dialog, int id) {   // Si se ha pulsado
                        // Informar de la eliminación del producto
                        Toast.makeText(getContext(), "Se ha eliminado", Toast.LENGTH_LONG).show();

                        // Eliminar producto de la cesta
                        listener.setPositiveButton(producto, position);

                        // Eliminar Diálogo
                        dismiss();
                    }
                })
                .setNegativeButton("Me he arrepentido", new DialogInterface.OnClickListener() {   // Asignar botón de rechazo
                    public void onClick(DialogInterface dialog, int id) {   // Si se ha pulsado
                        // Informar de la decisión
                        Toast.makeText(getContext(), "No se ha eliminado", Toast.LENGTH_LONG).show();

                        // Eliminar Diálogo
                        dismiss();
                    }
                });
        // Crear el objeto AlertDialog
        return builder.create();
    }
}
