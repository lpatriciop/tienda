/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.ModeloPersona;
import vista.VistaMenuPrincipal;
import vista.VistaPersona;

/**
 *
 * @author Patricio
 */
public class ControladorMenuPrincipal {
    VistaMenuPrincipal vmp;

    public ControladorMenuPrincipal(VistaMenuPrincipal vmp) {
        this.vmp = vmp;
        vmp.setVisible(true);
    }
    
    public void iniciaControl(){
        vmp.getMnuCrudPersonas().addActionListener(l->crudPersonas());
        vmp.getTlbCrudPersonas().addActionListener(l->crudPersonas());
    }
    
    private void crudPersonas(){
        ModeloPersona m= new ModeloPersona();
        VistaPersona v= new VistaPersona();
        vmp.getDesktop().add(v);
        ControlPersona c=new ControlPersona(m, v);
        c.iniciaControl();
    }
}
