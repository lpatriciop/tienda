/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.Image;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Patricio
 */
public class Persona {
    private String idPersona;
    private String nombres;
    private String apellidos;
    private Date fechaNacimiento;
    private int edad;
    private Image foto;

    public int getEdad() {
        if(getFechaNacimiento()!=null){
        LocalDate fechai = getFechaNacimiento().toLocalDate();
        this.edad= Period.between(fechai, LocalDate.now()).getYears();
        return edad;
        }else{
        return 0;
        }
    }

    

    public Persona() {
    }

    public Persona(String idPersona, String nombres, String apellidos, Date fechaNacimiento) {
        this.idPersona = idPersona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    
    
}
