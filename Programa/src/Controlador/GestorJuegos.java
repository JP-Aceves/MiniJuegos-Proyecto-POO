package controlador;
 
import modelo.Juego;
import modelo.Pasapalabra;
import modelo.TresEnRaya;
 
import java.util.ArrayList;
 
/**
 * GestorJuegos — capa controlador.
 * Métodos y atributos según el diagrama UML V4.
 *
 * Autor: Nacho
 */
public class GestorJuegos {
 
    // ------------------------------------------------------------------ //
    //  Atributos                                                           //
    // ------------------------------------------------------------------ //
 
    /** Catálogo de juegos registrados en el sistema. */
    private ArrayList<Juego> catalogoJuegos;
 
    // ------------------------------------------------------------------ //
    //  Constructor                                                         //
    // ------------------------------------------------------------------ //
 
    public GestorJuegos() {
        catalogoJuegos = new ArrayList<>();
    }
 
    // ------------------------------------------------------------------ //
    //  Métodos públicos (exactos del UML)                                  //
    // ------------------------------------------------------------------ //
 
    /**
     * Registra un juego en el catálogo del sistema.
     *
     * @param j instancia de Juego a registrar
     */
    public void registrarJuego(Juego j) {
        if (j != null && !catalogoJuegos.contains(j)) {
            catalogoJuegos.add(j);
        }
    }
 
    /**
     * Crea e inicializa una nueva instancia del juego indicado por nombre.
     *
     * @param n nombre del juego ("Pasapalabra", "TresEnRaya", …)
     * @return instancia inicializada de Juego, o null si el nombre no existe
     */
    public Juego crearNuevoJuego(String n) {
        Juego juego = null;
 
        switch (n) {
            case "Pasapalabra":
                juego = new Pasapalabra();
                break;
            case "TresEnRaya":
                juego = new TresEnRaya();
                break;
            default:
                System.err.println("GestorJuegos: juego desconocido -> " + n);
                return null;
        }
 
        juego.inicializar();
        return juego;
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
 