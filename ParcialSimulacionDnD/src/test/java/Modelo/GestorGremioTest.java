package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de la logica del gremio.
 *
 * @author Santiago Duque Serrano
 */
class GestorGremioTest {

    private List<Aventurero> coleccion;
    private GestorGremio gestor;

    @BeforeEach
    void preparar() {
        coleccion = new ArrayList<>();
        gestor = new GestorGremio(coleccion);
        gestor.agregarAventurero(new Guerrero("Thorin", 7, 82, true));
        gestor.agregarAventurero(new Mago("Elminster", 12, 60, 8));
        gestor.agregarAventurero(new Clerigo("Selune Alba", 6, 55, "Selune"));
    }

    @Test
    @DisplayName("El gestor comparte la coleccion unica del Controlador")
    void comparteLaColeccionRecibida() {
        assertEquals(3, coleccion.size());
        assertEquals(3, gestor.getCantidadAventureros());
        assertSame(coleccion.get(0), gestor.getAventureros().get(0));
    }

    @Test
    @DisplayName("No se permiten nombres duplicados (sin distinguir mayusculas)")
    void noPermiteNombresDuplicados() {
        DatoInvalidoException excepcion = assertThrows(DatoInvalidoException.class,
                () -> gestor.agregarAventurero(new Mago("thorin", 2, 15, 1)));

        assertTrue(excepcion.getMessage().contains("Ya existe"));
        assertEquals(3, gestor.getCantidadAventureros());
    }

    @Test
    @DisplayName("No se puede agregar un aventurero nulo")
    void noPermiteAventureroNulo() {
        assertThrows(DatoInvalidoException.class, () -> gestor.agregarAventurero(null));
        assertThrows(DatoInvalidoException.class, () -> new GestorGremio(null));
    }

    @Test
    @DisplayName("La busqueda por nombre ignora mayusculas y espacios")
    void busquedaPorNombre() {
        Aventurero encontrado = gestor.buscarPorNombre("  elminster ");

        assertNotNull(encontrado);
        assertEquals("Elminster", encontrado.getNombre());
        assertNull(gestor.buscarPorNombre("Inexistente"));
        assertNull(gestor.buscarPorNombre(null));
        assertTrue(gestor.existeAventurero("THORIN"));
        assertFalse(gestor.existeAventurero("Bardo"));
    }

    @Test
    @DisplayName("Se puede eliminar un aventurero existente")
    void eliminarAventurero() {
        assertTrue(gestor.eliminarAventurero("Thorin"));
        assertEquals(2, gestor.getCantidadAventureros());
        assertFalse(gestor.eliminarAventurero("Thorin"));
    }

    @Test
    @DisplayName("El filtro por clase devuelve solo las coincidencias")
    void filtroPorClase() {
        List<Aventurero> magos = gestor.filtrarPorClase("mago");

        assertEquals(1, magos.size());
        assertEquals("Elminster", magos.get(0).getNombre());
        assertTrue(gestor.filtrarPorClase("Bardo").isEmpty());
        assertTrue(gestor.filtrarPorClase(null).isEmpty());
    }

    @Test
    @DisplayName("El filtro por nivel minimo respeta el corte")
    void filtroPorNivelMinimo() {
        List<Aventurero> veteranos = gestor.filtrarPorNivelMinimo(7);

        assertEquals(2, veteranos.size());
        assertTrue(gestor.filtrarPorNivelMinimo(20).isEmpty());
    }

    @Test
    @DisplayName("El ordenamiento por nivel ubica primero al mas experimentado")
    void ordenamientoPorNivel() {
        List<Aventurero> ordenados = gestor.obtenerOrdenadosPorNivel();

        assertEquals("Elminster", ordenados.get(0).getNombre());
        assertEquals("Thorin", ordenados.get(1).getNombre());
        assertEquals("Selune Alba", ordenados.get(2).getNombre());
    }

    @Test
    @DisplayName("Los calculos estadisticos del gremio son correctos")
    void estadisticasDelGremio() {
        assertEquals(3, gestor.getCantidadAventureros());
        assertEquals(94, gestor.calcularPoderTotal());
        assertEquals((7 + 12 + 6) / 3.0, gestor.calcularNivelPromedio(), 0.0001);
        assertEquals("Elminster", gestor.obtenerAventureroMasPoderoso().getNombre());
        assertFalse(gestor.estaVacio());
    }

    @Test
    @DisplayName("El conteo por clase agrupa correctamente")
    void conteoPorClase() {
        Map<String, Integer> conteo = gestor.contarAventurerosPorClase();

        assertEquals(3, conteo.size());
        assertEquals(1, conteo.get("Guerrero"));
        assertEquals(1, conteo.get("Mago"));
        assertEquals(1, conteo.get("Clérigo"));
    }

    @Test
    @DisplayName("La simulacion de combate devuelve un mensaje por aventurero")
    void simulacionDeCombate() {
        List<String> mensajes = gestor.simularCombate();

        assertEquals(3, mensajes.size());
        assertTrue(mensajes.get(0).contains("Oleada de Acción"));
        assertTrue(mensajes.get(1).contains("Bola de Fuego"));
        assertTrue(mensajes.get(2).contains("Canalizar Divinidad"));
    }

    @Test
    @DisplayName("El reporte por clase informa totales y porcentajes")
    void reportePorClase() {
        String reporte = gestor.generarReportePorClase();

        assertTrue(reporte.contains("REPORTE 1"));
        assertTrue(reporte.contains("Guerrero"));
        assertTrue(reporte.contains("Mago"));
        assertTrue(reporte.contains("Total de aventureros registrados: 3"));
        assertTrue(reporte.contains("33,3") || reporte.contains("33.3"));
    }

    @Test
    @DisplayName("El reporte estadistico incluye promedio y top 3")
    void reporteEstadistico() {
        String reporte = gestor.generarReporteEstadistico();

        assertTrue(reporte.contains("REPORTE 2"));
        assertTrue(reporte.contains("Nivel promedio"));
        assertTrue(reporte.contains("Aventurero mas poderoso"));
        assertTrue(reporte.contains("Elminster"));
        assertTrue(reporte.contains("Top 3 por nivel"));
    }

    @Test
    @DisplayName("Los reportes de un gremio vacio informan la ausencia de datos")
    void reportesDeGremioVacio() {
        GestorGremio vacio = new GestorGremio(new ArrayList<>());

        assertTrue(vacio.generarReportePorClase().contains("No hay aventureros"));
        assertTrue(vacio.generarReporteEstadistico().contains("No hay aventureros"));
        assertNull(vacio.obtenerAventureroMasPoderoso());
        assertEquals(0.0, vacio.calcularNivelPromedio(), 0.0001);
        assertTrue(vacio.simularCombate().isEmpty());
        assertTrue(vacio.estaVacio());
    }

    @Test
    @DisplayName("La vista de la coleccion es de solo lectura")
    void laColeccionExpuestaNoEsModificable() {
        assertThrows(UnsupportedOperationException.class,
                () -> gestor.getAventureros().add(new Bardo("Lyra", 3, 30, "Laud")));
    }
}
