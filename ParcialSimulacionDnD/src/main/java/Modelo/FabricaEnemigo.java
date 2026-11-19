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

public class FabricaEnemigo {
    public static Enemigo goblin() {
        Enemigo e = new Enemigo("Goblin", 15, 7, 0.25, 50);
        e.agregarAtaque(new Enemigo.Ataque("Cimitarra", 4, 1, 6, TipoDano.CORTANTE));
        return e;
    }
    public static Enemigo orco() {
        Enemigo e = new Enemigo("Orco", 13, 15, 0.5, 100);
        e.agregarAtaque(new Enemigo.Ataque("Hacha", 5, 1, 12, TipoDano.CORTANTE));
        return e;
    }
    public static Enemigo esqueleto() {
        Enemigo e = new Enemigo("Esqueleto", 13, 13, 0.25, 50);
        e.agregarAtaque(new Enemigo.Ataque("Arco corto", 4, 1, 6, TipoDano.PERFORANTE));
        return e;
    }
    public static Enemigo ogro() {
        Enemigo e = new Enemigo("Ogro", 11, 59, 2, 450);
        e.agregarAtaque(new Enemigo.Ataque("Maza", 6, 2, 8, TipoDano.CONTUNDENTE));
        return e;
    }
    public static Enemigo dragonJoven() {
        Enemigo e = new Enemigo("Dragon joven", 18, 178, 10, 5900);
        e.agregarAtaque(new Enemigo.Ataque("Mordisco", 10, 2, 10, TipoDano.PERFORANTE));
        return e;
    }
    public static List<Enemigo> todos() {
        List<Enemigo> l = new ArrayList<>();
        l.add(goblin()); l.add(orco()); l.add(esqueleto()); l.add(ogro()); l.add(dragonJoven());
        return l;
    }
}