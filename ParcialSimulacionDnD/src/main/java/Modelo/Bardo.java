/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Bardo extends Aventurero {

    private final String instrumentoMusical;

    /**
     * @param nombre             nombre del bardo.
     * @param nivel              nivel entre 1 y 20.
     * @param puntosVida         puntos de vida actuales.
     * @param instrumentoMusical instrumento que interpreta (texto obligatorio).
     */
    public Bardo(String nombre, int nivel, int puntosVida, String instrumentoMusical) {
        super(nombre, nivel, puntosVida);
        if (instrumentoMusical == null || instrumentoMusical.isBlank()) {
            throw new DatoInvalidoException("El instrumento musical es obligatorio para un bardo.");
        }
        this.instrumentoMusical = instrumentoMusical.trim();
    }

    public String getInstrumentoMusical() {
        return instrumentoMusical;
    }

    @Override
    public String getClasePersonaje() {
        return "Bardo";
    }

    @Override
    public String getAtributoEspecial() {
        return "Instrumento musical: " + instrumentoMusical;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Bardo " + getNombre()
                + " toca su instrumento para otorgar Inspiración Bárdica a un aliado.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(1 + getNivel() / 5);
    }

    @Override
    public int getDadoGolpe() { return 8; }

}