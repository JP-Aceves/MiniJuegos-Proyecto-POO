package Vista;

import Controlador.GestorUsuarios;
import Modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana de inicio de sesión y registro de la aplicación MiniJuegos.
 *
 * <p>Presenta un formulario con dos modos intercambiables:</p>
 * <ul>
 *   <li><b>Login:</b> el usuario introduce sus credenciales para acceder.</li>
 *   <li><b>Registro:</b> el usuario crea una cuenta nueva.</li>
 * </ul>
 *
 * <p>La lógica de autenticación y validación se delega completamente en
 * {@link GestorUsuarios}; esta clase solo gestiona la presentación y
 * recoge la interacción del usuario.</p>
 *
 * <p>Al completarse correctamente cualquiera de las dos acciones, la ventana
 * se cierra y abre {@code VentanaMenuPrincipal}.</p>
 *
 * @author Adrián
 * @version 1.0
 */
public class VentanaLogin extends JFrame {

    // ── Controlador ──────────────────────────────────────────────────────────

    /** Gestor que centraliza la lógica de autenticación y registro de usuarios. */
    private final GestorUsuarios gestorUsuarios;

    // ── Componentes ───────────────────────────────────────────────────────────

    /** Etiqueta con el nombre de la aplicación. */
    private JLabel  lblTitulo;

    /** Subtítulo dinámico que describe el modo activo ("Inicia sesión" / "Crea tu cuenta"). */
    private JLabel  lblSubtitulo;

    /** Etiqueta del campo de nombre de usuario. */
    private JLabel     lblUsername;

    /** Campo de texto para introducir el nombre de usuario. */
    private JTextField txtUsername;

    /** Etiqueta del campo de contraseña. */
    private JLabel         lblPassword;

    /** Campo enmascarado para introducir la contraseña. */
    private JPasswordField txtPassword;

    /** Botón principal; su texto cambia según el modo activo ("Iniciar sesión" o "Registrarse"). */
    private JButton btnAccion;

    /** Enlace/botón secundario que permite alternar entre los modos login y registro. */
    private JButton btnCambiarModo;

    /** Etiqueta que muestra mensajes de error de validación o autenticación. */
    private JLabel  lblError;

    // ── Estado ────────────────────────────────────────────────────────────────

    /**
     * Indica el modo activo de la ventana.
     * {@code true} = login (por defecto); {@code false} = registro.
     */
    private boolean modoLogin = true;

    // ── Colores / fuentes (paleta coherente con el resto de la app) ───────────

    /** Color de fondo de la ventana. */
    private static final Color COLOR_FONDO    = new Color(18, 18, 28);

    /** Color de fondo de la tarjeta central. */
    private static final Color COLOR_PANEL    = new Color(28, 28, 42);

    /** Color de acento principal (azul claro): botones, bordes activos, cursor. */
    private static final Color COLOR_ACENTO   = new Color(99, 179, 237);

    /** Color del texto principal. */
    private static final Color COLOR_TEXTO    = new Color(230, 230, 240);

    /** Color del texto secundario (subtítulo, placeholders). */
    private static final Color COLOR_SUBTEXTO = new Color(140, 140, 160);

    /** Color de los mensajes de error. */
    private static final Color COLOR_ERROR    = new Color(252, 129, 129);

    /** Color de fondo de los campos de texto. */
    private static final Color COLOR_CAMPO    = new Color(38, 38, 58);

    /** Color de los bordes de los campos en reposo. */
    private static final Color COLOR_BORDE    = new Color(60, 60, 85);

    /** Fuente del título principal de la ventana. */
    private static final Font FUENTE_TITULO   = new Font("Segoe UI", Font.BOLD,  26);

    /** Fuente de etiquetas y mensajes. */
    private static final Font FUENTE_LABEL    = new Font("Segoe UI", Font.PLAIN, 13);

    /** Fuente del texto introducido en los campos de formulario. */
    private static final Font FUENTE_CAMPO    = new Font("Segoe UI", Font.PLAIN, 14);

    /** Fuente del botón de acción principal. */
    private static final Font FUENTE_BOTON    = new Font("Segoe UI", Font.BOLD,  14);

    /** Fuente del enlace para cambiar de modo. */
    private static final Font FUENTE_ENLACE   = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Crea la ventana de login/registro e inicializa todos los componentes Swing.
     *
     * <p>Recibe el gestor de usuarios por inyección para mantener el desacoplamiento
     * entre la vista y la capa de control.</p>
     *
     * @param gestorUsuarios instancia de {@link GestorUsuarios} que gestiona
     *                       autenticación y registro
     */
    public VentanaLogin(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        construirUI();
        actualizarModo();
    }

    // ── Construcción de la interfaz ────────────────────────────────────────────

    /**
     * Construye y ensambla todos los componentes Swing de la ventana.
     *
     * <p>Crea el panel raíz con fondo oscuro, la tarjeta central con los campos
     * de formulario y los botones, y registra los listeners de acción y foco.
     * Debe llamarse una sola vez desde el constructor.</p>
     */
    private void construirUI() {
        setTitle("MiniJuegos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel raíz con fondo oscuro
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(COLOR_FONDO);
        root.setBorder(new EmptyBorder(40, 40, 40, 40));
        setContentPane(root);

        // Tarjeta central
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                new EmptyBorder(36, 40, 36, 40)
        ));
        card.setPreferredSize(new Dimension(380, 420));

        // Título
        lblTitulo = new JLabel("MINIJUEGOS");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_ACENTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo dinámico
        lblSubtitulo = new JLabel();
        lblSubtitulo.setFont(FUENTE_LABEL);
        lblSubtitulo.setForeground(COLOR_SUBTEXTO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo username
        lblUsername = new JLabel("Usuario");
        lblUsername.setFont(FUENTE_LABEL);
        lblUsername.setForeground(COLOR_TEXTO);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = crearCampoTexto();

        // Campo password
        lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(FUENTE_LABEL);
        lblPassword.setForeground(COLOR_TEXTO);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        estilizarCampo(txtPassword);

        // Mensaje de error
        lblError = new JLabel(" ");
        lblError.setFont(FUENTE_LABEL);
        lblError.setForeground(COLOR_ERROR);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón principal
        btnAccion = new JButton();
        btnAccion.setFont(FUENTE_BOTON);
        btnAccion.setForeground(COLOR_FONDO);
        btnAccion.setBackground(COLOR_ACENTO);
        btnAccion.setFocusPainted(false);
        btnAccion.setBorderPainted(false);
        btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnAccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAccion.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btnAccion.setBackground(COLOR_ACENTO.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                btnAccion.setBackground(COLOR_ACENTO);
            }
        });
        btnAccion.addActionListener(e -> {
            if (modoLogin) accionLogin();
            else           accionRegistro();
        });

        // Enlace para cambiar modo
        btnCambiarModo = new JButton();
        btnCambiarModo.setFont(FUENTE_ENLACE);
        btnCambiarModo.setForeground(COLOR_ACENTO);
        btnCambiarModo.setBackground(new Color(0, 0, 0, 0));
        btnCambiarModo.setBorderPainted(false);
        btnCambiarModo.setFocusPainted(false);
        btnCambiarModo.setContentAreaFilled(false);
        btnCambiarModo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCambiarModo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarModo.addActionListener(e -> cambiarModo());

        // Atajo Enter en password
        txtPassword.addActionListener(e -> {
            if (modoLogin) accionLogin();
            else           accionRegistro();
        });

        // Ensamblar tarjeta
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(lblSubtitulo);
        card.add(Box.createRigidArea(new Dimension(0, 28)));
        card.add(lblUsername);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(txtUsername);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(lblPassword);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(lblError);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(btnAccion);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(btnCambiarModo);

        root.add(card);

        pack();
        setLocationRelativeTo(null);
    }

    // ── Helpers de estilo ─────────────────────────────────────────────────────

    /**
     * Crea un {@link JTextField} ya estilizado con la paleta de colores de la ventana.
     *
     * @return campo de texto listo para añadir al formulario
     */
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    /**
     * Aplica el estilo visual común (colores, fuente, borde, tamaño máximo) a cualquier
     * {@link JTextField} o {@link JPasswordField}, y registra los listeners de foco
     * que iluminan el borde con {@code COLOR_ACENTO} cuando el campo está activo.
     *
     * @param campo campo al que se aplica el estilo; puede ser {@code JTextField}
     *              o cualquier subclase (incluido {@code JPasswordField})
     */
    private void estilizarCampo(JTextField campo) {
        campo.setFont(FUENTE_CAMPO);
        campo.setForeground(COLOR_TEXTO);
        campo.setBackground(COLOR_CAMPO);
        campo.setCaretColor(COLOR_ACENTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Borde iluminado al enfocar
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_ACENTO, 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    // ── accionLogin ───────────────────────────────────────────────────────────

    /**
     * Gestiona el intento de inicio de sesión del usuario.
     *
     * <ol>
     *   <li>Lee y valida que los campos no estén vacíos.</li>
     *   <li>Llama a {@link GestorUsuarios#iniciarSesion(String, String)}.</li>
     *   <li>Si el resultado es un {@link Usuario} válido, limpia el error
     *       y abre {@code VentanaMenuPrincipal}.</li>
     *   <li>Si el resultado es {@code null}, muestra el mensaje de error
     *       y limpia el campo de contraseña.</li>
     * </ol>
     */
    private void accionLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Rellena todos los campos.");
            return;
        }

        Usuario usuario = gestorUsuarios.iniciarSesion(username, password);

        if (usuario != null) {
            limpiarError();
            abrirMenuPrincipal();
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
            txtPassword.setText("");
        }
    }

    // ── accionRegistro ────────────────────────────────────────────────────────

    /**
     * Gestiona el intento de registro de un nuevo usuario.
     *
     * <ol>
     *   <li>Lee y valida que los campos no estén vacíos.</li>
     *   <li>Llama a {@link GestorUsuarios#registrarUsuario(String, String)}.</li>
     *   <li>Si devuelve {@code null} (sin error), hace login automático para
     *       establecer {@code usuarioActual} y abre {@code VentanaMenuPrincipal}.</li>
     *   <li>Si devuelve un {@code String}, lo muestra como mensaje de error.</li>
     * </ol>
     */
    private void accionRegistro() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Rellena todos los campos.");
            return;
        }

        String error = gestorUsuarios.registrarUsuario(username, password);

        if (error == null) {
            // El gestor ya habrá hecho iniciarSesion internamente; si no,
            // lo hacemos aquí para que usuarioActual quede establecido.
            gestorUsuarios.iniciarSesion(username, password);
            limpiarError();
            abrirMenuPrincipal();
        } else {
            mostrarError(error);
        }
    }

    // ── cambiarModo ───────────────────────────────────────────────────────────

    /**
     * Alterna el modo activo de la ventana entre login y registro.
     *
     * <p>Limpia el error visible, vacía ambos campos de texto e invoca
     * {@link #actualizarModo()} para reflejar el nuevo estado en los
     * textos del botón principal y del enlace inferior.</p>
     */
    private void cambiarModo() {
        modoLogin = !modoLogin;
        limpiarError();
        txtUsername.setText("");
        txtPassword.setText("");
        actualizarModo();
    }

    // ── Helpers internos ──────────────────────────────────────────────────────

    /**
     * Actualiza los textos dinámicos de la ventana según el valor de {@link #modoLogin}.
     *
     * <p>Afecta a {@link #lblSubtitulo}, {@link #btnAccion} y {@link #btnCambiarModo}.</p>
     */
    private void actualizarModo() {
        if (modoLogin) {
            lblSubtitulo.setText("Inicia sesión para jugar");
            btnAccion.setText("Iniciar sesión");
            btnCambiarModo.setText("¿No tienes cuenta? Regístrate");
        } else {
            lblSubtitulo.setText("Crea tu cuenta");
            btnAccion.setText("Registrarse");
            btnCambiarModo.setText("¿Ya tienes cuenta? Inicia sesión");
        }
    }

    /**
     * Muestra un mensaje de error en {@link #lblError}.
     *
     * @param msg texto del error a mostrar
     */
    private void mostrarError(String msg) {
        lblError.setText(msg);
    }

    /**
     * Oculta cualquier mensaje de error visible en {@link #lblError}.
     *
     * <p>Establece un espacio en blanco para que la etiqueta conserve
     * su altura y el layout no se desplace.</p>
     */
    private void limpiarError() {
        lblError.setText(" ");
    }

    /**
     * Abre {@code VentanaMenuPrincipal} y cierra esta ventana.
     *
     * <p>Se invoca tras un login o registro exitoso. Pasa {@link #gestorUsuarios}
     * al menú principal; añade aquí los demás gestores cuando estén disponibles.</p>
     */
    private void abrirMenuPrincipal() {
        // Pasar aquí los gestores que necesite VentanaMenuPrincipal
        new VentanaMenuPrincipal(gestorUsuarios).setVisible(true);
        dispose();
    }
}