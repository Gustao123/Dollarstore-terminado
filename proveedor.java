/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author juriel
 */
public class proveedor {
   private int proveedor_id;
   private String company;
   private String telefono;
   private String correo_electro ;

    public proveedor(int proveedor_id, String company,
            String telefono, String correo_electro) {
        this.proveedor_id= proveedor_id;
        this.company = company;
        this.telefono = telefono;
        this.correo_electro = correo_electro;
    }

    public proveedor( String company, String telefono, String correo_electro) {
        this.company = company;
        this.telefono = telefono;
        this.correo_electro = correo_electro;
    }

    public int getproveedor_id() {
        return proveedor_id;
    }

    public void setproveedor_id(int proveedor_id) {
        this.proveedor_id = proveedor_id;
    }

   

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo_electro() {
        return correo_electro;
    }

    public void setCorreo_electro(String correo_electro) {
        this.correo_electro = correo_electro;
    }
           
}
