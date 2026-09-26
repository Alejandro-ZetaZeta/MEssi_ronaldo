package com.example.interfaz_to_microservicios

import org.junit.Assert.*
import org.junit.Test

class LogicaMicroserviciosTest {

    @Test
    fun testConversorTemperatura() {
        // 0 C = 32 F
        val f1 = LogicaMicroservicios.convertirTemperatura(0.0, LogicaMicroservicios.TipoConversionTemperatura.CELSIUS_A_FAHRENHEIT)
        assertEquals(32.0, f1, 0.01)

        // 100 C = 212 F
        val f2 = LogicaMicroservicios.convertirTemperatura(100.0, LogicaMicroservicios.TipoConversionTemperatura.CELSIUS_A_FAHRENHEIT)
        assertEquals(212.0, f2, 0.01)

        // 32 F = 0 C
        val c1 = LogicaMicroservicios.convertirTemperatura(32.0, LogicaMicroservicios.TipoConversionTemperatura.FAHRENHEIT_A_CELSIUS)
        assertEquals(0.0, c1, 0.01)

        // 0 C = 273.15 K
        val k1 = LogicaMicroservicios.convertirTemperatura(0.0, LogicaMicroservicios.TipoConversionTemperatura.CELSIUS_A_KELVIN)
        assertEquals(273.15, k1, 0.01)

        // 273.15 K = 0 C
        val c2 = LogicaMicroservicios.convertirTemperatura(273.15, LogicaMicroservicios.TipoConversionTemperatura.KELVIN_A_CELSIUS)
        assertEquals(0.0, c2, 0.01)
    }

    @Test
    fun testTablaMultiplicar() {
        val tabla5 = LogicaMicroservicios.generarTablaMultiplicar(5.0)
        assertEquals(10, tabla5.size)
        assertEquals("5 × 1 = 5", tabla5[0])
        assertEquals("5 × 10 = 50", tabla5[9])

        val tablaDec = LogicaMicroservicios.generarTablaMultiplicar(2.5)
        assertEquals("2.5 × 1 = 2.50", tablaDec[0])
        assertEquals("2.5 × 4 = 10", tablaDec[3])
    }

    @Test
    fun testCalculadoraImc() {
        // Peso 70 kg, altura 1.75 m -> IMC = 70 / (1.75^2) ≈ 22.86 (Normal)
        val resNormal = LogicaMicroservicios.calcularImc(70.0, 1.75)
        assertEquals(22.86, resNormal.imc, 0.05)
        assertEquals("Peso normal / Saludable", resNormal.categoria)

        // Bajo peso: 45 kg, 1.75 m -> IMC = 14.69
        val resBajo = LogicaMicroservicios.calcularImc(45.0, 1.75)
        assertTrue(resBajo.imc < 18.5)
        assertEquals("Bajo peso", resBajo.categoria)

        // Sobrepeso: 85 kg, 1.75 m -> IMC = 27.76
        val resSobrepeso = LogicaMicroservicios.calcularImc(85.0, 1.75)
        assertEquals("Sobrepeso", resSobrepeso.categoria)

        // Obesidad: 110 kg, 1.70 m -> IMC = 38.06
        val resObesidad2 = LogicaMicroservicios.calcularImc(110.0, 1.70)
        assertEquals("Obesidad Grado II", resObesidad2.categoria)
    }

    @Test
    fun testNumerosPrimos() {
        assertFalse(LogicaMicroservicios.esPrimo(0))
        assertFalse(LogicaMicroservicios.esPrimo(1))
        assertTrue(LogicaMicroservicios.esPrimo(2))
        assertTrue(LogicaMicroservicios.esPrimo(3))
        assertFalse(LogicaMicroservicios.esPrimo(4))
        assertTrue(LogicaMicroservicios.esPrimo(5))
        assertTrue(LogicaMicroservicios.esPrimo(13))
        assertFalse(LogicaMicroservicios.esPrimo(15))
        assertTrue(LogicaMicroservicios.esPrimo(97))

        val primosHasta20 = LogicaMicroservicios.generarPrimosHasta(20)
        assertEquals(listOf(2, 3, 5, 7, 11, 13, 17, 19), primosHasta20)

        val primosHasta1 = LogicaMicroservicios.generarPrimosHasta(1)
        assertTrue(primosHasta1.isEmpty())
    }

    @Test
    fun testAdivinaElNumero() {
        val juego = LogicaMicroservicios.JuegoAdivinaNumero(1, 100)
        juego.forzarNumeroSecreto(50)

        val intento1 = juego.jugarIntento(30)
        assertFalse(intento1.acertado)
        assertTrue(intento1.mensaje.contains("MAYOR"))
        assertEquals(1, juego.intentos)

        val intento2 = juego.jugarIntento(70)
        assertFalse(intento2.acertado)
        assertTrue(intento2.mensaje.contains("MENOR"))
        assertEquals(2, juego.intentos)

        val intento3 = juego.jugarIntento(50)
        assertTrue(intento3.acertado)
        assertTrue(intento3.mensaje.contains("Felicidades"))
        assertEquals(3, juego.intentos)
        assertTrue(juego.terminado)
    }

    @Test
    fun testInteresCompuesto() {
        // Capital = 1000, Tasa = 10% anual, 2 años, capitalización anual (n=1)
        // Monto = 1000 * (1 + 0.10)^2 = 1210.0
        val res = LogicaMicroservicios.calcularInteresCompuesto(
            capital = 1000.0,
            tasaAnualPct = 10.0,
            tiempoAnos = 2.0,
            capitalizacion = LogicaMicroservicios.TipoCapitalizacion.ANUAL
        )
        assertEquals(1210.0, res.montoFinal, 0.01)
        assertEquals(210.0, res.interesTotal, 0.01)
        assertEquals(2, res.detalles.size)
        assertEquals(1100.0, res.detalles[0].saldo, 0.01)
        assertEquals(100.0, res.detalles[0].interesGanadoEnPeriodo, 0.01)
        assertEquals(1210.0, res.detalles[1].saldo, 0.01)
        assertEquals(110.0, res.detalles[1].interesGanadoEnPeriodo, 0.01)
    }

    @Test
    fun testConversionMonedas() {
        // 100 USD a EUR (1 USD = 0.92 EUR)
        val resUsdEur = LogicaMicroservicios.convertirMoneda(
            monto = 100.0,
            origen = LogicaMicroservicios.Moneda.USD,
            destino = LogicaMicroservicios.Moneda.EUR
        )
        assertEquals(92.0, resUsdEur.montoDestino, 0.01)
        assertEquals(5, resUsdEur.equivalencias.size)

        // 175 MXN a USD (1 USD = 17.50 MXN) -> 175 MXN = 10 USD
        val resMxnUsd = LogicaMicroservicios.convertirMoneda(
            monto = 175.0,
            origen = LogicaMicroservicios.Moneda.MXN,
            destino = LogicaMicroservicios.Moneda.USD
        )
        assertEquals(10.0, resMxnUsd.montoDestino, 0.01)
    }
}
