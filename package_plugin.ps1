# PowerShell Script: Package Oxygen Plugin as installable ZIP add-on
# Usage: Run from project root directory
# 1. First run 'mvn package' to compile and generate target/your-plugin-name.jar
# 2. Run this script to generate oxygen-plugin.zip in dist/ directory

$ErrorActionPreference = 'Stop'

# Configuration
$jarName = "oxygen-lock-plugin-1.0.0.jar"   # Replace with actual jar name
$distDir = "dist"
$targetJar = "target/$jarName"
$pluginXml = "src/main/resources/plugin.xml"
$zipName = "oxygen-plugin.zip"

# Check if jar exists
if (!(Test-Path $targetJar)) {
    Write-Host "Jar not found: $targetJar. Please run 'mvn package' first."
    exit 1
}
if (!(Test-Path $pluginXml)) {
    Write-Host "plugin.xml not found: $pluginXml. Please check the path."
    exit 1
}

# 创建 dist 目录
if (!(Test-Path $distDir)) {
    New-Item -ItemType Directory -Path $distDir | Out-Null
}

# 拷贝 jar 和 plugin.xml 到 dist 临时目录
Copy-Item $targetJar "$distDir/$jarName" -Force
Copy-Item $pluginXml "$distDir/plugin.xml" -Force

# 进入 dist 目录并打包 zip
Push-Location $distDir
if (Test-Path $zipName) { Remove-Item $zipName -Force }
Compress-Archive -Path $jarName, "plugin.xml" -DestinationPath $zipName
Pop-Location

Write-Host "Plugin add-on zip generated: $distDir/$zipName"
Write-Host "You can install this zip in Oxygen via 'Install new add-ons...' menu."
