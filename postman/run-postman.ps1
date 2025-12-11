# Run the Postman collection using newman (PowerShell)
# Usage: Open PowerShell in this folder and run: .\run-postman.ps1

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$collection = Join-Path $scriptDir 'hyderabad-backend.postman_collection.json'
$env = Join-Path $scriptDir 'hyderabad-backend.postman_environment.json'
$reportsDir = Join-Path $scriptDir 'reports'

if (-not (Test-Path $reportsDir)) {
    New-Item -ItemType Directory -Path $reportsDir | Out-Null
}

# Build reporter output paths
$jsonReport = Join-Path $reportsDir 'newman-report.json'
$htmlReport = Join-Path $reportsDir 'newman-report.html'

# Prefer global newman if available, otherwise use npx
## Prefer local newman (node_modules/.bin/newman) -> then global newman -> then npx
$localNewman = Join-Path $scriptDir 'node_modules\.bin\newman'
if (Test-Path $localNewman) {
    Write-Host "Running collection with local newman (node_modules) ..."
    & $localNewman run $collection -e $env --bail --reporters cli,json,html --reporter-json-export $jsonReport --reporter-html-export $htmlReport
} elseif (Get-Command newman -ErrorAction SilentlyContinue) {
    Write-Host "Running collection with global newman..."
    newman run $collection -e $env --bail --reporters cli,json,html --reporter-json-export $jsonReport --reporter-html-export $htmlReport
} else {
    Write-Host "`nnewman not installed globally. Trying npx (requires Node.js)...`n"
    # Ensure the HTML reporter is available for npx/newman
    Write-Host "Ensuring newman html reporter is installed locally (may take a moment)..."
    npm install --no-audit --no-fund newman-reporter-html | Out-Null
    npx newman run $collection -e $env --bail --reporters cli,json,html --reporter-json-export $jsonReport --reporter-html-export $htmlReport
}
