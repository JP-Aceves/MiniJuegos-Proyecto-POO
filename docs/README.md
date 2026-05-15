# MiniJuegos POO 🎮
**Proyecto Final - Programación Orientada a Objetos**
> Aplicación de minijuegos en Java con arquitectura en capas, interfaz gráfica Swing y persistencia en ficheros

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

## 📁 Estructura del Proyecto

```
Proyecto-POO/
├── Programa/
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
│   │   │   ├── Usuario.java
│   │   │   ├── Administrador.java
│   │   │   ├── Partida.java
│   │   │   ├── Estadistica.java
│   │   │   ├── PuntuacionJugador.java
│   │   │   └── EstadoPartida.java
│   │   ├── Persistencia/         # Interfaz + implementación en ficheros
│   │   │   ├── GestorPersistencia.java
│   │   │   └── PersistenciaArchivos.java
│   │   └── Vista/                # Ventanas y paneles Swing
│   │       ├── VentanaLogin.java
│   │       ├── VentanaMenuPrincipal.java
│   │       ├── VentanaJuego.java
│   │       └── VentanaJuegoPasapalabra.java
│   └── data/
│       ├── usuarios.txt          # Persistencia de usuarios
│       ├── estadisticas.txt      # Persistencia de estadísticas
│       └── partidas/             # Partidas pausadas serializadas
├── Diagrama_TrabajoFinal.drawio  # Diagrama UML de clases
└── README.md
```

---

## 🧩 Componentes Principales

### 🎮 **Juegos Implementados**

#### Pasapalabra
- Rosco de letras con preguntas por cada letra del abecedario
- El jugador responde, pasa o falla cada letra
- Estado serializable para pausar y reanudar

#### Tres en Raya *(en desarrollo)*
- Modo dos jugadores en local
- Detección automática de victoria y empate

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

> ⚠️ Sin librerías externas — requisito de la asignatura.

---

## 🚀 Cómo Ejecutar

### Requisitos Previos
- Java 17 o superior
- Cualquier IDE compatible (IntelliJ IDEA, Eclipse, VS Code + Extension Pack for Java)

### Compilar y ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Adrian-Duque/Proyecto-POO.git
   cd Proyecto-POO/Programa
   ```

2. **Compilar**
   ```bash
   javac -d out src/**/*.java
   ```

3. **Ejecutar**
   ```bash
   java -cp out Sistema
   ```

### Desde un IDE
- Importar la carpeta `Programa/` como proyecto Java
- Ejecutar la clase `Sistema.java` como punto de entrada

---

## 👥 Reparto de Trabajo

| Miembro | Componentes |
|---------|------------|
| **JP** | `EstadoPartida`, `PuntuacionJugador`, `GestorPersistencia` (interfaz), `PersistenciaArchivos`, `Juego` (abstracta), `Partida`, `GestorPartidas`, `VentanaJuego` (abstracta), `Sistema` |
| **Adrián** | `Usuario`, `Administrador`, `PasaPalabra`, `PersistenciaArchivos` (colaboración), `VentanaLogin`, `VentanaMenuPrincipal`, `VentanaJuegoPasapalabra` |
| **Juan Carlos** | `Estadistica`, `GestorEstadisticas`, `VentanaEstadisticas`, `VentanaAdmin` |
| **Nacho** | `GestorJuegos`, `TresEnRaya`, `VentanaJuegoTresEnRaya` |

---

## 👥 Autores

- **JP Aceves** — [@jp-aceves](https://github.com/jp-aceves)
- **Adrián Duque** — [@Adrian-Duque](https://github.com/Adrian-Duque)
- **Juan Carlos Pérez**
- **Ignacio del Peso**
