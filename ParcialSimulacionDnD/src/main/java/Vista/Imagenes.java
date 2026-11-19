/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

/**
 *
 * @author santi
 */

import java.net.URL;
import javax.swing.ImageIcon;

public final class Imagenes {
    private Imagenes() { }

    public static ImageIcon cargar(String nombre) {
        String ruta = "/imagenes/" + nombre;
        URL url = Imagenes.class.getResource(ruta);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    public static ImageIcon clase(String nombreClase) {
        return cargar(nombreClase + ".png");
    }

    public static ImageIcon dado(int caras) {
        return cargar("d" + caras + ".png");
    }
}