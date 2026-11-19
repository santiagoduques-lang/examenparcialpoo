/*
 * Gremio de Aventureros - Wizard of the Coast
 * Vista principal construida con el disenador de formularios de NetBeans (JFrame Form).
 * Arquitectura MVC: esta clase solo presenta informacion y delega los eventos al Controlador.
 */
package Vista;

import Controlador.ControladorGremio;
import Modelo.Aventurero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;

/**
 * Ventana principal del gremio.
 *
 * <p>Formulario disenado con el editor visual de NetBeans: la estructura,
 * los colores, las tipografias, los bordes y los margenes viven en
 * {@code VentanaGremio.form} y se editan desde la pestana <b>Design</b>.
 * Por eso {@code initComponents()} solo contiene codigo generado dentro de
 * los bloques protegidos ({@code //GEN-BEGIN} / {@code //GEN-END}).</p>
 *
 * <p>La clase <b>no</b> contiene reglas de negocio ni decoracion escrita a
 * mano: cada evento ({@code ActionEvent}) se limita a delegar en
 * {@link ControladorGremio}. El codigo propio unicamente conecta los modelos
 * de datos (tabla, combo, filtros) y los renderizadores que el Look and Feel
 * del sistema sobrescribe.</p>
 *
 * @author Santiago Duque Serrano
 */
public class VentanaGremio extends javax.swing.JFrame {

    // ------------------------------------------------------------------
    // Colores de estado (mensajes de exito / error)
    // ------------------------------------------------------------------
    private static final Color COLOR_ERROR = new Color(0xB23A2E);
    private static final Color COLOR_OK = new Color(0x2E7D5B);

    /** Clases disponibles en el simulador. */
    private static final String[] CLASES = {
        "Guerrero", "Mago", "Clérigo", "Monje", "Paladín", "Explorador", "Pícaro", "Bardo"
    };

    private ControladorGremio controlador;
    private ModeloTablaAventurero modeloTabla;
    private Border bordeCampoNormal;
    private Border bordeCampoError;

    /** Crea la ventana principal del gremio. */
    public VentanaGremio() {
        initComponents();
        inicializarComponentesDinamicos();
        setMinimumSize(new Dimension(1180, 760));
        setSize(1280, 840);
        setLocationRelativeTo(null);
    }

    /**
     * Inyecta el controlador. La Vista nunca decide que hacer con los datos:
     * solo notifica los eventos al Controlador (patron MVC).
     *
     * @param controlador controlador del gremio.
     */
    public void setControlador(ControladorGremio controlador) {
        this.controlador = controlador;
    }

    // ------------------------------------------------------------------
    // Modelos de datos y detalles que el disenador no puede serializar
    // ------------------------------------------------------------------

    /**
     * Conecta los modelos de la tabla y del combo, activa las validaciones de
     * entrada y completa los detalles que dependen del Look and Feel. Se
     * mantiene fuera del codigo generado para que NetBeans no lo sobrescriba.
     */
    private void inicializarComponentesDinamicos() {
        cboClase.setModel(new javax.swing.DefaultComboBoxModel<>(CLASES));

        modeloTabla = new ModeloTablaAventurero();
        tablaAventureros.setModel(modeloTabla);

        // Los bordes normales vienen del disenador; el de error se deriva de ellos.
        bordeCampoNormal = txtNombre.getBorder();
        bordeCampoError = BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ERROR, 2, true), new EmptyBorder(5, 9, 5, 9));

        // Validacion en tiempo real: solo digitos en los campos numericos.
        ((AbstractDocument) txtNivel.getDocument()).setDocumentFilter(new FiltroSoloNumeros(2));
        ((AbstractDocument) txtPuntosVida.getDocument()).setDocumentFilter(new FiltroSoloNumeros(3));

        configurarEncabezadoTabla();
        configurarColumnas();
        configurarFilas();

        getRootPane().setDefaultButton(btnRegistrar);

        lblImagenPersonaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagenPersonaje.setIcon(Imagenes.clase(getClaseSeleccionada()));

        // Deslizador vertical para que no se pierda informacion cuando el
        // formulario crece (imagen, dado, combate avanzado, etc.).
        javax.swing.JScrollPane scrPrincipal = new javax.swing.JScrollPane(pnlRaiz);
        scrPrincipal.setBorder(null);
        scrPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(scrPrincipal, java.awt.BorderLayout.CENTER);

        actualizarEtiquetaAtributo();
    }

    /**
     * El Look and Feel del sistema pinta su propio encabezado e ignora los
     * colores del disenador. Se instala un renderizador que reutiliza los
     * colores y la tipografia definidos en el formulario.
     */
    private void configurarEncabezadoTabla() {
        final Color fondo = pnlEncabezado.getBackground();
        final Color texto = lblTitulo.getForeground();
        final Font fuente = tablaAventureros.getFont().deriveFont(Font.BOLD);

        javax.swing.table.JTableHeader encabezado = tablaAventureros.getTableHeader();
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(0, 34));
        encabezado.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable tabla, Object valor,
                    boolean seleccionada, boolean enfocada, int fila, int columna) {
                javax.swing.JLabel etiqueta = (javax.swing.JLabel) super.getTableCellRendererComponent(
                        tabla, valor, seleccionada, enfocada, fila, columna);
                etiqueta.setText(valor == null ? "" : valor.toString());
                etiqueta.setOpaque(true);
                etiqueta.setBackground(fondo);
                etiqueta.setForeground(texto);
                etiqueta.setFont(fuente);
                etiqueta.setBorder(new EmptyBorder(0, 10, 0, 10));
                etiqueta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                return etiqueta;
            }
        });
    }

    /** Ajusta los anchos iniciales de las columnas del modelo. */
    private void configurarColumnas() {
        int[] anchos = {90, 150, 60, 95, 260, 90};
        for (int i = 0; i < anchos.length && i < tablaAventureros.getColumnModel().getColumnCount(); i++) {
            tablaAventureros.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    /**
     * Aplica el rayado de filas y la alineacion de las columnas numericas.
     * Los colores se toman del disenador (fondo de la tabla y del panel raiz).
     */
    private void configurarFilas() {
        final Color fondoPar = tablaAventureros.getBackground();
        final Color fondoImpar = pnlRaiz.getBackground();
        final Color texto = tablaAventureros.getForeground();

        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable tabla, Object valor,
                    boolean seleccionada, boolean enfocada, int fila, int columna) {
                java.awt.Component celda = super.getTableCellRendererComponent(
                        tabla, valor, seleccionada, enfocada, fila, columna);
                setHorizontalAlignment(columna == 2 || columna == 3 || columna == 5
                        ? javax.swing.SwingConstants.CENTER : javax.swing.SwingConstants.LEFT);
                if (!seleccionada) {
                    celda.setBackground(fila % 2 == 0 ? fondoPar : fondoImpar);
                    celda.setForeground(texto);
                }
                setBorder(noFocusBorder);
                return celda;
            }
        };
        tablaAventureros.setDefaultRenderer(Object.class, renderizador);
        tablaAventureros.setDefaultRenderer(Integer.class, renderizador);
    }

    // ------------------------------------------------------------------
    // API que consume el Controlador (la Vista no contiene logica de negocio)
    // ------------------------------------------------------------------

    public String getNombreIngresado() {
        return txtNombre.getText().trim();
    }

    public String getNivelIngresado() {
        return txtNivel.getText().trim();
    }

    public String getPuntosVidaIngresado() {
        return txtPuntosVida.getText().trim();
    }

    public String getClaseSeleccionada() {
        Object seleccion = cboClase.getSelectedItem();
        return seleccion == null ? "" : seleccion.toString();
    }

    public String getAtributoIngresado() {
        return txtAtributo.getText().trim();
    }

    public Aventurero getAventureroSeleccionado() {
        int fila = tablaAventureros.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return modeloTabla.getAventurero(tablaAventureros.convertRowIndexToModel(fila));
    }

    public void actualizarTabla(List<Aventurero> aventureros) {
        modeloTabla.actualizar(aventureros);
        actualizarContador(aventureros.size());
    }

    private void actualizarContador(int cantidad) {
        lblCantidad.setText(cantidad + (cantidad == 1 ? " aventurero registrado" : " aventureros registrados"));
        lblSeccionListado.setText("Aventureros del gremio (" + cantidad + ")");
    }

    public void anexarBitacora(String texto) {
        areaBitacora.append(texto + System.lineSeparator());
        areaBitacora.setCaretPosition(areaBitacora.getDocument().getLength());
    }

    public void limpiarBitacora() {
        areaBitacora.setText("");
    }

    public void mostrarEstado(String mensaje, boolean esError) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(esError ? COLOR_ERROR : COLOR_OK);
    }

    public void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarInformacion(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /** Limpia el formulario y deja los valores por defecto. */
    public void limpiarFormulario() {
        txtNombre.setText("");
        txtNivel.setText("1");
        txtPuntosVida.setText("10");
        txtAtributo.setText("");
        cboClase.setSelectedIndex(0);
        limpiarErrores();
        actualizarEtiquetaAtributo();
        txtNombre.requestFocusInWindow();
    }

    /** Marca visualmente un campo con error de validacion. */
    public void marcarError(javax.swing.JTextField campo, boolean error) {
        campo.setBorder(error ? bordeCampoError : bordeCampoNormal);
    }

    /** Quita el resaltado de error de todos los campos. */
    public void limpiarErrores() {
        for (javax.swing.JTextField campo : new javax.swing.JTextField[]{
            txtNombre, txtNivel, txtPuntosVida, txtAtributo}) {
            marcarError(campo, false);
        }
    }

    /** Resalta el campo del nombre cuando la validacion falla. */
    public void marcarErrorNombre() {
        marcarError(txtNombre, true);
    }

    /** Resalta el campo del nivel cuando la validacion falla. */
    public void marcarErrorNivel() {
        marcarError(txtNivel, true);
    }

    /** Resalta el campo de puntos de vida cuando la validacion falla. */
    public void marcarErrorPuntosVida() {
        marcarError(txtPuntosVida, true);
    }

    /** Resalta el campo del atributo especial cuando la validacion falla. */
    public void marcarErrorAtributo() {
        marcarError(txtAtributo, true);
    }

    public void enfocarNombre() {
        txtNombre.requestFocusInWindow();
    }

    public void enfocarNivel() {
        txtNivel.requestFocusInWindow();
    }

    public void enfocarPuntosVida() {
        txtPuntosVida.requestFocusInWindow();
    }

    public void enfocarAtributo() {
        txtAtributo.requestFocusInWindow();
    }

    // ------------------------------------------------------------------
    // Eventos: solo delegan al Controlador (sin logica de negocio aqui)
    // ------------------------------------------------------------------

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.registrarAventurero();
        }
    }

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.limpiarFormulario();
        }
    }

    private void btnSimularCombateActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.simularCombate();
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.eliminarAventureroSeleccionado();
        }
    }

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.mostrarHistorial();
        }
    }

    private void btnDadoActionPerformed(java.awt.event.ActionEvent evt) {
        int r = Modelo.Dado.tirar(20);
        lblResultadoDado.setText("Resultado: " + r);
        lblResultadoDado.setIcon(Imagenes.dado(20));
    }

    private void btnCombateAvanzadoActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlador != null) {
            controlador.simularCombateAvanzado();
        }
    }

    private void cboClaseActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarEtiquetaAtributo();
        if (lblImagenPersonaje != null) {
            lblImagenPersonaje.setIcon(Imagenes.clase(getClaseSeleccionada()));
        }
    }

    /** Cambia la etiqueta y la ayuda del atributo segun la clase elegida. */
    private void actualizarEtiquetaAtributo() {
        String clase = getClaseSeleccionada();
        switch (clase) {
            case "Guerrero":
                lblAtributo.setText("\u00bfUsa armadura pesada? (si / no)");
                lblAyudaAtributo.setText("Escribe si o no. La armadura pesada aumenta su poder fisico.");
                break;
            case "Mago":
                lblAtributo.setText("Espacios de conjuro");
                lblAyudaAtributo.setText("Numero entero de espacios disponibles (0 o mas).");
                break;
            case "Clérigo":
                lblAtributo.setText("Dios venerado");
                lblAyudaAtributo.setText("Nombre de la deidad, por ejemplo: Tyr, Selune.");
                break;
            case "Monje":
                lblAtributo.setText("Puntos de Ki");
                lblAyudaAtributo.setText("Numero entero de puntos de Ki (0 o mas).");
                break;
            case "Paladín":
                lblAtributo.setText("Puntos de fuerza");
                lblAyudaAtributo.setText("Numero entero de puntos de fuerza (0 o mas).");
                break;
            case "Explorador":
                lblAtributo.setText("Terreno favorecido");
                lblAyudaAtributo.setText("Bosque, desierto, montana, costa...");
                break;
            case "Pícaro":
                lblAtributo.setText("\u00bfTiene herramientas de ladron? (si / no)");
                lblAyudaAtributo.setText("Escribe si o no. Mejora su ataque furtivo.");
                break;
            case "Bardo":
                lblAtributo.setText("Instrumento musical");
                lblAyudaAtributo.setText("Laud, flauta, tambor, arpa...");
                break;
            default:
                lblAtributo.setText("Atributo especial");
                lblAyudaAtributo.setText("Selecciona una clase para ver el atributo requerido.");
                break;
        }
        txtAtributo.setToolTipText(lblAyudaAtributo.getText());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRaiz = new javax.swing.JPanel();
        pnlEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        pnlCuerpo = new javax.swing.JPanel();
        pnlRegistro = new javax.swing.JPanel();
        lblSeccionRegistro = new javax.swing.JLabel();
        lblImagenPersonaje = new javax.swing.JLabel();
        btnDado = new javax.swing.JButton();
        lblResultadoDado = new javax.swing.JLabel();
        btnCombateAvanzado = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblNivel = new javax.swing.JLabel();
        txtNivel = new javax.swing.JTextField();
        lblPuntosVida = new javax.swing.JLabel();
        txtPuntosVida = new javax.swing.JTextField();
        lblClase = new javax.swing.JLabel();
        cboClase = new javax.swing.JComboBox();
        lblAtributo = new javax.swing.JLabel();
        txtAtributo = new javax.swing.JTextField();
        lblAyudaAtributo = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        pnlListado = new javax.swing.JPanel();
        lblSeccionListado = new javax.swing.JLabel();
        scrTabla = new javax.swing.JScrollPane();
        tablaAventureros = new javax.swing.JTable();
        btnSimularCombate = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        pnlBitacora = new javax.swing.JPanel();
        lblSeccionBitacora = new javax.swing.JLabel();
        scrBitacora = new javax.swing.JScrollPane();
        areaBitacora = new javax.swing.JTextArea();
        lblEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gremio de Aventureros - Wizard of the Coast");

        pnlRaiz.setBackground(new java.awt.Color(243, 239, 230));

        pnlEncabezado.setBackground(new java.awt.Color(204, 0, 51));
        pnlEncabezado.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(212, 175, 55)));
        pnlEncabezado.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N

        lblTitulo.setFont(new java.awt.Font("Courier New", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("GREMIO DE AVENTUREROS");

        lblSubtitulo.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(201, 211, 224));
        lblSubtitulo.setText("Wizard of the Coast  ·  Calabozos y Dragones 5ª edición");

        lblCantidad.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(212, 175, 55));
        lblCantidad.setText("0 aventureros registrados");

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo)
                    .addComponent(lblSubtitulo)
                    .addComponent(lblCantidad))
                .addGap(0, 800, Short.MAX_VALUE))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblTitulo)
                .addGap(2, 2, 2)
                .addComponent(lblSubtitulo)
                .addGap(3, 3, 3)
                .addComponent(lblCantidad)
                .addGap(11, 11, 11))
        );

        pnlCuerpo.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        pnlCuerpo.setOpaque(false);

        pnlRegistro.setBackground(new java.awt.Color(255, 255, 255));
        pnlRegistro.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 213, 198), 1, true), javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        lblSeccionRegistro.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblSeccionRegistro.setForeground(new java.awt.Color(17, 30, 51));
        lblSeccionRegistro.setText("Registrar aventurero");

        lblImagenPersonaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnDado.setBackground(new java.awt.Color(0, 102, 255));
        btnDado.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnDado.setForeground(new java.awt.Color(204, 0, 51));
        btnDado.setText("Tirar dado");
        btnDado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDadoActionPerformed(evt);
            }
        });

        lblResultadoDado.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblResultadoDado.setText("Resultado: -");

        btnCombateAvanzado.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        btnCombateAvanzado.setText("Combate avanzado");
        btnCombateAvanzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCombateAvanzadoActionPerformed(evt);
            }
        });

        lblNombre.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(91, 103, 112));
        lblNombre.setText("Nombre del aventurero");

        txtNombre.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(31, 41, 51));
        txtNombre.setToolTipText("Ejemplo: Thorin Escudo de Roble");
        txtNombre.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(201, 194, 178), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtNombre.setCaretColor(new java.awt.Color(17, 30, 51));

        lblNivel.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblNivel.setForeground(new java.awt.Color(91, 103, 112));
        lblNivel.setText("Nivel (1 - 20)");

        txtNivel.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtNivel.setForeground(new java.awt.Color(31, 41, 51));
        txtNivel.setText("1");
        txtNivel.setToolTipText("Solo se permiten numeros entre 1 y 20");
        txtNivel.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(201, 194, 178), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtNivel.setCaretColor(new java.awt.Color(17, 30, 51));

        lblPuntosVida.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblPuntosVida.setForeground(new java.awt.Color(91, 103, 112));
        lblPuntosVida.setText("Puntos de vida");

        txtPuntosVida.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtPuntosVida.setForeground(new java.awt.Color(31, 41, 51));
        txtPuntosVida.setText("10");
        txtPuntosVida.setToolTipText("Solo se permiten numeros entre 0 y 999");
        txtPuntosVida.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(201, 194, 178), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtPuntosVida.setCaretColor(new java.awt.Color(17, 30, 51));

        lblClase.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblClase.setForeground(new java.awt.Color(91, 103, 112));
        lblClase.setText("Clase de personaje");

        cboClase.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        cboClase.setToolTipText("Selecciona la clase para ver su atributo especial");
        cboClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClaseActionPerformed(evt);
            }
        });

        lblAtributo.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblAtributo.setForeground(new java.awt.Color(91, 103, 112));
        lblAtributo.setText("Atributo especial");

        txtAtributo.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        txtAtributo.setForeground(new java.awt.Color(31, 41, 51));
        txtAtributo.setToolTipText("Dato propio de la clase seleccionada");
        txtAtributo.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(201, 194, 178), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtAtributo.setCaretColor(new java.awt.Color(17, 30, 51));

        lblAyudaAtributo.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        lblAyudaAtributo.setForeground(new java.awt.Color(138, 122, 84));
        lblAyudaAtributo.setText("Selecciona una clase para ver el atributo requerido.");

        btnRegistrar.setBackground(new java.awt.Color(212, 175, 55));
        btnRegistrar.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(17, 30, 51));
        btnRegistrar.setText("+  Registrar");
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(231, 226, 214));
        btnLimpiar.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(31, 41, 51));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setOpaque(true);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRegistroLayout = new javax.swing.GroupLayout(pnlRegistro);
        pnlRegistro.setLayout(pnlRegistroLayout);
        pnlRegistroLayout.setHorizontalGroup(
            pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRegistroLayout.createSequentialGroup()
                        .addComponent(btnRegistrar, 0, 0, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(btnLimpiar, 0, 0, Short.MAX_VALUE))
                    .addComponent(btnCombateAvanzado, 0, 0, Short.MAX_VALUE)
                    .addGroup(pnlRegistroLayout.createSequentialGroup()
                        .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSeccionRegistro)
                            .addComponent(lblImagenPersonaje)
                            .addComponent(lblResultadoDado)
                            .addComponent(lblNivel)
                            .addComponent(lblClase)
                            .addComponent(lblAtributo)
                            .addComponent(lblAyudaAtributo)
                            .addComponent(txtNombre)
                            .addComponent(cboClase, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAtributo)
                            .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnDado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPuntosVida, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPuntosVida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        pnlRegistroLayout.setVerticalGroup(
            pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegistroLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblSeccionRegistro)
                .addGap(8, 8, 8)
                .addComponent(lblImagenPersonaje)
                .addGap(4, 4, 4)
                .addComponent(btnDado, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblResultadoDado)
                .addGap(13, 13, 13)
                .addComponent(lblNombre)
                .addGap(4, 4, 4)
                .addComponent(txtNombre, 32, 32, 32)
                .addGap(11, 11, 11)
                .addComponent(lblNivel)
                .addGap(4, 4, 4)
                .addComponent(txtNivel, 32, 32, 32)
                .addGap(11, 11, 11)
                .addComponent(lblPuntosVida)
                .addGap(4, 4, 4)
                .addComponent(txtPuntosVida, 32, 32, 32)
                .addGap(11, 11, 11)
                .addComponent(lblClase)
                .addGap(4, 4, 4)
                .addComponent(cboClase, 32, 32, 32)
                .addGap(11, 11, 11)
                .addComponent(lblAtributo)
                .addGap(4, 4, 4)
                .addComponent(txtAtributo, 32, 32, 32)
                .addGap(4, 4, 4)
                .addComponent(lblAyudaAtributo)
                .addGap(18, 18, 18)
                .addGroup(pnlRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, 40, 40, 40)
                    .addComponent(btnLimpiar, 40, 40, 40))
                .addGap(8, 8, 8)
                .addComponent(btnCombateAvanzado, 40, 40, 40)
                .addGap(0, 93, Short.MAX_VALUE))
        );

        pnlListado.setBackground(new java.awt.Color(255, 255, 255));
        pnlListado.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 213, 198), 1, true), javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        lblSeccionListado.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
        lblSeccionListado.setForeground(new java.awt.Color(17, 30, 51));
        lblSeccionListado.setText("Aventureros del gremio (0)");

        scrTabla.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 213, 198), 1, true));

        tablaAventureros.setAutoCreateRowSorter(true);
        tablaAventureros.setForeground(new java.awt.Color(31, 41, 51));
        tablaAventureros.setFillsViewportHeight(true);
        tablaAventureros.setGridColor(new java.awt.Color(237, 232, 220));
        tablaAventureros.setRowHeight(28);
        tablaAventureros.setSelectionBackground(new java.awt.Color(232, 221, 187));
        tablaAventureros.setSelectionForeground(new java.awt.Color(31, 41, 51));
        tablaAventureros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrTabla.setViewportView(tablaAventureros);

        btnSimularCombate.setBackground(new java.awt.Color(178, 58, 46));
        btnSimularCombate.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnSimularCombate.setForeground(new java.awt.Color(255, 255, 255));
        btnSimularCombate.setText("Simular combate");
        btnSimularCombate.setBorderPainted(false);
        btnSimularCombate.setFocusPainted(false);
        btnSimularCombate.setOpaque(true);
        btnSimularCombate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimularCombateActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(231, 226, 214));
        btnEliminar.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(31, 41, 51));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnHistorial.setBackground(new java.awt.Color(30, 58, 95));
        btnHistorial.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        btnHistorial.setText("Ver historial");
        btnHistorial.setBorderPainted(false);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setOpaque(true);
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlListadoLayout = new javax.swing.GroupLayout(pnlListado);
        pnlListado.setLayout(pnlListadoLayout);
        pnlListadoLayout.setHorizontalGroup(
            pnlListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListadoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListadoLayout.createSequentialGroup()
                        .addComponent(scrTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlListadoLayout.createSequentialGroup()
                        .addGroup(pnlListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSeccionListado)
                            .addGroup(pnlListadoLayout.createSequentialGroup()
                                .addComponent(btnSimularCombate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10)
                                .addComponent(btnEliminar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(10, 10, 10)
                                .addComponent(btnHistorial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(20, 20, 20))))
        );
        pnlListadoLayout.setVerticalGroup(
            pnlListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListadoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblSeccionListado)
                .addGap(12, 12, 12)
                .addComponent(scrTabla, 180, 200, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(pnlListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSimularCombate, 40, 40, 40)
                    .addComponent(btnEliminar, 40, 40, 40)
                    .addComponent(btnHistorial, 40, 40, 40))
                .addGap(18, 18, 18))
        );

        pnlBitacora.setBackground(new java.awt.Color(255, 255, 255));
        pnlBitacora.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 213, 198), 1, true), javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        lblSeccionBitacora.setFont(new java.awt.Font("Courier New", 1, 15)); // NOI18N
        lblSeccionBitacora.setForeground(new java.awt.Color(17, 30, 51));
        lblSeccionBitacora.setText("Bitácora de combate");

        scrBitacora.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 213, 198), 1, true));

        areaBitacora.setEditable(false);
        areaBitacora.setBackground(new java.awt.Color(251, 249, 244));
        areaBitacora.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        areaBitacora.setForeground(new java.awt.Color(43, 58, 69));
        areaBitacora.setLineWrap(true);
        areaBitacora.setWrapStyleWord(true);
        areaBitacora.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
        scrBitacora.setViewportView(areaBitacora);

        lblEstado.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(91, 103, 112));
        lblEstado.setText("Listo para registrar aventureros.");

        javax.swing.GroupLayout pnlBitacoraLayout = new javax.swing.GroupLayout(pnlBitacora);
        pnlBitacora.setLayout(pnlBitacoraLayout);
        pnlBitacoraLayout.setHorizontalGroup(
            pnlBitacoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBitacoraLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlBitacoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSeccionBitacora)
                    .addComponent(scrBitacora, 220, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstado))
                .addGap(20, 20, 20))
        );
        pnlBitacoraLayout.setVerticalGroup(
            pnlBitacoraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBitacoraLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(lblSeccionBitacora)
                .addGap(8, 8, 8)
                .addComponent(scrBitacora, 56, 154, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(lblEstado)
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout pnlCuerpoLayout = new javax.swing.GroupLayout(pnlCuerpo);
        pnlCuerpo.setLayout(pnlCuerpoLayout);
        pnlCuerpoLayout.setHorizontalGroup(
            pnlCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuerpoLayout.createSequentialGroup()
                .addComponent(pnlRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlListado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBitacora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 19, Short.MAX_VALUE))
        );
        pnlCuerpoLayout.setVerticalGroup(
            pnlCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuerpoLayout.createSequentialGroup()
                .addGroup(pnlCuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCuerpoLayout.createSequentialGroup()
                        .addComponent(pnlListado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBitacora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlRaizLayout = new javax.swing.GroupLayout(pnlRaiz);
        pnlRaiz.setLayout(pnlRaizLayout);
        pnlRaizLayout.setHorizontalGroup(
            pnlRaizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRaizLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(pnlRaizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCuerpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        pnlRaizLayout.setVerticalGroup(
            pnlRaizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRaizLayout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(pnlCuerpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlRaiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlRaiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaBitacora;
    private javax.swing.JButton btnCombateAvanzado;
    private javax.swing.JButton btnDado;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSimularCombate;
    private javax.swing.JComboBox cboClase;
    private javax.swing.JLabel lblAtributo;
    private javax.swing.JLabel lblAyudaAtributo;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblClase;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblImagenPersonaje;
    private javax.swing.JLabel lblNivel;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPuntosVida;
    private javax.swing.JLabel lblResultadoDado;
    private javax.swing.JLabel lblSeccionBitacora;
    private javax.swing.JLabel lblSeccionListado;
    private javax.swing.JLabel lblSeccionRegistro;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBitacora;
    private javax.swing.JPanel pnlCuerpo;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlListado;
    private javax.swing.JPanel pnlRaiz;
    private javax.swing.JPanel pnlRegistro;
    private javax.swing.JScrollPane scrBitacora;
    private javax.swing.JScrollPane scrTabla;
    private javax.swing.JTable tablaAventureros;
    private javax.swing.JTextField txtAtributo;
    private javax.swing.JTextField txtNivel;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPuntosVida;
    // End of variables declaration//GEN-END:variables
}
