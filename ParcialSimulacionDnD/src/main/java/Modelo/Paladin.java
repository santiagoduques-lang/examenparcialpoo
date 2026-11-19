/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Paladin extends Aventurero {

    private final int puntosFuerza;

    /**
     * @param nombre       nombre del paladin.
     * @param nivel        nivel entre 1 y 20.
     * @param puntosVida   puntos de vida actuales.
     * @param puntosFuerza puntos de fuerza (0 o mas).
     */
    public Paladin(String nombre, int nivel, int puntosVida, int puntosFuerza) {
        super(nombre, nivel, puntosVida);
        if (puntosFuerza < 0) {
            throw new DatoInvalidoException("Los puntos de fuerza no pueden ser negativos.");
        }
        this.puntosFuerza = puntosFuerza;
    }

    public int getPuntosFuerza() {
        return puntosFuerza;
    }

    @Override
    public String getClasePersonaje() {
        return "Paladín";
    }

    @Override
    public String getAtributoEspecial() {
        return "Puntos de fuerza: " + puntosFuerza;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Paladín " + getNombre()
                + " consume magia para imbuir su arma con un Castigo Divino de daño radiante.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(puntosFuerza / 2);
    }

    @Override
    public int getDadoGolpe() { return 10; }

}