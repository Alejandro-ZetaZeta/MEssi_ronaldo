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

class TablaMultiplicarActivity : AppCompatActivity() {

    private lateinit var etNumero: TextInputEditText
    private lateinit var btnGenerar: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tabla_multiplicar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etNumero = findViewById(R.id.etNumero)
        btnGenerar = findViewById(R.id.btnGenerar)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupListeners() {
        btnGenerar.setOnClickListener {
            generarTabla()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun generarTabla() {
        val inputStr = etNumero.text.toString().trim()
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un número", Toast.LENGTH_SHORT).show()
            return
        }

        val numero = inputStr.toDoubleOrNull()
        if (numero == null) {
            Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val lineas = LogicaMicroservicios.generarTablaMultiplicar(numero)
        val sb = StringBuilder()
        sb.append("Tabla del ")
        sb.append(if (numero % 1.0 == 0.0) numero.toInt().toString() else numero.toString())
        sb.append(":\n\n")

        for (linea in lineas) {
            sb.append(linea).append("\n")
        }

        tvResultado.text = sb.toString().trim()
    }
}
