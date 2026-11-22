package com.example.todoagenda;

import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class SecondActivity extends AppCompatActivity {
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        btnSalir = findViewById(R.id.btn_volver);
        // Configuración del botón para salir de la app.
        btnSalir.setOnClickListener(v -> finish());
    }

}
