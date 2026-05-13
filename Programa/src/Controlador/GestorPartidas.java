package Controlador;

import Modelo.Juego;
import Modelo.Partida;
import Modelo.Usuario;
import Persistencia.GestorPersistencia;
import java.util.ArrayList;

/**
 * Controlador responsable de gestionar el ciclo de vida de las partidas.
 * <p>
 * Coordina la creación, pausa, reanudación y finalización de partidas,
 * delegando la persistencia en {@link GestorPersistencia}.
 * </p>
 *
 * @author JP-Aceves
 * @author Adrián
 * @version 1.2
 */
public class GestorPartidas {

    /**
     * Contador global de partidas creadas. Se inicializa leyendo los ficheros
     * existentes en disco para evitar colisiones de id entre ejecuciones.
     */
    private static int contadorId = 0;

    /** Partida que se está jugando en este momento. {@code null} si no hay ninguna activa. */
    private Partida partidaActual;

    /** Historial de partidas finalizadas durante esta ejecución. */
    private ArrayList<Partida> listaPartidas = new ArrayList<>();

    /** Capa de persistencia para guardar y recuperar partidas pausadas. */
    private GestorPersistencia persistencia;

    /**
     * Construye el gestor con la implementación de persistencia indicada
     * e inicializa el contador de ids leyendo los ficheros existentes en disco.
     *
     * @param persistencia implementación de {@link GestorPersistencia} a usar
     */
    public GestorPartidas(GestorPersistencia persistencia) {
        this.persistencia = persistencia;
        this.partidaActual = null;
        inicializarContador();
    }

    /**
     * Lee los ids de las partidas pausadas en disco y fija {@code contadorId}
     * al valor máximo encontrado. Así cada nueva partida recibe un id único
     * aunque la aplicación se haya reiniciado entre sesiones.
     */
    private void inicializarContador() {
        ArrayList<Integer> pausadas = persistencia.listarPartidasPausadas();
        int maxId = 0;
        for (int id : pausadas) {
            if (id > maxId) maxId = id;
        }
        contadorId = maxId;
    }

    /**
     * Inicializa el juego y crea una nueva partida con los jugadores indicados.
     * El id se asigna incrementando {@code contadorId}, garantizando unicidad
     * entre ejecuciones.
     *
     * @param juego     juego que se va a jugar
     * @param jugadores lista de usuarios que participan
     * @param fecha     fecha de inicio de la partida
     * @return la {@link Partida} recién creada
     */
    public Partida iniciarPartida(Juego juego, ArrayList<Usuario> jugadores) {
        juego.inicializar();
        partidaActual = new Partida(++contadorId, juego, jugadores);
        return partidaActual;
    }

    /**
     * Pausa la partida actual y guarda su estado serializado en disco.
     * No hace nada si no hay ninguna partida activa.
     */
    public void pausarPartida() {
        if (partidaActual == null) return;
        partidaActual.pausar();
        String estadoSerializado = partidaActual.getJuego().serializarEstado();
        persistencia.guardarPartidaPausada(partidaActual.getId(), estadoSerializado);
    }

    /**
     * Reanuda una partida pausada restaurando su estado desde el string serializado.
     *
     * @param partida           partida a reanudar
     * @param estadoSerializado string con el estado guardado previamente
     */
    public void reanudarPartida(Partida partida, String estadoSerializado) {
        partida.reanudar(estadoSerializado);
        partidaActual = partida;
    }

    /**
     * Finaliza la partida actual, la añade al historial y elimina su fichero de pausa.
     * No hace nada si no hay ninguna partida activa.
     */
    public void finalizarPartida() {
        if (partidaActual == null) return;
        partidaActual.finalizar();
        listaPartidas.add(partidaActual);
        persistencia.eliminarPartidaPausada(partidaActual.getId());
        partidaActual = null;
    }

    /**
     * Devuelve los identificadores de las partidas pausadas almacenadas en disco.
     *
     * @return lista de ids como {@code Integer}
     */
    public ArrayList<Integer> listarPartidasPausadas() {
        return persistencia.listarPartidasPausadas();
    }

    /**
     * Carga el estado serializado de una partida pausada a partir de su id.
     *
     * @param id identificador de la partida
     * @return string con el estado serializado, o {@code null} si no existe
     */
    public String cargarDatosPartida(int id) {
        return persistencia.cargarPartidaPausada(id);
    }

    /**
     * Devuelve la partida que se está jugando en este momento.
     *
     * @return {@link Partida} actual, o {@code null} si no hay ninguna activa
     */
    public Partida getPartidaActual() {
        return partidaActual;
    }

    /**
     * Filtra el historial devolviendo solo las partidas en las que participó el usuario.
     *
     * @param usuario usuario por el que filtrar
     * @return lista de {@link Partida} que contienen al usuario
     */
    public ArrayList<Partida> getPartidasUsuario(Usuario usuario) {
        ArrayList<Partida> resultado = new ArrayList<>();
        for (Partida p : listaPartidas) {
            if (p.contieneJugador(usuario)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}