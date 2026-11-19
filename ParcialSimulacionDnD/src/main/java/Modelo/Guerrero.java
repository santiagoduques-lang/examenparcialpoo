/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Guerrero extends Aventurero {

    private final boolean armaduraPesada;

    /**
     * @param nombre         nombre del guerrero.
     * @param nivel          nivel entre 1 y 20.
     * @param puntosVida     puntos de vida actuales.
     * @param armaduraPesada {@code true} si domina armaduras pesadas.
     */
    public Guerrero(String nombre, int nivel, int puntosVida, boolean armaduraPesada) {
        super(nombre, nivel, puntosVida);
        this.armaduraPesada = armaduraPesada;
    }

    /**
     * Constructor <b>sobrecargado</b>: un guerrero recien reclutado empieza en
     * nivel 1 con 10 puntos de vida.
     *
     * @param nombre         nombre del guerrero.
     * @param armaduraPesada {@code true} si domina armaduras pesadas.
     */
    public Guerrero(String nombre, boolean armaduraPesada) {
        super(nombre);
        this.armaduraPesada = armaduraPesada;
    }

    public boolean isArmaduraPesada() {
        return armaduraPesada;
    }

    @Override
    public String getClasePersonaje() {
        return "Guerrero";
    }

    @Override
    public String getAtributoEspecial() {
        return "Armadura pesada: " + (armaduraPesada ? "si" : "no");
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Guerrero " + getNombre()
                + " usa Oleada de Acción para realizar múltiples ataques físicos continuos.";
    }

    /**
     * Polimorfismo de sobrescritura del calculo de poder: la armadura pesada
     * otorga una bonificacion ofensiva adicional.
     */
    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(armaduraPesada ? 5 : 2);
    }

    @Override
    public int getDadoGolpe() { return 10; }

}