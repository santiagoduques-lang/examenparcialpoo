/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
import java.util.ArrayList;
import java.util.List;

public class Enemigo {
    public static class Ataque {
        public String nombre;
        public int bono;
        public int cantidadDados;
        public int carasDado;
        public TipoDano tipo;
        public Ataque(String nombre, int bono, int cantidadDados, int carasDado, TipoDano tipo) {
            this.nombre = nombre; this.bono = bono; this.cantidadDados = cantidadDados;
            this.carasDado = carasDado; this.tipo = tipo;
        }
    }

    private String nombre;
    private int claseArmadura;
    private int puntosVida;
    private int puntosVidaMaximos;
    private List<Ataque> ataques = new ArrayList<>();
    private double valorDesafio;
    private int experiencia;

    public Enemigo(String nombre, int ca, int pv, double vd, int xp) {
        this.nombre = nombre; this.claseArmadura = ca; this.puntosVida = pv;
        this.puntosVidaMaximos = pv; this.valorDesafio = vd; this.experiencia = xp;
    }

    public void agregarAtaque(Ataque a) { ataques.add(a); }

    public String getNombre() { return nombre; }
    public int getClaseArmadura() { return claseArmadura; }
    public int getPuntosVida() { return puntosVida; }
    public int getPuntosVidaMaximos() { return puntosVidaMaximos; }
    public double getValorDesafio() { return valorDesafio; }
    public int getExperiencia() { return experiencia; }
    public List<Ataque> getAtaques() { return ataques; }

    public boolean estaMuerto() { return puntosVida <= 0; }

    public int recibirDanio(int danio) {
        if (danio < 0) throw new DatoInvalidoException("El danio no puede ser negativo.");
        puntosVida = Math.max(0, puntosVida - danio);
        return puntosVida;
    }

    public int atacar(Aventurero objetivo) {
        if (ataques.isEmpty()) return 0;
        Ataque a = ataques.get(0);
        int tirada = Dado.tirar(20);
        int total = tirada + a.bono;
        if (tirada == 1) return 0;
        if (tirada != 20 && total < objetivo.calcularCA()) return 0;
        int dados = (tirada == 20) ? a.cantidadDados * 2 : a.cantidadDados;
        int danio = Dado.tirarVarios(dados, a.carasDado) + a.bono / 2;
        danio = objetivo.aplicarDanioTipo(danio, a.tipo);
        objetivo.recibirDanio(danio, "ataque de " + nombre);
        return danio;
    }

    @Override
    public String toString() {
        return nombre + " (CA " + claseArmadura + ", PV " + puntosVida + "/" + puntosVidaMaximos + ", VD " + valorDesafio + ")";
    }
}