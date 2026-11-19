/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Explorador extends Aventurero {

    private final String terrenoFavorecido;

    /**
     * @param nombre            nombre del explorador.
     * @param nivel             nivel entre 1 y 20.
     * @param puntosVida        puntos de vida actuales.
     * @param terrenoFavorecido terreno en el que se especializa.
     */
    public Explorador(String nombre, int nivel, int puntosVida, String terrenoFavorecido) {
        super(nombre, nivel, puntosVida);
        if (terrenoFavorecido == null || terrenoFavorecido.isBlank()) {
            throw new DatoInvalidoException("El terreno favorecido es obligatorio para un explorador.");
        }
        this.terrenoFavorecido = terrenoFavorecido.trim();
    }

    public String getTerrenoFavorecido() {
        return terrenoFavorecido;
    }

    @Override
    public String getClasePersonaje() {
        return "Explorador";
    }

    @Override
    public String getAtributoEspecial() {
        return "Terreno favorecido: " + terrenoFavorecido;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Explorador " + getNombre()
                + " aplica Marca del Cazador sobre el objetivo para maximizar su daño sostenido.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(4);
    }

    @Override
    public int getDadoGolpe() { return 10; }

}