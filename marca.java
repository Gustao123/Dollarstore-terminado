/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author juriel
 */
public class marca {
    private int marca_id;
    private String nombre_marca;

    public marca(int marca_id, String nombre_marca) {
        this.marca_id = marca_id;
        this.nombre_marca = nombre_marca;
    }

    public marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }

    public int getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(int marca_id) {
        this.marca_id = marca_id;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }
    
     public String toString() {
        return marca_id + " - " + nombre_marca;
    }
   
    
}
