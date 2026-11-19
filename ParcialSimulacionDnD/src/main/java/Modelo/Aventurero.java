/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

/**
 *
 * @author santi
 */
import java.util.ArrayList;
import java.util.List;

public abstract class Aventurero implements IAccionCombate {

    public static final int NIVEL_MINIMO = 1;
    public static final int NIVEL_MAXIMO = 20;
    public static final int VIDA_MINIMA = 0;
    public static final int VIDA_MAXIMA = 999;
    private static final int VIDA_INICIAL = 10;
    private static int totalAventureros = 0;

    private String nombre;
    private int nivel;
    private int puntosVida;
    private int puntosVidaMaximos;
    private int puntosVidaTemporales;
    private int experiencia;
    private Atributos atributos;
    private int dadosGolpeRestantes;
    private int exitosMuerte;
    private int fallosMuerte;
    private boolean muerto;
    private List<TipoDano> resistencias = new ArrayList<>();
    private List<TipoDano> vulnerabilidades = new ArrayList<>();
    private List<TipoDano> inmunidades = new ArrayList<>();
    // (mecanicas de condiciones, conjuros y habilidades eliminadas: no actuan en la ventana)

    public Aventurero(String nombre, int nivel, int puntosVida) {
        setNombre(nombre);
        setNivel(nivel);
        setPuntosVida(puntosVida);
        this.puntosVidaMaximos = puntosVida;
        this.puntosVidaTemporales = 0;
        this.experiencia = 0;
        this.atributos = new Atributos();
        this.dadosGolpeRestantes = nivel;
        this.exitosMuerte = 0;
        this.fallosMuerte = 0;
        this.muerto = false;
        totalAventureros++;
    }

    public Aventurero(String nombre) {
        this(nombre, NIVEL_MINIMO, VIDA_INICIAL);
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre del aventurero es obligatorio.");
        }
        if (nombre.trim().length() > 40) {
            throw new DatoInvalidoException("El nombre no puede superar los 40 caracteres.");
        }
        this.nombre = nombre.trim();
    }

    public int getNivel() { return nivel; }

    public void setNivel(int nivel) {
        if (nivel < NIVEL_MINIMO || nivel > NIVEL_MAXIMO) {
            throw new DatoInvalidoException("El nivel debe estar entre " + NIVEL_MINIMO + " y " + NIVEL_MAXIMO + ".");
        }
        this.nivel = nivel;
    }

    public int getPuntosVida() { return puntosVida; }

    public void setPuntosVida(int puntosVida) {
        if (puntosVida < VIDA_MINIMA || puntosVida > VIDA_MAXIMA) {
            throw new DatoInvalidoException("Los puntos de vida deben estar entre " + VIDA_MINIMA + " y " + VIDA_MAXIMA + ".");
        }
        this.puntosVida = puntosVida;
    }

    public int getPuntosVidaMaximos() { return puntosVidaMaximos; }
    public void setPuntosVidaMaximos(int v) { this.puntosVidaMaximos = v; }

    public int getPuntosVidaTemporales() { return puntosVidaTemporales; }
    public void setPuntosVidaTemporales(int v) { this.puntosVidaTemporales = Math.max(0, v); }

    public int getExperiencia() { return experiencia; }
    public void setExperiencia(int xp) { if (xp < 0) throw new DatoInvalidoException("La experiencia no puede ser negativa."); this.experiencia = xp; }

    public Atributos getAtributos() { return atributos; }
    public void setAtributos(Atributos a) { if (a == null) throw new DatoInvalidoException("Atributos obligatorios."); this.atributos = a; }

    public int getDadosGolpeRestantes() { return dadosGolpeRestantes; }
    public void setDadosGolpeRestantes(int v) { this.dadosGolpeRestantes = Math.max(0, v); }

    public static int getTotalAventureros() { return totalAventureros; }

    public int getBonoCompetencia() { return TablaProgresion.bonoCompetencia(nivel); }

    public abstract int getDadoGolpe();

    public void ganarExperiencia(int xp) {
        if (xp < 0) throw new DatoInvalidoException("La experiencia no puede ser negativa.");
        this.experiencia += xp;
        while (nivel < NIVEL_MAXIMO && experiencia >= TablaProgresion.xpParaNivel(nivel + 1)) {
            subirNivel();
        }
    }

    protected void subirNivel() {
        if (nivel >= NIVEL_MAXIMO) return;
        setNivel(nivel + 1);
        dadosGolpeRestantes++;
        int curacion = (getDadoGolpe() / 2 + 1) + atributos.modConstitucion();
        if (curacion < 1) curacion = 1;
        puntosVidaMaximos += curacion;
        puntosVida = puntosVidaMaximos;
    }


    public int calcularCA() {
        return 10 + atributos.modDestreza();
    }

    public boolean estaConsciente() { return puntosVida > 0; }

    public int calcularPoderAtaque() { return calcularPoderAtaque(0); }

    public int calcularPoderAtaque(int bonificacion) {
        if (bonificacion < 0) throw new DatoInvalidoException("La bonificacion no puede ser negativa.");
        return (nivel * 2) + (puntosVida / 10) + bonificacion;
    }

    public int recibirDanio(int danio) { return recibirDanio(danio, "ataque desconocido"); }

    public int recibirDanio(int danio, String fuente) {
        if (danio < 0) throw new DatoInvalidoException("El danio no puede ser negativo.");
        if (muerto) return 0;
        if (puntosVidaTemporales > 0) {
            if (danio <= puntosVidaTemporales) { puntosVidaTemporales -= danio; return puntosVida; }
            danio -= puntosVidaTemporales;
            puntosVidaTemporales = 0;
        }
        if (puntosVida == 0) {
            // ya inconsciente: cada impacto = fallo
            fallosMuerte += 1;
            if (fallosMuerte >= 3) muerto = true;
            if (danio >= puntosVidaMaximos) muerto = true;
            return 0;
        }
        int antes = puntosVida;
        puntosVida = Math.max(VIDA_MINIMA, puntosVida - danio);
        if (puntosVida == 0) {
            int sobrante = danio - antes;
            if (sobrante >= puntosVidaMaximos) muerto = true;
        }
        return puntosVida;
    }

    public int curar(int cantidad) { return curar(cantidad, false); }

    public int curar(int cantidad, boolean temporales) {
        if (cantidad < 0) throw new DatoInvalidoException("La curacion no puede ser negativa.");
        if (temporales) {
            puntosVidaTemporales = Math.max(puntosVidaTemporales, cantidad);
            return puntosVida;
        }
        if (puntosVida == 0 && cantidad > 0) {
            puntosVida = Math.min(puntosVidaMaximos, cantidad);
            exitosMuerte = 0; fallosMuerte = 0;
            return puntosVida;
        }
        puntosVida = Math.min(puntosVidaMaximos, puntosVida + cantidad);
        return puntosVida;
    }

    public int tirarSalvacion(int modificador) {
        return Dado.tirar(20) + modificador;
    }

    public boolean estaMuerto() { return muerto; }
    public boolean estaInconsciente() { return puntosVida == 0 && !muerto; }
    public int getExitosMuerte() { return exitosMuerte; }
    public int getFallosMuerte() { return fallosMuerte; }

    public List<TipoDano> getResistencias() { return resistencias; }
    public List<TipoDano> getVulnerabilidades() { return vulnerabilidades; }
    public List<TipoDano> getInmunidades() { return inmunidades; }

    public boolean esResistente(TipoDano t) { return resistencias.contains(t); }
    public boolean esVulnerable(TipoDano t) { return vulnerabilidades.contains(t); }
    public boolean esInmune(TipoDano t) { return inmunidades.contains(t); }

    public void agregarResistencia(TipoDano t) { if (!resistencias.contains(t)) resistencias.add(t); }
    public void agregarVulnerabilidad(TipoDano t) { if (!vulnerabilidades.contains(t)) vulnerabilidades.add(t); }
    public void agregarInmunidad(TipoDano t) { if (!inmunidades.contains(t)) inmunidades.add(t); }

    public int aplicarDanioTipo(int danio, TipoDano tipo) {
        if (esInmune(tipo)) return 0;
        if (esResistente(tipo)) return danio / 2;
        if (esVulnerable(tipo)) return danio * 2;
        return danio;
    }

    public int tirarIniciativa() {
        return Dado.tirar(20) + atributos.modDestreza();
    }

    public int atacar(Aventurero objetivo) {
        return atacar(objetivo, false, TipoDano.CORTANTE);
    }

    public int atacar(Aventurero objetivo, boolean ventaja) {
        return atacar(objetivo, ventaja, TipoDano.CORTANTE);
    }

    public int atacar(Aventurero objetivo, boolean ventaja, TipoDano tipo) {
        if (objetivo == null) throw new DatoInvalidoException("Objetivo obligatorio.");
        if (objetivo.estaMuerto()) throw new DatoInvalidoException("El objetivo ya esta muerto.");
        int d1 = Dado.tirar(20);
        int d2 = ventaja ? Dado.tirar(20) : d1;
        int tirada = ventaja ? Math.max(d1, d2) : d1;
        boolean critico = tirada == 20;
        boolean fallo = tirada == 1;
        int bono = getBonoCompetencia() + atributos.modFuerza();
        int total = tirada + bono - (ventaja ? 0 : 0);
        // ventaja ya reflejada en tirada
        if (fallo) return 0;
        if (!critico && tirada + bono < objetivo.calcularCA()) return 0;
        int dados = critico ? 2 : 1;
        int danio = Dado.tirarVarios(dados, 6) + atributos.modFuerza();
        danio = objetivo.aplicarDanioTipo(danio, tipo);
        if (danio < 0) danio = 0;
        objetivo.recibirDanio(danio, "ataque de " + getNombre());
        // death saves si llego a 0
        if (objetivo.getPuntosVida() == 0 && !objetivo.estaMuerto()) {
            objetivo.registrarImpactoACero(danio, critico);
        }
        return danio;
    }

    protected void registrarImpactoACero(int danio, boolean critico) {
        if (danio >= puntosVidaMaximos) { muerto = true; return; }
        if (critico) fallosMuerte += 2; else fallosMuerte += 1;
        if (fallosMuerte >= 3) muerto = true;
    }

    public void salvacionContraMuerte() {
        if (!estaInconsciente()) return;
        int t = Dado.tirar(20);
        if (t == 20) { puntosVida = 1; exitosMuerte = 0; fallosMuerte = 0; return; }
        if (t == 1) { fallosMuerte += 2; }
        else if (t >= 10) { exitosMuerte++; }
        else { fallosMuerte++; }
        if (exitosMuerte >= 3) { exitosMuerte = 0; fallosMuerte = 0; /* estable */ }
        if (fallosMuerte >= 3) { muerto = true; }
    }




    public void descansoCorto() {
        // recuperar mitad de dados de golpe (min 1)
        int rec = Math.max(1, getDadoGolpe() > 0 ? (dadosGolpeRestantes / 2) : 0);
        dadosGolpeRestantes = Math.min(nivel, dadosGolpeRestantes + rec);
        // recursos especificos se manejan en subclases
    }

    public void descansoLargo() {
        puntosVida = puntosVidaMaximos;
        puntosVidaTemporales = 0;
        int rec = Math.max(1, nivel / 2);
        dadosGolpeRestantes = Math.min(nivel, dadosGolpeRestantes + rec);
        exitosMuerte = 0; fallosMuerte = 0;
    }
    public int atacar(Enemigo objetivo) {
        return atacar(objetivo, false, TipoDano.CORTANTE);
    }

    public int atacar(Enemigo objetivo, boolean ventaja, TipoDano tipo) {
        if (objetivo == null) throw new DatoInvalidoException("Objetivo obligatorio.");
        if (objetivo.estaMuerto()) throw new DatoInvalidoException("El objetivo ya esta muerto.");
        int d1 = Dado.tirar(20);
        int tirada = ventaja ? Math.max(d1, Dado.tirar(20)) : d1;
        boolean critico = tirada == 20;
        boolean fallo = tirada == 1;
        int bono = getBonoCompetencia() + atributos.modFuerza();
        if (fallo) return 0;
        if (!critico && tirada + bono < objetivo.getClaseArmadura()) return 0;
        int dados = critico ? 2 : 1;
        int danio = Dado.tirarVarios(dados, 6) + atributos.modFuerza();
        // Enemigo no tiene resistencias modeladas; aplicar tipo si inmune? no
        objetivo.recibirDanio(danio);
        return danio;
    }
    public abstract String getAtributoEspecial();

    @Override
    public String toString() {
        return String.format("%-11s | %-18s | Nivel %2d | PV %3d/%3d | %-40s | Poder %2d",
                getClasePersonaje(), nombre, nivel, puntosVida, puntosVidaMaximos, getAtributoEspecial(), calcularPoderAtaque());
    }
}
