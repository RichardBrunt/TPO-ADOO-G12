# Script de Migración MVC - Actualización Automática de Imports y Packages
# Fecha: 10 de Noviembre 2025
# Propósito: Migrar de estructura DDD a MVC pura

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Migración a Estructura MVC Completa" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

$projectRoot = "c:\Users\jpgar\Desktop\uade\segundo año\Segundo cuatri\Proceso y desarrollo de software\TP-PROCESO-G12"
$srcPath = "$projectRoot\src\main\java\com\escrims"

# Contador de archivos procesados
$filesProcessed = 0
$importsUpdated = 0
$packagesUpdated = 0

# Función para actualizar imports en un archivo
function Update-JavaImports {
    param (
        [string]$filePath
    )
    
    $content = Get-Content $filePath -Raw -Encoding UTF8
    $originalContent = $content
    $changed = $false
    
    # Mapeo de imports antiguos -> nuevos
    $importMappings = @{
        # Domain a Model/Domain
        'import com.escrims.domain.model.' = 'import com.escrims.model.domain.model.'
        'import com.escrims.domain.state.' = 'import com.escrims.model.domain.state.'
        'import com.escrims.domain.strategy.' = 'import com.escrims.model.domain.strategy.'
        'import com.escrims.domain.events.' = 'import com.escrims.model.domain.events.'
        'import com.escrims.domain.builder.' = 'import com.escrims.model.domain.builder.'
        'import com.escrims.domain.facade.' = 'import com.escrims.model.domain.facade.'
        'import com.escrims.domain.factory.' = 'import com.escrims.model.domain.factory.'
        'import com.escrims.domain.adapter.' = 'import com.escrims.model.domain.adapter.'
        'import com.escrims.domain.decorator.' = 'import com.escrims.model.domain.decorator.'
        'import com.escrims.domain.command.' = 'import com.escrims.model.domain.command.'
        'import com.escrims.domain.template.' = 'import com.escrims.model.domain.template.'
        'import com.escrims.domain.chain.' = 'import com.escrims.model.domain.chain.'
        
        # Repository a Model/Repository
        'import com.escrims.repository.' = 'import com.escrims.model.repository.'
        
        # Service a Model/Service
        'import com.escrims.service.' = 'import com.escrims.model.service.'
        
        # UI/Model a Model/Application
        'import com.escrims.ui.model.' = 'import com.escrims.model.application.'
        
        # UI/Controller a Controller
        'import com.escrims.ui.controller.' = 'import com.escrims.controller.'
        
        # UI/View a View/GUI
        'import com.escrims.ui.view.' = 'import com.escrims.view.gui.'
        
        # App a View/Console
        'import com.escrims.app.' = 'import com.escrims.view.console.'
        
        # UI raíz a View/GUI (para ScrimApplication)
        'import com.escrims.ui.ScrimApplication' = 'import com.escrims.view.gui.ScrimApplication'
    }
    
    # Aplicar cada mapeo
    foreach ($oldImport in $importMappings.Keys) {
        $newImport = $importMappings[$oldImport]
        if ($content -match [regex]::Escape($oldImport)) {
            $content = $content -replace [regex]::Escape($oldImport), $newImport
            $changed = $true
        }
    }
    
    if ($changed) {
        Set-Content -Path $filePath -Value $content -Encoding UTF8 -NoNewline
        return $true
    }
    
    return $false
}

# Función para actualizar package declaration
function Update-PackageDeclaration {
    param (
        [string]$filePath
    )
    
    $content = Get-Content $filePath -Raw -Encoding UTF8
    $originalContent = $content
    $changed = $false
    
    # Determinar el nuevo package basado en la ruta del archivo
    $relativePath = $filePath.Replace("$srcPath\", "").Replace("\", ".")
    $relativePath = $relativePath -replace "\.java$", ""
    $packagePath = $relativePath -replace "\.[^.]+$", ""  # Remover el nombre del archivo
    
    # Mapeo de packages antiguos -> nuevos
    $packageMappings = @{
        'package com.escrims.domain.model;' = 'package com.escrims.model.domain.model;'
        'package com.escrims.domain.state;' = 'package com.escrims.model.domain.state;'
        'package com.escrims.domain.strategy;' = 'package com.escrims.model.domain.strategy;'
        'package com.escrims.domain.events;' = 'package com.escrims.model.domain.events;'
        'package com.escrims.domain.builder;' = 'package com.escrims.model.domain.builder;'
        'package com.escrims.domain.facade;' = 'package com.escrims.model.domain.facade;'
        'package com.escrims.domain.factory;' = 'package com.escrims.model.domain.factory;'
        'package com.escrims.domain.adapter;' = 'package com.escrims.model.domain.adapter;'
        'package com.escrims.domain.decorator;' = 'package com.escrims.model.domain.decorator;'
        'package com.escrims.domain.command;' = 'package com.escrims.model.domain.command;'
        'package com.escrims.domain.template;' = 'package com.escrims.model.domain.template;'
        'package com.escrims.domain.chain;' = 'package com.escrims.model.domain.chain;'
        'package com.escrims.repository;' = 'package com.escrims.model.repository;'
        'package com.escrims.service;' = 'package com.escrims.model.service;'
        'package com.escrims.ui.model;' = 'package com.escrims.model.application;'
        'package com.escrims.ui.controller;' = 'package com.escrims.controller;'
        'package com.escrims.ui.view;' = 'package com.escrims.view.gui;'
        'package com.escrims.app;' = 'package com.escrims.view.console;'
        'package com.escrims.ui;' = 'package com.escrims.view.gui;'
    }
    
    # Aplicar cada mapeo
    foreach ($oldPackage in $packageMappings.Keys) {
        $newPackage = $packageMappings[$oldPackage]
        if ($content -match [regex]::Escape($oldPackage)) {
            $content = $content -replace [regex]::Escape($oldPackage), $newPackage
            $changed = $true
        }
    }
    
    if ($changed) {
        Set-Content -Path $filePath -Value $content -Encoding UTF8 -NoNewline
        return $true
    }
    
    return $false
}

Write-Host "[1/4] Buscando archivos Java..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path $srcPath -Filter "*.java" -Recurse -File
Write-Host "      Encontrados: $($javaFiles.Count) archivos" -ForegroundColor Green
Write-Host ""

Write-Host "[2/4] Actualizando package declarations..." -ForegroundColor Yellow
foreach ($file in $javaFiles) {
    if (Update-PackageDeclaration -filePath $file.FullName) {
        $packagesUpdated++
        Write-Host "      ✓ $($file.Name)" -ForegroundColor Green
    }
}
Write-Host "      Packages actualizados: $packagesUpdated" -ForegroundColor Cyan
Write-Host ""

Write-Host "[3/4] Actualizando imports..." -ForegroundColor Yellow
foreach ($file in $javaFiles) {
    if (Update-JavaImports -filePath $file.FullName) {
        $importsUpdated++
        Write-Host "      ✓ $($file.Name)" -ForegroundColor Green
    }
    $filesProcessed++
}
Write-Host "      Archivos con imports actualizados: $importsUpdated" -ForegroundColor Cyan
Write-Host ""

Write-Host "[4/4] Limpiando carpetas antiguas..." -ForegroundColor Yellow

# Eliminar carpetas antiguas vacías (ui y app)
$oldFolders = @(
    "$srcPath\ui\controller",
    "$srcPath\ui\view",
    "$srcPath\ui\model",
    "$srcPath\ui",
    "$srcPath\app"
)

foreach ($folder in $oldFolders) {
    if (Test-Path $folder) {
        $items = Get-ChildItem -Path $folder -Recurse
        if ($items.Count -eq 0) {
            Remove-Item -Path $folder -Recurse -Force
            Write-Host "      ✓ Eliminada carpeta vacía: $folder" -ForegroundColor Green
        } else {
            Write-Host "      ⚠ Carpeta no vacía (revisar manualmente): $folder" -ForegroundColor Yellow
        }
    }
}
Write-Host ""

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Migración Completada" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Resumen:" -ForegroundColor White
Write-Host "  - Archivos procesados: $filesProcessed" -ForegroundColor White
Write-Host "  - Packages actualizados: $packagesUpdated" -ForegroundColor White
Write-Host "  - Archivos con imports actualizados: $importsUpdated" -ForegroundColor White
Write-Host ""
Write-Host "Proximos pasos:" -ForegroundColor Yellow
Write-Host "  1. Revisar la estructura en: $srcPath" -ForegroundColor White
Write-Host "  2. Recompilar el proyecto: .\compilar.ps1" -ForegroundColor White
Write-Host "  3. Ejecutar tests para verificar" -ForegroundColor White
Write-Host ""
