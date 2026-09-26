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

class NumerosPrimosActivity : AppCompatActivity() {

    private lateinit var etLimite: TextInputEditText
    private lateinit var btnGenerar: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_numeros_primos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etLimite = findViewById(R.id.etLimite)
        btnGenerar = findViewById(R.id.btnGenerar)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupListeners() {
        btnGenerar.setOnClickListener {
            generarPrimos()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun generarPrimos() {
        val inputStr = etLimite.text.toString().trim()
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un límite", Toast.LENGTH_SHORT).show()
            return
        }

        val limite = inputStr.toIntOrNull()
        if (limite == null || limite < 2) {
            Toast.makeText(this, "Ingrese un número entero mayor o igual a 2", Toast.LENGTH_SHORT).show()
            return
        }

        if (limite > 100000) {
            Toast.makeText(this, "Por favor ingrese un límite menor o igual a 100,000", Toast.LENGTH_SHORT).show()
            return
        }

        val primos = LogicaMicroservicios.generarPrimosHasta(limite)
        val sb = StringBuilder()
        sb.append("Primos hasta $limite (Total: ${primos.size}):\n\n")
        sb.append(primos.joinToString(", "))

        tvResultado.text = sb.toString()
    }
}
