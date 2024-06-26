/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import modelo.conexion.conexion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author juriel
 */
public class DAOVenta {
    conexion conectar=conexion.getInstancia();
   
    
    public int insertarventa(venta vent)throws SQLException{
        try{
            CallableStatement st =conectar.conectar()
                    .prepareCall("{CALL sp_venta(?,?)}");
            st.setInt(1, vent.getCliente_id());
             st.setDate(2, (java.sql.Date) vent.getFecha());
            st.executeUpdate();
        }catch(SQLException e){
             System.out.println(e+"Error");
          conectar.cerrarConexion();
          return -1;
        }
         conectar.cerrarConexion();
      return 0;
    }
    
    public int insertardetalleventa(venta vent)throws SQLException{
        try{
            CallableStatement st =conectar.conectar().
                    prepareCall("{CALL sp_detalle_venta(?,?,?,?)}");
            st.setInt(1, vent.getCantidad());
            st.setFloat(2, vent.getPrecioUnitario());
            st.setInt(3, vent.getVenta_id());
            st.setInt(4, vent.getProducto_id());
            st.executeUpdate();
        }catch(SQLException e){
            System.out.println(e+"Error");
            conectar.cerrarConexion();
            return -1;
        }
       conectar.cerrarConexion();
       return 0;
    }
    
      
    public int actualizarExistenciaProducto(producto pro)throws  SQLException{
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL ActualizarExistenciapro(?,?)}");
            st.setInt(1, pro.getExistencia());
            st.setInt(2, pro.getProducto_id());
            st.executeUpdate();
            
            return 0;
        }catch(SQLException e){
            System.out.println("No se a realizado la consulta:"+e.getMessage());
            return -1;
        }
    }
          public int obtenerUltimoventaid()throws SQLException{
        int ventaid=0;
        ResultSet rs = null;
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL obtenerultimoventaid()}");
            rs = st.executeQuery();
            if(rs.next()){
                ventaid = rs.getInt(1);
            }
            return ventaid;
        }catch(SQLException e){
            System.out.println("No se a realizado la consulta:"+ e.getMessage());
            return 0;
        }
          }
          
          
     public void reporteventafechas(java.sql.Date fechaI, java.sql.Date fechaF) throws JRException{
        conectar.conectar();
        String path="C:\\Users\\gusta\\OneDrive\\Escritorio\\Documentos"
        + "\\NetBeansProjects\\DollarStore1\\src\\Reportes\\ventaporfecha.jrxml";
        
        JasperReport jr;
        Map parametro=new HashMap();
        
        parametro.put("fecha_inicio",fechaI); 
        parametro.put("fecha_final",fechaF);
        
        try{
        jr=JasperCompileManager.compileReport(path);
            JasperPrint mostrarReporte =JasperFillManager.fillReport
                (jr, parametro, conectar.conectar());
        
            JasperViewer.viewReport(mostrarReporte, false);
        
    }catch(JRException e){
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Error"+e);
    }
     }
     
     
     

      public void factura(int ventaid) throws JRException{
        conectar.conectar();
       String path="C:\\Users\\gusta\\OneDrive\\Escritorio\\Documentos"
        + "\\NetBeansProjects\\DollarStore1\\src\\Reportes\\Factura.jrxml";
                
                
        
        JasperReport jr;
        Map parametro=new HashMap();    
        
        parametro.put("Parameterventa_id",ventaid);
        
        try{
        jr=JasperCompileManager.compileReport(path);
            JasperPrint mostrarReporte =JasperFillManager.fillReport
                (jr, parametro, conectar.conectar());
        
            JasperViewer.viewReport(mostrarReporte, false);
        
    }catch(JRException e){
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Error"+e);
    }
     }

}

    

    
    



