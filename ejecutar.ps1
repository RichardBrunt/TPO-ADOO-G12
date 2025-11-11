# Script para ejecutar demos del TP eScrims

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet('completo', 'basico')]
    [string]$Demo = 'completo'
)

Write-Host "Ejecutando demo $Demo..." -ForegroundColor Cyan
Write-Host ""

if ($Demo -eq 'completo') {
    java -cp out com.escrims.app.CompleteDemoMain
} else {
    java -cp out com.escrims.app.DemoMain
}

Write-Host ""
Write-Host "Demo finalizado." -ForegroundColor Green
