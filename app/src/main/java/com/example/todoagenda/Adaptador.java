package com.example.todoagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/* Clase adaptador encargado de mostrar y gestionar el listado de tareas. */
public class Adaptador extends ArrayAdapter<Tarea> {

    public Adaptador(Context context, List<Tarea> tareas){
        super(context, 0, tareas);
    }

    @Override
    public View getView(int indice, View view, ViewGroup parent){
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.tarea, parent, false);
        }

        Tarea tarea = getItem(indice);

        TextView txtTitulo = view.findViewById(R.id.txt_titulo);
        TextView txtFecha = view.findViewById(R.id.txt_fecha);

        txtTitulo.setText(tarea.getTitulo());
        txtFecha.setText(tarea.getFecha());

        return view; // Devuelve la fila ya rellena.
    }

}
