# Script para corregir todos los packages en model/domain
$ErrorActionPreference = "Stop"

Write-Host "=== Corrigiendo packages en model/domain ===" -ForegroundColor Cyan

# Definir mapeos de packages para archivos en model/domain
$packageMap = @{
    'package com.escrims.domain.builder;' = 'package com.escrims.model.domain.builder;'
    'package com.escrims.domain.chain;' = 'package com.escrims.model.domain.chain;'
    'package com.escrims.domain.factory;' = 'package com.escrims.model.domain.factory;'
    'package com.escrims.domain.state;' = 'package com.escrims.model.domain.state;'
    'package com.escrims.domain.strategy;' = 'package com.escrims.model.domain.strategy;'
    'package com.escrims.domain.adapter;' = 'package com.escrims.model.domain.adapter;'
    'package com.escrims.domain.command;' = 'package com.escrims.model.domain.command;'
    'package com.escrims.domain.decorator;' = 'package com.escrims.model.domain.decorator;'
    'package com.escrims.domain.events;' = 'package com.escrims.model.domain.events;'
    'package com.escrims.domain.notification;' = 'package com.escrims.model.domain.notification;'
    'package com.escrims.domain.template;' = 'package com.escrims.model.domain.template;'
}

# Procesar archivos en model/domain
$files = Get-ChildItem -Path "src\main\java\com\escrims\model\domain" -Filter "*.java" -Recurse
$count = 0

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    $modified = $false
    
    foreach ($old in $packageMap.Keys) {
        if ($content -match [regex]::Escape($old)) {
            $content = $content -replace [regex]::Escape($old), $packageMap[$old]
            $modified = $true
        }
    }
    
    if ($modified) {
        $utf8 = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8)
        $count++
        Write-Host "  Actualizado: $($file.Name)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "Archivos actualizados: $count" -ForegroundColor Green
Write-Host ""
