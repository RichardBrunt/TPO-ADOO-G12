# Corregir encoding UTF-8 BOM en archivos Java
$ErrorActionPreference = "Stop"
$srcPath = "src\main\java\com\escrims"

Write-Host "=== Corrigiendo encoding de archivos Java ===" -ForegroundColor Cyan
Write-Host ""

$javaFiles = Get-ChildItem -Path $srcPath -Filter "*.java" -Recurse
$total = $javaFiles.Count
$fixed = 0

Write-Host "Archivos encontrados: $total" -ForegroundColor Green
Write-Host ""

foreach ($file in $javaFiles) {
    # Leer contenido como bytes
    $content = Get-Content $file.FullName -Raw
    
    # Guardar sin BOM usando UTF8NoBOM
    $utf8NoBom = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
    
    $fixed++
}

Write-Host ""
Write-Host "=== Encoding Corregido ===" -ForegroundColor Green
Write-Host "  - Archivos procesados: $fixed" -ForegroundColor White
Write-Host ""
