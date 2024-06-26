/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author juriel
 */
public class categoria {
    private int categoria_id;
    private String descripcion;

    public categoria(int categoria_id, String descripcion) {
        this.categoria_id = categoria_id;
        this.descripcion = descripcion;
    }

    public categoria(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String toString(){
        return categoria_id + " - " + descripcion;
    }
}
