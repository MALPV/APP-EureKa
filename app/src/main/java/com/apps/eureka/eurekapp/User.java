package com.apps.eureka.eurekapp;

/**
 * Clase Modelo
 */
public class User {

    private String nombre;
    private String apellidos;
    private String numDoc;
    private String especialidad;
    private String numContact;
    private String correo;
    private String pass;
    private String uid;
    private String descripServicio;
    private String precio;

    public User() {
    }

    public User(String nombre, String apellidos, String numDoc, String especialidad,
                String numContact, String correo, String pass, String uid, String descripServicio, String precio) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numDoc = numDoc;
        this.especialidad = especialidad;
        this.numContact = numContact;
        this.correo = correo;
        this.pass = pass;
        this.uid = uid;
        this.descripServicio = descripServicio;
        this.precio = precio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescripServicio() {
        return descripServicio;
    }

    public void setDescripServicio(String descripServicio) {
        this.descripServicio = descripServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumContact() {
        return numContact;
    }

    public void setNumContact(String numContact) {
        this.numContact = numContact;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
