@echo off
rem Spusti TeamsKeepAlive v okne konzole. Ukonceni: Ctrl+C nebo zavreni okna.
rem Soubor Kel.jar musi byt ve stejne slozce jako tento run.bat.
cd /d "%~dp0"
java -jar Kel.jar
echo.
echo Program skoncil. Stisknete klavesu pro zavreni okna...
pause >nul
