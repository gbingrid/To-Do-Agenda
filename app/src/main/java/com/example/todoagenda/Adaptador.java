package com.example.todoagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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

        View fondoTarea = view.findViewById(R.id.lyt_fondo_tarea);
        View bordeTarea = view.findViewById(R.id.lyt_border_tarea);

        Tarea tarea = getItem(indice);

        TextView txtTitulo = view.findViewById(R.id.txt_titulo);
        TextView txtFecha = view.findViewById(R.id.txt_fecha);

        if (tarea.getCategoria().equalsIgnoreCase(Coleccion.Categorias.Estudios.toString())){
            fondoTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_orange));
            bordeTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));

        } else if (tarea.getCategoria().equalsIgnoreCase(Coleccion.Categorias.Trabajo.toString())){
            fondoTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ice_blue));
            bordeTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sapphire_blue));

        } else if (tarea.getCategoria().equalsIgnoreCase(Coleccion.Categorias.Personal.toString())){
            fondoTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_green));
            bordeTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));

        } else if (tarea.getCategoria().equalsIgnoreCase(Coleccion.Categorias.Otros.toString())){
            fondoTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
            bordeTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_grey));

        } else {
            fondoTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            bordeTarea.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        }


        txtTitulo.setText(tarea.getTitulo());
        txtFecha.setText(tarea.getFecha());

        return view; // Devuelve la fila ya rellena.
    }

}
