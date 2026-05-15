package Controlador;

import Vista.Aplicacion;

/**
 * Punto de entrada alternativo. Delega en {@link Aplicacion#main(String[])}
 * que inicializa todos los gestores y arranca la GUI.
 */
public class Sistema {
    public static void main(String[] args) {
        Aplicacion.main(args);
    }
}
