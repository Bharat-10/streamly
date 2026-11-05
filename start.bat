@echo off
cd /d "%~dp0"
echo Starting MySQL and Elasticsearch...
docker compose up -d
if errorlevel 1 (
  echo.
  echo Could not start the databases. Is Docker Desktop open and showing "Engine running"?
  echo If the error mentions port 3306, stop the URL Shortener first ^(its stop.bat^).
  pause
  exit /b 1
)
echo Waiting for Elasticsearch to be ready ^(can take up to a minute^)...
:waites
curl -s -o nul http://localhost:9200
if errorlevel 1 (
  timeout /t 3 /nobreak >nul
  goto waites
)
echo Elasticsearch is ready.
set "KEYS="
if exist "%~dp0aws-keys.bat" set "KEYS=call "%~dp0aws-keys.bat" && "
start "Streamly - Backend" cmd /k "cd /d "%~dp0backend" && set "DB_PASSWORD=root" && %KEYS%mvnw.cmd spring-boot:run"
start "Streamly - Frontend" cmd /k "cd /d "%~dp0frontend" && npm run dev"
echo Opening the app in your browser in 30 seconds...
timeout /t 30 /nobreak >nul
start http://localhost:5173
