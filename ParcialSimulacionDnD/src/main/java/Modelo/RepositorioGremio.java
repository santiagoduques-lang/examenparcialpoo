/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de la persistencia del gremio en un archivo de texto
 * plano ({@code registro_gremio.txt}).
 *
 * <p>Implementa <b>flujos de entrada/salida</b> con {@link BufferedWriter} y
 * {@link BufferedReader} dentro de bloques try-with-resources, de modo que los
 * recursos se cierran automaticamente incluso si ocurre una excepcion. La
 * escritura es siempre en modo <i>append</i>: el historial nunca se borra.</p>
 *
 * <p><b>Encapsulamiento:</b> la ruta del archivo es privada y solo se expone de
 * forma controlada con {@link #getRutaArchivo()}. Ademas la clase admite dos
 * constructores (sobrecarga): uno usa el archivo por defecto y otro permite
 * indicar una ruta alternativa, lo que hace la clase facil de probar con
 * JUnit.</p>
 *
 * @author Santiago Duque Serrano
 */
public class RepositorioGremio {

    /** Nombre del archivo de persistencia exigido por el requerimiento RF4. */
    public static final String NOMBRE_ARCHIVO = "registro_gremio.txt";

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String SEPARADOR = "========================================================================";

    private final Path rutaArchivo;

    /**
     * Constructor por defecto: guarda el historial en la carpeta desde la que
     * se ejecuta la aplicacion.
     */
    public RepositorioGremio() {
        this(Paths.get(System.getProperty("user.dir"), NOMBRE_ARCHIVO));
    }

    /**
     * Constructor sobrecargado para pruebas o rutas personalizadas.
     *
     * @param rutaArchivo ruta del archivo de texto a utilizar.
     */
    public RepositorioGremio(Path rutaArchivo) {
        if (rutaArchivo == null) {
            throw new DatoInvalidoException("La ruta del archivo de persistencia no puede ser nula.");
        }
        this.rutaArchivo = rutaArchivo;
    }

    public Path getRutaArchivo() {
        return rutaArchivo;
    }

    public boolean existeArchivo() {
        return Files.exists(rutaArchivo);
    }

    /**
     * Anexa el registro de un aventurero al historial.
     *
     * @param aventurero aventurero recien registrado.
     * @throws IOException si no se puede escribir el archivo.
     */
    public void registrarAventurero(Aventurero aventurero) throws IOException {
        if (aventurero == null) {
            throw new DatoInvalidoException("No se puede persistir un aventurero nulo.");
        }
        registrarEvento("REGISTRO", aventurero.toString());
    }

    /**
     * Anexa el resultado de una accion de combate al historial.
     *
     * @param mensaje mensaje generado por el aventurero.
     * @throws IOException si no se puede escribir el archivo.
     */
    public void registrarCombate(String mensaje) throws IOException {
        registrarEvento("COMBATE", mensaje);
    }

    /**
     * Anexa una linea con fecha y tipo de evento al archivo plano.
     *
     * @param tipoEvento categoria del evento (REGISTRO, COMBATE, BAJA...).
     * @param detalle    descripcion del evento.
     * @throws IOException si no se puede escribir el archivo.
     */
    public void registrarEvento(String tipoEvento, String detalle) throws IOException {
        if (tipoEvento == null || tipoEvento.isBlank()) {
            throw new DatoInvalidoException("El tipo de evento es obligatorio.");
        }
        if (detalle == null) {
            throw new DatoInvalidoException("El detalle del evento no puede ser nulo.");
        }

        crearArchivoSiNoExiste();

        String linea = String.format("[%s] [%-8s] %s",
                LocalDateTime.now().format(FORMATO_FECHA), tipoEvento.toUpperCase(), detalle);

        // try-with-resources: el BufferedWriter se cierra automaticamente.
        try (BufferedWriter escritor = Files.newBufferedWriter(rutaArchivo,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            escritor.write(linea);
            escritor.newLine();
        }
    }

    /**
     * Lee todo el historial del archivo.
     *
     * @return lista de lineas (vacia si el archivo no existe).
     * @throws IOException si no se puede leer el archivo.
     */
    public List<String> leerHistorial() throws IOException {
        List<String> lineas = new ArrayList<>();
        if (!Files.exists(rutaArchivo)) {
            return lineas;
        }
        try (BufferedReader lector = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                lineas.add(linea);
            }
        }
        return lineas;
    }

    /**
     * Crea el archivo con su encabezado la primera vez que se escribe.
     *
     * @throws IOException si no se puede crear el archivo.
     */
    private void crearArchivoSiNoExiste() throws IOException {
        if (Files.exists(rutaArchivo)) {
            return;
        }
        Path carpeta = rutaArchivo.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }
        try (BufferedWriter escritor = Files.newBufferedWriter(rutaArchivo,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            escritor.write(SEPARADOR);
            escritor.newLine();
            escritor.write("  GREMIO DE AVENTUREROS - WIZARD OF THE COAST");
            escritor.newLine();
            escritor.write("  Historial de registros y combates - " + IAccionCombate.SISTEMA);
            escritor.newLine();
            escritor.write(SEPARADOR);
            escritor.newLine();
        }
    }
    public void registrarSubidaNivel(String nombre, int nivelNuevo) throws IOException {
        registrarEvento("SUBIDA_NIVEL", nombre + " sube a nivel " + nivelNuevo);
    }

    public void registrarDescanso(String nombre, String tipo) throws IOException {
        registrarEvento("DESCANSO", nombre + " realiza " + tipo);
    }
}
