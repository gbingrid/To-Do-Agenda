package com.example.todoagenda;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;


public class SecondActivity extends AppCompatActivity {
    private Button btnVolver, btnGuardar, btnCancelar;
    private EditText edtTitulo, edtCategoria, edtFecha, edtHora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // Inicializar componentes del archivo XML.
        btnVolver = findViewById(R.id.btn_volver);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        edtTitulo = findViewById(R.id.etxt_titulo);
        edtCategoria = findViewById(R.id.etxt_categoria);
        edtFecha = findViewById(R.id.etxt_fecha);
        edtHora = findViewById(R.id.etxt_hora);

        // Llamada a los respectivos métodos.
        edtCategoria.setOnClickListener(v -> mostrarDialogoCategoria());
        edtFecha.setOnClickListener(v -> seleccionarFecha());
        edtHora.setOnClickListener(v -> seleccionarHora());


        // Configuración del botón para volver a la pantalla anterior.
        btnVolver.setOnClickListener(v -> finish());
    }



    /* Método que muestra el diálogo para seleccionar la categoría y guarda la selección en el campo. */
    private void mostrarDialogoCategoria(){
        Coleccion.Categorias[] valores = Coleccion.Categorias.values();
        String[] categorias = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            categorias[i] = valores[i].toString();
        }

        new AlertDialog.Builder(this)
                .setTitle("Categoría")
                .setItems(categorias, (dialog, which) ->
                        edtCategoria.setText(categorias[which]))
                .show();
    }

    /* Método que muestra el selector de fecha y guarda la selección en el campo. */
    private void seleccionarFecha(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog fecha = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edtFecha.setText(fechaSeleccionada);
                    }
                }, year, month, day);
        fecha.show();
    }

    /* Método que muestra el selector de hora y guarda la selección en el campo. */
    private void seleccionarHora(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog hora = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String minuto = (minute < 10) ? "0" + minute : String.valueOf(minute);
                        String hora = hourOfDay + ":" + minuto;
                        edtHora.setText(hora);
                    }
                }, hour, minute, true);
        hora.show();
    }
}
