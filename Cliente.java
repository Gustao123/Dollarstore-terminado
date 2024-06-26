/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author juriel
 */
public class Cliente {
    private int cliente_id;
    private String Nombre;
    private String Apellido;
    private String telefono;
    private String direccion;

    public Cliente(int cliente_id, String Nombre, 
            String Apellido, String telefono, String direccion) {
        this.cliente_id= cliente_id;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Cliente(String Nombre, String Apellido
            , String telefono, String direccion) {
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getcliente_id() {
        return cliente_id;
    }

    public void setIcliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
