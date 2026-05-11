package controlador;
 
import modelo.Juego;
import modelo.Pasapalabra;
import modelo.TresEnRaya;
 
import java.util.ArrayList;
 
/**
 * GestorJuegos — capa controlador.
 * Métodos y atributos según el diagrama UML V4.
 *
 * Autor: Ignacio del Peso Dominguez
 * Fecha: 07/05/2026
 * Versión: 1.0
 * Descripción: Clase que gestiona los juegos del sistema.
 * Métodos y atributos según el diagrama UML V4.
 * 
 * 
 * 
 * 
 * 
 */
public class GestorJuegos {
 
    // ------------------------------------------------------------------ //
    //  Atributos                                                           //
    // ------------------------------------------------------------------ //
 
    /** Catálogo de juegos registrados en el sistema. */
    private ArrayList<Juego> catalogoJuegos = new ArrayList<>();
 
    // ------------------------------------------------------------------ //
    //  Métodos públicos (exactos del UML)                                  //
    // ------------------------------------------------------------------ //
 
    /**
     * Registra un juego en el catálogo del sistema si no existe ya.
     *
     * @param nombre nombre del juego ("Pasapalabra", "TresEnRaya", …)
     */
    public void registrarJuego(String nombre) {
        for (Juego j : catalogoJuegos) {
            if (j.getNombre().equals(nombre)) {
                return;
            }
        }
        Juego nuevo = crearJuego(nombre);
        if (nuevo != null) {
            catalogoJuegos.add(nuevo);
        }
    }
 
    /**
     * Crea e inicializa una nueva instancia del juego indicado por nombre.
     *
     * @param juego nombre del juego ("Pasapalabra", "TresEnRaya", …)
     * @return instancia inicializada de Juego, o null si el nombre no existe
     */
    public Juego crearJuego(String juego) {
        Juego j = null;
 
        switch (juego) {
            case "Pasapalabra":
                j = new Pasapalabra();
                break;
            case "TresEnRaya":
                j = new TresEnRaya();
                break;
            default:
                System.err.println("GestorJuegos: juego desconocido -> " + juego);
                return null;
        }
 
        j.inicializar();
        return j;
    }
 
    /**
     * Devuelve los nombres de los juegos disponibles (registrados).
     *
     * @return ArrayList con los nombres de los juegos del catálogo
     */
    public ArrayList<String> getJuegosDisponibles() {
        ArrayList<String> nombres = new ArrayList<>();
        for (Juego j : catalogoJuegos) {
            nombres.add(j.getNombre());
        }
        return nombres;
    }
 
    /**
     * Devuelve el catálogo completo de juegos registrados.
     *
     * @return ArrayList de instancias Juego registradas
     */
    public ArrayList<Juego> getCatalogoJuegos() {
        return new ArrayList<>(catalogoJuegos); // copia defensiva
    }
}