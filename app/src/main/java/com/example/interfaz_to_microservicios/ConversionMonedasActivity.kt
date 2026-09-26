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

class ConversionMonedasActivity : AppCompatActivity() {

    private lateinit var etMonto: TextInputEditText
    private lateinit var spOrigen: Spinner
    private lateinit var spDestino: Spinner
    private lateinit var btnConvertir: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conversion_monedas)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupSpinners()
        setupListeners()
    }

    private fun initViews() {
        etMonto = findViewById(R.id.etMonto)
        spOrigen = findViewById(R.id.spOrigen)
        spDestino = findViewById(R.id.spDestino)
        btnConvertir = findViewById(R.id.btnConvertir)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupSpinners() {
        val nombresMonedas = LogicaMicroservicios.Moneda.values().map { "${it.codigo} - ${it.nombre} (${it.simbolo})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresMonedas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spOrigen.adapter = adapter
        spDestino.adapter = adapter
        if (nombresMonedas.size > 1) {
            spDestino.setSelection(1) // Default to second currency (e.g. EUR)
        }
    }

    private fun setupListeners() {
        btnConvertir.setOnClickListener {
            realizarConversion()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun realizarConversion() {
        val montoStr = etMonto.text.toString().trim()
        if (montoStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un monto a convertir", Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoStr.toDoubleOrNull()
        if (monto == null || monto < 0) {
            Toast.makeText(this, "Ingrese un monto válido mayor o igual a 0", Toast.LENGTH_SHORT).show()
            return
        }

        val idxOrigen = spOrigen.selectedItemPosition
        val idxDestino = spDestino.selectedItemPosition

        val monedaOrigen = LogicaMicroservicios.Moneda.values()[idxOrigen]
        val monedaDestino = LogicaMicroservicios.Moneda.values()[idxDestino]

        try {
            val res = LogicaMicroservicios.convertirMoneda(monto, monedaOrigen, monedaDestino)
            val sb = StringBuilder()
            sb.append(String.format(Locale.getDefault(), "Resultado principal:\n%.2f %s (%s) = %.2f %s (%s)\n\n",
                res.montoOrigen, res.monedaOrigen.codigo, res.monedaOrigen.simbolo,
                res.montoDestino, res.monedaDestino.codigo, res.monedaDestino.simbolo))
            sb.append("Equivalencias en 5 monedas:\n")

            for ((moneda, valor) in res.equivalencias) {
                sb.append(String.format(Locale.getDefault(), "• %s (%s): %.2f\n", moneda.codigo, moneda.simbolo, valor))
            }

            tvResultado.text = sb.toString().trim()
        } catch (e: Exception) {
            Toast.makeText(this, e.message ?: "Error en la conversión", Toast.LENGTH_SHORT).show()
        }
    }
}
