/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Monje extends Aventurero {

    private final int puntosKi;

    /**
     * @param nombre   nombre del monje.
     * @param nivel    nivel entre 1 y 20.
     * @param puntosVida puntos de vida actuales.
     * @param puntosKi puntos de Ki disponibles (0 o mas).
     */
    public Monje(String nombre, int nivel, int puntosVida, int puntosKi) {
        super(nombre, nivel, puntosVida);
        if (puntosKi < 0) {
            throw new DatoInvalidoException("Los puntos de Ki no pueden ser negativos.");
        }
        this.puntosKi = puntosKi;
    }

    public int getPuntosKi() {
        return puntosKi;
    }

    @Override
    public String getClasePersonaje() {
        return "Monje";
    }

    @Override
    public String getAtributoEspecial() {
        return "Puntos de Ki: " + puntosKi;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Monje " + getNombre()
                + " gasta puntos de Ki para ejecutar una Ráfaga de Golpes y un Ataque Aturdidor.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(puntosKi);
    }

    @Override
    public int getDadoGolpe() { return 8; }

}