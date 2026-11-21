package com.example.todoagenda;

import java.util.ArrayList;

/* Clase para almacenar y gestionar las tareas registradas. */
public class Coleccion {
    private static final ArrayList<Tarea> listaCompleta = new ArrayList<>();

    // Método que agrega todas las tareas a la lista.
    public static void agregarTarea(Tarea tarea){
        listaCompleta.add(tarea);
    }

    // Método que devuelve la lista completa de tareas.
    public static ArrayList<Tarea> getListaCompleta(){
        return listaCompleta;
    }

    // Método que permite filtrar las tareas por categoría.
    public static ArrayList<Tarea> filtrarLista(String categoria){
        ArrayList<Tarea> listaFiltrada = new ArrayList<>();

        for(Tarea t: listaCompleta){
            if (t.getCategoria().equalsIgnoreCase(categoria)){
                listaFiltrada.add(t);
            }
        }

        return listaFiltrada;
    }


}
