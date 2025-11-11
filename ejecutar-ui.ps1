# Script para ejecutar la interfaz gráfica de eScrims
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  eScrims - Interfaz Grafica (GUI)"  -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que el proyecto esté compilado
if (-not (Test-Path "out\com\escrims\ui\ScrimApplication.class")) {
    Write-Host "El proyecto no esta compilado. Compilando..." -ForegroundColor Yellow
    .\compilar.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR - No se pudo compilar el proyecto" -ForegroundColor Red
        exit 1
    }
}

Write-Host "Ejecutando aplicacion grafica..." -ForegroundColor Green
Write-Host ""

# Ejecutar la aplicación GUI
java -cp out com.escrims.ui.ScrimApplication

Write-Host ""
Write-Host "Aplicacion cerrada." -ForegroundColor Yellow
