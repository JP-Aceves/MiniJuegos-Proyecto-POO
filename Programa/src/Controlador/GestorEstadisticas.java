/**
 * 
 * @author Sistema de Gestión de Juegos
 * @version 1.0
 * @since 1.0
 * 
 * @see Estadistica
 */
import java.io.*;
import java.util.ArrayList;

public class GestorEstadisticas {
    
    /**
     * Lista que almacena todas las estadísticas del sistema.
     * Cada elemento es un objeto Estadistica con información de una partida.
     */
    private ArrayList<Estadistica> estadisticas;
    
    /**
     * Ruta del archivo CSV donde se persisten las estadísticas.
     * Se carga al iniciar y se guarda después de cada operación.
     */
    private String rutaArchivo;

    /**
     * Constructor de GestorEstadisticas.
     * 
     * Inicializa el gestor y carga las estadísticas existentes desde el archivo.
     * Si el archivo no existe, comienza con una lista vacía.
     * 
     * @param rutaArchivo Ruta completa del archivo CSV donde guardar/cargar
     *                    estadísticas. Ej: "estadisticas_pasapalabra.csv"
     * 
     * @example
     *     GestorEstadisticas gestor = new GestorEstadisticas("datos/estadisticas.csv");
     * 
     * @see #cargarEstadisticas()
     */
    public GestorEstadisticas(String rutaArchivo) {
        this.estadisticas = new ArrayList<>();
        this.rutaArchivo = rutaArchivo;
        cargarEstadisticas();
    }

    /**
     * Registra el resultado de una partida en el sistema.
     * 
     * Agrega la estadística a la lista y guarda automáticamente los cambios
     * en el archivo CSV.
     * 
     * @param partida Objeto Estadistica con los datos de la partida
     *                (usuario, juego, puntuación, resultado, fecha)
     * 
     * @throws NullPointerException si partida es null
     * 
     * @example
     *     Estadistica est = new Estadistica("Juan", "Pasapalabra", 2450, true, "2026-05-01");
     *     gestor.registrarResultado(est);
     * 
     * @see Estadistica
     * @see #guardarEstadisticas()
     */
    public void registrarResultado(Estadistica partida) {
        estadisticas.add(partida);
        guardarEstadisticas();
    }

    /**
     * Obtiene todas las estadísticas de un usuario específico.
     * 
     * Busca linealmente a través de todas las estadísticas y devuelve
     * aquellas que pertenecen al usuario especificado.
     * La búsqueda no es sensible a mayúsculas/minúsculas.
     * 
     * @param username Nombre del usuario a buscar (no sensible a mayúsculas)
     * 
     * @return ArrayList con todas las estadísticas del usuario.
     *         Devuelve una lista vacía si el usuario no tiene estadísticas.
     * 
     * @example
     *     ArrayList&lt;Estadistica&gt; estadisticas = gestor.getEstadisticasUsuario("Juan");
     *     for (int i = 0; i < estadisticas.size(); i++) {
     *         System.out.println(estadisticas.get(i));
     *     }
     * 
     * @see #contarPartidas(String)
     * @see #contarVictorias(String)
     */
    public ArrayList<Estadistica> getEstadisticasUsuario(String username) {
        ArrayList<Estadistica> resultado = new ArrayList<>();
        
        // Recorrer todas las estadísticas
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            
            // Si el username coincide, agregar al resultado
            if (est.getUsername().equalsIgnoreCase(username)) {
                resultado.add(est);
            }
        }
        
        return resultado;
    }

    /**
     * Obtiene las últimas n partidas jugadas por un usuario.
     * 
     * Si el usuario tiene menos de n partidas, devuelve todas.
     * Las partidas se devuelven en el mismo orden que fueron jugadas.
     * 
     * @param username Nombre del usuario
     * @param n        Número de últimas partidas a recuperar
     * 
     * @return ArrayList con las últimas n partidas del usuario
     * 
     * @example
     *     // Obtener las últimas 5 partidas de Juan
     *     ArrayList&lt;Estadistica&gt; ultimas = gestor.getUltimasPartidas("Juan", 5);
     *     
     *     // Obtener la última partida
     *     ArrayList&lt;Estadistica&gt; ultima = gestor.getUltimasPartidas("Juan", 1);
     * 
     * @see #getEstadisticasUsuario(String)
     */
    public ArrayList<Estadistica> getUltimasPartidas(String username, int n) {
        // Primero obtener todas las partidas del usuario
        ArrayList<Estadistica> partidasUsuario = getEstadisticasUsuario(username);
        ArrayList<Estadistica> resultado = new ArrayList<>();
        
        // Calcular desde dónde empezar
        int inicio = partidasUsuario.size() - n;
        if (inicio < 0) {
            inicio = 0;
        }
        
        // Agregar desde ese punto hasta el final
        for (int i = inicio; i < partidasUsuario.size(); i++) {
            resultado.add(partidasUsuario.get(i));
        }
        
        return resultado;
    }

    /**
     * Calcula el ranking de jugadores por juego.
     * 
     * Ordena todos los jugadores de un juego específico según su puntuación
     * promedio de mayor a menor.
     * 
     * @param nombreJuego Nombre del juego para el que calcular ranking
     * 
     * @return ArrayList con los nombres de usuarios ordenados por puntuación
     *         promedio descendente. Primer elemento = mejor puntuación.
     * 
     * @example
     *     ArrayList&lt;String&gt; ranking = gestor.calcularRanking("Pasapalabra");
     *     
     *     // Mostrar ranking
     *     for (int i = 0; i < ranking.size(); i++) {
     *         System.out.println((i+1) + ". " + ranking.get(i));
     *     }
     *     // Salida:
     *     // 1. Juan
     *     // 2. María
     *     // 3. Carlos
     * 
     * @see #contarPartidas(String)
     */
    public ArrayList<String> calcularRanking(String nombreJuego) {
        ArrayList<String> usuarios = new ArrayList<>();
        ArrayList<Double> promedios = new ArrayList<>();
        
        // Primero, obtener todos los usuarios únicos del juego
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            
            // Si es del juego especificado
            if (est.getNombreJuego().equalsIgnoreCase(nombreJuego)) {
                String username = est.getUsername();
                
                // Verificar si ya existe en la lista de usuarios
                boolean existe = false;
                for (int j = 0; j < usuarios.size(); j++) {
                    if (usuarios.get(j).equalsIgnoreCase(username)) {
                        existe = true;
                        break;
                    }
                }
                
                // Si no existe, agregarlo
                if (!existe) {
                    usuarios.add(username);
                }
            }
        }
        
        // Ahora calcular el promedio de cada usuario
        for (int i = 0; i < usuarios.size(); i++) {
            String usuario = usuarios.get(i);
            int sumaPuntos = 0;
            int conteo = 0;
            
            // Sumar todos los puntos del usuario en este juego
            for (int j = 0; j < estadisticas.size(); j++) {
                Estadistica est = estadisticas.get(j);
                
                if (est.getNombreJuego().equalsIgnoreCase(nombreJuego) && 
                    est.getUsername().equalsIgnoreCase(usuario)) {
                    sumaPuntos += est.getPuntuacion();
                    conteo++;
                }
            }
            
            // Calcular promedio
            double promedio = (conteo > 0) ? (double) sumaPuntos / conteo : 0;
            promedios.add(promedio);
        }
        
        // Ordenar de mayor a menor promedio (burbuja)
        for (int i = 0; i < promedios.size(); i++) {
            for (int j = 0; j < promedios.size() - 1 - i; j++) {
                if (promedios.get(j) < promedios.get(j + 1)) {
                    // Intercambiar en promedios
                    double tempPromedio = promedios.get(j);
                    promedios.set(j, promedios.get(j + 1));
                    promedios.set(j + 1, tempPromedio);
                    
                    // Intercambiar en usuarios
                    String tempUsuario = usuarios.get(j);
                    usuarios.set(j, usuarios.get(j + 1));
                    usuarios.set(j + 1, tempUsuario);
                }
            }
        }
        
        return usuarios;
    }

    /**
     * Cuenta el número total de partidas jugadas por un usuario.
     * 
     * Recorre todas las estadísticas y cuenta cuántas pertenecen al usuario.
     * 
     * @param username Nombre del usuario
     * 
     * @return int con el número total de partidas jugadas
   
     */
    public int contarPartidas(String username) {
        int contador = 0;
        
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            if (est.getUsername().equalsIgnoreCase(username)) {
                contador++;
            }
        }
        
        return contador;
    }

    /**
     * Cuenta el número de victorias de un usuario.
     * 
     * Recorre todas las estadísticas del usuario y cuenta aquellas donde
     * isVictoria() devuelve true.
     * 
     * @param username Nombre del usuario
     * 
     * @return int con el número de victorias
     * 
     * @example
     *     int victorias = gestor.contarVictorias("Juan");
     *     int partidas = gestor.contarPartidas("Juan");
     *     double porcentaje = (victorias * 100.0) / partidas;
     *     System.out.println("Juan: " + porcentaje + "% de victorias");
     * 
     * @see #contarPartidas(String)
     * @see Estadistica#isVictoria()
     */
    public int contarVictorias(String username) {
        int contador = 0;
        
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            if (est.getUsername().equalsIgnoreCase(username) && est.isVictoria()) {
                contador++;
            }
        }
        
        return contador;
    }

    /**
     * Guarda todas las estadísticas en el archivo CSV.
     * 
     * Crea un archivo CSV con encabezado y una línea por cada estadística.
     * Formato: username,nombreJuego,puntuacion,victoria,fecha
     * 
     * Si el archivo existe, lo sobrescribe.
     * Este método se llama automáticamente después de registrarResultado().
     * 
     * @throws IOException si hay error al escribir el archivo
     * 
     * @see #registrarResultado(Estadistica)
     * @see #cargarEstadisticas()
     */
    private void guardarEstadisticas() {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezado
            writer.write("username,nombreJuego,puntuacion,victoria,fecha\n");
            
            // Escribir registros
            for (int i = 0; i < estadisticas.size(); i++) {
                Estadistica est = estadisticas.get(i);
                writer.write(est.toArchivo() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Carga las estadísticas desde el archivo CSV.
     * 
     * Lee el archivo CSV línea por línea y crea objetos Estadistica.
     * Salta la primera línea (encabezado).
     * Si el archivo no existe, inicia con lista vacía.
     * 
     * Formato esperado del CSV:
     * <pre>
     * username,nombreJuego,puntuacion,victoria,fecha
     * Juan,Pasapalabra,2450,true,2026-05-01 14:30
     * María,Pasapalabra,2800,true,2026-05-01 14:30
     * </pre>
     * 
     * @throws IOException si hay error al leer el archivo
     * 
     * @see #GestorEstadisticas(String)
     * @see #guardarEstadisticas()
     */
    private void cargarEstadisticas() {
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
}
        ArrayList<Estadistica> resultado = new ArrayList<>();
        
        // Recorrer todas las estadísticas
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            
            // Si el username coincide, agregar al resultado
            if (est.getUsername().equalsIgnoreCase(username)) {
                resultado.add(est);
            }
        }
        
        return resultado;
    }

    /**
     * Obtiene las últimas n partidas de un usuario
     * 
     * @param username Nombre del usuario
     * @param n Número de partidas a recuperar
     * @return ArrayList con las últimas n partidas
     */
    public ArrayList<Estadistica> getUltimasPartidas(String username, int n) {
        // Primero obtener todas las partidas del usuario
        ArrayList<Estadistica> partidasUsuario = getEstadisticasUsuario(username);
        ArrayList<Estadistica> resultado = new ArrayList<>();
        
        // Calcular desde dónde empezar
        int inicio = partidasUsuario.size() - n;
        if (inicio < 0) {
            inicio = 0;
        }
        
        // Agregar desde ese punto hasta el final
        for (int i = inicio; i < partidasUsuario.size(); i++) {
            resultado.add(partidasUsuario.get(i));
        }
        
        return resultado;
    }

    /**
     * Calcula el ranking de jugadores por juego
     * 
     * @param nombreJuego Nombre del juego
     * @return ArrayList con los usuarios ordenados por puntuación promedio (descendente)
     */
    public ArrayList<String> calcularRanking(String nombreJuego) {
        ArrayList<String> usuarios = new ArrayList<>();
        ArrayList<Double> promedios = new ArrayList<>();
        
        // Primero, obtener todos los usuarios únicos del juego
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            
            // Si es del juego especificado
            if (est.getNombreJuego().equalsIgnoreCase(nombreJuego)) {
                String username = est.getUsername();
                
                // Verificar si ya existe en la lista de usuarios
                boolean existe = false;
                for (int j = 0; j < usuarios.size(); j++) {
                    if (usuarios.get(j).equalsIgnoreCase(username)) {
                        existe = true;
                        break;
                    }
                }
                
                // Si no existe, agregarlo
                if (!existe) {
                    usuarios.add(username);
                }
            }
        }
        
        // Ahora calcular el promedio de cada usuario
        for (int i = 0; i < usuarios.size(); i++) {
            String usuario = usuarios.get(i);
            int sumaPuntos = 0;
            int conteo = 0;
            
            // Sumar todos los puntos del usuario en este juego
            for (int j = 0; j < estadisticas.size(); j++) {
                Estadistica est = estadisticas.get(j);
                
                if (est.getNombreJuego().equalsIgnoreCase(nombreJuego) && 
                    est.getUsername().equalsIgnoreCase(usuario)) {
                    sumaPuntos += est.getPuntuacion();
                    conteo++;
                }
            }
            
            // Calcular promedio
            double promedio = (conteo > 0) ? (double) sumaPuntos / conteo : 0;
            promedios.add(promedio);
        }
        
        // Ordenar de mayor a menor promedio (burbuja)
        for (int i = 0; i < promedios.size(); i++) {
            for (int j = 0; j < promedios.size() - 1 - i; j++) {
                if (promedios.get(j) < promedios.get(j + 1)) {
                    // Intercambiar en promedios
                    double tempPromedio = promedios.get(j);
                    promedios.set(j, promedios.get(j + 1));
                    promedios.set(j + 1, tempPromedio);
                    
                    // Intercambiar en usuarios
                    String tempUsuario = usuarios.get(j);
                    usuarios.set(j, usuarios.get(j + 1));
                    usuarios.set(j + 1, tempUsuario);
                }
            }
        }
        
        return usuarios;
    }

    /**
     * Cuenta el número de partidas de un usuario
     * 
     * @param username Nombre del usuario
     * @return int número de partidas
     */
    public int contarPartidas(String username) {
        int contador = 0;
        
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            if (est.getUsername().equalsIgnoreCase(username)) {
                contador++;
            }
        }
        
        return contador;
    }

    /**
     * Cuenta el número de victorias de un usuario
     * 
     * @param username Nombre del usuario
     * @return int número de victorias
     */
    public int contarVictorias(String username) {
        int contador = 0;
        
        for (int i = 0; i < estadisticas.size(); i++) {
            Estadistica est = estadisticas.get(i);
            if (est.getUsername().equalsIgnoreCase(username) && est.isVictoria()) {
                contador++;
            }
        }
        
        return contador;
    }

    /**
     * Guarda las estadísticas en un archivo CSV
     */
    private void guardarEstadisticas() {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Escribir encabezado
            writer.write("username,nombreJuego,puntuacion,victoria,fecha\n");
            
            // Escribir registros
            for (int i = 0; i < estadisticas.size(); i++) {
                Estadistica est = estadisticas.get(i);
                writer.write(est.toArchivo() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Carga las estadísticas desde el archivo CSV
     */
    private void cargarEstadisticas() {
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
}