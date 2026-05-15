#!/bin/bash
# Compila y ejecuta MiniJuegos desde la carpeta Programa/
set -e

mkdir -p out
find src -name "*.java" > sources.txt
javac -d out -sourcepath src @sources.txt
echo "Compilación exitosa. Iniciando aplicación..."
java -cp out Vista.Aplicacion
