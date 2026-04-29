package Modelo;

import java.util.ArrayList;

/**
 * Representa una sesión concreta de juego.
 * Coordina el flujo de la partida: gestión de turnos, estados y resultados.
 * Una partida puede estar en curso, pausada o finalizada.
 *
 * @author JP
 * @version 1.0
 */
public class Partida {

    /** Identificador único de la partida. */
    private int id;

    /** Fecha de inicio de la partida. */
    private String fecha;

    /** Índice del jugador que tiene el turno actual en {@code listaJugadores}. */
    private int turnoActual;

    /** Estado actual de la partida: EN_CURSO, PAUSADA o FINALIZADA. */
    private EstadoPartida estadoActual;

    /** Juego al que se está jugando en esta partida. */
    private Juego juego;

    /** Lista de jugadores que participan en la partida. */
    private ArrayList<Usuario> listaJugadores = new ArrayList<>();

    /**
     * Crea una nueva partida en estado EN_CURSO.
     *
     * @param id             identificador único de la partida
     * @param juego          juego al que se va a jugar
     * @param fecha          fecha de inicio en formato String
     * @param listaJugadores lista de usuarios que participan
     */
    public Partida(int id, Juego juego, String fecha, ArrayList<Usuario> listaJugadores) {
        this.id = id;
        this.juego = juego;
        this.listaJugadores = listaJugadores;
        this.turnoActual = 0;
        this.estadoActual = EstadoPartida.EN_CURSO;
        this.fecha = fecha;
    }

    /**
     * Pausa la partida cambiando su estado a PAUSADA.
     * El estado del juego debe ser serializado por {@code GestorPartidas}
     * llamando a {@code getJuego().serializarEstado()} tras este método.
     */
    public void pausar() {
        this.estadoActual = EstadoPartida.PAUSADA;
    }

    /**
     * Reanuda una partida pausada restaurando el estado del juego.
     *
     * @param estadoSerializado String con el estado guardado previamente
     */
    public void reanudar(String estadoSerializado) {
        juego.deserializarEstado(estadoSerializado);
        this.estadoActual = EstadoPartida.EN_CURSO;
    }

    /**
     * Finaliza la partida cambiando su estado a FINALIZADA.
     */
    public void finalizar() {
        this.estadoActual = EstadoPartida.FINALIZADA;
    }

    /**
     * Devuelve el usuario que tiene el turno actual.
     *
     * @return usuario cuyo turno es el actual
     */
    public Usuario getJugadorActual() {
        return listaJugadores.get(turnoActual);
    }

    /**
     * Avanza al siguiente turno de forma circular.
     * Cuando llega al último jugador vuelve al primero.
     */
    public void siguienteTurno() {
        turnoActual = (turnoActual + 1) % listaJugadores.size();
    }

    /**
     * Comprueba si un usuario participa en esta partida.
     *
     * @param u usuario a buscar
     * @return {@code true} si el usuario está en la partida, {@code false} en caso contrario
     */
    public boolean contieneJugador(Usuario u) {
        for (Usuario jugador : listaJugadores) {
            if (jugador.getUsername().equals(u.getUsername())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve la puntuación actual de un jugador delegando en el juego.
     *
     * @param u usuario del que se quiere la puntuación
     * @return puntuación del usuario en el juego actual
     */
    public int getPuntuacion(Usuario u) {
        return juego.getPuntuacion(u.getUsername());
    }

    /**
     * Devuelve la lista completa de puntuaciones de todos los jugadores.
     *
     * @return lista de {@code PuntuacionJugador}
     */
    public ArrayList<PuntuacionJugador> getPuntuaciones() {
        return juego.getPuntuaciones();
    }

    /**
     * Devuelve el jugador con mayor puntuación al final de la partida.
     *
     * @return usuario ganador, o {@code null} si no hay jugadores
     */
    public Usuario getGanador() {
        Usuario ganador = null;
        int maxPuntos = -1;
        for (Usuario u : listaJugadores) {
            int pts = juego.getPuntuacion(u.getUsername());
            if (pts > maxPuntos) {
                maxPuntos = pts;
                ganador = u;
            }
        }
        return ganador;
    }

    /**
     * @return identificador único de la partida
     */
    public int getId() {
        return id;
    }

    /**
     * @return fecha de inicio de la partida
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @return estado actual de la partida
     */
    public EstadoPartida getEstadoActual() {
        return estadoActual;
    }

    /**
     * @return juego asociado a esta partida
     */
    public Juego getJuego() {
        return juego;
    }

    /**
     * @return lista de usuarios que participan en la partida
     */
    public ArrayList<Usuario> getListaJugadores() {
        return listaJugadores;
    }

    /**
     * Devuelve una representación legible de la partida con id, juego, fecha y estado.
     *
     * @return String con los datos principales de la partida
     */
    @Override
    public String toString() {
        return "Partida{id=" + id +
               ", juego=" + juego.getNombre() +
               ", fecha=" + fecha +
               ", estado=" + estadoActual + "}";
    }
}