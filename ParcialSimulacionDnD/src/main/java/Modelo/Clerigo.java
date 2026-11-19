/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Clerigo extends Aventurero {

    private final String diosVenerado;

    /**
     * @param nombre       nombre del clerigo.
     * @param nivel        nivel entre 1 y 20.
     * @param puntosVida   puntos de vida actuales.
     * @param diosVenerado deidad a la que sirve (texto obligatorio).
     */
    public Clerigo(String nombre, int nivel, int puntosVida, String diosVenerado) {
        super(nombre, nivel, puntosVida);
        if (diosVenerado == null || diosVenerado.isBlank()) {
            throw new DatoInvalidoException("El dios venerado es obligatorio para un clerigo.");
        }
        this.diosVenerado = diosVenerado.trim();
    }

    public String getDiosVenerado() {
        return diosVenerado;
    }

    @Override
    public String getClasePersonaje() {
        return "Clérigo";
    }

    @Override
    public String getAtributoEspecial() {
        return "Dios venerado: " + diosVenerado;
    }

    @Override
    public String ejecutarAccionCombate() {
        return "El Clérigo " + getNombre()
                + " usa Canalizar Divinidad para curar a sus aliados y expulsar muertos vivientes.";
    }

    @Override
    public int calcularPoderAtaque() {
        return super.calcularPoderAtaque(3 + getNivel() / 4);
    }

    @Override
    public int getDadoGolpe() { return 8; }

}