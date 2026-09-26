package com.example.interfaz_to_microservicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Áreas y Perímetros
        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, AreaPerimetroActivity::class.java))
        }

        // 2. Conversor de Temperatura
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, TemperaturaActivity::class.java))
        }

        // 3. Tabla de Multiplicar
        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, TablaMultiplicarActivity::class.java))
        }

        // 4. Calculador IMC
        findViewById<Button>(R.id.button7).setOnClickListener {
            startActivity(Intent(this, ImcActivity::class.java))
        }

        // 5. Números Primos
        findViewById<Button>(R.id.button5).setOnClickListener {
            startActivity(Intent(this, NumerosPrimosActivity::class.java))
        }

        // 6. Adivina el Número
        findViewById<Button>(R.id.button6).setOnClickListener {
            startActivity(Intent(this, AdivinaNumeroActivity::class.java))
        }

        // 7. Conversión de Monedas
        findViewById<Button>(R.id.button15).setOnClickListener {
            startActivity(Intent(this, ConversionMonedasActivity::class.java))
        }

        // 8. Interés Compuesto
        findViewById<Button>(R.id.button14).setOnClickListener {
            startActivity(Intent(this, InteresCompuestoActivity::class.java))
        }
    }
}
