package com.example.interfaz_to_microservicios

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class TemperaturaActivity : AppCompatActivity() {

    private lateinit var etTemperatura: TextInputEditText
    private lateinit var spTipoConversion: Spinner
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_temperatura)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupSpinner()
        setupListeners()
    }

    private fun initViews() {
        etTemperatura = findViewById(R.id.etTemperatura)
        spTipoConversion = findViewById(R.id.spTipoConversion)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupSpinner() {
        val tipos = LogicaMicroservicios.TipoConversionTemperatura.values().map { it.label }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoConversion.adapter = adapter
    }

    private fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcularConversion()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun calcularConversion() {
        val inputStr = etTemperatura.text.toString().trim()
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un valor de temperatura", Toast.LENGTH_SHORT).show()
            return
        }

        val valor = inputStr.toDoubleOrNull()
        if (valor == null) {
            Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val posicionSeleccionada = spTipoConversion.selectedItemPosition
        val tipo = LogicaMicroservicios.TipoConversionTemperatura.values()[posicionSeleccionada]

        val resultado = LogicaMicroservicios.convertirTemperatura(valor, tipo)

        val resultadoTexto = when (tipo) {
            LogicaMicroservicios.TipoConversionTemperatura.CELSIUS_A_FAHRENHEIT ->
                String.format(Locale.getDefault(), "%.2f °C = %.2f °F", valor, resultado)
            LogicaMicroservicios.TipoConversionTemperatura.FAHRENHEIT_A_CELSIUS ->
                String.format(Locale.getDefault(), "%.2f °F = %.2f °C", valor, resultado)
            LogicaMicroservicios.TipoConversionTemperatura.CELSIUS_A_KELVIN ->
                String.format(Locale.getDefault(), "%.2f °C = %.2f K", valor, resultado)
            LogicaMicroservicios.TipoConversionTemperatura.KELVIN_A_CELSIUS ->
                String.format(Locale.getDefault(), "%.2f K = %.2f °C", valor, resultado)
            LogicaMicroservicios.TipoConversionTemperatura.FAHRENHEIT_A_KELVIN ->
                String.format(Locale.getDefault(), "%.2f °F = %.2f K", valor, resultado)
            LogicaMicroservicios.TipoConversionTemperatura.KELVIN_A_FAHRENHEIT ->
                String.format(Locale.getDefault(), "%.2f K = %.2f °F", valor, resultado)
        }

        tvResultado.text = getString(R.string.label_resultado, resultadoTexto)
    }
}
