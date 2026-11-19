/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
public class Atributos {

    public static final int MINIMO = 1;
    public static final int MAXIMO = 30;

    private int fuerza;
    private int destreza;
    private int constitucion;
    private int inteligencia;
    private int sabiduria;
    private int carisma;

    public Atributos() {
        this(10, 10, 10, 10, 10, 10);
    }

    public Atributos(int fuerza, int destreza, int constitucion,
                     int inteligencia, int sabiduria, int carisma) {
        setFuerza(fuerza);
        setDestreza(destreza);
        setConstitucion(constitucion);
        setInteligencia(inteligencia);
        setSabiduria(sabiduria);
        setCarisma(carisma);
    }

    public void generarEstandar() {
        setFuerza(15); setDestreza(14); setConstitucion(13);
        setInteligencia(12); setSabiduria(10); setCarisma(8);
    }

    public void generarTirada() {
        setFuerza(Dado.tirar4d6DescartarMenor());
        setDestreza(Dado.tirar4d6DescartarMenor());
        setConstitucion(Dado.tirar4d6DescartarMenor());
        setInteligencia(Dado.tirar4d6DescartarMenor());
        setSabiduria(Dado.tirar4d6DescartarMenor());
        setCarisma(Dado.tirar4d6DescartarMenor());
    }

    public static int modificador(int valor) {
        return (int) Math.floor((valor - 10) / 2.0);
    }

    public int modFuerza() { return modificador(fuerza); }
    public int modDestreza() { return modificador(destreza); }
    public int modConstitucion() { return modificador(constitucion); }
    public int modInteligencia() { return modificador(inteligencia); }
    public int modSabiduria() { return modificador(sabiduria); }
    public int modCarisma() { return modificador(carisma); }

    private void validar(int v, String nombre) {
        if (v < MINIMO || v > MAXIMO) {
            throw new DatoInvalidoException("El atributo " + nombre + " debe estar entre " + MINIMO + " y " + MAXIMO + ".");
        }
    }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { validar(fuerza, "Fuerza"); this.fuerza = fuerza; }
    public int getDestreza() { return destreza; }
    public void setDestreza(int destreza) { validar(destreza, "Destreza"); this.destreza = destreza; }
    public int getConstitucion() { return constitucion; }
    public void setConstitucion(int constitucion) { validar(constitucion, "Constitucion"); this.constitucion = constitucion; }
    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { validar(inteligencia, "Inteligencia"); this.inteligencia = inteligencia; }
    public int getSabiduria() { return sabiduria; }
    public void setSabiduria(int sabiduria) { validar(sabiduria, "Sabiduria"); this.sabiduria = sabiduria; }
    public int getCarisma() { return carisma; }
    public void setCarisma(int carisma) { validar(carisma, "Carisma"); this.carisma = carisma; }

    @Override
    public String toString() {
        return String.format("FUE %d (%+d) DES %d (%+d) CON %d (%+d) INT %d (%+d) SAB %d (%+d) CAR %d (%+d)",
                fuerza, modFuerza(), destreza, modDestreza(), constitucion, modConstitucion(),
                inteligencia, modInteligencia(), sabiduria, modSabiduria(), carisma, modCarisma());
    }
}