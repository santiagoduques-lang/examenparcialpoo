/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author santi
 */
import Controlador.ControladorGremio;
import Modelo.Aventurero;
import Modelo.Bardo;
import Modelo.Clerigo;
import Modelo.Enemigo;
import Modelo.Explorador;
import Modelo.FabricaEnemigo;
import Modelo.GestorGremio;
import Modelo.Guerrero;
import Modelo.Mago;
import Modelo.Monje;
import Modelo.MotorCombate;
import Modelo.Paladin;
import Modelo.Picaro;
import Modelo.RepositorioGremio;
import Modelo.TipoDano;
import Vista.VentanaGremio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

public final class Main {

    private Main() { }

    public static void main(String[] args) {
        if (args.length > 0 && "--consola".equalsIgnoreCase(args[0])) {
            ejecutarDemostracionConsola();
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName())
                    .log(java.util.logging.Level.WARNING, "No se pudo aplicar el Look and Feel", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            VentanaGremio ventana = new VentanaGremio();
            ControladorGremio controlador = new ControladorGremio(ventana);
            controlador.iniciar();
        });
    }

    private static void ejecutarDemostracionConsola() {
        RepositorioGremio repositorio = new RepositorioGremio();
        List<Aventurero> coleccion = new ArrayList<>();
        GestorGremio gestor = new GestorGremio(coleccion);

        // --- Fase 1: nucleo (atributos, XP, PV) ---
        Guerrero thorin = new Guerrero("Thorin Escudo de Roble", 5, 40, true);
        thorin.getAtributos().setFuerza(16);
        thorin.getAtributos().setConstitucion(14);
        thorin.getAtributos().setDestreza(12);

        Mago gandalf = new Mago("Gandalf el Gris", 5, 24, 4);
        gandalf.getAtributos().setInteligencia(18);

        Clerigo selune = new Clerigo("Selune Alba", 6, 55, "Selune");
        Monje kai = new Monje("Kai del Rio", 5, 45, 5);
        Paladin aurelia = new Paladin("Aurelia Luz", 9, 76, 18);
        Explorador artemis = new Explorador("Artemis Sombra", 4, 40, "Bosque de Neverwinter");
        Picaro vex = new Picaro("Vex la Silenciosa", 8, 58, true);
        Bardo lyra = new Bardo("Lyra Cantoclaro", 3, 30, "Laud elfico");

        List<Aventurero> reclutas = List.of(thorin, gandalf, selune, kai, aurelia, artemis, vex, lyra);

        System.out.println("Registrando aventureros en el gremio...");
        for (Aventurero a : reclutas) {
            gestor.agregarAventurero(a);
            try { repositorio.registrarAventurero(a); }
            catch (IOException ex) { System.err.println("No se pudo persistir: " + ex.getMessage()); }
        }

        // --- Fase 2: combate basico (resistencias) ---
        thorin.agregarResistencia(TipoDano.FUEGO);
        System.out.println("\nCA Thorin: " + thorin.calcularCA());
        System.out.println("Bono competencia N5: " + thorin.getBonoCompetencia());

        // --- Fase 3: combate basico (daño directo) ---
        thorin.recibirDanio(10);
        System.out.println("PV tras recibir danio: " + thorin.getPuntosVida());
        try { repositorio.registrarDescanso(thorin.getNombre(), "corto"); } catch (IOException ex) { }

        // --- Fase 4: progresion ---
        thorin.ganarExperiencia(6500);
        System.out.println("Nivel tras XP: " + thorin.getNivel());
        try { repositorio.registrarSubidaNivel(thorin.getNombre(), thorin.getNivel()); } catch (IOException ex) { }

        // --- Fase 5: descanso ---
        gandalf.descansoLargo();
        System.out.println("Tras descanso largo PV: " + gandalf.getPuntosVida() + " / " + gandalf.getPuntosVidaMaximos());

        // --- Fase 6: enemigos y motor de combate por rondas ---
        System.out.println("\n=== COMBATE AVANZADO (MotorCombate) ===");
        List<Aventurero> heroes = new ArrayList<>();
        heroes.add(new Guerrero("Thorin", 5, 40, true));
        heroes.add(new Mago("Gandalf", 5, 24, 4));
        List<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(FabricaEnemigo.goblin());
        enemigos.add(FabricaEnemigo.orco());
        MotorCombate mc = new MotorCombate(heroes, enemigos);
        String log = mc.ejecutar();
        System.out.println(log);
        try { repositorio.registrarCombate("Combate avanzado vs Goblin+Orco"); } catch (IOException ex) { }

        // --- Reportes UML (estructura dinamica) ---
        System.out.println();
        System.out.println(gestor.generarReportePorClase());
        System.out.println();
        System.out.println(gestor.generarReporteEstadistico());
        System.out.println();
        System.out.println("Historial guardado en: " + repositorio.getRutaArchivo());
    }
}