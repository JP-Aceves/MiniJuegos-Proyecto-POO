import java.util.ArrayList;

/**
 * Gestor de estadísticas del sistema.
 * Coordina el registro, consulta y ranking de resultados de partidas.
 * Delega toda la persistencia en GestorPersistencia.
 *
 * @author juancarlos
 * @version 3
 */
public class GestorEstadisticas {

    private GestorPersistencia persistencia;
    private ArrayList<Estadistica> listaEstadisticas;

    /**
     * Constructor de GestorEstadisticas.
     *
     * @param persistencia Implementación de Gestorpersistencia a utilizar para guardar y cargar estadisticas 
     */
    public GestorEstadisticas(GestorPersistencia persistencia) {
        this.persistencia = persistencia;
        this.listaEstadisticas = persistencia.cargarEstadisticas();
    }

    /**
     * Registra el resultado de una partida finalizada.
     * Extrae los datos de cada jugador, crea una Estadistica por cada uno
     * y la guarda en persistencia como tambien en la lista en memoria.
     *
     * @param partida Partida finalizada de la que se extrae el resultado
     */
    public void registrarResultado(Partida partida) {
        ArrayList<PuntuacionJugador> puntuaciones = partida.getPuntuaciones();
        Usuario ganador = partida.getGanador();

        // Procesar cada jugador de la partida
        for (PuntuacionJugador pj : puntuaciones) {
            String username = pj.getUsername();
            String nombreJuego = partida.getJuego().getNombre();
            int puntuacion = pj.getPuntos();
            boolean victoria = ganador != null && ganador.getUsername().equals(username);
            String fecha = partida.getFecha();

            // Crear una nueva estadistica para el jugador
            Estadistica e = new Estadistica(username, nombreJuego, puntuacion, victoria, fecha);
            // Guardar la estadistica en persistencia
            persistencia.agregarEstadistica(e);
            // Añadir la estadistica a la lista en memoria
            listaEstadisticas.add(e);
        }
    }

    /**
     * Devuelve todas las estadísticas de un usuario concreto.
     *
     * @param u Usuario del que se quieren las estadísticas
     * @return ArrayList con las estadísticas del usuario
     */
    public ArrayList<Estadistica> getEstadisticasUsuario(Usuario u) {
        ArrayList<Estadistica> resultado = new ArrayList<>();
        for (Estadistica e : listaEstadisticas) {
            if (e.getUsername().equals(u.getUsername())) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /**
     * Devuelve las últimas n estadísticas de un usuario, ordenadas de más reciente a más antigua.
     * Si el usuario tiene menos de n partidas, devuelve todas.
     *
     * @param u Usuario del que se quieren las estadísticas
     * @param n Número máximo de resultados a devolver
     * @return ArrayList con las últimas n estadísticas del usuario
     */
    public ArrayList<Estadistica> getUltimasPartidas(Usuario u, int n) {
        ArrayList<Estadistica> todas = getEstadisticasUsuario(u);

        // Ordenar por fecha descendente
        for (int i = 0; i < todas.size() - 1; i++) {
            for (int j = i + 1; j < todas.size(); j++) {
                if (todas.get(i).getFecha().compareTo(todas.get(j).getFecha()) < 0) {
                    Estadistica temp = todas.get(i);
                    todas.set(i, todas.get(j));
                    todas.set(j, temp);
                }
            }
        }

        // Devolver las últimas n estadísticas del usuario
        ArrayList<Estadistica> resultado = new ArrayList<>();
        for (int i = 0; i < n && i < todas.size(); i++) {
            resultado.add(todas.get(i));
        }
        return resultado;
    }

    /**
     * Calcula el ranking de un juego concreto ordenado por puntuación de mayor a menor.
     *
     * @param nombreJuego Nombre del juego del que se quiere el ranking
     * @return ArrayList de estadísticas ordenadas por puntuación descendente
     */
    public ArrayList<Estadistica> calcularRanking(String nombreJuego) {
        ArrayList<Estadistica> ranking = new ArrayList<>();

        // Filtrar las estadisticas del juego
        for (Estadistica e : listaEstadisticas) {
            if (e.getNombreJuego().equals(nombreJuego)) {
                ranking.add(e);
            }
        }

        // Ordenar por puntuación descendente (burbuja)
        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = i + 1; j < ranking.size(); j++) {
                if (ranking.get(i).getPuntuacion() < ranking.get(j).getPuntuacion()) {
                    Estadistica temp = ranking.get(i);
                    ranking.set(i, ranking.get(j));
                    ranking.set(j, temp);
                }
            }
        }

        return ranking;
    }

    /**
     * Cuenta el total de partidas jugadas por un usuario.
     *
     * @param username Nombre del usuario
     * @return Número de partidas jugadas
     */
    public int contarPartidas(String username) {
        int contador = 0;
        for (Estadistica e : listaEstadisticas) {
            if (e.getUsername().equals(username)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Cuenta el total de victorias de un usuario.
     *
     * @param username Nombre del usuario
     * @return Número de victorias
     */
    public int contarVictorias(String username) {
        int contador = 0;
        for (Estadistica e : listaEstadisticas) {
            if (e.getUsername().equals(username) && e.isVictoria()) {
                contador++;
            }
        }
        return contador;
    }
}