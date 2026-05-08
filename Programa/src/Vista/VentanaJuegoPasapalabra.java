package Vista;

import Controlador.GestorPartidas;
import Modelo.Partida;
import Modelo.PasaPalabra;
import Modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana gráfica para el juego PasaPalabra.
 * Por ahora extiende JFrame directamente porque VentanaJuego aún está vacía.
 * Cuando VentanaJuego tenga su estructura definitiva, se cambia la línea de extends.
 *
 * @author Adrián
 * @version 1.0
 */
public class VentanaJuegoPasapalabra extends JFrame {

    // ── Colores del rosco ────────────────────────────────────────────────────
    private static final Color COLOR_PENDIENTE   = new Color(60, 60, 80);
    private static final Color COLOR_CORRECTA    = new Color(34, 197, 94);
    private static final Color COLOR_INCORRECTA  = new Color(239, 68, 68);
    private static final Color COLOR_PASAPALABRA = new Color(59, 130, 246);
    private static final Color COLOR_ACTUAL      = new Color(250, 204, 21);
    private static final Color COLOR_FONDO       = new Color(15, 15, 25);
    private static final Color COLOR_TEXTO       = new Color(240, 240, 245);

    // ── Referencias ─────────────────────────────────────────────────────────
    private final PasaPalabra    juego;
    private final Usuario        jugador;
    private final Partida        partida;
    private final GestorPartidas gestorPartidas;

    // ── Componentes Swing ────────────────────────────────────────────────────
    private PanelRosco panelRosco;
    private JLabel     lblDefinicion;
    private JLabel     lblLetra;
    private JLabel     lblAciertos;
    private JLabel     lblFallos;
    private JLabel     lblPasapalabras;
    private JLabel     lblTiempo;
    private JTextField txtRespuesta;
    private JButton    btnResponder;
    private JButton    btnPasapalabra;
    private JButton    btnPausar;

    // ── Temporizador ─────────────────────────────────────────────────────────
    private Timer timerSwing;
    private int   segundosRestantes;
    private static final int TIEMPO_INICIAL = 150; // 2 min 30 seg

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * @param partida        Partida activa (contiene el juego y los jugadores).
     * @param gestorPartidas Para pausar y finalizar.
     */
    public VentanaJuegoPasapalabra(Partida partida, GestorPartidas gestorPartidas) {
        this.partida         = partida;
        this.juego           = (PasaPalabra) partida.getJuego();
        this.jugador         = partida.getJugadorActual();
        this.gestorPartidas  = gestorPartidas;
        this.segundosRestantes = TIEMPO_INICIAL;

        construirUI();
        actualizarVista();
        iniciarTimer();

        setTitle("PasaPalabra - " + jugador.getUsername());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { accionPausar(); }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Construcción de la interfaz ──────────────────────────────────────────

    private void construirUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        panelRosco = new PanelRosco();
        panelRosco.setPreferredSize(new Dimension(500, 500));
        panelRosco.setBackground(COLOR_FONDO);

        add(panelRosco,              BorderLayout.CENTER);
        add(construirPanelDerecho(), BorderLayout.EAST);
        add(construirPanelControl(), BorderLayout.SOUTH);
    }

    private JPanel construirPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        panel.setPreferredSize(new Dimension(300, 500));

        // Temporizador
        lblTiempo = crearLabel("2:30", 36, Font.BOLD, new Color(250, 204, 21));
        lblTiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTiempo);
        panel.add(Box.createVerticalStrut(16));

        // Letra actual
        lblLetra = crearLabel("A", 48, Font.BOLD, COLOR_ACTUAL);
        lblLetra.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblLetra);
        panel.add(Box.createVerticalStrut(10));

        // Definición
        lblDefinicion = new JLabel("<html><div style='text-align:center;width:260px'>...</div></html>");
        lblDefinicion.setForeground(COLOR_TEXTO);
        lblDefinicion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDefinicion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblDefinicion);
        panel.add(Box.createVerticalStrut(20));

        // Campo de respuesta (Enter también responde)
        txtRespuesta = new JTextField();
        txtRespuesta.setMaximumSize(new Dimension(260, 35));
        txtRespuesta.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtRespuesta.setBackground(new Color(30, 30, 45));
        txtRespuesta.setForeground(COLOR_TEXTO);
        txtRespuesta.setCaretColor(COLOR_TEXTO);
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        txtRespuesta.addActionListener(e -> onResponder());
        panel.add(txtRespuesta);
        panel.add(Box.createVerticalStrut(10));

        // Botones de juego
        btnResponder   = crearBoton("Responder",   new Color(34, 197, 94),  e -> onResponder());
        btnPasapalabra = crearBoton("PasaPalabra", new Color(59, 130, 246), e -> onPasapalabra());
        btnResponder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPasapalabra.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnResponder);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnPasapalabra);
        panel.add(Box.createVerticalStrut(20));

        // Contadores
        lblAciertos     = crearLabel("Aciertos: 0",     13, Font.PLAIN, COLOR_CORRECTA);
        lblFallos       = crearLabel("Fallos: 0",       13, Font.PLAIN, COLOR_INCORRECTA);
        lblPasapalabras = crearLabel("Pasapalabras: 0", 13, Font.PLAIN, COLOR_PASAPALABRA);
        lblAciertos.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFallos.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPasapalabras.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAciertos);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblFallos);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblPasapalabras);

        return panel;
    }

    private JPanel construirPanelControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(COLOR_FONDO);
        btnPausar = crearBoton("Pausar partida", new Color(107, 114, 128), e -> accionPausar());
        panel.add(btnPausar);
        return panel;
    }

    // ── Actualización de la vista ────────────────────────────────────────────

    /**
     * Refresca todos los componentes con el estado actual del juego.
     * Llamado tras cada respuesta, pasapalabra y tick del timer.
     */
    public void actualizarVista() {
        if (juego.isTerminado()) {
            accionFinalizar();
            return;
        }

        int idx = juego.getLetraActual();
        String[] datos = juego.getDatosLetra(idx);
        if (datos != null) {
            lblLetra.setText(datos[0]);
            lblDefinicion.setText(
                    "<html><div style='text-align:center;width:260px'>" + datos[1] + "</div></html>"
            );
        }

        lblAciertos.setText("Aciertos: "     + juego.getAciertos());
        lblFallos.setText("Fallos: "         + juego.getFallos());
        lblPasapalabras.setText("Pasapalabras: " + juego.getPasaPalabras());

        panelRosco.repaint();
        txtRespuesta.setText("");
        txtRespuesta.requestFocusInWindow();
    }

    // ── Acciones de juego ────────────────────────────────────────────────────

    private void onResponder() {
        String respuesta = txtRespuesta.getText().trim();
        if (respuesta.isEmpty()) return;
        boolean correcta = juego.procesarRespuesta(respuesta);
        Color color = correcta ? COLOR_CORRECTA : COLOR_INCORRECTA;
        String msg  = correcta ? "Correcto!" : "Incorrecto";
        flashMensaje(msg, color);
        actualizarVista();
    }

    private void onPasapalabra() {
        juego.pasarPalabra();
        flashMensaje("PasaPalabra", COLOR_PASAPALABRA);
        actualizarVista();
    }

    // ── Pausar y finalizar ───────────────────────────────────────────────────

    public void accionPausar() {
        detenerTimer();
        gestorPartidas.pausarPartida();
        dispose();
        // TODO: abrir VentanaMenuPrincipal cuando esté disponible
    }

    public void accionFinalizar() {
        detenerTimer();
        gestorPartidas.finalizarPartida();
        // TODO: llamar a GestorEstadisticas.registrarResultado() cuando esté disponible

        String resumen = String.format(
                "<html><center><h2>Partida terminada</h2>" +
                        "<p>Aciertos: <b>%d</b></p>" +
                        "<p>Fallos: <b>%d</b></p>" +
                        "<p>Pasapalabras: <b>%d</b></p>" +
                        "<p>Puntuacion: <b>%d</b></p></center></html>",
                juego.getAciertos(),
                juego.getFallos(),
                juego.getPasaPalabras(),
                juego.getPuntuacion(jugador.getUsername())
        );

        JOptionPane.showMessageDialog(this, resumen, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        // TODO: abrir VentanaMenuPrincipal cuando esté disponible
    }

    // ── Temporizador ─────────────────────────────────────────────────────────

    private void iniciarTimer() {
        timerSwing = new Timer(1000, e -> {
            segundosRestantes--;
            mostrarTiempo(segundosRestantes);
            if (segundosRestantes <= 0) {
                detenerTimer();
                JOptionPane.showMessageDialog(this,
                        "Se acabo el tiempo!", "Tiempo", JOptionPane.WARNING_MESSAGE);
                accionFinalizar();
            }
        });
        timerSwing.start();
    }

    private void detenerTimer() {
        if (timerSwing != null && timerSwing.isRunning()) timerSwing.stop();
    }

    private void mostrarTiempo(int segundos) {
        lblTiempo.setText(String.format("%d:%02d", segundos / 60, segundos % 60));
        lblTiempo.setForeground(segundos <= 30 ? COLOR_INCORRECTA : new Color(250, 204, 21));
    }

    // ── Helpers de UI ────────────────────────────────────────────────────────

    private JLabel crearLabel(String texto, int size, int style, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    private JButton crearBoton(String texto, Color bgColor, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        btn.setMaximumSize(new Dimension(240, 40));
        return btn;
    }

    /**
     * Muestra un mensaje de color breve en lblDefinicion durante 700ms.
     * No bloquea el EDT.
     */
    private void flashMensaje(String mensaje, Color color) {
        lblDefinicion.setText("<html><div style='text-align:center'>"
                + "<font color='#" + String.format("%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue())
                + "'><b>" + mensaje + "</b></font></div></html>");
        Timer t = new Timer(700, e -> {
            // actualizarVista() ya sobreescribirá lblDefinicion después
        });
        t.setRepeats(false);
        t.start();
    }

    // ── Panel del rosco ──────────────────────────────────────────────────────

    /**
     * Componente personalizado que dibuja el rosco circular.
     * Se repinta automáticamente cada vez que se llama a actualizarVista().
     */
    private class PanelRosco extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (juego == null) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int cx          = getWidth()  / 2;
            int cy          = getHeight() / 2;
            int radio       = Math.min(getWidth(), getHeight()) / 2 - 40;
            int totalLetras = juego.getTotalLetras();
            int actual      = juego.getLetraActual();

            for (int i = 0; i < totalLetras; i++) {
                String[] datos = juego.getDatosLetra(i);
                if (datos == null) continue;

                double angulo = (2 * Math.PI * i / totalLetras) - Math.PI / 2;
                int x = (int) (cx + radio * Math.cos(angulo));
                int y = (int) (cy + radio * Math.sin(angulo));

                // Color según estado
                Color fondo;
                if (i == actual) {
                    fondo = COLOR_ACTUAL;
                } else {
                    switch (datos[3]) {
                        case PasaPalabra.ESTADO_CORRECTA:    fondo = COLOR_CORRECTA;    break;
                        case PasaPalabra.ESTADO_INCORRECTA:  fondo = COLOR_INCORRECTA;  break;
                        case PasaPalabra.ESTADO_PASAPALABRA: fondo = COLOR_PASAPALABRA; break;
                        default:                             fondo = COLOR_PENDIENTE;
                    }
                }

                int r = 20;
                g2.setColor(fondo);
                g2.fillOval(x - r, y - r, r * 2, r * 2);

                if (i == actual) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawOval(x - r, y - r, r * 2, r * 2);
                }

                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.setColor(i == actual ? COLOR_FONDO : Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                String letra = datos[0];
                g2.drawString(letra, x - fm.stringWidth(letra) / 2, y + fm.getAscent() / 2 - 2);
            }

            g2.dispose();
        }
    }
}