# Script de verificación del proyecto eScrims
# Verifica compilación, ejecución y patrones implementados

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  VERIFICACION DEL PROYECTO eScrims - 13 Patrones" -ForegroundColor Cyan
Write-Host "============================================================`n" -ForegroundColor Cyan

# 1. Verificar archivos Java
Write-Host "[1/5] Contando archivos Java..." -ForegroundColor Yellow
$javaFiles = (Get-ChildItem -Path "src" -Filter "*.java" -Recurse).Count
Write-Host "   OK - Total de archivos Java: $javaFiles" -ForegroundColor Green

# 2. Compilar
Write-Host "`n[2/5] Compilando proyecto..." -ForegroundColor Yellow
& .\compilar.ps1
if ($LASTEXITCODE -eq 0) {
    Write-Host "   OK - Compilacion exitosa" -ForegroundColor Green
} else {
    Write-Host "   ERROR - Fallo en compilacion" -ForegroundColor Red
    exit 1
}

# 3. Ejecutar demo y capturar salida
Write-Host "`n[3/5] Ejecutando demo completo..." -ForegroundColor Yellow
$output = java -cp out com.escrims.app.CompleteDemoMain 2>&1 | Out-String

# 4. Verificar que aparecen los 10 DEMOS
Write-Host "`n[4/5] Verificando DEMOS..." -ForegroundColor Yellow
$demos = @(
    "DEMO 1: Patrón BUILDER",
    "DEMO 2: Patrón STRATEGY",
    "DEMO 3: Integración",
    "DEMO 4: Patron FACTORY METHOD",
    "DEMO 5: Patron ADAPTER",
    "DEMO 6: Patron DECORATOR",
    "DEMO 7: Patron COMMAND",
    "DEMO 8: Patron TEMPLATE METHOD",
    "DEMO 9: Patron CHAIN OF RESPONSIBILITY",
    "DEMO 10: Patron SINGLETON"
)

$demosEncontrados = 0
foreach ($demo in $demos) {
    if ($output -match [regex]::Escape($demo)) {
        $demosEncontrados++
        Write-Host "   ✓ $demo" -ForegroundColor Green
    } else {
        Write-Host "   X $demo - NO ENCONTRADO" -ForegroundColor Red
    }
}

# 5. Verificar resumen final
Write-Host "`n[5/5] Verificando resumen de patrones..." -ForegroundColor Yellow
if ($output -match "Total de patrones implementados: 13") {
    Write-Host "   OK - Resumen confirma 13 patrones implementados" -ForegroundColor Green
} else {
    Write-Host "   ERROR - No se encontro el resumen de 13 patrones" -ForegroundColor Red
}

# Verificar patrones específicos en el resumen
$patronesResumen = @(
    "STATE",
    "STRATEGY",
    "OBSERVER",
    "REPOSITORY",
    "BUILDER",
    "FACADE",
    "FACTORY METHOD",
    "ADAPTER",
    "DECORATOR",
    "COMMAND",
    "TEMPLATE METHOD",
    "CHAIN OF RESPONSIBILITY",
    "SINGLETON"
)

Write-Host "`n   Patrones listados en resumen:" -ForegroundColor Cyan
$patronesEncontrados = 0
foreach ($patron in $patronesResumen) {
    if ($output -match [regex]::Escape($patron)) {
        $patronesEncontrados++
        Write-Host "   ✓ $patron" -ForegroundColor Green
    }
}

# Resultado final
Write-Host "`n============================================================" -ForegroundColor Cyan
Write-Host "  RESULTADO DE LA VERIFICACION" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "Archivos Java: $javaFiles/68" -ForegroundColor White
Write-Host "Compilacion: OK" -ForegroundColor Green
Write-Host "DEMOS encontrados: $demosEncontrados/10" -ForegroundColor $(if ($demosEncontrados -eq 10) { "Green" } else { "Red" })
Write-Host "Patrones en resumen: $patronesEncontrados/13" -ForegroundColor $(if ($patronesEncontrados -eq 13) { "Green" } else { "Red" })

if ($demosEncontrados -eq 10 -and $patronesEncontrados -eq 13) {
    Write-Host "`n✅ PROYECTO VERIFICADO CORRECTAMENTE" -ForegroundColor Green
    Write-Host "   Todos los patrones funcionan como esperado`n" -ForegroundColor Green
    exit 0
} else {
    Write-Host "`n⚠ VERIFICACION INCOMPLETA" -ForegroundColor Yellow
    Write-Host "   Revisar los items marcados con X`n" -ForegroundColor Yellow
    exit 1
}
