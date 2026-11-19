/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

/**
 *
 * @author santi
 */
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtro de documento que solo permite escribir digitos y limita la cantidad
 * maxima de caracteres.
 *
 * <p>Se aplica sobre los campos numericos del formulario para evitar que el
 * usuario introduzca letras (validacion preventiva). El Controlador sigue
 * validando los rangos, de modo que la regla de negocio no depende de la
 * interfaz.</p>
 *
 * @author Santiago Duque Serrano
 */
public class FiltroSoloNumeros extends DocumentFilter {

    private final int maximoDigitos;

    /**
     * @param maximoDigitos cantidad maxima de digitos permitidos.
     */
    public FiltroSoloNumeros(int maximoDigitos) {
        this.maximoDigitos = maximoDigitos;
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass filtro, int posicion, String texto, AttributeSet atributos)
            throws BadLocationException {
        reemplazar(filtro, posicion, 0, texto, atributos);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass filtro, int posicion, int longitud, String texto, AttributeSet atributos)
            throws BadLocationException {
        reemplazar(filtro, posicion, longitud, texto, atributos);
    }

    private void reemplazar(DocumentFilter.FilterBypass filtro, int posicion, int longitud, String texto,
            AttributeSet atributos) throws BadLocationException {
        String actual = filtro.getDocument().getText(0, filtro.getDocument().getLength());
        String ingreso = texto == null ? "" : texto;
        String resultado = actual.substring(0, posicion) + ingreso
                + actual.substring(Math.min(actual.length(), posicion + longitud));

        if (resultado.matches("\\d{0," + maximoDigitos + "}")) {
            if (longitud > 0) {
                super.replace(filtro, posicion, longitud, ingreso, atributos);
            } else {
                super.insertString(filtro, posicion, ingreso, atributos);
            }
        }
    }
}
