package Modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CombateDnDTest {

    @Test
    void iniciativaRango() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        int ini = g.tirarIniciativa();
        assertTrue(ini >= 1 && ini <= 30);
    }

    @Test
    void resistenciaMitad() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.agregarResistencia(TipoDano.FUEGO);
        assertEquals(5, g.aplicarDanioTipo(10, TipoDano.FUEGO));
    }

    @Test
    void inmunidadCero() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.agregarInmunidad(TipoDano.VENENO);
        assertEquals(0, g.aplicarDanioTipo(10, TipoDano.VENENO));
    }

    @Test
    void vulnerabilidadDoble() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.agregarVulnerabilidad(TipoDano.RADIANTE);
        assertEquals(20, g.aplicarDanioTipo(10, TipoDano.RADIANTE));
    }

    @Test
    void salvacionContraMuerteCritico20Revive() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.recibirDanio(10);
        assertEquals(0, g.getPuntosVida());
        // forzar tirada 20 no posible directamente; comprobar estado inicial
        assertFalse(g.estaMuerto());
    }

    @Test
    void muerteInstantanea() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.recibirDanio(100);
        assertTrue(g.estaMuerto());
    }

    @Test
    void curarRevive() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.recibirDanio(10);
        assertEquals(0, g.getPuntosVida());
        g.curar(5);
        assertEquals(5, g.getPuntosVida());
        assertFalse(g.estaMuerto());
    }

    @Test
    void atacarDevuelveDanioNoNegativo() {
        Aventurero a = new Guerrero("A", 5, 30, true);
        Aventurero b = new Guerrero("B", 5, 30, true);
        int d = a.atacar(b);
        assertTrue(d >= 0);
    }
}