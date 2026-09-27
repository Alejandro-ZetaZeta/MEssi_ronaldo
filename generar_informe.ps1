# Script para generar Informe en formato .docx utilizando Word COM Automation
$ErrorActionPreference = "Stop"

$workspaceDir = "C:\Users\Me-and-Mythos\AndroidStudioProjects\Interfaz_to_microservicios"
$capturasDir = "$workspaceDir\capturas"
$outputDocx = "$workspaceDir\Informe_Interfaz_to_Microservicios.docx"

Write-Host "Iniciando Microsoft Word..."
$word = New-Object -ComObject Word.Application
$word.Visible = $false
$doc = $word.Documents.Add()

# Margenes del documento (en puntos: 54 pt = 1.9 cm)
$doc.PageSetup.TopMargin = 54
$doc.PageSetup.BottomMargin = 54
$doc.PageSetup.LeftMargin = 54
$doc.PageSetup.RightMargin = 54

# Colores en formato BGR para Word COM
function Get-BGR($r, $g, $b) {
    return ($r + ($g * 256) + ($b * 65536))
}

$colorNavy   = Get-BGR 30 58 138     # Azul marino institucional #1E3A8A
$colorBlue   = Get-BGR 41 82 163     # Azul primario #2952A3
$colorDark   = Get-BGR 33 37 41      # Texto oscuro #212529
$colorMuted  = Get-BGR 100 116 139   # Gris texto secundario #64748B
$colorBgHead = Get-BGR 234 242 253   # Fondo encabezado tabla #EAF2FD
$colorBgAlt  = Get-BGR 248 250 252   # Fondo alterno filas #F8FAFC
$colorBorder = Get-BGR 203 213 225   # Gris borde #CBD5E1

function Add-Paragraph($text, $fontName="Calibri", $fontSize=11, $bold=$false, $color=$colorDark, $align=0, $spaceAfter=6, $spaceBefore=0) {
    $p = $doc.Paragraphs.Add()
    $p.Range.Text = $text
    $p.Range.Font.Name = $fontName
    $p.Range.Font.Size = $fontSize
    $p.Range.Font.Bold = if ($bold) { 1 } else { 0 }
    $p.Range.Font.Color = $color
    $p.Alignment = $align
    $p.SpaceBefore = $spaceBefore
    $p.SpaceAfter = $spaceAfter
    $p.Range.InsertParagraphAfter()
    return $null
}

function Add-Heading1($text) {
    return Add-Paragraph $text "Calibri" 18 $true $colorNavy 0 6 16
}

function Add-Heading2($text) {
    return Add-Paragraph $text "Calibri" 14 $true $colorBlue 0 4 12
}

function Add-Callout($title, $text) {
    $tbl = $doc.Tables.Add($doc.Paragraphs.Add().Range, 1, 1)
    $tbl.Borders.Enable = 0
    $tbl.Cell(1,1).Range.Shading.BackgroundPatternColor = $colorBgHead
    $tbl.Cell(1,1).TopPadding = 8
    $tbl.Cell(1,1).BottomPadding = 8
    $tbl.Cell(1,1).LeftPadding = 12
    $tbl.Cell(1,1).RightPadding = 12
    $tbl.Borders.Item(-2).LineStyle = 1 # Left border (-2 = wdBorderLeft)
    $tbl.Borders.Item(-2).LineWidth = 24 # 3pt
    $tbl.Borders.Item(-2).Color = $colorNavy

    $cellRange = $tbl.Cell(1,1).Range
    $cellRange.Font.Name = "Calibri"
    $cellRange.Font.Size = 10.5
    $cellRange.Font.Color = $colorDark
    $cellRange.Text = "$title`r`n$text"
    $doc.Paragraphs.Add().Range.InsertParagraphAfter()
}

function Add-ImageWithCaption($imagePath, $captionText, $widthPt=180) {
    if (Test-Path $imagePath) {
        $pImg = $doc.Paragraphs.Add()
        $pImg.Alignment = 1 # Center
        $pImg.SpaceBefore = 6
        $pImg.SpaceAfter = 4
        
        $shape = $doc.InlineShapes.AddPicture($imagePath, $false, $true, $pImg.Range)
        $ratio = $shape.Height / $shape.Width
        $shape.Width = $widthPt
        $shape.Height = $widthPt * $ratio

        $pCap = $doc.Paragraphs.Add()
        $pCap.Alignment = 1 # Center
        $pCap.Range.Font.Name = "Calibri"
        $pCap.Range.Font.Size = 9.5
        $pCap.Range.Font.Italic = 1
        $pCap.Range.Font.Color = $colorMuted
        $pCap.Range.Text = $captionText
        $pCap.SpaceAfter = 10
        $pCap.Range.InsertParagraphAfter()
    }
}

function Add-ModuleTable($dataDict) {
    $rows = $dataDict.Keys.Count
    $tbl = $doc.Tables.Add($doc.Paragraphs.Add().Range, $rows, 2)
    $tbl.Borders.InsideLineStyle = 1
    $tbl.Borders.OutsideLineStyle = 1
    $tbl.Borders.InsideColor = $colorBorder
    $tbl.Borders.OutsideColor = $colorBorder
    $tbl.Columns.Item(1).Width = 140
    $tbl.Columns.Item(2).Width = 360

    $rowIndex = 1
    foreach ($key in $dataDict.Keys) {
        $cellKey = $tbl.Cell($rowIndex, 1)
        $cellKey.Range.Text = $key
        $cellKey.Range.Font.Name = "Calibri"
        $cellKey.Range.Font.Size = 10
        $cellKey.Range.Font.Bold = 1
        $cellKey.Range.Font.Color = $colorNavy
        $cellKey.Range.Shading.BackgroundPatternColor = $colorBgHead
        $cellKey.TopPadding = 5
        $cellKey.BottomPadding = 5
        $cellKey.LeftPadding = 8
        $cellKey.RightPadding = 8

        $cellVal = $tbl.Cell($rowIndex, 2)
        $cellVal.Range.Text = $dataDict[$key]
        $cellVal.Range.Font.Name = "Calibri"
        $cellVal.Range.Font.Size = 10
        $cellVal.Range.Font.Color = $colorDark
        if ($rowIndex % 2 -eq 0) {
            $cellVal.Range.Shading.BackgroundPatternColor = $colorBgAlt
        }
        $cellVal.TopPadding = 5
        $cellVal.BottomPadding = 5
        $cellVal.LeftPadding = 8
        $cellVal.RightPadding = 8

        $rowIndex++
    }
    $doc.Paragraphs.Add().Range.InsertParagraphAfter()
}

Write-Host "Generando Portada..."
# --- PORTADA ---
Add-Paragraph "INFORME TECNICO DE LABORATORIO" "Calibri" 13 $true $colorMuted 1 12 36
Add-Paragraph "DESARROLLO DE APLICACION ANDROID" "Calibri" 22 $true $colorNavy 1 4 0
Add-Paragraph "SUITE DE MICROSERVICIOS Y MODULOS DE CALCULO" "Calibri" 15 $true $colorBlue 1 14 0

# Linea decorativa
$pDiv = $doc.Paragraphs.Add()
$pDiv.Alignment = 1
$pDiv.Range.Text = "__________________________________________________"
$pDiv.Range.Font.Color = $colorBorder
$pDiv.Range.Font.Bold = 1
$pDiv.SpaceAfter = 20
$pDiv.Range.InsertParagraphAfter()

# Imagen de portada (Menu Principal)
Add-ImageWithCaption "$capturasDir\00_menu_principal.png" "Figura 1: Vista general del Menu de Microservicios en ejecucion sobre Emulador Android" 160

# Metadatos en portada
$metaTbl = $doc.Tables.Add($doc.Paragraphs.Add().Range, 5, 2)
$metaTbl.Borders.InsideLineStyle = 1
$metaTbl.Borders.OutsideLineStyle = 1
$metaTbl.Borders.InsideColor = $colorBorder
$metaTbl.Borders.OutsideColor = $colorBorder
$metaTbl.Columns.Item(1).Width = 150
$metaTbl.Columns.Item(2).Width = 320

$metaData = [ordered]@{
    "Proyecto / Aplicacion"  = "Interfaz_to_microservicios"
    "Autor / Desarrollador"  = "Me-and-Mythos"
    "Entorno y Lenguaje"     = "Android Studio / Kotlin / Android SDK 34"
    "Arquitectura"           = "Activities Material 3 desacopladas de LogicaMicroservicios.kt"
    "Fecha del Informe"      = "Septiembre 2026"
}

$rIdx = 1
foreach ($k in $metaData.Keys) {
    $cK = $metaTbl.Cell($rIdx, 1)
    $cK.Range.Text = $k
    $cK.Range.Font.Bold = 1
    $cK.Range.Font.Size = 10
    $cK.Range.Font.Color = $colorNavy
    $cK.Range.Shading.BackgroundPatternColor = $colorBgHead
    $cK.TopPadding = 5; $cK.BottomPadding = 5; $cK.LeftPadding = 8; $cK.RightPadding = 8

    $cV = $metaTbl.Cell($rIdx, 2)
    $cV.Range.Text = $metaData[$k]
    $cV.Range.Font.Size = 10
    $cV.Range.Font.Color = $colorDark
    $cV.TopPadding = 5; $cV.BottomPadding = 5; $cV.LeftPadding = 8; $cV.RightPadding = 8
    $rIdx++
}

# Salto de pagina
$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Seccion 1: Introduccion..."
# --- INTRODUCCION Y ARQUITECTURA ---
Add-Heading1 "1. Introduccion y Arquitectura General"
Add-Paragraph "El presente proyecto consiste en el diseno y construccion de una aplicacion movil nativa para Android, desarrollada en el lenguaje Kotlin. La aplicacion funciona como una plataforma centralizada que reune una suite de ocho (8) modulos o 'microservicios' de utilidad practica, abarcando calculos geometricos, conversiones termicas y de divisas, operaciones matematicas, evaluacion de indices de salud, calculos financieros y un juego interactivo de adivinanza." "Calibri" 11 $false $colorDark 3 8 0

Add-Callout "Patron de Diseno: Desacoplamiento de la Logica de Negocio" "A fin de cumplir con las mejores practicas de ingenieria de software (Single Responsibility Principle), las pantallas (Activities) unicamente gestionan la captura de datos y la presentacion visual. Todos los algoritmos, validaciones y formulas matematicas residen de forma independiente en el objeto singleton LogicaMicroservicios.kt, lo que permite ejecutar pruebas unitarias con JUnit de forma aislada y sin dependencias del ciclo de vida de Android."

Add-Heading2 "Navegacion y Pantalla Principal (MainActivity)"
Add-Paragraph "La interfaz de inicio (MainActivity) organiza los accesos a los ocho modulos mediante una disposicion en cuadricula utilizando TableLayout y botones estilizados con Material Design 3. Cada opcion dispara un Intent explicito hacia la Activity correspondiente. La aplicacion cuenta con diseno adaptable para modo vertical (portrait) y horizontal (landscape) y compatibilidad Edge-to-Edge para aprovechar la totalidad de la pantalla del dispositivo." "Calibri" 11 $false $colorDark 3 8 0

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 1..."
# --- MODULO 1 ---
Add-Heading1 "2. Demostracion y Analisis de cada Modulo"
Add-Heading2 "Modulo 1: Calculadora de Areas y Perimetros"

$mod1Info = [ordered]@{
    "Componentes Fuente"   = "AreaPerimetroActivity.kt / activity_area_perimetro.xml"
    "Proposito"            = "Calcular el area o perimetro de tres figuras geometricas fundamentales: Circulo, Rectangulo y Triangulo."
    "Funcionamiento Clave" = "La pantalla utiliza RadioGroups interactivos que adaptan dinamicamente los campos de entrada: para el Circulo solicita el Radio; para el Rectangulo, Base y Altura; y para el Triangulo conmuta entre Base/Altura (para Area) o los 3 Lados (para Perimetro)."
    "Formulas"             = "Circulo: Area = pi * r^2, Perimetro = 2 * pi * r. Rectangulo: Area = b * h, Perimetro = 2*(b+h). Triangulo: Area = (b*h)/2, Perimetro = a+b+c."
    "Validaciones"         = "Verifica que ningun campo quede vacio, conversion segura con toDoubleOrNull() y rechazo de valores menores o iguales a cero mediante mensajes Toast."
    "Resultado de Prueba"  = "Calculo de Area para un Circulo con Radio = 7. Resultado desplegado: 'Area del Circulo: 153.94'."
}
Add-ModuleTable $mod1Info
Add-ImageWithCaption "$capturasDir\01_areas_perimetros.png" "Figura 2: Modulo de Areas y Perimetros en ejecucion (Calculo de Area de Circulo)" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 2..."
# --- MODULO 2 ---
Add-Heading2 "Modulo 2: Conversor de Temperatura"

$mod2Info = [ordered]@{
    "Componentes Fuente"   = "TemperaturaActivity.kt / activity_temperatura.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Conversion bidireccional inmediata entre las escalas termicas Celsius, Fahrenheit y Kelvin."
    "Funcionamiento Clave" = "Dispone de un control Spinner vinculado al enum TipoConversionTemperatura con las 6 combinaciones posibles. LogicaMicroservicios.convertirTemperatura() procesa la formula correspondiente."
    "Formulas"             = "C a F: (C * 9/5) + 32 | F a C: (F - 32) * 5/9 | C a K: C + 273.15 | K a C: K - 273.15 | F a K: ((F-32)*5/9)+273.15 | K a F: ((K-273.15)*9/5)+32."
    "Validaciones"         = "Soporta valores con signo y decimales. Muestra alerta preventiva si el campo esta vacio o no es numerico."
    "Resultado de Prueba"  = "Valor ingresado: 100 en tipo Celsius a Fahrenheit. Resultado desplegado: '100.00 C = 212.00 F'."
}
Add-ModuleTable $mod2Info
Add-ImageWithCaption "$capturasDir\02_conversor_temperatura.png" "Figura 3: Modulo de Conversor de Temperatura (100 C convertidos a 212 F)" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 3..."
# --- MODULO 3 ---
Add-Heading2 "Modulo 3: Tabla de Multiplicar"

$mod3Info = [ordered]@{
    "Componentes Fuente"   = "TablaMultiplicarActivity.kt / activity_tabla_multiplicar.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Generar en pantalla la tabla de multiplicar completa (del 1 al 10) para cualquier numero ingresado."
    "Funcionamiento Clave" = "LogicaMicroservicios.generarTablaMultiplicar() recorre un bucle de 1 a 10 multiplicando el factor base. Cuenta con formateo automatico: si el resultado es entero omite los decimales, y si es fraccionario conserva 2 decimales."
    "Visualizacion"        = "Despliega los 10 renglones perfectamente alineados en una tarjeta con estilo destacado para facilitar la comprension visual."
    "Validaciones"         = "Asegura que el dato ingresado sea un numero valido antes de proceder con el calculo."
    "Resultado de Prueba"  = "Numero base: 8. Se genera la lista completa de multiplicaciones desde 8 x 1 = 8 hasta 8 x 10 = 80."
}
Add-ModuleTable $mod3Info
Add-ImageWithCaption "$capturasDir\03_tabla_multiplicar.png" "Figura 4: Modulo de Tabla de Multiplicar (Generacion de la tabla del 8)" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 4..."
# --- MODULO 4 ---
Add-Heading2 "Modulo 4: Calculadora de Indice de Masa Corporal (IMC)"

$mod4Info = [ordered]@{
    "Componentes Fuente"   = "ImcActivity.kt / activity_imc.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Determinar el estado nutricional y nivel ponderal de una persona a partir de su peso y estatura."
    "Funcionamiento Clave" = "Aplica la formula IMC = peso (kg) / [estatura (m)]^2. LogicaMicroservicios.calcularImc() evalúa el valor en los rangos oficiales de la OMS y genera un objeto ResultadoImc con el valor numerico, clasificacion y recomendacion clinica."
    "Escala de Clasificacion"= "Bajo peso (< 18.5) | Peso normal (18.5 - 24.99) | Sobrepeso (25.0 - 29.99) | Obesidad I, II y III (Morbida)."
    "Validaciones"         = "Comprobacion de valores de peso y altura estrictamente positivos (mayores a cero)."
    "Resultado de Prueba"  = "Peso = 72 kg, Altura = 1.75 m. Resultado: 'IMC: 23.51 - Categoria: Peso normal / Saludable - Excelente! Tu peso se encuentra en un rango saludable'."
}
Add-ModuleTable $mod4Info
Add-ImageWithCaption "$capturasDir\04_calculador_imc.png" "Figura 5: Modulo de Calculadora de IMC con diagnostico y mensaje saludable" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 5..."
# --- MODULO 5 ---
Add-Heading2 "Modulo 5: Generador de Numeros Primos"

$mod5Info = [ordered]@{
    "Componentes Fuente"   = "NumerosPrimosActivity.kt / activity_numeros_primos.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Evaluar la primalidad y obtener la serie ascendente de todos los numeros primos existentes hasta un limite superior."
    "Algoritmo"            = "Metodo optimizado O(raiz de N): descarta inmediatamente pares y recorre unicamente divisores impares hasta que divisor * divisor sea mayor a n."
    "Rendimiento y Control"= "Incluye tope maximo preventivo de 100,000 en UI para proteger la fluidez del hilo principal (UI thread) de Android."
    "Validaciones"         = "El limite debe ser un numero entero mayor o igual a 2."
    "Resultado de Prueba"  = "Limite indicado: 50. Se encontraron y listaron 15 numeros primos: 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47."
}
Add-ModuleTable $mod5Info
Add-ImageWithCaption "$capturasDir\05_numeros_primos.png" "Figura 6: Modulo de Numeros Primos en ejecucion (Lista de primos hasta 50)" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 6..."
# --- MODULO 6 ---
Add-Heading2 "Modulo 6: Juego 'Adivina el Numero'"

$mod6Info = [ordered]@{
    "Componentes Fuente"   = "AdivinaNumeroActivity.kt / activity_adivina_numero.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Proveer una dinamica interactiva donde el usuario adivina un numero secreto pseudoaleatorio entre 1 y 100."
    "Gestion del Estado"   = "Implementada en la clase LogicaMicroservicios.JuegoAdivinaNumero, preservando numeroSecreto, contador de intentos y bandera terminado."
    "Mecanica de Pistas"   = "En cada intento no acertado indica si el objetivo es 'MAYOR' o 'MENOR' que el ingresado e incrementa el contador de intentos en pantalla."
    "Boton de Reinicio"    = "Permite restablecer la partida en cualquier momento generando un nuevo numero secreto y limpiando el contador."
    "Resultado de Prueba"  = "Ingreso de 50. El sistema notifica: '[Pista] El numero secreto es MAYOR que 50' y actualiza el contador a 'Intentos realizados: 1'."
}
Add-ModuleTable $mod6Info
Add-ImageWithCaption "$capturasDir\06_adivina_numero.png" "Figura 7: Modulo de Adivina el Numero (Pista y contador de intentos en juego activo)" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 7..."
# --- MODULO 7 ---
Add-Heading2 "Modulo 7: Conversion de Monedas Multidivisa"

$mod7Info = [ordered]@{
    "Componentes Fuente"   = "ConversionMonedasActivity.kt / activity_conversion_monedas.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Efectuar conversiones monetarias entre 5 divisas mundiales (USD, EUR, GBP, MXN, JPY) y desplegar un desglose simultaneo de equivalencias."
    "Funcionamiento Clave" = "Utiliza el Dolar (USD) como divisa estandar pivote. La funcion LogicaMicroservicios.convertirMoneda() transforma el monto a USD y calcula en un solo paso el valor convertido y la matriz completa de equivalencias."
    "Visualizacion"        = "Muestra el resultado directo seleccionado en la cabecera y una lista con los 5 tipos de moneda con sus simbolos oficiales ($, euro, libra, yen)."
    "Validaciones"         = "Valida que el monto ingresado sea numerico y no negativo."
    "Resultado de Prueba"  = "150 USD a EUR -> Resultado directo: 138.00 EUR. Resumen completo: USD: $150.00 | EUR: €138.00 | GBP: £118.50 | MXN: $2625.00 | JPY: ¥23250.00."
}
Add-ModuleTable $mod7Info
Add-ImageWithCaption "$capturasDir\07_conversion_monedas.png" "Figura 8: Modulo de Conversion de Monedas con equivalencias completas" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Modulo 8..."
# --- MODULO 8 ---
Add-Heading2 "Modulo 8: Calculadora de Interes Compuesto"

$mod8Info = [ordered]@{
    "Componentes Fuente"   = "InteresCompuestoActivity.kt / activity_interes_compuesto.xml / LogicaMicroservicios.kt"
    "Proposito"            = "Calcular el crecimiento de un capital financiero a traves del tiempo segun la periodicidad de capitalizacion de intereses."
    "Formula Financiera"   = "Monto Final = Capital * (1 + r/n)^(n*t), donde r es la tasa anual, n la frecuencia por ano y t los anos de inversion."
    "Frecuencias"          = "Soporta capitalizacion Anual (n=1), Semestral (n=2), Trimestral (n=4), Mensual (n=12) y Diaria (n=365)."
    "Desglose Periodico"   = "Genera la evolucion del saldo e intereses ganados periodo por periodo para un analisis financiero riguroso."
    "Resultado de Prueba"  = "Capital = $1,000 USD, Tasa = 5%, Tiempo = 3 anos, Capitalizacion = Anual. Monto Final: $1157.63 USD, Interes Total: $157.63 USD con desglose periodo a periodo (P1: $1050, P2: $1102.50, P3: $1157.63)."
}
Add-ModuleTable $mod8Info
Add-ImageWithCaption "$capturasDir\08_interes_compuesto.png" "Figura 9: Modulo de Interes Compuesto con desglose de crecimiento periodico" 175

$doc.Paragraphs.Add().Range.InsertBreak(7)

Write-Host "Generando Seccion de Calidad y Conclusiones..."
# --- SECCION 3: CALIDAD Y CONCLUSIONES ---
Add-Heading1 "3. Calidad de Software y Verificacion Unitaria"
Add-Paragraph "El proyecto incorpora una bateria de pruebas unitarias automatizadas desarrolladas sobre el framework JUnit 4 (LogicaMicroserviciosTest.kt). Estas pruebas garantizan que cada formula, calculo de limites y flujo logico funcione exactamente como se espera antes de ser consumido por las Activities de Android." "Calibri" 11 $false $colorDark 3 8 0

$testTbl = $doc.Tables.Add($doc.Paragraphs.Add().Range, 8, 3)
$testTbl.Borders.InsideLineStyle = 1
$testTbl.Borders.OutsideLineStyle = 1
$testTbl.Borders.InsideColor = $colorBorder
$testTbl.Borders.OutsideColor = $colorBorder
$testTbl.Columns.Item(1).Width = 130
$testTbl.Columns.Item(2).Width = 240
$testTbl.Columns.Item(3).Width = 130

$testRows = @(
    @("Metodo de Prueba", "Casos y Condiciones Verificadas", "Estado"),
    @("testConversorTemperatura", "Punto congelacion 0C -> 32F, ebullicion 100C -> 212F, 0C -> 273.15K", "Superado (Passed)"),
    @("testTablaMultiplicar", "Tamano de coleccion (10 elementos), enteros y numeros decimales (2.5)", "Superado (Passed)"),
    @("testCalculadoraImc", "Casos de prueba: Normal (70kg/1.75m), Bajo peso, Sobrepeso y Obesidad II", "Superado (Passed)"),
    @("testNumerosPrimos", "Prueba de primalidad (0, 1, 2, 97) y serie generada hasta 20", "Superado (Passed)"),
    @("testAdivinaElNumero", "Flujo de juego con forzado de numero, verificacion de pistas mayor/menor", "Superado (Passed)"),
    @("testInteresCompuesto", "Capital $1000 al 10% anual a 2 anos -> Monto $1210 e intereses desglosados", "Superado (Passed)"),
    @("testConversionMonedas", "Conversion 100 USD a EUR ($92.00) y 175 MXN a USD ($10.00)", "Superado (Passed)")
)

for ($i = 0; $i -lt $testRows.Count; $i++) {
    for ($j = 0; $j -lt 3; $j++) {
        $c = $testTbl.Cell($i+1, $j+1)
        $c.Range.Text = $testRows[$i][$j]
        $c.Range.Font.Name = "Calibri"
        $c.Range.Font.Size = 9.5
        $c.TopPadding = 5; $c.BottomPadding = 5; $c.LeftPadding = 6; $c.RightPadding = 6
        if ($i -eq 0) {
            $c.Range.Font.Bold = 1
            $c.Range.Font.Color = $colorNavy
            $c.Range.Shading.BackgroundPatternColor = $colorBgHead
        } elseif ($j -eq 2) {
            $c.Range.Font.Bold = 1
            $c.Range.Font.Color = (Get-BGR 22 101 52) # Verde oscuro
        }
    }
}
$doc.Paragraphs.Add().Range.InsertParagraphAfter()

Add-Heading1 "4. Conclusiones y Aprendizajes"
$conclusiones = @"
1. Arquitectura Limpia y Desacoplamiento: Separar la logica de negocio en un objeto dedicado (LogicaMicroservicios.kt) asegura una arquitectura escalable, mantenible y facilmente verificable sin depender del entorno de Android.
2. Experiencia de Usuario Adaptable: El diseno interactivo con componentes Material 3 (TextInputEditText, RadioGroups dinamicos, Spinners y Toasts informativos) ofrece al usuario una experiencia fluida, intuitiva y a prueba de errores de entrada.
3. Preparacion para Microservicios Remotos: El diseno modular permite que, en una fase posterior, las funciones de calculo locales puedan ser reemplazadas por llamadas a APIs REST externas o servicios en la nube sin necesidad de modificar sustancialmente la interfaz grafica de la aplicacion.
"@
Add-Paragraph $conclusiones "Calibri" 11 $false $colorDark 3 8 0

Write-Host "Guardando documento en $outputDocx..."
$doc.SaveAs([ref]$outputDocx, [ref]16) # 16 = wdFormatXMLDocument (.docx)
$doc.Close([ref]0)
$word.Quit()
[System.Runtime.Interopservices.Marshal]::ReleaseComObject($word) | Out-Null

Write-Host "INFORME DOCX GENERADO EXITOSAMENTE: $outputDocx"
