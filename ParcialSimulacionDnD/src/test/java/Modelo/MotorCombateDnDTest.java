package Modelo;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MotorCombateDnDTest {

    @Test
    void heroesVencenGoblin() {
        List<Aventurero> heroes = new ArrayList<>();
        heroes.add(new Guerrero("Thorin", 5, 40, true));
        List<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(FabricaEnemigo.goblin());
        MotorCombate mc = new MotorCombate(heroes, enemigos);
        String log = mc.ejecutar();
        assertTrue(log.contains("VICTORIA") || log.contains("muerto"));
        assertTrue(enemigos.get(0).estaMuerto());
    }

    @Test
    void fabricaCrea5Enemigos() {
        assertEquals(5, FabricaEnemigo.todos().size());
    }

    @Test
    void enemigoRecibeDanio() {
        Enemigo g = FabricaEnemigo.goblin();
        g.recibirDanio(100);
        assertTrue(g.estaMuerto());
    }
}