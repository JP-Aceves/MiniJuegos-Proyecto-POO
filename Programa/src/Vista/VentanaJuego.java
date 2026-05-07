package Vista;
import Controlador.GestorPartidas;
import Controlador.GestorEstadisticas;
import javax.swing.*;

public abstract class VentanaJuego extends JFrame {
    protected GestorPartidas gestorPartidas;
    protected GestorEstadisticas gestorEstadisticas;
    protected JFrame ventanaPadre;

    public VentanaJuego(JFrame ventanaPadre, GestorPartidas gestorPartidas, GestorEstadisticas gestorEstadisticas) {
        this.ventanaPadre = ventanaPadre;
        this.gestorPartidas = gestorPartidas;
        this.gestorEstadisticas = gestorEstadisticas;
    }

    public abstract void actualizarVista();

    protected void accionPausar() {
        gestorPartidas.pausarPartida();
        JOptionPane.showMessageDialog(this, "Partida pausada y guardada.");
        dispose();
        ventanaPadre.setVisible(true);
    }
    
    protected void accionFinalizar() {
        String fecha = obtenerFechaHoy();
        gestorEstadisticas.registrarResultado(gestorPartidas.getPartidaActual(), fecha);
        gestorPartidas.finalizarPartida();
        dispose();
        ventanaPadre.setVisible(true);
    }
    
    protected String obtenerFechaHoy() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.DAY_OF_MONTH) + "/" +
               (cal.get(java.util.Calendar.MONTH) + 1) + "/" +
               cal.get(java.util.Calendar.YEAR);
    }

}


