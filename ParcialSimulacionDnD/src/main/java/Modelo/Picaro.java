/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Picaro extends Aventurero {

    private final boolean herramientasLadron;

    /**
     * @param nombre             nombre del picaro.
     * @param nivel              nivel entre 1 y 20.
     * @param puntosVida         puntos de vida actuales.
     * @param herramientasLadron {@code true} si domina herramientas de ladron.
     */
    public Picaro(String nombre, int nivel, int puntosVida, boolean herramientasLadron) {
        super(nombre, nivel, puntosVida);
        this.herramientasLadron = herramientasLadron;
    }

    public boolean isHerramientasLadron() {
        return herramientasLadron;
    }

    @Override
    public String getClasePersonaje() {
        return "Pícaro";
    }

    @Override
    public String getAtributoEspecial() {
        return "Herramientas de ladron: " + (herramientasLadron ? "si" : "no");
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Pícaro " + getNombre()
                + " aprovecha la ventaja táctica para asestar un Ataque Furtivo masivo y se reposiciona.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(herramientasLadron ? 6 : 3);
    }

    @Override
    public int getDadoGolpe() { return 8; }

}