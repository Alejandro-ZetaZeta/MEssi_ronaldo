package com.example.interfaz_to_microservicios

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale
import kotlin.math.PI

class AreaPerimetroActivity : AppCompatActivity() {

    private lateinit var rgFigura: RadioGroup
    private lateinit var rbCirculo: RadioButton
    private lateinit var rbRectangulo: RadioButton
    private lateinit var rbTriangulo: RadioButton

    private lateinit var rgOperacion: RadioGroup
    private lateinit var rbArea: RadioButton
    private lateinit var rbPerimetro: RadioButton

    private lateinit var tilDato1: TextInputLayout
    private lateinit var etDato1: TextInputEditText

    private lateinit var tilDato2: TextInputLayout
    private lateinit var etDato2: TextInputEditText

    private lateinit var tilDato3: TextInputLayout
    private lateinit var etDato3: TextInputEditText

    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_area_perimetro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupListeners()
        actualizarCampos()
    }

    private fun initViews() {
        rgFigura = findViewById(R.id.rgFigura)
        rbCirculo = findViewById(R.id.rbCirculo)
        rbRectangulo = findViewById(R.id.rbRectangulo)
        rbTriangulo = findViewById(R.id.rbTriangulo)

        rgOperacion = findViewById(R.id.rgOperacion)
        rbArea = findViewById(R.id.rbArea)
        rbPerimetro = findViewById(R.id.rbPerimetro)

        tilDato1 = findViewById(R.id.tilDato1)
        etDato1 = findViewById(R.id.etDato1)

        tilDato2 = findViewById(R.id.tilDato2)
        etDato2 = findViewById(R.id.etDato2)

        tilDato3 = findViewById(R.id.tilDato3)
        etDato3 = findViewById(R.id.etDato3)

        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun setupListeners() {
        rgFigura.setOnCheckedChangeListener { _, _ ->
            limpiarCampos()
            actualizarCampos()
        }

        rgOperacion.setOnCheckedChangeListener { _, _ ->
            limpiarCampos()
            actualizarCampos()
        }

        btnCalcular.setOnClickListener {
            calcular()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun limpiarCampos() {
        etDato1.text?.clear()
        etDato2.text?.clear()
        etDato3.text?.clear()
        tvResultado.setText(R.string.label_resultado_inicial)
    }

    private fun actualizarCampos() {
        when {
            rbCirculo.isChecked -> {
                tilDato1.visibility = View.VISIBLE
                tilDato1.hint = getString(R.string.hint_radio)

                tilDato2.visibility = View.GONE
                tilDato3.visibility = View.GONE
            }
            rbRectangulo.isChecked -> {
                tilDato1.visibility = View.VISIBLE
                tilDato1.hint = getString(R.string.hint_base)

                tilDato2.visibility = View.VISIBLE
                tilDato2.hint = getString(R.string.hint_altura)

                tilDato3.visibility = View.GONE
            }
            rbTriangulo.isChecked -> {
                if (rbArea.isChecked) {
                    tilDato1.visibility = View.VISIBLE
                    tilDato1.hint = getString(R.string.hint_base)

                    tilDato2.visibility = View.VISIBLE
                    tilDato2.hint = getString(R.string.hint_altura)

                    tilDato3.visibility = View.GONE
                } else {
                    tilDato1.visibility = View.VISIBLE
                    tilDato1.hint = getString(R.string.hint_lado_a)

                    tilDato2.visibility = View.VISIBLE
                    tilDato2.hint = getString(R.string.hint_lado_b)

                    tilDato3.visibility = View.VISIBLE
                    tilDato3.hint = getString(R.string.hint_lado_c)
                }
            }
        }
    }

    private fun calcular() {
        val d1Str = etDato1.text.toString().trim()
        val d2Str = etDato2.text.toString().trim()
        val d3Str = etDato3.text.toString().trim()

        val esArea = rbArea.isChecked

        when {
            rbCirculo.isChecked -> {
                if (d1Str.isEmpty()) {
                    Toast.makeText(this, "Por favor ingrese el radio", Toast.LENGTH_SHORT).show()
                    return
                }
                val r = d1Str.toDoubleOrNull()
                if ((r == null) || (r <= 0)) {
                    Toast.makeText(this, "Ingrese un radio válido mayor a 0", Toast.LENGTH_SHORT).show()
                    return
                }

                if (esArea) {
                    val area = PI * r * r
                    val resText = String.format(Locale.getDefault(), "Área del Círculo: %.2f", area)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                } else {
                    val perimetro = 2 * PI * r
                    val resText = String.format(Locale.getDefault(), "Perímetro (Circunferencia): %.2f", perimetro)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                }
            }

            rbRectangulo.isChecked -> {
                if (d1Str.isEmpty() || d2Str.isEmpty()) {
                    Toast.makeText(this, "Por favor ingrese la base y la altura", Toast.LENGTH_SHORT).show()
                    return
                }
                val b = d1Str.toDoubleOrNull()
                val h = d2Str.toDoubleOrNull()
                if ((b == null) || (b <= 0) || (h == null) || (h <= 0)) {
                    Toast.makeText(this, "Ingrese valores válidos mayores a 0", Toast.LENGTH_SHORT).show()
                    return
                }

                if (esArea) {
                    val area = b * h
                    val resText = String.format(Locale.getDefault(), "Área del Rectángulo: %.2f", area)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                } else {
                    val perimetro = 2 * (b + h)
                    val resText = String.format(Locale.getDefault(), "Perímetro del Rectángulo: %.2f", perimetro)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                }
            }

            rbTriangulo.isChecked -> {
                if (esArea) {
                    if (d1Str.isEmpty() || d2Str.isEmpty()) {
                        Toast.makeText(this, "Por favor ingrese la base y la altura", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val b = d1Str.toDoubleOrNull()
                    val h = d2Str.toDoubleOrNull()
                    if ((b == null) || (b <= 0) || (h == null) || (h <= 0)) {
                        Toast.makeText(this, "Ingrese valores válidos mayores a 0", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val area = (b * h) / 2.0
                    val resText = String.format(Locale.getDefault(), "Área del Triángulo: %.2f", area)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                } else {
                    if (d1Str.isEmpty() || d2Str.isEmpty() || d3Str.isEmpty()) {
                        Toast.makeText(this, "Por favor ingrese los tres lados", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val a = d1Str.toDoubleOrNull()
                    val b = d2Str.toDoubleOrNull()
                    val c = d3Str.toDoubleOrNull()
                    if ((a == null) || (a <= 0) || (b == null) || (b <= 0) || (c == null) || (c <= 0)) {
                        Toast.makeText(this, "Ingrese lados válidos mayores a 0", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val perimetro = a + b + c
                    val resText = String.format(Locale.getDefault(), "Perímetro del Triángulo: %.2f", perimetro)
                    tvResultado.text = getString(R.string.label_resultado, resText)
                }
            }
        }
    }
}
