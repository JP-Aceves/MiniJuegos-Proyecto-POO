package Vista;

import Controlador.GestorPartidas;
import Controlador.GestorUsuarios;
import Modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Ventana principal de navegación de la aplicación MiniJuegos.
 *
 * <p>Se muestra inmediatamente después de que el usuario completa
 * el inicio de sesión en {@code VentanaLogin}. Actúa como hub central
 * desde el que el usuario accede a todas las funcionalidades:</p>
 * <ul>
 *   <li>Iniciar una partida nueva.</li>
 *   <li>Reanudar una partida pausada previamente.</li>
 *   <li>Consultar su historial de estadísticas.</li>
 *   <li>Acceder al panel de administración (solo usuarios admin).</li>
 *   <li>Cerrar sesión y volver a {@code VentanaLogin}.</li>
 * </ul>
 *
 * <p>El botón de administración se oculta automáticamente si el usuario
 * no tiene privilegios de administrador, y se realiza una segunda
 * comprobación dentro de {@link #accionAdmin()} como medida de seguridad
 * adicional.</p>
 *
 * @see GestorUsuarios
 * @see GestorPartidas
 *
 * @author Adrián
 * @version 1.0
 */
public class VentanaMenuPrincipal extends JFrame {

    /** Gestor que controla la sesión activa y las operaciones sobre usuarios. */
    private final GestorUsuarios gestorUsuarios;

    /** Gestor que controla el ciclo de vida de las partidas. */
    private final GestorPartidas gestorPartidas;

    /** Usuario que ha iniciado sesión. Se obtiene de {@code gestorUsuarios} al construir la ventana. */
    private final Usuario usuarioActual;

    // ── Componentes ──────────────────────────────────────────────────────────

    /** Etiqueta de bienvenida personalizada con el username del usuario. */
    private JLabel lblBienvenida;

    /** Botón para iniciar una partida nueva. */
    private JButton btnJugar;

    /** Botón para cargar y reanudar una partida pausada. */
    private JButton btnCargarPartida;

    /** Botón para abrir la ventana de estadísticas del usuario. */
    private JButton btnEstadisticas;

    /**
     * Botón para abrir el panel de administración.
     * Solo visible si {@code gestorUsuarios.esAdministrador()} devuelve {@code true}.
     */
    private JButton btnAdmin;

    /** Botón para cerrar la sesión actual y volver a {@code VentanaLogin}. */
    private JButton btnCerrarSesion;

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Construye la ventana del menú principal para el usuario que acaba de iniciar sesión.
     *
     * <p>Obtiene el usuario actual a través de {@link GestorUsuarios#getUsuarioActual()}
     * y delega la construcción de la interfaz gráfica en {@link #initUI()}.</p>
     *
     * @param gestorUsuarios gestor de usuarios con sesión activa; no debe ser {@code null}
     *                       y {@code getUsuarioActual()} debe devolver un usuario válido.
     * @param gestorPartidas gestor de partidas utilizado para listar y reanudar partidas pausadas.
     */
    public VentanaMenuPrincipal(GestorUsuarios gestorUsuarios,
                                GestorPartidas gestorPartidas) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorPartidas = gestorPartidas;
        this.usuarioActual   = gestorUsuarios.getUsuarioActual();

        initUI();
    }

    // ── Construcción de la interfaz ───────────────────────────────────────────

    /**
     * Inicializa y monta todos los componentes gráficos de la ventana.
     *
     * <p>Crea el panel principal con disposición vertical ({@code BoxLayout}),
     * instancia los botones llamando a {@link #crearBoton(String)}, registra
     * los listeners de cada acción y añade los componentes al panel en orden.</p>
     *
     * <p>Este método se invoca una única vez desde el constructor.</p>
     */
    private void initUI() {
        setTitle("MiniJuegos — Menú principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Bienvenida
        lblBienvenida = new JLabel("Bienvenido/a, " + usuarioActual.getUsername());
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones
        btnJugar         = crearBoton("🎮  Jugar");
        btnCargarPartida = crearBoton("💾  Cargar partida guardada");
        btnEstadisticas  = crearBoton("📊  Mis estadísticas");
        btnAdmin         = crearBoton("⚙️  Panel de administración");
        btnCerrarSesion  = crearBoton("🚪  Cerrar sesión");

        // El botón de admin solo se muestra si el usuario es administrador
        btnAdmin.setVisible(gestorUsuarios.esAdministrador());

        // Listeners
        btnJugar.addActionListener(e         -> accionJugar());
        btnCargarPartida.addActionListener(e  -> accionCargarPartida());
        btnEstadisticas.addActionListener(e   -> accionEstadisticas());
        btnAdmin.addActionListener(e          -> accionAdmin());
        btnCerrarSesion.addActionListener(e   -> accionCerrarSesion());

        // Montaje
        panel.add(lblBienvenida);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(btnJugar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCargarPartida);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnEstadisticas);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnAdmin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCerrarSesion);

        add(panel);
    }

    /**
     * Factoría interna que crea un {@code JButton} con el estilo visual
     * uniforme que comparten todos los botones del menú.
     *
     * <p>Características aplicadas:</p>
     * <ul>
     *   <li>Alineación horizontal centrada dentro del {@code BoxLayout}.</li>
     *   <li>Anchura máxima ilimitada para que ocupe todo el ancho disponible.</li>
     *   <li>Altura fija de 40 px.</li>
     *   <li>Sin borde de foco pintado ({@code setFocusPainted(false)}).</li>
     * </ul>
     *
     * @param texto etiqueta que se mostrará en el botón.
     * @return botón configurado listo para añadir al panel.
     */
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        return btn;
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    // ── Acciones ─────────────────────────────────────────────────────────────

    /**
     * Inicia el flujo para comenzar una partida nueva.
     *
     * <p>Flujo previsto (pendiente de implementar):</p>
     * <ol>
     *   <li>Abrir {@code VentanaSeleccionJuego} para que el usuario elija el juego.</li>
     *   <li>Obtener el nombre del juego seleccionado.</li>
     *   <li>Delegar en {@code GestorJuegos.crearJuego(nombre)} para instanciar el juego.</li>
     *   <li>Llamar a {@link GestorPartidas#iniciarPartida} con el juego y el usuario actual.</li>
     *   <li>Abrir la ventana de juego correspondiente y cerrar esta ventana.</li>
     * </ol>
     *
     * <p><b>TODO:</b> instanciar {@code VentanaSeleccionJuego} e inyectar
     * {@code GestorJuegos} cuando estén disponibles.</p>
     */
    private void accionJugar() {
        // Pendiente: VentanaSeleccionJuego aún no existe.
        // Flujo previsto:
        //   VentanaSeleccionJuego seleccion = new VentanaSeleccionJuego(gestorJuegos);
        //   seleccion.setVisible(true);
        //   String nombreJuego = seleccion.getJuegoSeleccionado();
        //   if (nombreJuego == null) return;
        //   Juego juego = gestorJuegos.crearJuego(nombreJuego);
        //   List<Usuario> jugadores = List.of(usuarioActual);
        //   gestorPartidas.iniciarPartida(juego, jugadores);
        //   abrirVentanaJuego(nombreJuego);
        //   dispose();
        JOptionPane.showMessageDialog(this,
                "Selección de juego en desarrollo.",
                "Próximamente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra las partidas pausadas disponibles y permite reanudar la elegida.
     *
     * <p>Obtiene la lista de identificadores de partidas pausadas a través de
     * {@link GestorPartidas#listarPartidasPausadas()}. Si no hay ninguna,
     * informa al usuario mediante un diálogo. En caso contrario, muestra un
     * selector con los identificadores disponibles.</p>
     *
     * <p>Flujo previsto tras la selección (pendiente de implementar):</p>
     * <ol>
     *   <li>Cargar el estado serializado con {@code GestorPartidas.cargarDatosPartida(id)}.</li>
     *   <li>Reanudar la partida con {@code GestorPartidas.reanudarPartida(id, estado)}.</li>
     *   <li>Abrir la ventana de juego correspondiente y cerrar esta ventana.</li>
     * </ol>
     *
     * <p><b>TODO:</b> completar el flujo de reanudación cuando
     * {@code reanudarPartida} esté implementado en {@code GestorPartidas}.</p>
     */
    private void accionCargarPartida() {
        ArrayList<Integer> ids = gestorPartidas.listarPartidasPausadas();

        if (ids == null || ids.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No tienes partidas guardadas.",
                    "Sin partidas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Integer[] opciones = ids.toArray(new Integer[0]);
        Integer seleccionada = (Integer) JOptionPane.showInputDialog(
                this,
                "Selecciona una partida para reanudar:",
                "Cargar partida",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccionada == null) return; // usuario canceló

        // Flujo previsto:
        //   String estado = gestorPartidas.cargarDatosPartida(seleccionada);
        //   gestorPartidas.reanudarPartida(seleccionada, estado);
        //   abrirVentanaJuego(gestorPartidas.getPartidaActual().getJuego().getNombre());
        //   dispose();
        JOptionPane.showMessageDialog(this,
                "Reanudación de partida en desarrollo.\nID: " + seleccionada,
                "Próximamente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre la ventana de estadísticas del usuario actual.
     *
     * <p>Flujo previsto (pendiente de implementar):</p>
     * <ol>
     *   <li>Instanciar {@code VentanaEstadisticas} pasándole {@code GestorEstadisticas}
     *       y el username del usuario actual.</li>
     *   <li>Mostrar la ventana.</li>
     * </ol>
     *
     * <p><b>TODO:</b> instanciar {@code VentanaEstadisticas} e inyectar
     * {@code GestorEstadisticas} cuando estén disponibles.</p>
     */
    private void accionEstadisticas() {
        // Pendiente: VentanaEstadisticas aún no existe.
        // Flujo previsto:
        //   VentanaEstadisticas v = new VentanaEstadisticas(gestorEstadisticas, usuarioActual.getUsername());
        //   v.setVisible(true);
        JOptionPane.showMessageDialog(this,
                "Estadísticas en desarrollo.",
                "Próximamente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre el panel de administración si el usuario tiene privilegios de administrador.
     *
     * <p>Aunque el botón que invoca esta acción ya está oculto para usuarios
     * sin privilegios (ver {@link #initUI()}), se realiza una segunda comprobación
     * con {@link GestorUsuarios#esAdministrador()} antes de abrir la ventana,
     * como capa de seguridad adicional frente a posibles accesos indebidos.</p>
     *
     * <p>Flujo previsto (pendiente de implementar):</p>
     * <ol>
     *   <li>Instanciar {@code VentanaAdmin} pasándole {@code GestorUsuarios}
     *       y {@code GestorEstadisticas}.</li>
     *   <li>Mostrar la ventana.</li>
     * </ol>
     *
     * <p><b>TODO:</b> instanciar {@code VentanaAdmin} e inyectar
     * {@code GestorEstadisticas} cuando estén disponibles.</p>
     */
    private void accionAdmin() {
        if (!gestorUsuarios.esAdministrador()) {
            JOptionPane.showMessageDialog(this,
                    "Acceso denegado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Pendiente: VentanaAdmin aún no existe.
        // Flujo previsto:
        //   VentanaAdmin v = new VentanaAdmin(gestorUsuarios, gestorEstadisticas);
        //   v.setVisible(true);
        JOptionPane.showMessageDialog(this,
                "Panel de administración en desarrollo.",
                "Próximamente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cierra la sesión del usuario actual y regresa a la pantalla de login.
     *
     * <p>Llama a {@link GestorUsuarios#cerrarSesion()} para limpiar el estado
     * de sesión, abre una nueva instancia de {@code VentanaLogin} y destruye
     * esta ventana con {@code dispose()}.</p>
     */
    private void accionCerrarSesion() {
        gestorUsuarios.cerrarSesion();
        new VentanaLogin(gestorUsuarios).setVisible(true);
        dispose();
    }
}