package com.example.interfaz_to_microservicios

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class ImcActivity : AppCompatActivity() {

    private lateinit var etPeso: TextInputEditText
    private lateinit var etAltura: TextInputEditText
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_imc)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etPeso = findViewById(R.id.etPeso)
        etAltura = findViewById(R.id.etAltura)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcularImc()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun calcularImc() {
        val pesoStr = etPeso.text.toString().trim()
        val alturaStr = etAltura.text.toString().trim()

        if (pesoStr.isEmpty() || alturaStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese peso y altura", Toast.LENGTH_SHORT).show()
            return
        }

        var peso = pesoStr.toDoubleOrNull()
        var altura = alturaStr.toDoubleOrNull()

        if (peso == null || altura == null || peso <= 0 || altura <= 0) {
            Toast.makeText(this, "Ingrese valores válidos mayores a 0", Toast.LENGTH_SHORT).show()
            return
        }

        // Si el usuario ingresó altura en cm (ej. 175 en lugar de 1.75), lo convertimos automáticamente a metros
        if (altura > 3.0) {
            altura /= 100.0
        }

        try {
            val res = LogicaMicroservicios.calcularImc(peso, altura)
            val texto = String.format(
                Locale.getDefault(),
                "IMC: %.2f\nCategoría: %s\n\n%s",
                res.imc,
                res.categoria,
                res.descripcion
            )
            tvResultado.text = getString(R.string.label_resultado, texto)
        } catch (e: Exception) {
            Toast.makeText(this, e.message ?: "Error al calcular IMC", Toast.LENGTH_SHORT).show()
        }
    }
}
