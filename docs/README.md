# MiniJuegos POO 🎮
**Proyecto Final - Programación Orientada a Objetos**
> Aplicación de minijuegos en Java con arquitectura en capas, interfaz gráfica Swing y persistencia en ficheros

**Documentación: https://jp-aceves.github.io/MiniJuegos-Proyecto-POO/**

**Web: https://jp-aceves.github.io/MiniJuegos-Proyecto-POO/Presentacion.html**

---

## 📋 Descripción General

MiniJuegos POO es el proyecto final de la asignatura de Programación Orientada a Objetos (1º de Ingeniería Informática). Se trata de una aplicación de escritorio en Java que integra varios minijuegos clásicos bajo una arquitectura en cuatro capas: **Vista (Swing)**, **Controladores**, **Modelo** y **Persistencia**. El sistema gestiona usuarios, partidas, estadísticas y permite pausar y reanudar partidas en curso.

---

## 🎯 Objetivos

- ✅ Implementar una arquitectura en capas limpia y desacoplada
- ✅ Aplicar polimorfismo, herencia y abstracción en el diseño del modelo
- ✅ Desarrollar una interfaz gráfica funcional con Java Swing
- ✅ Persistir usuarios, estadísticas y partidas en ficheros de texto
- ✅ Gestionar sesiones con roles diferenciados (jugador / administrador)
- ✅ Permitir pausar y reanudar partidas mediante serialización de estado

---

## 🚀 Cómo compilar y ejecutar

> Requisito: **Java 17 o superior** instalado y `javac`/`java` en el PATH.

### Mac / Linux

```bash
cd Programa
chmod +x compilar.sh
./compilar.sh
```

### Windows (CMD)

```cmd
cd Programa
compilar.bat
```

### Manual (cualquier sistema)

```bash
cd Programa
find src -name "*.java" > sources.txt
javac -d out -sourcepath src @sources.txt
java -cp out Vista.Aplicacion
```

> **Importante:** ejecuta siempre desde la carpeta `Programa/` — los ficheros de roscos se buscan con ruta relativa desde ahí.

---

## 📁 Estructura del Proyecto

```
MiniJuegos-Proyecto-POO/
├── Programa/
│   ├── compilar.sh               # Script de compilación (Mac/Linux)
│   ├── compilar.bat              # Script de compilación (Windows)
│   ├── src/
│   │   ├── Controlador/          # Gestores: usuarios, partidas, estadísticas, juegos
│   │   │   ├── GestorUsuarios.java
│   │   │   ├── GestorPartidas.java
│   │   │   ├── GestorEstadisticas.java
│   │   │   ├── GestorJuegos.java
│   │   │   └── Sistema.java
│   │   ├── Modelo/               # Entidades del dominio
│   │   │   ├── Juego.java        # Clase abstracta base
│   │   │   ├── PasaPalabra.java
│   │   │   ├── TresEnRaya.java
│   │   │   ├── Usuario.java
│   │   │   ├── Administrador.java
│   │   │   ├── Partida.java
│   │   │   ├── Estadistica.java
│   │   │   ├── PuntuacionJugador.java
│   │   │   ├── EstadoPartida.java
│   │   │   └── roscos/           # Ficheros de preguntas para Pasapalabra
│   │   │       ├── rosco_facil.txt
│   │   │       ├── rosco_medio.txt
│   │   │       ├── rosco_avanzado.txt
│   │   │       └── rosco_infantil.txt
│   │   ├── Persistencia/         # Interfaz + implementación en ficheros
│   │   │   ├── GestorPersistencia.java
│   │   │   └── PersistenciaArchivos.java
│   │   └── Vista/                # Ventanas y paneles Swing
│   │       ├── Aplicacion.java   # Punto de entrada principal
│   │       ├── Tema.java         # Paleta de colores y tipografía global
│   │       ├── VentanaLogin.java
│   │       ├── VentanaMenuPrincipal.java
│   │       ├── VentanaSeleccionJuego.java
│   │       ├── VentanaJuego.java           # Clase abstracta base de juego
│   │       ├── VentanaJuegoTresEnRaya.java
│   │       ├── VentanaJuegoPasapalabra.java
│   │       ├── VentanaEstadisticas.java
│   │       └── VentanaAdmin.java
│   └── data/
│       ├── usuarios.txt          # Persistencia de usuarios
│       ├── estadisticas.txt      # Persistencia de estadísticas
│       └── partidas/             # Partidas pausadas serializadas
├── docs/
│   └── README.md
└── Diagrama_TrabajoFinal.drawio  # Diagrama UML de clases
```

---

## 🧩 Componentes Principales

### 🎮 **Juegos Implementados**

#### Pasapalabra
- Rosco de letras con preguntas por cada letra del abecedario
- El jugador responde, pasa o falla cada letra
- Estado serializable para pausar y reanudar

#### Tres en Raya
- Modo dos jugadores en local
- Detección automática de victoria y empate
- Estado serializable para pausar y reanudar

---

### 🏗️ **Arquitectura en Capas**

| Capa | Paquete | Responsabilidad |
|------|---------|----------------|
| **Vista** | `Vista/` | Ventanas y paneles Swing — recoge input y muestra estado |
| **Controlador** | `Controlador/` | Coordina vista con modelo; contiene toda la lógica de negocio |
| **Modelo** | `Modelo/` | Entidades del dominio: usuarios, juegos, partidas, estadísticas |
| **Persistencia** | `Persistencia/` | Interfaz `GestorPersistencia` + implementación en ficheros de texto |

---

### 🔑 **Decisiones de Diseño**

- **`Juego` es clase abstracta** — todos los juegos comparten estado (nombre, finalizado, puntuaciones) pero su mecánica es distinta. Métodos abstractos: `inicializar()`, `getEstadoTexto()`, `serializarEstado()`, `deserializarEstado()`.
- **`GestorPersistencia` es interfaz** — desacopla el resto del código del sistema de almacenamiento, permitiendo migrar de ficheros a base de datos sin tocar las capas superiores.
- **Polimorfismo en `GestorPartidas`** — trabaja con referencias tipo `Juego` sin saber si es `PasaPalabra` o `TresEnRaya`.
- **Serialización de estado** — las partidas pausadas se serializan a string mediante `serializarEstado()` y cada juego define su propio formato.

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Uso |
|-----------|-----|
| **Java 17+** | Lenguaje principal |
| **Java Swing** | Interfaz gráfica de usuario |
| **Ficheros de texto** | Persistencia de datos (requisito de la asignatura) |
| **Draw.io** | Diagramas UML |

---

## 👥 Reparto de Trabajo

| Miembro | Componentes |
|---------|------------|
| **JP-Aceves** | `EstadoPartida`, `PuntuacionJugador`, `GestorPersistencia` (interfaz), `PersistenciaArchivos`, `Juego` (abstracta), `Partida`, `GestorPartidas`, `VentanaJuego` (abstracta), `Sistema` |
| **Adrián Duque** | `Usuario`, `Administrador`, `PasaPalabra`, `PersistenciaArchivos` (colaboración), `VentanaLogin`, `VentanaMenuPrincipal`, `VentanaJuegoPasapalabra` |
| **Juan Carlos Alcazarde** | `Estadistica`, `GestorEstadisticas`, `VentanaEstadisticas`, `VentanaAdmin` |
| **Ignacio del Peso** | `GestorJuegos`, `TresEnRaya`, `VentanaJuegoTresEnRaya` |

---

## 👥 Autores

- **JP Aceves** — [@jp-aceves](https://github.com/jp-aceves)
- **Adrián Duque** — [@Adrian-Duque](https://github.com/Adrian-Duque)
- **Juan Carlos Pérez**
- **Ignacio del Peso**
