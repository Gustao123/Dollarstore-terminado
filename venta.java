/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author juriel
 */
public class venta {
    int venta_id;
    int cliente_id;
    Date fecha;
    int detalle_v_id;
    int producto_id;
    int cantidad;
    float precioUnitario;
    Double subtotal;
    Double total;
    Date fechaI;
    Date fechaF;

    public venta(Date fechaI, Date fechaF) {
        this.fechaI = fechaI;
        this.fechaF = fechaF;
    }

    public Date getFechaI() {
        return fechaI;
    }

    public void setFechaI(Date fechaI) {
        this.fechaI = fechaI;
    }

    public Date getFechaF() {
        return fechaF;
    }

    public void setFechaF(Date fechaF) {
        this.fechaF = fechaF;
    }
    

    public venta(int cliente_id, Date fecha, int detalle_v_id, int producto_id, int cantidad, float precioUnitario, Double subtotal, Double total) {
        this.cliente_id = cliente_id;
        this.fecha = fecha;
        this.detalle_v_id = detalle_v_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.total = total;
    }

    public venta(int venta_id, int cliente_id, Date fecha,
            int detalle_v_id, int producto_id, int cantidad, 
            float precioUnitario, Double subtotal, Double total) {
        this.venta_id = venta_id;
        this.cliente_id = cliente_id;
        this.fecha = fecha;
        this.detalle_v_id = detalle_v_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.total = total;
    }

    public venta(int cliente_id, Date fecha) {
        this.cliente_id = cliente_id;
        this.fecha = fecha;
    }

    public venta(int venta_id, int producto_id, int cantidad, float precioUnitario) {
        this.venta_id = venta_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

  

    public int getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(int venta_id) {
        this.venta_id = venta_id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDetalle_v_id() {
        return detalle_v_id;
    }

    public void setDetalle_v_id(int detalle_v_id) {
        this.detalle_v_id = detalle_v_id;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
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

}