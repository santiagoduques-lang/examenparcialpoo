package Modelo;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de polimorfismo: una unica coleccion de tipo {@link Aventurero}
 * invoca la accion de combate de cada subclase.
 *
 * @author Santiago Duque Serrano
 */
class PolimorfismoTest {

    private List<Aventurero> aventureros;

    @BeforeEach
    void prepararGremio() {
        aventureros = new ArrayList<>();
        aventureros.add(new Guerrero("Thorin", 7, 82, true));
        aventureros.add(new Mago("Elminster", 12, 60, 8));
        aventureros.add(new Clerigo("Selune Alba", 6, 55, "Selune"));
        aventureros.add(new Monje("Kai", 5, 45, 5));
        aventureros.add(new Paladin("Aurelia", 9, 76, 18));
        aventureros.add(new Explorador("Artemis", 4, 40, "Bosque de Neverwinter"));
        aventureros.add(new Picaro("Vex", 8, 58, true));
        aventureros.add(new Bardo("Lyra", 3, 30, "Laud elfico"));
    }

    @Test
    @DisplayName("Cada subclase devuelve el mensaje de combate del requerimiento RF3")
    void mensajesDeCombatePorClase() {
        assertEquals("El Guerrero Thorin usa Oleada de Acción para realizar múltiples ataques físicos continuos.",
                aventureros.get(0).ejecutarAccionCombate());
        assertEquals("El Mago Elminster gasta un espacio de conjuro para lanzar una poderosa Bola de Fuego.",
                aventureros.get(1).ejecutarAccionCombate());
        assertEquals("El Clérigo Selune Alba usa Canalizar Divinidad para curar a sus aliados y expulsar muertos vivientes.",
                aventureros.get(2).ejecutarAccionCombate());
        assertEquals("El Monje Kai gasta puntos de Ki para ejecutar una Ráfaga de Golpes y un Ataque Aturdidor.",
                aventureros.get(3).ejecutarAccionCombate());
        assertEquals("El Paladín Aurelia consume magia para imbuir su arma con un Castigo Divino de daño radiante.",
                aventureros.get(4).ejecutarAccionCombate());
        assertEquals("El Explorador Artemis aplica Marca del Cazador sobre el objetivo para maximizar su daño sostenido.",
                aventureros.get(5).ejecutarAccionCombate());
        assertEquals("El Pícaro Vex aprovecha la ventaja táctica para asestar un Ataque Furtivo masivo y se reposiciona.",
                aventureros.get(6).ejecutarAccionCombate());
        assertEquals("El Bardo Lyra toca su instrumento para otorgar Inspiración Bárdica a un aliado.",
                aventureros.get(7).ejecutarAccionCombate());
    }

    @Test
    @DisplayName("La referencia IAccionCombate despacha al metodo de la subclase")
    void polimorfismoConInterfaz() {
        IAccionCombate accion = new Picaro("Vex", 8, 58, true);

        assertEquals("Pícaro", accion.getClasePersonaje());
        assertTrue(accion.ejecutarAccionCombate().contains("Ataque Furtivo"));
        assertEquals(IAccionCombate.SISTEMA, "Calabozos y Dragones 5e");
    }

    @Test
    @DisplayName("Cada clase reporta su atributo especial propio")
    void atributosEspecialesPorClase() {
        assertEquals("Armadura pesada: si", aventureros.get(0).getAtributoEspecial());
        assertEquals("Espacios de conjuro: 8", aventureros.get(1).getAtributoEspecial());
        assertEquals("Dios venerado: Selune", aventureros.get(2).getAtributoEspecial());
        assertEquals("Puntos de Ki: 5", aventureros.get(3).getAtributoEspecial());
        assertEquals("Puntos de fuerza: 18", aventureros.get(4).getAtributoEspecial());
        assertEquals("Terreno favorecido: Bosque de Neverwinter", aventureros.get(5).getAtributoEspecial());
        assertEquals("Herramientas de ladron: si", aventureros.get(6).getAtributoEspecial());
        assertEquals("Instrumento musical: Laud elfico", aventureros.get(7).getAtributoEspecial());
    }

    @Test
    @DisplayName("El calculo de poder es polimorfico y positivo")
    void poderDeAtaquePolimorfico() {
        for (Aventurero aventurero : aventureros) {
            assertTrue(aventurero.calcularPoderAtaque() > 0,
                    "El poder de " + aventurero.getNombre() + " debe ser positivo");
        }
    }
}
