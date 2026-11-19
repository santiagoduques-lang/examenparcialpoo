/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author santi
 */
import Modelo.Aventurero;
import Modelo.Bardo;
import Modelo.Clerigo;
import Modelo.DatoInvalidoException;
import Modelo.Enemigo;
import Modelo.Explorador;
import Modelo.FabricaEnemigo;
import Modelo.GestorGremio;
import Modelo.MotorCombate;
import Modelo.Guerrero;
import Modelo.Mago;
import Modelo.Monje;
import Modelo.Paladin;
import Modelo.Picaro;
import Modelo.RepositorioGremio;
import Vista.VentanaGremio;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del gremio: une el modelo con la vista.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Validar y transformar los datos que llegan del formulario.</li>
 *   <li>Crear la subclase de {@link Aventurero} correspondiente (fabrica).</li>
 *   <li>Guardar la <b>coleccion unica</b> de aventureros y delegar la logica en
 *       {@link GestorGremio}.</li>
 *   <li>Persistir los eventos a traves de {@link RepositorioGremio}.</li>
 *   <li>Actualizar la vista sin que la vista conozca el modelo.</li>
 * </ul>
 *
 * <p>Relaciones UML: asociacion con {@code VentanaGremio}, con
 * {@code GestorGremio} y con {@code RepositorioGremio}; dependencia con cada
 * subclase concreta de {@code Aventurero} porque las instancia.</p>
 *
 * @author Santiago Duque Serrano
 */
public class ControladorGremio {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Coleccion unica de aventureros del sistema (requisito RNF2). */
    private final ArrayList<Aventurero> aventureros;

    private final GestorGremio gestorGremio;
    private final RepositorioGremio repositorio;
    private final VentanaGremio ventana;

    /**
     * @param ventana vista principal del sistema.
     */
    public ControladorGremio(VentanaGremio ventana) {
        this(ventana, new RepositorioGremio());
    }

    /**
     * Constructor sobrecargado que permite inyectar el repositorio (util para
     * pruebas o para elegir otra ruta de persistencia).
     *
     * @param ventana     vista principal del sistema.
     * @param repositorio repositorio de persistencia a utilizar.
     */
    public ControladorGremio(VentanaGremio ventana, RepositorioGremio repositorio) {
        if (ventana == null || repositorio == null) {
            throw new DatoInvalidoException("La vista y el repositorio son obligatorios.");
        }
        this.ventana = ventana;
        this.repositorio = repositorio;
        this.aventureros = new ArrayList<>();
        this.gestorGremio = new GestorGremio(aventureros);
    }

    /**
     * Conecta la vista, carga los datos iniciales y muestra la ventana.
     */
    public void iniciar() {
        ventana.setControlador(this);
        ventana.limpiarFormulario();
        actualizarVista();
        ventana.anexarBitacora("Gremio de aventureros iniciado.");
        ventana.anexarBitacora("Archivo de persistencia: " + repositorio.getRutaArchivo());
        ventana.setVisible(true);
    }

    // ------------------------------------------------------------------
    // RF1 - Registro de aventureros
    // ------------------------------------------------------------------

    /**
     * Toma los datos de la vista, valida, crea la subclase adecuada, la
     * registra y persiste el evento.
     */
    public void registrarAventurero() {
        ventana.limpiarErrores();
        try {
            String nombre = ventana.getNombreIngresado();
            if (nombre.isEmpty()) {
                ventana.marcarErrorNombre();
                ventana.enfocarNombre();
                throw new DatoInvalidoException("El nombre del aventurero es obligatorio.");
            }

            int nivel = leerEntero(ventana.getNivelIngresado(), "nivel", Aventurero.NIVEL_MINIMO,
                    Aventurero.NIVEL_MAXIMO, CampoFormulario.NIVEL);
            int puntosVida = leerEntero(ventana.getPuntosVidaIngresado(), "puntos de vida",
                    Aventurero.VIDA_MINIMA, Aventurero.VIDA_MAXIMA, CampoFormulario.PUNTOS_VIDA);

            String clase = ventana.getClaseSeleccionada();
            String atributo = ventana.getAtributoIngresado();
            if (atributo.isEmpty()) {
                ventana.marcarErrorAtributo();
                ventana.enfocarAtributo();
                throw new DatoInvalidoException(
                        "El atributo especial de la clase " + clase + " es obligatorio.");
            }

            Aventurero aventurero = crearAventurero(nombre, nivel, puntosVida, clase, atributo);
            gestorGremio.agregarAventurero(aventurero);
            repositorio.registrarAventurero(aventurero);

            actualizarVista();
            ventana.limpiarFormulario();
            ventana.anexarBitacora("[REGISTRO] " + aventurero.toString());
            ventana.mostrarEstado("Aventurero registrado: " + aventurero.getNombre()
                    + " (" + aventurero.getClasePersonaje() + ").", false);
            ventana.mostrarInformacion("Registro exitoso",
                    aventurero.getNombre() + " se unio al gremio como "
                            + aventurero.getClasePersonaje() + ".\n\n" + aventurero.getAtributoEspecial());
        } catch (DatoInvalidoException ex) {
            ventana.mostrarEstado(ex.getMessage(), true);
            ventana.mostrarError("Datos invalidos", ex.getMessage());
        } catch (IOException ex) {
            ventana.mostrarEstado("El aventurero quedo en memoria, pero fallo la persistencia.", true);
            ventana.mostrarError("Error de archivo",
                    "No se pudo escribir en " + RepositorioGremio.NOMBRE_ARCHIVO + ":\n" + ex.getMessage());
        }
    }

    /**
     * Fabrica de aventureros: convierte la clase elegida y su atributo especial
     * en la subclase concreta correspondiente. Aqui se evidencia la
     * <b>dependencia</b> del Controlador hacia las clases del modelo.
     *
     * @param nombre     nombre del aventurero.
     * @param nivel      nivel validado.
     * @param puntosVida puntos de vida validados.
     * @param clase      clase seleccionada en la vista.
     * @param atributo   texto del atributo especial.
     * @return la subclase de aventurero creada.
     */
    private Aventurero crearAventurero(String nombre, int nivel, int puntosVida,
            String clase, String atributo) {
        switch (clase) {
            case "Guerrero":
                return new Guerrero(nombre, nivel, puntosVida,
                        interpretarBooleano(atributo, "armadura pesada"));
            case "Mago":
                return new Mago(nombre, nivel, puntosVida,
                        interpretarEnteroAtributo(atributo, "espacios de conjuro"));
            case "Clérigo":
                return new Clerigo(nombre, nivel, puntosVida, atributo);
            case "Monje":
                return new Monje(nombre, nivel, puntosVida,
                        interpretarEnteroAtributo(atributo, "puntos de Ki"));
            case "Paladín":
                return new Paladin(nombre, nivel, puntosVida,
                        interpretarEnteroAtributo(atributo, "puntos de fuerza"));
            case "Explorador":
                return new Explorador(nombre, nivel, puntosVida, atributo);
            case "Pícaro":
                return new Picaro(nombre, nivel, puntosVida,
                        interpretarBooleano(atributo, "herramientas de ladron"));
            case "Bardo":
                return new Bardo(nombre, nivel, puntosVida, atributo);
            default:
                throw new DatoInvalidoException("La clase " + clase + " no esta registrada en el gremio.");
        }
    }

    // ------------------------------------------------------------------
    // RF3 - Simulacion de combate (polimorfismo)
    // ------------------------------------------------------------------

    /**
     * Recorre la coleccion y muestra la accion de combate de cada aventurero.
     * El mensaje proviene de la implementacion polimorfica de cada subclase.
     */
    public void simularCombate() {
        if (gestorGremio.estaVacio()) {
            ventana.mostrarEstado("No hay aventureros registrados para simular.", true);
            ventana.mostrarError("Gremio vacio", "Registra al menos un aventurero antes de simular un combate.");
            return;
        }

        List<String> mensajes = gestorGremio.simularCombate();
        ventana.limpiarBitacora();
        ventana.anexarBitacora("=== SIMULACION DE COMBATE - " + LocalDateTime.now().format(FORMATO_FECHA) + " ===");

        int fallosPersistencia = 0;
        for (String mensaje : mensajes) {
            ventana.anexarBitacora("  \u25b8 " + mensaje);
            try {
                repositorio.registrarCombate(mensaje);
            } catch (IOException ex) {
                fallosPersistencia++;
            }
        }
        ventana.anexarBitacora("=== FIN DE LA SIMULACION (" + mensajes.size() + " turnos) ===");
        ventana.mostrarEstado("Combate simulado para " + mensajes.size() + " aventureros.", false);

        if (fallosPersistencia > 0) {
            ventana.mostrarError("Error de archivo",
                    "No se pudieron guardar " + fallosPersistencia + " lineas del historial.");
        }
    }

    // ------------------------------------------------------------------
    // Combate avanzado (MotorCombate por rondas)
    // ------------------------------------------------------------------
    public void simularCombateAvanzado() {
        List<Aventurero> heroes = new ArrayList<>(gestorGremio.getAventureros());
        if (heroes.isEmpty()) {
            heroes.add(new Guerrero("Thorin", 5, 40, true));
            heroes.add(new Mago("Gandalf", 5, 24, 4));
        }
        List<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(FabricaEnemigo.goblin());
        enemigos.add(FabricaEnemigo.orco());

        MotorCombate motor = new MotorCombate(heroes, enemigos);
        String log = motor.ejecutar();

        ventana.limpiarBitacora();
        ventana.anexarBitacora(log);
        actualizarVista();
        ventana.mostrarEstado("Combate avanzado ejecutado.", false);
        try {
            repositorio.registrarCombate("Combate avanzado (GUI)");
        } catch (IOException ex) {
            ventana.mostrarError("Error de archivo", "No se pudo registrar el combate: " + ex.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // RF2 / RF4 - Listado, eliminacion e historial
    // ------------------------------------------------------------------

    /**
     * Refresca la tabla de la vista con la coleccion actual.
     */
    public void actualizarVista() {
        ventana.actualizarTabla(gestorGremio.getAventureros());
    }

    /**
     * Elimina el aventurero seleccionado en la tabla.
     */
    public void eliminarAventureroSeleccionado() {
        Aventurero seleccionado = ventana.getAventureroSeleccionado();
        if (seleccionado == null) {
            ventana.mostrarEstado("Selecciona un aventurero de la tabla para eliminarlo.", true);
            ventana.mostrarError("Sin seleccion", "Primero selecciona una fila de la tabla de aventureros.");
            return;
        }
        if (!ventana.confirmar("Retirar del gremio a " + seleccionado.getNombre() + "?")) {
            return;
        }
        if (gestorGremio.eliminarAventurero(seleccionado.getNombre())) {
            try {
                repositorio.registrarEvento("BAJA", seleccionado.toString());
            } catch (IOException ex) {
                ventana.mostrarError("Error de archivo", "No se pudo registrar la baja: " + ex.getMessage());
            }
            actualizarVista();
            ventana.anexarBitacora("[BAJA] " + seleccionado.getNombre() + " dejo el gremio.");
            ventana.mostrarEstado(seleccionado.getNombre() + " fue eliminado del gremio.", false);
        }
    }

    /**
     * Muestra el contenido del archivo {@code registro_gremio.txt} en la
     * bitacora de la vista.
     */
    public void mostrarHistorial() {
        try {
            List<String> lineas = repositorio.leerHistorial();
            if (lineas.isEmpty()) {
                ventana.mostrarEstado("El historial esta vacio todavia.", true);
                ventana.mostrarInformacion("Historial vacio",
                        "Registra un aventurero o simula un combate para crear el historial.");
                return;
            }
            ventana.limpiarBitacora();
            int desde = Math.max(0, lineas.size() - 200);
            for (int i = desde; i < lineas.size(); i++) {
                ventana.anexarBitacora(lineas.get(i));
            }
            ventana.mostrarEstado("Historial cargado: " + lineas.size() + " lineas en "
                    + repositorio.getRutaArchivo().getFileName() + ".", false);
        } catch (IOException ex) {
            ventana.mostrarEstado("No se pudo leer el historial.", true);
            ventana.mostrarError("Error de archivo", "No se pudo leer el historial:\n" + ex.getMessage());
        }
    }

    /**
     * Limpia el formulario de registro.
     */
    public void limpiarFormulario() {
        ventana.limpiarFormulario();
        ventana.mostrarEstado("Formulario limpio. Listo para un nuevo aventurero.", false);
    }

    // ------------------------------------------------------------------
    // Utilidades de validacion
    // ------------------------------------------------------------------

    private enum CampoFormulario {
        NIVEL, PUNTOS_VIDA
    }

    private int leerEntero(String texto, String nombreCampo, int minimo, int maximo,
            CampoFormulario campo) {
        if (texto == null || texto.isBlank()) {
            resaltar(campo);
            throw new DatoInvalidoException("El campo " + nombreCampo + " es obligatorio.");
        }
        int valor;
        try {
            valor = Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            resaltar(campo);
            throw new DatoInvalidoException("El campo " + nombreCampo + " solo admite numeros enteros.", ex);
        }
        if (valor < minimo || valor > maximo) {
            resaltar(campo);
            throw new DatoInvalidoException(
                    "El campo " + nombreCampo + " debe estar entre " + minimo + " y " + maximo + ".");
        }
        return valor;
    }

    private void resaltar(CampoFormulario campo) {
        if (campo == CampoFormulario.NIVEL) {
            ventana.marcarErrorNivel();
            ventana.enfocarNivel();
        } else {
            ventana.marcarErrorPuntosVida();
            ventana.enfocarPuntosVida();
        }
    }

    private int interpretarEnteroAtributo(String texto, String campo) {
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor < 0) {
                throw new DatoInvalidoException("El campo " + campo + " no puede ser negativo.");
            }
            return valor;
        } catch (NumberFormatException ex) {
            ventana.marcarErrorAtributo();
            ventana.enfocarAtributo();
            throw new DatoInvalidoException("El campo " + campo + " debe ser un numero entero.", ex);
        }
    }

    private boolean interpretarBooleano(String texto, String campo) {
        String valor = texto.trim().toLowerCase();
        switch (valor) {
            case "si":
            case "sí":
            case "s":
            case "true":
            case "1":
            case "yes":
                return true;
            case "no":
            case "n":
            case "false":
            case "0":
                return false;
            default:
                ventana.marcarErrorAtributo();
                ventana.enfocarAtributo();
                throw new DatoInvalidoException("El campo " + campo + " solo acepta si o no.");
        }
    }
}
