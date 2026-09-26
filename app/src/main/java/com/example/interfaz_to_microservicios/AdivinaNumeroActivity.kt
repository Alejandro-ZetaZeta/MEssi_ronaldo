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

class AdivinaNumeroActivity : AppCompatActivity() {

    private lateinit var etIntento: TextInputEditText
    private lateinit var btnAdivinar: Button
    private lateinit var btnReiniciar: Button
    private lateinit var tvIntentos: TextView
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    private val juego = LogicaMicroservicios.JuegoAdivinaNumero(1, 100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adivina_numero)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
        actualizarUI()
    }

    private fun initViews() {
        etIntento = findViewById(R.id.etIntento)
        btnAdivinar = findViewById(R.id.btnAdivinar)
        btnReiniciar = findViewById(R.id.btnReiniciar)
        tvIntentos = findViewById(R.id.tvIntentos)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupListeners() {
        btnAdivinar.setOnClickListener {
            procesarIntento()
        }

        btnReiniciar.setOnClickListener {
            juego.reiniciar()
            etIntento.text?.clear()
            tvResultado.text = "¡Nuevo juego iniciado! Adivina un número entre 1 y 100."
            actualizarUI()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun procesarIntento() {
        val inputStr = etIntento.text.toString().trim()
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un número", Toast.LENGTH_SHORT).show()
            return
        }

        val intento = inputStr.toIntOrNull()
        if (intento == null || intento !in 1..100) {
            Toast.makeText(this, "Ingrese un número válido entre 1 y 100", Toast.LENGTH_SHORT).show()
            return
        }

        val pista = juego.jugarIntento(intento)
        tvResultado.text = pista.mensaje
        actualizarUI()

        if (pista.acertado) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarUI() {
        tvIntentos.text = "Intentos realizados: ${juego.intentos}"
        if (juego.terminado) {
            btnAdivinar.isEnabled = false
            etIntento.isEnabled = false
        } else {
            btnAdivinar.isEnabled = true
            etIntento.isEnabled = true
        }
    }
}
