/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import modelo.ModeloPersona;
import modelo.Persona;
import sun.swing.table.DefaultTableCellHeaderRenderer;
import vista.VistaPersona;

/**
 *
 * @author Patricio
 */
public class ControlPersona {
    private ModeloPersona modelo;
    private VistaPersona vista;

    public ControlPersona(ModeloPersona modelo, VistaPersona vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);//Mostrar la Vista.
    }
    public void iniciaControl(){
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
             //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
             //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                cargaLista(vista.getTxtBuscar().getText());
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        //Listeners   
        vista.getBntActualizar().addActionListener(l->cargaLista(""));
        vista.getBtnNuevo().addActionListener(l->muestraDialogo());
        vista.getBtnAceptar().addActionListener(l->grabaPersona());
        vista.getTxtBuscar().addKeyListener(kl);
        vista.getBtnExaminar().addActionListener(l->cargarImagen());
    }
    
    
    private void cargaLista(String aguja){
        
        vista.getTblPersonas().setDefaultRenderer(Object.class, new ImagenTabla());
        vista.getTblPersonas().setRowHeight(100);
        DefaultTableCellRenderer renderer= new DefaultTableCellHeaderRenderer();
        
        DefaultTableModel tblModel;
        tblModel=(DefaultTableModel)vista.getTblPersonas().getModel();
        tblModel.setNumRows(0);
        List<Persona> lista=ModeloPersona.listarPersonas(aguja);
        int ncols=tblModel.getColumnCount();
        Holder<Integer> i = new Holder<>(0);
        lista.stream().forEach(p1->{
       //   String[] persona={p1.getIdPersona(),p1.getNombres(),p1.getApellidos(),String.valueOf(p1.getEdad())};
           
           tblModel.addRow(new Object[ncols]);
           vista.getTblPersonas().setValueAt(p1.getIdPersona(), i.value , 0);
            vista.getTblPersonas().setValueAt(p1.getNombres(), i.value , 1);
            //completar datos
           Image img = p1.getFoto();
           if(img!=null){
                Image newimg = img.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(newimg);
                renderer.setIcon(icon);
                vista.getTblPersonas().setValueAt(new JLabel(icon), i.value, 4);
           }else{
               vista.getTblPersonas().setValueAt(null, i.value, 4);
           }
           i.value++;
          ;
        });
    }
    
     private void cargaLista2(String aguja){
        
        
        DefaultTableModel tblModel;
        tblModel=(DefaultTableModel)vista.getTblPersonas().getModel();
        tblModel.setNumRows(0);
        List<Persona> lista=ModeloPersona.listarPersonas(aguja);
        lista.stream().forEach(p1->{
          String[] persona={p1.getIdPersona(),p1.getNombres(),p1.getApellidos(),String.valueOf(p1.getEdad())};
         
         tblModel.addRow(persona);
        });
    }
    private void muestraDialogo(){
    vista.getDlgPersona().setSize(600,300);
    vista.getDlgPersona().setTitle("NUEVA PERSONA");
    vista.getDlgPersona().setLocationRelativeTo(vista);
    vista.getTxtID().setText("");
    vista.getTxtNombres().setText("");
    vista.getTxtApellidos().setText("");
    vista.getDlgPersona().setVisible(true);
    }
    private void grabaPersona(){
    String idpersona=vista.getTxtID().getText();
    String nombres=vista.getTxtNombres().getText();
    String apellidos=vista.getTxtApellidos().getText();
    Instant instant=vista.getDtcFecha().getDate().toInstant();
    ZoneId zid=ZoneId.of("America/Guayaquil");
    ZonedDateTime zdt=ZonedDateTime.ofInstant(instant, zid);
    Date fechaNacimiento=Date.valueOf(zdt.toLocalDate());
    ModeloPersona persona = new ModeloPersona(idpersona, nombres, apellidos, fechaNacimiento);
    //Foto
    ImageIcon ic= (ImageIcon)vista.getLblFoto().getIcon();
    persona.setFoto(ic.getImage());
    if(persona.grabar()){
        cargaLista("");
        vista.getDlgPersona().setVisible(false);
        JOptionPane.showMessageDialog(vista, "Registro grabado satisfactoriamente");
        }else{
       JOptionPane.showMessageDialog(vista, "ERROR");
    }
    
    }
    
    //Cargar Imagen desde el label
    private void cargarImagen(){
    
        JFileChooser jfc= new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado=jfc.showOpenDialog(null);
        if(estado==JFileChooser.APPROVE_OPTION){
            try {
                Image icono = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vista.getLblFoto().getWidth(),
                        vista.getLblFoto().getHeight(),
                        Image.SCALE_DEFAULT
                );
             Icon ic=new ImageIcon(icono);
             vista.getLblFoto().setIcon(ic);
             vista.getLblFoto().updateUI();
            } catch (IOException ex) {
                Logger.getLogger(ControlPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }
}
