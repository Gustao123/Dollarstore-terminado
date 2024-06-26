/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Edgar
 */
public class compra {
    
    int compra_id;
    int proveedor_id;
    Date fecha;
    Float pago;
    int detalle_c_id;
    int cantidad;
    int producto_id;
    Double subtotal;
    Double total;
    Double precio;

    public compra(int compra_id, int proveedor_id,
            Date fecha, Float pago, int detalle_c_id,
            int cantidad, int producto_id, Double subtotal, Double total, Double precio) {
        this.compra_id = compra_id;
        this.proveedor_id = proveedor_id;
        this.fecha = fecha;
        this.pago = pago;
        this.detalle_c_id = detalle_c_id;
        this.cantidad = cantidad;
        this.producto_id = producto_id;
        this.subtotal = subtotal;
        this.total = total;
        this.precio = precio;
    }

    public compra(int proveedor_id, Date fecha, 
            Float pago, int detalle_c_id, int cantidad, 
            int producto_id, Double subtotal, Double total, Double precio) {
        this.proveedor_id = proveedor_id;
        this.fecha = fecha;
        this.pago = pago;
        this.detalle_c_id = detalle_c_id;
        this.cantidad = cantidad;
        this.producto_id = producto_id;
        this.subtotal = subtotal;
        this.total = total;
        this.precio = precio;
    }

    public compra(int proveedor_id, Date fecha) {
        this.proveedor_id = proveedor_id;
        this.fecha = fecha;
    }

    public compra(int compra_id, int cantidad, int producto_id, Double precio) {
        this.compra_id = compra_id;
        this.cantidad = cantidad;
        this.producto_id = producto_id;
        this.precio = precio;
    }

    public int getCompra_id() {
        return compra_id;
    }

    public void setCompra_id(int compra_id) {
        this.compra_id = compra_id;
    }

    public int getProveedor_id() {
        return proveedor_id;
    }

    public void setProveedor_id(int proveedor_id) {
        this.proveedor_id = proveedor_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Float getPago() {
        return pago;
    }

    public void setPago(Float pago) {
        this.pago = pago;
    }

    public int getDetalle_c_id() {
        return detalle_c_id;
    }

    public void setDetalle_c_id(int detalle_c_id) {
        this.detalle_c_id = detalle_c_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
    

  