/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Dado {

    private Dado() { }

    public static int tirar(int caras) {
        if (caras < 1) throw new DatoInvalidoException("El dado debe tener al menos 1 cara.");
        return (int) (Math.random() * caras) + 1;
    }

    public static int tirar4d6DescartarMenor() {
        int suma = 0, menor = 7;
        for (int i = 0; i < 4; i++) {
            int d = tirar(6);
            suma += d;
            if (d < menor) menor = d;
        }
        return suma - menor;
    }

    public static int tirarVarios(int cantidad, int caras) {
        int total = 0;
        for (int i = 0; i < cantidad; i++) total += tirar(caras);
        return total;
    }
}