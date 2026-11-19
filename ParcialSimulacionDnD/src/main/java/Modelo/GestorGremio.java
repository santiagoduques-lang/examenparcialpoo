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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Servicio del modelo que concentra la logica del gremio: altas, bajas,
 * busquedas, filtros, reportes y simulacion de combate.
 *
 * <p>La clase <b>no crea</b> la coleccion: recibe por constructor la lista de
 * {@link Aventurero} que administra el Controlador. De esta forma existe una
 * <b>unica coleccion</b> de aventureros en todo el sistema (requisito RNF2) y
 * el servicio trabaja siempre sobre el mismo estado.</p>
 *
 * <p>Relaciones del diagrama de clases:</p>
 * <ul>
 *   <li><b>Asociacion</b> {@code GestorGremio 1 --> 0..* Aventurero}: administra
 *       una coleccion de aventureros.</li>
 *   <li><b>Dependencia</b> de {@code GestorGremio} hacia {@link Aventurero} para
 *       invocar sus metodos polimorficos.</li>
 * </ul>
 *
 * @author Santiago Duque Serrano
 */
public class GestorGremio {

    private static final String SEPARADOR = "========================================================================";

    /** Coleccion unica de aventureros (referencia compartida con el Controlador). */
    private final List<Aventurero> aventureros;

    /**
     * @param aventureros coleccion dinamica que el Controlador comparte con el
     *                    modelo.
     * @throws DatoInvalidoException si la coleccion es nula.
     */
    public GestorGremio(List<Aventurero> aventureros) {
        if (aventureros == null) {
            throw new DatoInvalidoException("La coleccion de aventureros no puede ser nula.");
        }
        this.aventureros = aventureros;
    }

    // ------------------------------------------------------------------
    // Operaciones basicas del gremio
    // ------------------------------------------------------------------

    /**
     * Registra un aventurero evitando nombres duplicados.
     *
     * @param aventurero aventurero a registrar.
     * @throws DatoInvalidoException si es nulo o si el nombre ya existe.
     */
    public void agregarAventurero(Aventurero aventurero) {
        if (aventurero == null) {
            throw new DatoInvalidoException("No se puede registrar un aventurero nulo.");
        }
        if (existeAventurero(aventurero.getNombre())) {
            throw new DatoInvalidoException(
                    "Ya existe un aventurero registrado con el nombre " + aventurero.getNombre() + ".");
        }
        aventureros.add(aventurero);
    }

    /**
     * @param nombre nombre a buscar (sin distinguir mayusculas).
     * @return {@code true} si ya existe un aventurero con ese nombre.
     */
    public boolean existeAventurero(String nombre) {
        return buscarPorNombre(nombre) != null;
    }

    /**
     * @param nombre nombre a buscar.
     * @return el aventurero encontrado o {@code null} si no existe.
     */
    public Aventurero buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Aventurero aventurero : aventureros) {
            if (aventurero.getNombre().equalsIgnoreCase(nombre.trim())) {
                return aventurero;
            }
        }
        return null;
    }

    /**
     * @param nombre nombre del aventurero a retirar del gremio.
     * @return {@code true} si se elimino correctamente.
     */
    public boolean eliminarAventurero(String nombre) {
        Aventurero encontrado = buscarPorNombre(nombre);
        if (encontrado == null) {
            return false;
        }
        return aventureros.remove(encontrado);
    }

    /**
     * @return vista no modificable de los aventureros registrados.
     */
    public List<Aventurero> getAventureros() {
        return Collections.unmodifiableList(aventureros);
    }

    public int getCantidadAventureros() {
        return aventureros.size();
    }

    public boolean estaVacio() {
        return aventureros.isEmpty();
    }

    // ------------------------------------------------------------------
    // Filtros y ordenamientos reutilizables
    // ------------------------------------------------------------------

    /**
     * Filtra los aventureros por el nombre de su clase.
     *
     * @param clase clase a filtrar (por ejemplo "Mago").
     * @return lista (posiblemente vacia) con las coincidencias.
     */
    public List<Aventurero> filtrarPorClase(String clase) {
        List<Aventurero> resultado = new ArrayList<>();
        if (clase == null) {
            return resultado;
        }
        for (Aventurero aventurero : aventureros) {
            if (aventurero.getClasePersonaje().equalsIgnoreCase(clase.trim())) {
                resultado.add(aventurero);
            }
        }
        return resultado;
    }

    /**
     * Filtra los aventureros que alcanzan un nivel minimo.
     *
     * @param nivelMinimo nivel de corte.
     * @return lista de aventureros con nivel mayor o igual al indicado.
     */
    public List<Aventurero> filtrarPorNivelMinimo(int nivelMinimo) {
        List<Aventurero> resultado = new ArrayList<>();
        for (Aventurero aventurero : aventureros) {
            if (aventurero.getNivel() >= nivelMinimo) {
                resultado.add(aventurero);
            }
        }
        return resultado;
    }

    /**
     * @return copia ordenada de mayor a menor nivel (desempata por nombre).
     */
    public List<Aventurero> obtenerOrdenadosPorNivel() {
        List<Aventurero> copia = new ArrayList<>(aventureros);
        copia.sort(Comparator.comparingInt(Aventurero::getNivel)
                .reversed()
                .thenComparing(Aventurero::getNombre));
        return copia;
    }

    /**
     * @return el aventurero con mayor poder de ataque o {@code null} si el
     *         gremio esta vacio.
     */
    public Aventurero obtenerAventureroMasPoderoso() {
        Aventurero masPoderoso = null;
        for (Aventurero aventurero : aventureros) {
            if (masPoderoso == null
                    || aventurero.calcularPoderAtaque() > masPoderoso.calcularPoderAtaque()) {
                masPoderoso = aventurero;
            }
        }
        return masPoderoso;
    }

    /**
     * @return promedio de nivel de los aventureros (0 si no hay registros).
     */
    public double calcularNivelPromedio() {
        if (aventureros.isEmpty()) {
            return 0.0;
        }
        int suma = 0;
        for (Aventurero aventurero : aventureros) {
            suma += aventurero.getNivel();
        }
        return (double) suma / aventureros.size();
    }

    /**
     * @return suma del poder de ataque de todos los aventureros.
     */
    public int calcularPoderTotal() {
        int total = 0;
        for (Aventurero aventurero : aventureros) {
            total += aventurero.calcularPoderAtaque();
        }
        return total;
    }

    /**
     * Cuenta cuantos aventureros hay de cada clase.
     *
     * @return mapa ordenado alfabeticamente (clase -&gt; cantidad).
     */
    public Map<String, Integer> contarAventurerosPorClase() {
        Map<String, Integer> conteo = new TreeMap<>();
        for (Aventurero aventurero : aventureros) {
            String clase = aventurero.getClasePersonaje();
            conteo.merge(clase, 1, Integer::sum);
        }
        return conteo;
    }

    // ------------------------------------------------------------------
    // Simulacion de combate (polimorfismo)
    // ------------------------------------------------------------------

    /**
     * Recorre la coleccion e invoca la accion de combate de cada aventurero.
     *
     * <p>Aqui se evidencia el <b>polimorfismo por sobreescritura</b>: la
     * variable es de tipo {@link Aventurero}, pero en tiempo de ejecucion se
     * ejecuta la version del metodo de la clase concreta (Guerrero, Mago...).</p>
     *
     * @return un mensaje de combate por aventurero, en el orden de registro.
     */
    public List<String> simularCombate() {
        List<String> mensajes = new ArrayList<>();
        for (Aventurero aventurero : aventureros) {
            mensajes.add(aventurero.ejecutarAccionCombate());
        }
        return mensajes;
    }

    // ------------------------------------------------------------------
    // Reportes
    // ------------------------------------------------------------------

    /**
     * Reporte 1: distribucion porcentual de aventureros por clase.
     *
     * @return texto listo para imprimir en consola o en la Vista.
     */
    public String generarReportePorClase() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(SEPARADOR).append('\n');
        reporte.append("  REPORTE 1 - DISTRIBUCION DE AVENTUREROS POR CLASE\n");
        reporte.append(SEPARADOR).append('\n');

        if (aventureros.isEmpty()) {
            reporte.append("  No hay aventureros registrados todavia.\n");
        } else {
            Map<String, Integer> conteo = contarAventurerosPorClase();
            int total = aventureros.size();
            for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
                double porcentaje = entrada.getValue() * 100.0 / total;
                reporte.append(String.format("  %-12s : %2d aventurero(s)  (%5.1f%%)%n",
                        entrada.getKey(), entrada.getValue(), porcentaje));
            }
        }

        reporte.append(SEPARADOR).append('\n');
        reporte.append("  Total de aventureros registrados: ").append(aventureros.size()).append('\n');
        reporte.append(SEPARADOR);
        return reporte.toString();
    }

    /**
     * Reporte 2: estadisticas generales del gremio y top 3 por nivel.
     *
     * @return texto listo para imprimir en consola o en la Vista.
     */
    public String generarReporteEstadistico() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(SEPARADOR).append('\n');
        reporte.append("  REPORTE 2 - ESTADISTICAS DEL GREMIO\n");
        reporte.append(SEPARADOR).append('\n');

        if (aventureros.isEmpty()) {
            reporte.append("  No hay aventureros registrados todavia.\n");
            reporte.append(SEPARADOR);
            return reporte.toString();
        }

        Aventurero masPoderoso = obtenerAventureroMasPoderoso();
        reporte.append(String.format("  Aventureros registrados : %d%n", aventureros.size()));
        reporte.append(String.format("  Nivel promedio          : %.2f%n", calcularNivelPromedio()));
        reporte.append(String.format("  Poder total del gremio  : %d%n", calcularPoderTotal()));
        reporte.append(String.format("  Aventurero mas poderoso : %s (%s) con %d de poder%n",
                masPoderoso.getNombre(), masPoderoso.getClasePersonaje(), masPoderoso.calcularPoderAtaque()));
        reporte.append("  Top 3 por nivel:\n");

        List<Aventurero> ordenados = obtenerOrdenadosPorNivel();
        int limite = Math.min(3, ordenados.size());
        for (int i = 0; i < limite; i++) {
            Aventurero aventurero = ordenados.get(i);
            reporte.append(String.format("    %d. %-18s %-11s (Nivel %2d)%n",
                    i + 1, aventurero.getNombre(), aventurero.getClasePersonaje(), aventurero.getNivel()));
        }

        reporte.append(SEPARADOR);
        return reporte.toString();
    }
}
