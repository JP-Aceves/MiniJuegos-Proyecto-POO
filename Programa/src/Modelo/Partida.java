package Modelo;

import java.util.ArrayList;

public class Partida {
    private int id;
    private String fecha;
    private int turnoActual;
    private EstadoPartida estadoActual;
    private Juego juego;
    private ArrayList<Usuario>listaJugadores = new ArrayList<>();

    public Partida(int id, Juego juego, String fecha, ArrayList<Usuario> listaJugadores) {
        this.id = id;
        this.juego = juego;
        this.listaJugadores = listaJugadores;
        this.turnoActual = 0;
        this.estadoActual = EstadoPartida.EN_CURSO;
        this.fecha = fecha;
    }

    public void pausar() {
        this.estadoActual = EstadoPartida.PAUSADA;
    }

    public void reanudar(String estadoSerializado) {
        juego.deserializarEstado(estadoSerializado);
        this.estadoActual = EstadoPartida.EN_CURSO;
    }

    public void finalizar() {
        this.estadoActual = EstadoPartida.FINALIZADA;
    }

    public Usuario getJugadorActual() {
        return listaJugadores.get(turnoActual);
    }

    public void siguienteTurno() {
        turnoActual = (turnoActual + 1) % listaJugadores.size();
    }

    public boolean contieneJugador(Usuario u) {
        for (Usuario jugador : listaJugadores) {
            if (jugador.getUsername().equals(u.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public int getPuntuacion(Usuario u) {
        return juego.getPuntuacion(u.getUsername());
    }

    public ArrayList<PuntuacionJugador> getPuntuaciones() {
        return juego.getPuntuaciones();
    }

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


    public int getId(){
        return id; 
    }

    public String getFecha() {
        return fecha;
    }

    public EstadoPartida getEstadoActual() {
        return estadoActual;
    }

    public Juego getJuego() {
        return juego;
    }

    public ArrayList<Usuario> getListaJugadores() {
        return listaJugadores;
    }

    @Override
    public String toString() {
        return "Partida{id=" + id +
               ", juego=" + juego.getNombre() +
               ", fecha=" + fecha +
               ", estado=" + estadoActual + "}";
    }

}
