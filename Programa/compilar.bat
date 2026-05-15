@echo off
REM Compila y ejecuta MiniJuegos desde la carpeta Programa/
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -d out -sourcepath src @sources.txt
echo Compilacion exitosa. Iniciando aplicacion...
java -cp out Vista.Aplicacion
