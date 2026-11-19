package Modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NucleoDnDTest {

    @Test
    void modificadorAtributo() {
        assertEquals(-1, Atributos.modificador(8));
        assertEquals(0, Atributos.modificador(10));
        assertEquals(2, Atributos.modificador(15));
        assertEquals(5, Atributos.modificador(20));
    }

    @Test
    void generarEstandar() {
        Atributos a = new Atributos();
        a.generarEstandar();
        assertEquals(15, a.getFuerza());
        assertEquals(8, a.getCarisma());
    }

    @Test
    void tirada4d6Rango() {
        for (int i = 0; i < 50; i++) {
            int v = Dado.tirar4d6DescartarMenor();
            assertTrue(v >= 3 && v <= 18);
        }
    }

    @Test
    void bonoCompetencia() {
        assertEquals(2, TablaProgresion.bonoCompetencia(1));
        assertEquals(2, TablaProgresion.bonoCompetencia(4));
        assertEquals(3, TablaProgresion.bonoCompetencia(5));
        assertEquals(6, TablaProgresion.bonoCompetencia(20));
    }

    @Test
    void xpParaNivel() {
        assertEquals(300, TablaProgresion.xpParaNivel(2));
        assertEquals(355000, TablaProgresion.xpParaNivel(20));
    }

    @Test
    void ganarExperienciaSubeNivel() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.ganarExperiencia(300);
        assertEquals(2, g.getNivel());
    }

    @Test
    void pvTemporalesSePierdenPrimero() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.curar(5, true);
        assertEquals(5, g.getPuntosVidaTemporales());
        g.recibirDanio(3);
        assertEquals(2, g.getPuntosVidaTemporales());
        assertEquals(10, g.getPuntosVida());
        g.recibirDanio(4);
        assertEquals(0, g.getPuntosVidaTemporales());
        assertEquals(8, g.getPuntosVida());
    }

    @Test
    void curarNoSuperaMaximo() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.curar(100);
        assertEquals(10, g.getPuntosVida());
    }

    @Test
    void calcularCA() {
        Aventurero g = new Guerrero("Thorin", 1, 10, true);
        g.getAtributos().setDestreza(14);
        assertEquals(12, g.calcularCA());
    }
}