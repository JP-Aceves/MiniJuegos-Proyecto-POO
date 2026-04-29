package Controlador;

import java.util.ArrayList;

public class GestorPartidas {

    private Partida partidaActual;
    private ArrayList<Partida> listaPartidas = new ArrayList<>();
    private GestorPersistencia persistencia;


    public GestorPartidas(GestorPersistencia persistencia) {
        this.persistencia = persistencia;
        this.partidaActual = null;
    }

    public Partida iniciarPartida(Juego juego, ArrayList<Usuario> jugadores) {
        juego.inicializar();
        partidaActual = new Partida(juego, jugadores);
        return partidaActual;
    }

    public void pausarPartida() {
        if (partidaActual == null) return;
        String estadoSerializado = partidaActual.pausar();
        persistencia.guardarPartidaPausada(partidaActual.getId(), estadoSerializado);
    }

    public void reanudarPartida(Partida partida, String estadoSerializado) {
        partida.reanudar(estadoSerializado);
        partidaActual = partida;
    }

    public void finalizarPartida() {
        if (partidaActual == null) return;
        partidaActual.finalizar();
        listaPartidas.add(partidaActual);
        persistencia.eliminarPartidaPausada(partidaActual.getId());
        partidaActual = null;
    }

    public ArrayList<String> listarPartidasPausadas() {
        return persistencia.listarPartidasPausadas();
    }

    public String cargarDatosPartida(int id) {
        return persistencia.cargarPartidaPausada(id);
    }

    public Partida getPartidaActual() {
        return partidaActual;
    }

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
