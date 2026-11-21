package com.example.todoagenda;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.WindowCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnMenu, btnAgregar;
    private TextView txvTotal;
    private ListView lstvTareas;
    private Adaptador adaptador;
    private final ArrayList<Tarea> listaCompleta = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        // Inicializar Componentes del XML.
        btnMenu = findViewById(R.id.btn_menu);
        btnAgregar = findViewById(R.id.btn_agregar);
        txvTotal = findViewById(R.id.txt_total);
        lstvTareas = findViewById(R.id.lstv_tareas);

        adaptador = new Adaptador(this, listaCompleta);
        lstvTareas.setAdapter(adaptador);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listaCompleta.clear();
        listaCompleta.addAll(Coleccion.getListaCompleta());
        adaptador.notifyDataSetChanged();
    }
}