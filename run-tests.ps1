# Script para ejecutar pruebas unitarias y mostrar resultados
Write-Host "Ejecutando pruebas unitarias..." -ForegroundColor Cyan

# Ejecutar pruebas
& .\gradlew.bat :app:testDebugUnitTest --console=plain 2>&1 | Tee-Object -FilePath "test-output.txt"

# Verificar resultado
if ($LASTEXITCODE -eq 0) {
    Write-Host "`nTODAS LAS PRUEBAS PASARON!" -ForegroundColor Green
} else {
    Write-Host "`nALGUNAS PRUEBAS FALLARON" -ForegroundColor Red
}

# Mostrar resumen
Write-Host "`nAbriendo reporte HTML..." -ForegroundColor Yellow
$reportPath = ".\app\build\reports\tests\testDebugUnitTest\index.html"
if (Test-Path $reportPath) {
    Start-Process $reportPath
} else {
    Write-Host "No se encontró el reporte en: $reportPath" -ForegroundColor Red
}

