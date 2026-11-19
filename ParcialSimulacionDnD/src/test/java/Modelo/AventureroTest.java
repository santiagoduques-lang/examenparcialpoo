package Modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de la clase abstracta {@link Aventurero}.
 *
 * <p>Se validan las reglas del modelo: encapsulamiento con validacion,
 * constructores sobrecargados, sobrecarga de metodos y representacion textual.
 * No se prueba ninguna clase de la Vista (requisito RNF5).</p>
 *
 * @author Santiago Duque Serrano
 */
class AventureroTest {

    @Test
    @DisplayName("El constructor sobrecargado asigna nivel 1 y 10 puntos de vida")
    void constructorSobrecargadoAsignaValoresPorDefecto() {
        Aventurero novato = new Guerrero("Novato", true);

        assertEquals(1, novato.getNivel());
        assertEquals(10, novato.getPuntosVida());
        assertTrue(novato.estaConsciente());
    }

    @Test
    @DisplayName("El constructor principal conserva los valores recibidos")
    void constructorPrincipalConservaValores() {
        Aventurero guerrero = new Guerrero("Thorin", 7, 82, true);

        assertEquals("Thorin", guerrero.getNombre());
        assertEquals(7, guerrero.getNivel());
        assertEquals(82, guerrero.getPuntosVida());
        assertEquals("Guerrero", guerrero.getClasePersonaje());
    }

    @Test
    @DisplayName("Un nombre vacio o nulo lanza DatoInvalidoException")
    void nombreInvalidoLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> new Guerrero("   ", 3, 20, true));
        assertThrows(DatoInvalidoException.class, () -> new Guerrero(null, 3, 20, true));
    }

    @Test
    @DisplayName("El nombre se guarda sin espacios sobrantes")
    void nombreSeDepuraConTrim() {
        Aventurero mago = new Mago("  Elminster  ", 12, 60, 8);

        assertEquals("Elminster", mago.getNombre());
    }

    @Test
    @DisplayName("El nivel fuera de 1-20 lanza DatoInvalidoException")
    void nivelFueraDeRangoLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> new Mago("Mago", 0, 10, 2));
        assertThrows(DatoInvalidoException.class, () -> new Mago("Mago", 21, 10, 2));
    }

    @Test
    @DisplayName("Los puntos de vida negativos lanzan DatoInvalidoException")
    void puntosVidaInvalidosLanzanExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> new Monje("Monje", 3, -1, 2));
        assertThrows(DatoInvalidoException.class, () -> new Monje("Monje", 3, 1000, 2));
    }

    @Test
    @DisplayName("Los setters validan y actualizan el encapsulamiento")
    void settersValidanDatos() {
        Aventurero bardo = new Bardo("Lyra", 3, 30, "Laud");

        bardo.setNivel(5);
        bardo.setPuntosVida(44);
        assertEquals(5, bardo.getNivel());
        assertEquals(44, bardo.getPuntosVida());

        assertThrows(DatoInvalidoException.class, () -> bardo.setNivel(25));
        assertThrows(DatoInvalidoException.class, () -> bardo.setPuntosVida(-5));
        assertThrows(DatoInvalidoException.class, () -> bardo.setNombre(" "));
    }

    @Test
    @DisplayName("La sobrecarga de calcularPoderAtaque suma la bonificacion")
    void sobrecargaDePoderAtaque() {
        Aventurero explorador = new Explorador("Artemis", 4, 40, "Bosque");

        int sinBonificacion = explorador.calcularPoderAtaque(0);
        int conBonificacion = explorador.calcularPoderAtaque(10);

        assertEquals(sinBonificacion + 10, conBonificacion);
        assertThrows(DatoInvalidoException.class, () -> explorador.calcularPoderAtaque(-1));
    }

    @Test
    @DisplayName("La sobrecarga de recibirDanio descuenta y no baja de cero")
    void sobrecargaDeRecibirDanio() {
        Aventurero paladin = new Paladin("Aurelia", 9, 20, 18);

        assertEquals(15, paladin.recibirDanio(5));
        assertEquals(0, paladin.recibirDanio(100, "bola de fuego"));
        assertFalse(paladin.estaConsciente());
        assertThrows(DatoInvalidoException.class, () -> paladin.recibirDanio(-2));
    }

    @Test
    @DisplayName("El toString incluye clase, nombre y nivel")
    void toStringIncluyeDatosRelevantes() {
        Aventurero clerigo = new Clerigo("Selune Alba", 6, 55, "Selune");
        String texto = clerigo.toString();

        assertTrue(texto.contains("Clérigo"));
        assertTrue(texto.contains("Selune Alba"));
        assertTrue(texto.contains("Nivel  6") || texto.contains("Nivel 6"));
        assertTrue(texto.contains("Dios venerado: Selune"));
    }

    @Test
    @DisplayName("El contador estatico registra los aventureros creados")
    void contadorEstaticoAumenta() {
        int antes = Aventurero.getTotalAventureros();
        new Picaro("Vex", 8, 58, true);

        assertEquals(antes + 1, Aventurero.getTotalAventureros());
    }
}
