/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

/**
 *
 * @author santi
 */
import Modelo.Aventurero;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla que adapta la coleccion de {@link Aventurero} al componente
 * {@code JTable}.
 *
 * <p>Se usa {@link AbstractTableModel} para que la Vista no manipule la
 * coleccion del Controlador: recibe una copia y notifica los cambios con
 * {@code fireTableDataChanged()}. Esto mantiene el principio de
 * responsabilidad unica dentro de MVC.</p>
 *
 * @author Santiago Duque Serrano
 */
public class ModeloTablaAventurero extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {
        "Clase", "Nombre", "Nivel", "Puntos de vida", "Atributo especial", "Poder"
    };

    private List<Aventurero> aventureros = new ArrayList<>();

    /**
     * Reemplaza los datos de la tabla.
     *
     * @param aventureros lista actual de aventureros del gremio.
     */
    public void actualizar(List<Aventurero> aventureros) {
        this.aventureros = new ArrayList<>(aventureros);
        fireTableDataChanged();
    }

    /**
     * @param fila indice de la fila en el modelo.
     * @return el aventurero de esa fila o {@code null} si el indice no existe.
     */
    public Aventurero getAventurero(int fila) {
        if (fila < 0 || fila >= aventureros.size()) {
            return null;
        }
        return aventureros.get(fila);
    }

    @Override
    public int getRowCount() {
        return aventureros.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        switch (columna) {
            case 2:
            case 3:
            case 5:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Aventurero aventurero = aventureros.get(fila);
        switch (columna) {
            case 0:
                return aventurero.getClasePersonaje();
            case 1:
                return aventurero.getNombre();
            case 2:
                return aventurero.getNivel();
            case 3:
                return aventurero.getPuntosVida();
            case 4:
                return aventurero.getAtributoEspecial();
            case 5:
                return aventurero.calcularPoderAtaque();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
