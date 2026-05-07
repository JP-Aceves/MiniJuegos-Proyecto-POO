import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase GestorEstadisticas
 * Gestiona todas las estadísticas de las partidas jugadas en el sistema.
 * Permite almacenar, recuperar, filtrar y exportar estadísticas.
 *  * @author juancarlos
 * @version 1.0
 */

public class GestorEstadisticas {
    private List<Estadistica> estadisticas;
    private String rutaArchivo;

    /**
     * Constructor de GestorEstadisticas
     * 
     * @param rutaArchivo Ruta del archivo donde se guardarán las estadísticas
     */
    public GestorEstadisticas(String rutaArchivo) {
        this.estadisticas = new ArrayList<>();
        this.rutaArchivo = rutaArchivo;
        cargarEstadisticas();
    }

    /**
     * Agrega una nueva estadística
     * 
     * @param estadistica Objeto Estadistica a agregar
     */
    public void agregarEstadistica(Estadistica estadistica) {
        estadisticas.add(estadistica);
        guardarEstadisticas();
    }

    /**
     * Obtiene todas las estadísticas
     * 
     * @return List<Estadistica> con todas las estadísticas
     */
    public List<Estadistica> obtenerTodas() {
        return new ArrayList<>(estadisticas);
    }

    /**
     * Obtiene las estadísticas de un jugador específico
     * 
     * @param username Nombre del usuario
     * @return List<Estadistica> estadísticas del jugador
     */
    public List<Estadistica> obtenerPorJugador(String username) {
        return estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las estadísticas de un juego específico
     * 
     * @param nombreJuego Nombre del juego
     * @return List<Estadistica> estadísticas del juego
     */
    public List<Estadistica> obtenerPorJuego(String nombreJuego) {
        return estadisticas.stream()
                .filter(e -> e.getNombreJuego().equalsIgnoreCase(nombreJuego))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las victorias de un jugador
     * 
     * @param username Nombre del usuario
     * @return int número de victorias
     */
    public int obtenerVictorias(String username) {
        return (int) estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username) && e.isVictoria())
                .count();
    }

    /**
     * Obtiene las derrotas de un jugador
     * 
     * @param username Nombre del usuario
     * @return int número de derrotas
     */
    public int obtenerDerrotas(String username) {
        return (int) estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username) && !e.isVictoria())
                .count();
    }

    /**
     * Calcula la tasa de victorias de un jugador
     * 
     * @param username Nombre del usuario
     * @return double porcentaje de victorias
     */
    public double obtenerTasaVictorias(String username) {
        List<Estadistica> estadisticasJugador = obtenerPorJugador(username);
        if (estadisticasJugador.isEmpty()) {
            return 0.0;
        }
        int victorias = obtenerVictorias(username);
        return (victorias * 100.0) / estadisticasJugador.size();
    }

    /**
     * Obtiene la puntuación promedio de un jugador
     * 
     * @param username Nombre del usuario
     * @return int puntuación promedio
     */
    public int obtenerPuntuacionPromedio(String username) {
        List<Estadistica> estadisticasJugador = obtenerPorJugador(username);
        if (estadisticasJugador.isEmpty()) {
            return 0;
        }
        return (int) estadisticasJugador.stream()
                .mapToInt(Estadistica::getPuntuacion)
                .average()
                .orElse(0);
    }

    /**
     * Obtiene la puntuación máxima de un jugador
     * 
     * @param username Nombre del usuario
     * @return int puntuación máxima
     */
    public int obtenerPuntuacionMaxima(String username) {
        return estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username))
                .mapToInt(Estadistica::getPuntuacion)
                .max()
                .orElse(0);
    }

    /**
     * Obtiene la puntuación mínima de un jugador
     * 
     * @param username Nombre del usuario
     * @return int puntuación mínima
     */
    public int obtenerPuntuacionMinima(String username) {
        return estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username))
                .mapToInt(Estadistica::getPuntuacion)
                .min()
                .orElse(0);
    }

    /**
     * Obtiene el total de partidas jugadas por un jugador
     * 
     * @param username Nombre del usuario
     * @return int total de partidas
     */
    public int obtenerTotalPartidas(String username) {
        return (int) estadisticas.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username))
                .count();
    }

    /**
     * Obtiene todos los nombres de usuario únicos
     * 
     * @return List<String> lista de usuarios
     */
    public List<String> obtenerUsuarios() {
        return estadisticas.stream()
                .map(Estadistica::getUsername)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Guarda las estadísticas en un archivo CSV
     */
    public void guardarEstadisticas() {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezado
            writer.write("username,nombreJuego,puntuacion,victoria,fecha\n");
            
            // Escribir registros
            for (Estadistica est : estadisticas) {
                writer.write(est.toArchivo() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Carga las estadísticas desde el archivo CSV
     */
    public void cargarEstadisticas() {
        estadisticas.clear();
        File archivo = new File(rutaArchivo);
        
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar encabezado
                }
                
                String[] partes = linea.split(",");
                if (partes.length == 5) {
                    String username = partes[0];
                    String nombreJuego = partes[1];
                    int puntuacion = Integer.parseInt(partes[2]);
                    boolean victoria = Boolean.parseBoolean(partes[3]);
                    String fecha = partes[4];
                    
                    estadisticas.add(new Estadistica(username, nombreJuego, puntuacion, victoria, fecha));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Muestra un resumen de estadísticas de un jugador
     * 
     * @param username Nombre del usuario
     */
    public void mostrarResumenJugador(String username) {
        List<Estadistica> estadisticasJugador = obtenerPorJugador(username);
        
        if (estadisticasJugador.isEmpty()) {
            System.out.println("No hay estadísticas para el jugador: " + username);
            return;
        }

        System.out.println("\n" + "═".repeat(60));
        System.out.println("RESUMEN DE ESTADÍSTICAS - " + username);
        System.out.println("═".repeat(60));
        System.out.printf("Total de partidas:        %d\n", obtenerTotalPartidas(username));
        System.out.printf("Victorias:                %d\n", obtenerVictorias(username));
        System.out.printf("Derrotas:                 %d\n", obtenerDerrotas(username));
        System.out.printf("Tasa de victorias:        %.1f%%\n", obtenerTasaVictorias(username));
        System.out.printf("Puntuación promedio:      %d puntos\n", obtenerPuntuacionPromedio(username));
        System.out.printf("Puntuación máxima:        %d puntos\n", obtenerPuntuacionMaxima(username));
        System.out.printf("Puntuación mínima:        %d puntos\n", obtenerPuntuacionMinima(username));
        System.out.println("═".repeat(60));
    }

    /**
     * Muestra todas las estadísticas de forma legible
     */
    public void mostrarTodas() {
        if (estadisticas.isEmpty()) {
            System.out.println("No hay estadísticas registradas.");
            return;
        }

        System.out.println("\n" + "═".repeat(110));
        System.out.printf("%-15s | %-20s | %-12s | %-10s | %-20s\n", "Usuario", "Juego", "Puntuación", "Resultado", "Fecha");
        System.out.println("═".repeat(110));

        for (Estadistica est : estadisticas) {
            String resultado = est.isVictoria() ? "VICTORIA" : "DERROTA";
            System.out.printf("%-15s | %-20s | %-12d | %-10s | %-20s\n",
                    est.getUsername(), est.getNombreJuego(), est.getPuntuacion(), resultado, est.getFecha());
        }

        System.out.println("═".repeat(110));
    }

    /**
     * Obtiene el número total de estadísticas registradas
     * 
     * @return int total de estadísticas
     */
    public int obtenerTotal() {
        return estadisticas.size();
    }
}