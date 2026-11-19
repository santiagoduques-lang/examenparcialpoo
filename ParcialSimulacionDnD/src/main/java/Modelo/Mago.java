/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Mago extends Aventurero {

    private final int espaciosConjuro;

    /**
     * @param nombre          nombre del mago.
     * @param nivel           nivel entre 1 y 20.
     * @param puntosVida      puntos de vida actuales.
     * @param espaciosConjuro espacios de conjuro disponibles (0 o mas).
     */
    public Mago(String nombre, int nivel, int puntosVida, int espaciosConjuro) {
        super(nombre, nivel, puntosVida);
        if (espaciosConjuro < 0) {
            throw new DatoInvalidoException("Los espacios de conjuro no pueden ser negativos.");
        }
        this.espaciosConjuro = espaciosConjuro;
    }

    public int getEspaciosConjuro() {
        return espaciosConjuro;
    }

    @Override
    public String getClasePersonaje() {
        return "Mago";
    }

    @Override
    public String getAtributoEspecial() {
        return "Espacios de conjuro: " + espaciosConjuro;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Mago " + getNombre()
                + " gasta un espacio de conjuro para lanzar una poderosa Bola de Fuego.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(espaciosConjuro * 2);
    }

    @Override
    public int getDadoGolpe() { return 6; }

}