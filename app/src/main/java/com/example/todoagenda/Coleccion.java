package com.example.todoagenda;

import java.util.ArrayList;

/* Clase para almacenar y gestionar las tareas registradas. */
public class Coleccion {
    private static final ArrayList<Tarea> listaCompleta = new ArrayList<>();
    public enum Categorias {Estudios, Trabajo, Personal, Otros};
    public static boolean porDefectoCargadas = false;

    // Método que agrega todas las tareas a la lista.
    public static void agregarTarea(Tarea tarea){
        listaCompleta.add(tarea);
    }

    // Método que devuelve la lista completa de tareas.
    public static ArrayList<Tarea> getListaCompleta(){
        return listaCompleta;
    }

    // Método que permite filtrar las tareas por categoría.
    public static ArrayList<Tarea> getListaFiltrada(String categoria){
        ArrayList<Tarea> listaFiltrada = new ArrayList<>();

        for(Tarea t: listaCompleta){
            if (t.getCategoria().equalsIgnoreCase(categoria)){
                listaFiltrada.add(t);
            }
        }

        return listaFiltrada;
    }

    // Método que se ejecuta una sola vez para añadir tareas predeterminadas.
    public static void agregarTareasPorDefecto(){
        if (!porDefectoCargadas) {
            return;
        }

        listaCompleta.add(new Tarea("Realizar proyecto final", Categorias.Estudios.toString(), "12/12/2026", "14:00"));
        listaCompleta.add(new Tarea("Preparar presentación", Categorias.Trabajo.toString(), "14/12/2026", "10:00"));
        porDefectoCargadas = true;
    }

    /* Método para eliminar tarea registrada. */
    public static void eliminarTarea(Tarea tarea){
        listaCompleta.remove(tarea);
    }

    /* Método para eliminar todas las tareas registradas. */
    public static void vaciarAgenda() {
        listaCompleta.clear();
    }


}
