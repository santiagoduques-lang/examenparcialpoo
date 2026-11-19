/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public interface IAccionCombate {

    /**
     * Constante de la interfaz ({@code public static final} implicito).
     */
    String SISTEMA = "Calabozos y Dragones 5e";

    /**
     * Ejecuta la mecanica de combate propia de la clase del aventurero.
     *
     * @return mensaje narrativo del turno de combate.
     */
    String ejecutarAccionCombate();

    /**
     * Nombre de la clase de personaje (Guerrero, Mago, Clerigo...).
     *
     * @return nombre legible de la clase.
     */
    String getClasePersonaje();
}
