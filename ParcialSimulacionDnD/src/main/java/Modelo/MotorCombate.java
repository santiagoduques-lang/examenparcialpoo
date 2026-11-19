/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
import java.util.ArrayList;
import java.util.List;

public class MotorCombate {

    private List<Aventurero> heroes;
    private List<Enemigo> enemigos;
    private StringBuilder bitacora = new StringBuilder();

    public MotorCombate(List<Aventurero> heroes, List<Enemigo> enemigos) {
        this.heroes = new ArrayList<>(heroes);
        this.enemigos = new ArrayList<>(enemigos);
    }

    public String ejecutar() {
        bitacora.append("=== INICIO DEL COMBATE ===\n");
        int ronda = 1;
        while (heroesVivos() && enemigosVivos() && ronda <= 50) {
            bitacora.append("--- Ronda ").append(ronda).append(" ---\n");
            // heroes atacan
            for (Aventurero h : heroes) {
                if (h.estaMuerto()) continue;
                Enemigo objetivo = primerEnemigoVivo();
                if (objetivo == null) break;
                int d = h.atacar(objetivo);
                bitacora.append(h.getNombre()).append(" ataca a ").append(objetivo.getNombre())
                       .append(" -> ").append(d).append(" de dano. PV enemigo: ").append(objetivo.getPuntosVida()).append("\n");
                if (objetivo.estaMuerto()) bitacora.append(objetivo.getNombre()).append(" ha muerto.\n");
            }
            // enemigos atacan
            for (Enemigo e : enemigos) {
                if (e.estaMuerto()) continue;
                Aventurero objetivo = primerHeroeVivo();
                if (objetivo == null) break;
                int d = e.atacar(objetivo);
                bitacora.append(e.getNombre()).append(" ataca a ").append(objetivo.getNombre())
                       .append(" -> ").append(d).append(" de dano. PV heroe: ").append(objetivo.getPuntosVida()).append("\n");
                if (objetivo.estaMuerto()) bitacora.append(objetivo.getNombre()).append(" ha caido.\n");
            }
            ronda++;
        }
        // reparto de XP
        if (heroesVivos()) {
            int xpTotal = 0;
            for (Enemigo e : enemigos) xpTotal += e.getExperiencia();
            int porHeroe = heroesVivosCount() > 0 ? xpTotal / heroesVivosCount() : 0;
            for (Aventurero h : heroes) {
                if (!h.estaMuerto()) {
                    h.ganarExperiencia(porHeroe);
                    bitacora.append(h.getNombre()).append(" gana ").append(porHeroe).append(" XP (total ").append(h.getExperiencia()).append(").\n");
                }
            }
            bitacora.append("=== VICTORIA DE LOS HEROES ===\n");
        } else {
            bitacora.append("=== DERROTA ===\n");
        }
        return bitacora.toString();
    }

    private boolean heroesVivos() { for (Aventurero h : heroes) if (!h.estaMuerto()) return true; return false; }
    private boolean enemigosVivos() { for (Enemigo e : enemigos) if (!e.estaMuerto()) return true; return false; }
    private int heroesVivosCount() { int c=0; for (Aventurero h : heroes) if (!h.estaMuerto()) c++; return c; }
    private Enemigo primerEnemigoVivo() { for (Enemigo e : enemigos) if (!e.estaMuerto()) return e; return null; }
    private Aventurero primerHeroeVivo() { for (Aventurero h : heroes) if (!h.estaMuerto()) return h; return null; }
}