/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class TablaProgresion {

    /** XP necesaria para alcanzar cada nivel (indice = nivel). */
    public static final int[] XP_POR_NIVEL = new int[] {
        0, 0, 300, 900, 2700, 6500, 14000, 23000, 34000, 48000, 64000,
        85000, 100000, 120000, 140000, 165000, 195000, 225000, 265000, 305000, 355000
    };

    public static int xpParaNivel(int nivel) {
        if (nivel < 1 || nivel > 20) throw new DatoInvalidoException("Nivel fuera de rango.");
        return XP_POR_NIVEL[nivel];
    }

    public static int bonoCompetencia(int nivel) {
        if (nivel <= 4) return 2;
        if (nivel <= 8) return 3;
        if (nivel <= 12) return 4;
        if (nivel <= 16) return 5;
        return 6;
    }
}