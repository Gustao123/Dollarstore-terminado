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
import modelo.compra;
import modelo.conexion.conexion;
import modelo.producto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author PC
 */
public class DAOcompra {
    
    conexion conectar= conexion.getInstancia();
    
    public int insertarCompra(compra comp)throws SQLException {
        try{
            CallableStatement st =conectar.conectar()
                    .prepareCall("{CALL sp_compra(?,?)}");
            st.setInt(1, comp.getProveedor_id());
            st.setDate(2, (Date) comp.getFecha());

            st.executeUpdate();
        }catch(SQLException e){
             System.out.println(e+"Error");
          conectar.cerrarConexion();
          return -1;
        }
         conectar.cerrarConexion();
      return 0;
    }
    
    public int insertarDetalleCompra(compra comp)throws SQLException{
        try{
            CallableStatement st =conectar.conectar().
                    prepareCall("{CALL sp_detalle_compra(?,?,?,?)}");
            st.setInt(1, comp.getCantidad());
            st.setDouble(2, comp.getPrecio());
            st.setInt(3, comp.getCompra_id());
            st.setInt(4, comp.getProducto_id());
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
    
       public int obtenerUltimoNumCompra()throws SQLException{
        int compraid=0;
        ResultSet rs = null;
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL obtenerultimocompra_id()}");
            rs = st.executeQuery();
            if(rs.next()){
                compraid = rs.getInt(1);
            }
            return compraid;
        }catch(SQLException e){
            System.out.println("No se a realizado la consulta:"+ e.getMessage());
            return 0;
        }
        
       }
     public List<producto> buscarproducto(String parametrobuscar)throws SQLException{
       ResultSet rs=null;
       List<Map> registro =null;
       List<producto> productos =new ArrayList();
       List resultado=new ArrayList();
       
       try{
           CallableStatement st=conectar.conectar().
                 prepareCall("{CALL buscarproducto(?)}");
           st.setString(1,parametrobuscar);
           rs=st.executeQuery();
           resultado=OrganizarDatos(rs);
           registro=resultado;
           
           for (Map registros:registro){
               producto pro=new producto((int)registros.get("producto_id"),
               (String)registros.get("descripcion"),
               (int)registros.get("cantidad"),
               (Double)registros.get("precio_produc"),
               (int)registros.get("existencia"));
               productos.add(pro);
           }
       }catch(SQLException e){
            System.out.println("No se a realizado la consulta:"+e.getMessage());
        }
       return productos;
   
    } 
     
     private List OrganizarDatos(ResultSet rs){
        List filas=new ArrayList();
        try{
            int numcolumnas = rs.getMetaData().getColumnCount();
            while(rs.next()){
                Map<String,Object> renglon =new HashMap();
                for(int i=1; i<=numcolumnas; i++){
                    String nombrecampos=rs.getMetaData().getColumnName(i);
                    Object valor=rs.getObject(nombrecampos);
                    
                    renglon.put(nombrecampos, valor);
                            }
                filas.add(renglon);
            }
        }catch(SQLException e){
            System.out.println(e+"Error");
        }
        return filas;
    }
     
     public void reportescomprasfecha(java.sql.Date fechaI, java.sql.Date fechaF) throws SQLException{
         conectar.conectar();
         String path="C:\\Users\\gusta\\OneDrive\\Escritorio\\Documentos"
        + "\\NetBeansProjects\\DollarStore1\\src\\Reportes\\reportefechacompra.jrxml";
         
         JasperReport jr;
         Map parametro=new HashMap();
         
         parametro.put("fecha_inicio",fechaI); 
        parametro.put("fecha_final",fechaF);
        
        try{
         jr=JasperCompileManager.compileReport(path);
            JasperPrint mostrarReporte= JasperFillManager.fillReport
        (jr, parametro,conectar.conectar());
            
            JasperViewer.viewReport(mostrarReporte, false);
        }catch(JRException e){
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Error"+e);
        }
         
     }
     
   
}


    


