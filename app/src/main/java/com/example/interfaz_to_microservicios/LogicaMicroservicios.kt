package com.example.interfaz_to_microservicios

import java.util.Locale
import kotlin.math.pow

object LogicaMicroservicios {

    // 1. Conversor de temperatura
    enum class TipoConversionTemperatura(val label: String) {
        CELSIUS_A_FAHRENHEIT("Celsius a Fahrenheit (°C → °F)"),
        FAHRENHEIT_A_CELSIUS("Fahrenheit a Celsius (°F → °C)"),
        CELSIUS_A_KELVIN("Celsius a Kelvin (°C → K)"),
        KELVIN_A_CELSIUS("Kelvin a Celsius (K → °C)"),
        FAHRENHEIT_A_KELVIN("Fahrenheit a Kelvin (°F → K)"),
        KELVIN_A_FAHRENHEIT("Kelvin a Fahrenheit (K → °F)")
    }

    fun convertirTemperatura(valor: Double, tipo: TipoConversionTemperatura): Double {
        return when (tipo) {
            TipoConversionTemperatura.CELSIUS_A_FAHRENHEIT -> (valor * 9.0 / 5.0) + 32.0
            TipoConversionTemperatura.FAHRENHEIT_A_CELSIUS -> (valor - 32.0) * 5.0 / 9.0
            TipoConversionTemperatura.CELSIUS_A_KELVIN -> valor + 273.15
            TipoConversionTemperatura.KELVIN_A_CELSIUS -> valor - 273.15
            TipoConversionTemperatura.FAHRENHEIT_A_KELVIN -> ((valor - 32.0) * 5.0 / 9.0) + 273.15
            TipoConversionTemperatura.KELVIN_A_FAHRENHEIT -> ((valor - 273.15) * 9.0 / 5.0) + 32.0
        }
    }

    // 2. Tabla de multiplicar
    fun generarTablaMultiplicar(numero: Double): List<String> {
        val lineas = mutableListOf<String>()
        val formatoNumero = if (numero % 1.0 == 0.0) numero.toInt().toString() else numero.toString()
        for (i in 1..10) {
            val resultado = numero * i
            val formatoResultado = if (resultado % 1.0 == 0.0) {
                resultado.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", resultado)
            }
            lineas.add("$formatoNumero × $i = $formatoResultado")
        }
        return lineas
    }

    // 3. Calculadora de IMC
    data class ResultadoImc(val imc: Double, val categoria: String, val descripcion: String)

    fun calcularImc(pesoKg: Double, alturaM: Double): ResultadoImc {
        require(pesoKg > 0) { "El peso debe ser mayor a 0" }
        require(alturaM > 0) { "La altura debe ser mayor a 0" }

        val imc = pesoKg / (alturaM * alturaM)
        val (categoria, descripcion) = when {
            imc < 18.5 -> Pair(
                "Bajo peso",
                "Peso inferior al recomendado. Se sugiere consultar a un profesional de la salud."
            )
            imc < 25.0 -> Pair(
                "Peso normal / Saludable",
                "¡Excelente! Tu peso se encuentra en un rango saludable."
            )
            imc < 30.0 -> Pair(
                "Sobrepeso",
                "Peso superior al recomendado. Se aconseja actividad física regular y dieta equilibrada."
            )
            imc < 35.0 -> Pair(
                "Obesidad Grado I",
                "Obesidad moderada. Se recomienda consultar con un médico o nutricionista."
            )
            imc < 40.0 -> Pair(
                "Obesidad Grado II",
                "Obesidad severa. Alto riesgo de complicaciones de salud asociadas."
            )
            else -> Pair(
                "Obesidad Grado III (Mórbida)",
                "Obesidad muy severa. Requiere atención médica especializada urgente."
            )
        }
        return ResultadoImc(imc, categoria, descripcion)
    }

    // 4. Generador de números primos
    fun esPrimo(n: Int): Boolean {
        if (n <= 1) return false
        if (n == 2) return true
        if (n % 2 == 0) return false
        var divisor = 3
        while (divisor * divisor <= n) {
            if (n % divisor == 0) return false
            divisor += 2
        }
        return true
    }

    fun generarPrimosHasta(limite: Int): List<Int> {
        if (limite < 2) return emptyList()
        val primos = mutableListOf<Int>()
        for (i in 2..limite) {
            if (esPrimo(i)) {
                primos.add(i)
            }
        }
        return primos
    }

    // 5. Adivina el número
    data class PistaAdivinanza(
        val acertado: Boolean,
        val mensaje: String
    )

    class JuegoAdivinaNumero(val min: Int = 1, val max: Int = 100) {
        var numeroSecreto: Int = 0
            private set
        var intentos: Int = 0
            private set
        var terminado: Boolean = false
            private set

        init {
            reiniciar()
        }

        fun reiniciar() {
            numeroSecreto = kotlin.random.Random.nextInt(min, max + 1)
            intentos = 0
            terminado = false
        }

        // Permite fijar el secreto para pruebas unitarias
        fun forzarNumeroSecreto(secreto: Int) {
            numeroSecreto = secreto
            intentos = 0
            terminado = false
        }

        fun jugarIntento(intento: Int): PistaAdivinanza {
            if (terminado) {
                return PistaAdivinanza(true, "El juego ya finalizó. Inicia una nueva partida.")
            }
            intentos++
            return when {
                intento == numeroSecreto -> {
                    terminado = true
                    PistaAdivinanza(true, "🎉 ¡Felicidades! Adivinaste el número $numeroSecreto en $intentos intento(s).")
                }
                intento < numeroSecreto -> {
                    PistaAdivinanza(false, "📈 El número secreto es MAYOR que $intento.")
                }
                else -> {
                    PistaAdivinanza(false, "📉 El número secreto es MENOR que $intento.")
                }
            }
        }
    }

    // 6. Calculadora de interés compuesto
    data class DetallePeriodo(
        val periodo: Int,
        val saldo: Double,
        val interesGanadoEnPeriodo: Double
    )

    data class ResultadoInteresCompuesto(
        val capitalInicial: Double,
        val montoFinal: Double,
        val interesTotal: Double,
        val detalles: List<DetallePeriodo>
    )

    enum class TipoCapitalizacion(val descripcion: String, val frecuenciaAnual: Int) {
        ANUAL("Anual (1 vez al año)", 1),
        SEMESTRAL("Semestral (2 veces al año)", 2),
        TRIMESTRAL("Trimestral (4 veces al año)", 4),
        MENSUAL("Mensual (12 veces al año)", 12),
        DIARIA("Diaria (365 veces al año)", 365)
    }

    fun calcularInteresCompuesto(
        capital: Double,
        tasaAnualPct: Double,
        tiempoAnos: Double,
        capitalizacion: TipoCapitalizacion
    ): ResultadoInteresCompuesto {
        require(capital > 0) { "El capital debe ser mayor a 0" }
        require(tasaAnualPct >= 0) { "La tasa debe ser mayor o igual a 0" }
        require(tiempoAnos > 0) { "El período de tiempo debe ser mayor a 0" }

        val r = tasaAnualPct / 100.0
        val n = capitalizacion.frecuenciaAnual
        val totalPeriodos = (n * tiempoAnos).toInt().coerceAtLeast(1)
        val tasaPorPeriodo = r / n

        // Fórmula de interés compuesto usando potencias
        val factorPotencia = (1.0 + tasaPorPeriodo).pow(totalPeriodos.toDouble())
        val montoFinal = capital * factorPotencia
        val interesTotal = montoFinal - capital

        // Crecimiento período a período usando ciclo for
        val detalles = mutableListOf<DetallePeriodo>()
        var saldoAnterior = capital
        // Mostramos hasta un máximo razonable de períodos para no saturar memoria en diaria de muchos años
        val periodosAMostrar = minOf(totalPeriodos, 120)
        for (periodo in 1..periodosAMostrar) {
            val saldoActual = capital * (1.0 + tasaPorPeriodo).pow(periodo.toDouble())
            val interesGanado = saldoActual - saldoAnterior
            detalles.add(DetallePeriodo(periodo, saldoActual, interesGanado))
            saldoAnterior = saldoActual
        }

        return ResultadoInteresCompuesto(capital, montoFinal, interesTotal, detalles)
    }

    // 7. Conversión de 5 tipos de monedas
    enum class Moneda(val codigo: String, val nombre: String, val simbolo: String, val tasaRespectoUsd: Double) {
        USD("USD", "Dólar Estadounidense", "$", 1.0),
        EUR("EUR", "Euro", "€", 0.92),
        GBP("GBP", "Libra Esterlina", "£", 0.79),
        MXN("MXN", "Peso Mexicano", "$", 17.50),
        JPY("JPY", "Yen Japonés", "¥", 155.0)
    }

    data class ResultadoConversionMoneda(
        val montoOrigen: Double,
        val monedaOrigen: Moneda,
        val montoDestino: Double,
        val monedaDestino: Moneda,
        val equivalencias: Map<Moneda, Double>
    )

    fun convertirMoneda(monto: Double, origen: Moneda, destino: Moneda): ResultadoConversionMoneda {
        require(monto >= 0) { "El monto debe ser mayor o igual a 0" }

        val montoEnUsd = monto / origen.tasaRespectoUsd
        val montoDestino = montoEnUsd * destino.tasaRespectoUsd

        val equivalencias = mutableMapOf<Moneda, Double>()
        for (m in Moneda.values()) {
            equivalencias[m] = montoEnUsd * m.tasaRespectoUsd
        }

        return ResultadoConversionMoneda(monto, origen, montoDestino, destino, equivalencias)
    }
}
