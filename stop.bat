@echo off
cd /d "%~dp0"
taskkill /fi "WINDOWTITLE eq Streamly - Backend*" /t /f >nul 2>&1
taskkill /fi "WINDOWTITLE eq Streamly - Frontend*" /t /f >nul 2>&1
docker compose down
echo Everything stopped.
pause
