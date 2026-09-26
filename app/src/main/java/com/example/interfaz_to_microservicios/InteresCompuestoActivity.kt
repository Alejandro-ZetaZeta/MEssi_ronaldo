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

class InteresCompuestoActivity : AppCompatActivity() {

    private lateinit var etCapital: TextInputEditText
    private lateinit var etTasa: TextInputEditText
    private lateinit var etTiempo: TextInputEditText
    private lateinit var spCapitalizacion: Spinner
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_interes_compuesto)

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
        etCapital = findViewById(R.id.etCapital)
        etTasa = findViewById(R.id.etTasa)
        etTiempo = findViewById(R.id.etTiempo)
        spCapitalizacion = findViewById(R.id.spCapitalizacion)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupSpinner() {
        val tipos = LogicaMicroservicios.TipoCapitalizacion.values().map { it.descripcion }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCapitalizacion.adapter = adapter
    }

    private fun setupListeners() {
        btnCalcular.setOnClickListener {
            calcular()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun calcular() {
        val capitalStr = etCapital.text.toString().trim()
        val tasaStr = etTasa.text.toString().trim()
        val tiempoStr = etTiempo.text.toString().trim()

        if (capitalStr.isEmpty() || tasaStr.isEmpty() || tiempoStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val capital = capitalStr.toDoubleOrNull()
        val tasa = tasaStr.toDoubleOrNull()
        val tiempo = tiempoStr.toDoubleOrNull()

        if (capital == null || tasa == null || tiempo == null || capital <= 0 || tasa < 0 || tiempo <= 0) {
            Toast.makeText(this, "Ingrese valores válidos mayores a 0", Toast.LENGTH_SHORT).show()
            return
        }

        val posicion = spCapitalizacion.selectedItemPosition
        val tipoCap = LogicaMicroservicios.TipoCapitalizacion.values()[posicion]

        try {
            val res = LogicaMicroservicios.calcularInteresCompuesto(capital, tasa, tiempo, tipoCap)
            val sb = StringBuilder()
            sb.append(String.format(Locale.getDefault(), "Monto Final: $%.2f USD\n", res.montoFinal))
            sb.append(String.format(Locale.getDefault(), "Interés Total: $%.2f USD\n\n", res.interesTotal))
            sb.append("Crecimiento período a período:\n")

            for (det in res.detalles) {
                sb.append(String.format(Locale.getDefault(), "• P%d: Saldo: $%.2f (+$%.2f)\n", det.periodo, det.saldo, det.interesGanadoEnPeriodo))
            }

            tvResultado.text = sb.toString().trim()
        } catch (e: Exception) {
            Toast.makeText(this, e.message ?: "Error en el cálculo", Toast.LENGTH_SHORT).show()
        }
    }
}
