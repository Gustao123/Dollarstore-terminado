/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author juriel
 */
public class producto {
     int producto_id;
     String descripcion;
    int cantidad;
     Double precio_produc;
    int existencia;
     int marca_id;
     int categoria_id;

    public producto(int producto_id, String descripcion, 
            int cantidad, Double precio_produc, int existencia,
            int marca_id, int categoria_id) {
        this. producto_id =  producto_id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_produc = precio_produc;
        this.existencia = existencia;
        this.marca_id = marca_id;
        this.categoria_id = categoria_id;
    }

    public producto(String descripcion, int cantidad, Double precio_produc, 
            int existencia, int marca_id, int categoria_id) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_produc = precio_produc;
        this.existencia= existencia;
        this.marca_id = marca_id;
        this.categoria_id = categoria_id;
    }

    public producto(int producto_id, String descripcion, int cantidad, Double 
            precio_produc, int existencia) {
        this.producto_id = producto_id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_produc = precio_produc;
        this.existencia = existencia;
        
    }

    public producto(int producto_id, int existencia) {
        this.producto_id = producto_id;
        this.existencia = existencia;
    }

    public producto(int producto_id, String descripcion, Double precio_produc, int existencia) {
        this.producto_id = producto_id;
        this.descripcion = descripcion;
        this.precio_produc = precio_produc;
        this.existencia = existencia;
    }

    public producto(int producto_id, String descripcion, int cantidad, Double precio_produc) {
        this.producto_id = producto_id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio_produc = precio_produc;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_produc() {
        return precio_produc;
    }

    public void setPrecio_produc(Double precio_produc) {
        this.precio_produc = precio_produc;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(int marca_id) {
        this.marca_id = marca_id;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

}
