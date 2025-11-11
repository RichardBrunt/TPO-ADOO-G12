# Script de compilacion para TP eScrims
# Compila todo el proyecto Java con encoding UTF-8

Write-Host "Compilando proyecto eScrims..." -ForegroundColor Cyan

# Limpiar directorio de salida
if (Test-Path "out") {
    Remove-Item -Recurse -Force out
}
New-Item -ItemType Directory -Force -Path out | Out-Null

# Obtener todos los archivos .java
$javaFiles = Get-ChildItem -Path src\main\java -Recurse -Filter *.java | ForEach-Object { $_.FullName }

# Compilar con UTF-8
javac -encoding UTF-8 -d out $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "OK - Compilacion exitosa" -ForegroundColor Green
    Write-Host ""
    Write-Host "Para ejecutar el demo completo:" -ForegroundColor Yellow
    Write-Host "  java -cp out com.escrims.app.CompleteDemoMain" -ForegroundColor White
    Write-Host ""
    Write-Host "Para ejecutar el demo basico:" -ForegroundColor Yellow
    Write-Host "  java -cp out com.escrims.app.DemoMain" -ForegroundColor White
} else {
    Write-Host "ERROR - Error en la compilacion" -ForegroundColor Red
    exit 1
}
