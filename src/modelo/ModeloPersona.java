/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.postgresql.util.Base64;

/**
 *
 * @author Patricio
 */
public class ModeloPersona extends Persona{
    private static ConexionPG con=new ConexionPG();
    public ModeloPersona() {
    }

    public ModeloPersona(String idPersona, String nombres, String apellidos, Date fechaNacimiento) {
        super(idPersona, nombres, apellidos, fechaNacimiento);
    }
    public boolean grabar(){
        String foto64=null;
        //Transformar imgagen a base64 para postgresql
        
        ByteArrayOutputStream bos= new ByteArrayOutputStream();
        
        try{
        BufferedImage img =imgBimage(getFoto());
        ImageIO.write(img, "PNG", bos);
        byte[] imgb=bos.toByteArray();
        foto64=Base64.encodeBytes(imgb);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        String sql;
        sql="INSERT INTO persona (idpersona,nombres,apellidos,fechanacimiento,foto)";
        sql+="VALUES('"+getIdPersona() +"','"+getNombres()+"','"+getApellidos()+"','"+getFechaNacimiento()+"','"+foto64+"')";
        if(con.noQuery(sql)==null){
            return true;
        }else{
            return false;
        }
    }
    
    private BufferedImage imgBimage(Image img){
        
        if (img instanceof BufferedImage){
            return (BufferedImage)img;
        }
        BufferedImage bi = new BufferedImage(
                img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB
        );
        
        Graphics2D bGR = bi.createGraphics();
        bGR.drawImage(img, 0, 0,null);
        bGR.dispose();
        return bi;        
    }
    public static List<Persona> listarPersonas(String aguja){
    
        try {
            String query="select * from persona WHERE ";
            query+="UPPER(nombres)  LIKE UPPER('%"+aguja+"%') OR ";
            query+="UPPER(apellidos)  LIKE UPPER('%"+aguja+"%') OR ";
            query+="UPPER(idpersona)  LIKE UPPER('%"+aguja+"%')";
            ResultSet rs=con.query(query);
            List<Persona> lista = new ArrayList<Persona>();
            byte[] bf;
            while(rs.next()){
                Persona persona=new Persona();
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombres(rs.getString("nombres"));
                persona.setApellidos(rs.getString("apellidos"));
                persona.setFechaNacimiento(rs.getDate("fechanacimiento"));
                bf=rs.getBytes("foto");
                
                if(bf!=null){
                    bf=Base64.decode(bf,0,bf.length);
                    try {
                        //OBTENER IMAGEN
                        persona.setFoto(obtenImagen(bf));
                    } catch (IOException ex) {
                        persona.setFoto(null);
                        Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                 persona.setFoto(null);
                }
                lista.add(persona);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Image obtenImagen(byte[] bytes)throws IOException{
        ByteArrayInputStream bis= new ByteArrayInputStream(bytes);
        Iterator it= ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = (ImageReader) it.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis,true);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        return reader.read(0,param);
    }
    
}
