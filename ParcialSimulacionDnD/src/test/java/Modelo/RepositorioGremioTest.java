package Modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de la persistencia en archivo plano.
 *
 * <p>Se usa {@link TempDir} para no ensuciar el archivo real del proyecto y se
 * comprueba que los eventos se anexan (append) con su encabezado.</p>
 *
 * @author Santiago Duque Serrano
 */
class RepositorioGremioTest {

    @TempDir
    Path carpetaTemporal;

    private Path archivo;
    private RepositorioGremio repositorio;

    @BeforeEach
    void preparar() {
        archivo = carpetaTemporal.resolve("registro_gremio.txt");
        repositorio = new RepositorioGremio(archivo);
    }

    @Test
    @DisplayName("El primer registro crea el archivo con su encabezado")
    void primerRegistroCreaArchivoConEncabezado() throws IOException {
        assertFalse(repositorio.existeArchivo());

        repositorio.registrarAventurero(new Guerrero("Thorin", 7, 82, true));

        assertTrue(Files.exists(archivo));
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        assertTrue(lineas.stream().anyMatch(l -> l.contains("GREMIO DE AVENTUREROS")));
        assertTrue(lineas.stream().anyMatch(l -> l.contains("[REGISTRO]") && l.contains("Thorin")));
    }

    @Test
    @DisplayName("Los eventos se anexan sin borrar el historial previo")
    void losEventosSeAnexan() throws IOException {
        repositorio.registrarAventurero(new Mago("Elminster", 12, 60, 8));
        repositorio.registrarCombate("El Mago Elminster lanza Bola de Fuego.");
        repositorio.registrarEvento("BAJA", "Elminster dejo el gremio.");

        List<String> lineas = repositorio.leerHistorial();

        assertTrue(lineas.stream().anyMatch(l -> l.contains("REGISTRO")));
        assertTrue(lineas.stream().anyMatch(l -> l.contains("COMBATE")));
        assertTrue(lineas.stream().anyMatch(l -> l.contains("BAJA")));
        assertEquals(3, lineas.stream().filter(l -> l.startsWith("[")).count());
    }

    @Test
    @DisplayName("La linea incluye fecha y tipo de evento en mayusculas")
    void formatoDeLinea() throws IOException {
        repositorio.registrarCombate("Ataque furtivo");

        String linea = repositorio.leerHistorial().stream()
                .filter(l -> l.contains("COMBATE"))
                .findFirst()
                .orElse("");

        assertTrue(linea.matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}] \\[COMBATE ] .*"));
    }

    @Test
    @DisplayName("Leer un archivo inexistente devuelve una lista vacia")
    void leerArchivoInexistenteDevuelveListaVacia() throws IOException {
        assertTrue(repositorio.leerHistorial().isEmpty());
    }

    @Test
    @DisplayName("Un tipo de evento vacio lanza DatoInvalidoException")
    void tipoDeEventoInvalido() {
        assertThrows(DatoInvalidoException.class, () -> repositorio.registrarEvento("  ", "detalle"));
        assertThrows(DatoInvalidoException.class, () -> repositorio.registrarEvento("REGISTRO", null));
    }

    @Test
    @DisplayName("No se puede persistir un aventurero nulo")
    void aventureroNuloNoSePersiste() {
        assertThrows(DatoInvalidoException.class, () -> repositorio.registrarAventurero(null));
    }

    @Test
    @DisplayName("El repositorio por defecto usa el nombre exigido por el RF4")
    void nombreDeArchivoPorDefecto() {
        assertEquals("registro_gremio.txt", RepositorioGremio.NOMBRE_ARCHIVO);
        assertEquals(RepositorioGremio.NOMBRE_ARCHIVO,
                new RepositorioGremio().getRutaArchivo().getFileName().toString());
    }
}
