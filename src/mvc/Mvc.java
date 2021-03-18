/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import controlador.ControlPersona;
import controlador.ControladorMenuPrincipal;
import modelo.ModeloPersona;
import vista.VistaMenuPrincipal;
import vista.VistaPersona;

/**
 *
 * @author Patricio
 */
public class Mvc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        VistaMenuPrincipal vista = new VistaMenuPrincipal();
        ControladorMenuPrincipal c = new ControladorMenuPrincipal(vista);
        c.iniciaControl();
    }
    
}
