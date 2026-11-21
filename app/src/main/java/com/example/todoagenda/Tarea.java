package com.example.todoagenda;

/* Clase que representa una tarea con sus atributos. */
public class Tarea {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String fecha;
    private String hora;

    // Constructor vacío.
    public Tarea(){
    }

    // Constructor inicializado con sus atributos.
    public Tarea(String titulo, String descripcion, String categoria, String fecha, String hora){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters
    public String getTitulo(){
        return titulo;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public String getCategoria(){
        return categoria;
    }

    public String getFecha(){
        return fecha;
    }

    public String getHora(){
        return hora;
    }

}
