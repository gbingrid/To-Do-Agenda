package com.example.todoagenda;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "Canal_ID";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;
    private NotificationManager notificador;
    private Toolbar toolbar;
    private Button btnMenu, btnAgregar;
    private ListView lstvTareas;
    private Adaptador adaptador;
    private final ArrayList<Tarea> listaCompleta = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes del XML.
        toolbar = findViewById(R.id.tlb_contenedor_encabezado);
        btnAgregar = findViewById(R.id.btn_agregar);
        lstvTareas = findViewById(R.id.lstv_tareas);

        // Configurar toolbar para que sea la barra de la pantalla.
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.txt_app_name);

        adaptador = new Adaptador(this, listaCompleta);
        lstvTareas.setAdapter(adaptador);

        // Llamada al método que carga tareas por defecto (solo se añaden una vez).
        Coleccion.agregarTareasPorDefecto();

        // Configuración del botón para agregar nueva tarea (se muestra la pantalla del formulario).
        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SecondActivity.class)));

        // Configurar el diálogo para mostrar todos los datos de una tarea.
        lstvTareas.setOnItemClickListener((parent, view, indice, id) ->
                mostrarInformacion(listaCompleta.get(indice)));

        // Configurar el canal de notificaciones.
        notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        crearCanalNotificacion();

    }

    // ----------- MENÚ DE OPCIONES ---------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.estudios) {
            mostrarConteo("Estudios");

        } else if (id == R.id.trabajo) {
            mostrarConteo("Trabajo");

        } else if (id == R.id.personal) {
            mostrarConteo("Personal");

        } else if (id == R.id.otros) {
            mostrarConteo("Otros");

        } else if (id == R.id.opc_vaciar) {
            confirmarVaciarAgenda();

        } else if (id == R.id.opc_acercade) {
            mostrarAcercaDe();

        } else if (id == R.id.opc_salir) {
            confirmarSalir();

        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }


    /* Método para mostrar el total de tareas según la categoría. */
    private void mostrarConteo(String categoria) {
        int cantidad = Coleccion.getListaCompleta().size();
        Toast.makeText(this, categoria + ": " + cantidad, Toast.LENGTH_SHORT).show();

    }

    /* Método que invoca a vaciarAgenda de la clase Coleccion y solicita confirmación previamente. */
    private void confirmarVaciarAgenda() {
        new AlertDialog.Builder(this)
                .setTitle("Vaciar agenda")
                .setMessage("¿Desea eliminar todas las tareas?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Coleccion.vaciarAgenda();
                    mostrarTareas();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    /* Método que muestra en un diálogo información relevante sobre la app. */
    private void mostrarAcercaDe(){
        String informacion = "Aplicación: To-Do Agenda \n"
                + "Versión: 1.0 \n"
                + "Desarrollador: Ingrid Galeano \n";

        new AlertDialog.Builder(this)
                .setTitle("Acerca de")
                .setMessage(informacion)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    /* Método que solicita confirmarción para salir de la app y gestiona su cierre. */
    private void confirmarSalir() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Seguro que quieres salir?")
                .setPositiveButton("Sí", (dialog, which) -> finishAffinity())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ---------------- INFORMACIÓN DE LAS TAREAS -------------
    /* Método que crear un alertDialog para mostrar toda la información de una tarea. */
    private void mostrarInformacion(Tarea tarea) {
        String descripcion = tarea.getDescripcion();
        String categoria = tarea.getCategoria();
        String fecha = tarea.getFecha();
        String hora = tarea.getHora();

        String detalles = "Descripción: " + descripcion + "\n"
                + "Fecha: " + tarea.getFecha() + "\n"
                + "Hora: " + tarea.getHora() + "\n";

        new AlertDialog.Builder(this)
                .setTitle(tarea.getTitulo())
                .setMessage(detalles)
                .setPositiveButton()
    }
    /* Método que carga la lista de tareas según su filtro. */
    private void mostrarTareas(String categoria){
        listaCompleta.clear();

        if (categoria == null){
            listaCompleta.addAll(Coleccion.getListaCompleta());
        } else {
            listaCompleta.addAll(Coleccion.filtrarLista(categoria));
        }

        adaptador.notifyDataSetChanged();
        ///txvTotal.setText("Total: " + listaCompleta.size());
    }


    @Override
    protected void onResume() {
        super.onResume();
        listaCompleta.clear();
        listaCompleta.addAll(Coleccion.getListaCompleta());
        adaptador.notifyDataSetChanged();
    }



}