# Script de migracion MVC - Version Simplificada
# Actualiza imports y packages en archivos Java

$ErrorActionPreference = "Stop"
$srcPath = "src\main\java\com\escrims"

Write-Host "=== Migracion MVC - Actualizando archivos Java ===" -ForegroundColor Cyan
Write-Host ""

# Definir reemplazos de imports
$importReplacements = @{
    'import com.escrims.domain.' = 'import com.escrims.model.domain.'
    'import com.escrims.repository.' = 'import com.escrims.model.repository.'
    'import com.escrims.service.' = 'import com.escrims.model.service.'
    'import com.escrims.ui.model.' = 'import com.escrims.model.application.'
    'import com.escrims.ui.controller.' = 'import com.escrims.controller.'
    'import com.escrims.ui.view.' = 'import com.escrims.view.gui.'
    'import com.escrims.app.' = 'import com.escrims.view.console.'
}

# Definir reemplazos de packages
$packageReplacements = @{
    'package com.escrims.domain.model;' = 'package com.escrims.model.domain.model;'
    'package com.escrims.domain.service;' = 'package com.escrims.model.domain.service;'
    'package com.escrims.domain.patterns.' = 'package com.escrims.model.domain.patterns.'
    'package com.escrims.repository;' = 'package com.escrims.model.repository;'
    'package com.escrims.service;' = 'package com.escrims.model.service;'
    'package com.escrims.ui.model;' = 'package com.escrims.model.application;'
    'package com.escrims.ui.controller;' = 'package com.escrims.controller;'
    'package com.escrims.ui.view;' = 'package com.escrims.view.gui;'
    'package com.escrims.app;' = 'package com.escrims.view.console;'
}

# Obtener todos los archivos Java
$javaFiles = Get-ChildItem -Path $srcPath -Filter "*.java" -Recurse
$total = $javaFiles.Count
$processed = 0

Write-Host "Archivos encontrados: $total" -ForegroundColor Green
Write-Host ""

foreach ($file in $javaFiles) {
    $processed++
    Write-Progress -Activity "Procesando archivos" -Status "Archivo $processed de $total" -PercentComplete (($processed / $total) * 100)
    
    # Leer contenido
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $modified = $false
    
    # Actualizar imports
    foreach ($old in $importReplacements.Keys) {
        if ($content -match [regex]::Escape($old)) {
            $content = $content -replace [regex]::Escape($old), $importReplacements[$old]
            $modified = $true
        }
    }
    
    # Actualizar package
    foreach ($old in $packageReplacements.Keys) {
        if ($content -match [regex]::Escape($old)) {
            $content = $content -replace [regex]::Escape($old), $packageReplacements[$old]
            $modified = $true
        }
    }
    
    # Guardar si hubo cambios
    if ($modified) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        Write-Host "  Actualizado: $($file.Name)" -ForegroundColor Yellow
    }
}

Write-Progress -Activity "Procesando archivos" -Completed

Write-Host ""
Write-Host "=== Migracion Completada ===" -ForegroundColor Green
Write-Host "  - Archivos procesados: $processed" -ForegroundColor White
Write-Host ""
Write-Host "Proximos pasos:" -ForegroundColor Cyan
Write-Host "  1. Eliminar carpetas antiguas: ui, app" -ForegroundColor White
Write-Host "  2. Recompilar: .\compilar.ps1" -ForegroundColor White
Write-Host "  3. Probar aplicacion" -ForegroundColor White
Write-Host ""
