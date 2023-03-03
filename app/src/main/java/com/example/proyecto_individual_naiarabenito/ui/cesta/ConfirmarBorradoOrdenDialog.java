package com.example.proyecto_individual_naiarabenito.ui.cesta;

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
import androidx.fragment.app.FragmentManager;

import com.example.proyecto_individual_naiarabenito.R;

public class ConfirmarBorradoOrdenDialog extends DialogFragment {

    private ConfirmarBorradoOrdenDialog.ListenerDialogo listener;
    public interface ListenerDialogo{
        void setPositiveButton(String produto, int position);
    }

    @Override
    public void onAttach(@NonNull Activity context) {
        super.onAttach(context);
        try{
            listener = (ListenerDialogo) this.context;
        }catch (ClassCastException e){
            throw new ClassCastException(this.toString() + " debe implementar ListenerDialogo");
        }
    }

    private String producto;
    private int position;
    private Context context;
    public ConfirmarBorradoOrdenDialog(String producto, int position, Context context){
        this.producto = producto;
        this.position = position;
        this.context = context;
    }

    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState, String producto, int position) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("¡Espera!");
        builder.setMessage("¿Seguro que deseas eliminar el producto de la cesta?")
                .setPositiveButton("Seguro", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Se ha eliminado", Toast.LENGTH_LONG).show();
                        listener.setPositiveButton(producto, position);
                        dismiss();
                    }
                })
                .setNegativeButton("Me he arrepentido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "No se ha eliminado", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
