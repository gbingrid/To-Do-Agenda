package com.example.todoagenda;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "Canal_ID";
    private static final String DESCRIPCION = "Recordatorio";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;
    private NotificationManager notificador;
    private Toolbar toolbar;
    private Button btnAgregar;
    private TextView txtvTotal, txtvVacio;
    private ListView lstvTareas;
    private Adaptador adaptador;
    private Tarea tareaSeleccionada;
    private final ArrayList<Tarea> listaCompleta = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes del XML.
        toolbar = findViewById(R.id.tlb_contenedor_encabezado);
        btnAgregar = findViewById(R.id.btn_agregar);
        txtvTotal = findViewById(R.id.txt_total);
        lstvTareas = findViewById(R.id.lstv_tareas);
        txtvVacio = findViewById(R.id.txt_lista_vacia);

        // Configurar toolbar para que sea la barra de la pantalla.
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adaptador = new Adaptador(this, listaCompleta);
        lstvTareas.setAdapter(adaptador);
        lstvTareas.setEmptyView(txtvVacio);

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
    // ----------MÉTODOS PARA GESTIÓN DE PERMISOS Y NOTIFICACIONES ------

    /* Método que lanza una notificación de recordatorio de tarea. */
    private void enviarRecordatorio(Tarea tarea) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                tareaSeleccionada = tarea;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
                return;
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle(tarea.getTitulo())
                .setContentText(tarea.getFecha() + " " + tarea.getHora())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificador.notify(tarea.getTitulo().hashCode(), builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED && tareaSeleccionada != null) {
            enviarRecordatorio(tareaSeleccionada);
        }
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(CHANNEL_ID, DESCRIPCION, NotificationManager.IMPORTANCE_HIGH);
            canal.enableLights(true);
            canal.enableVibration(true);
            notificador.createNotificationChannel(canal);
        }
    }

    // ------------- MENÚ DE OPCIONES ---------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem opcion = menu.getItem(i);
            pintarTexto(opcion);

            if (opcion.hasSubMenu()) {
                SubMenu submenu = opcion.getSubMenu();
                for (int j = 0; j < submenu.size(); j++) {
                    pintarTexto(submenu.getItem(j));
                }
            }
        }
        return true;
    }

    /* Método para cambiar el color del texto del menú. */
    private void pintarTexto(MenuItem opcion) {
        SpannableString texto = new SpannableString(opcion.getTitle());
        texto.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)), 0, texto.length(), 0);
        opcion.setTitle(texto);
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
        int cantidad = Coleccion.getListaFiltrada(categoria).size();
        Toast.makeText(this, "Tareas de " + categoria + ": " + cantidad, Toast.LENGTH_SHORT).show();
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
    private void mostrarAcercaDe() {
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


    // ----------------MÉTODOS PARA GESTIÓN DE TAREAS EN EL LISTVIEW -------------
    /* Método que crear un alertDialog para mostrar toda la información de una tarea. */
    private void mostrarInformacion(Tarea tarea) {
        String informacion = "Categoría: " + tarea.getCategoria() + "\n"
                + "Fecha: " + tarea.getFecha() + "\n"
                + "Hora: " + tarea.getHora() + "\n";

        new AlertDialog.Builder(this)
                .setTitle(tarea.getTitulo())
                .setMessage(informacion)
                .setPositiveButton("Cerrar", null)
                .setNegativeButton("Eliminar", (dialog, which) -> confirmarEliminar(tarea))
                .setNeutralButton("Recordatorio", (dialog, which) -> enviarRecordatorio(tarea))
                .show();
    }

    /* Método que solicita confirmación mediante diálogo y elimina una tarea de la lista. */
    private void confirmarEliminar(Tarea tarea) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar tarea")
                .setMessage("¿Seguro que desea eliminar esta tarea?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Coleccion.eliminarTarea(tarea);
                    mostrarTareas();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /* Método que carga la lista de tareas según su filtro. */
    private void mostrarTareas() {
        listaCompleta.clear();
        listaCompleta.addAll(Coleccion.getListaCompleta());
        adaptador.notifyDataSetChanged();
        txtvTotal.setText("Total: " + listaCompleta.size());
    }

    // --------------
    @Override
    protected void onResume() {
        super.onResume();
        listaCompleta.clear();
        listaCompleta.addAll(Coleccion.getListaCompleta());
        adaptador.notifyDataSetChanged();
        txtvTotal.setText("Total: " + listaCompleta.size());
    }
}