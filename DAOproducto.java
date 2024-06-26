 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import modelo.conexion.conexion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
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
public class DAOproducto {
   conexion conectar=conexion.getInstancia();
  
   public List ObtenerDatos() throws SQLException{
       String proced="listarproducto()";
        
        List<Map> registro=new DataBase().listar(proced);
        List<producto> productos = new ArrayList();
        
        for (Map registros : registro){
            producto pro=new producto((int)registros.get("producto_id"),
                                       (String)registros.get("descripcion"),
                                          (int)registros.get("cantidad"),
                                           ((Double)registros.get("precio_produc")),
                                                (int)registros.get("existencia"),
                                                   (int)registros.get("marca_id"),
                                                   (int)registros.get("categoria_id"));
                                                    productos.add(pro);
        }
        return productos;
    }
   public int insertar(producto pro)throws SQLException{
       try{
           CallableStatement st=conectar.conectar().
                   prepareCall("{CALL sp_producto(?,?,?,?,?,?)}");
           st.setString(1, pro.getDescripcion());
              st.setInt(2, pro.getCantidad());
                 st.setDouble(3, pro.getPrecio_produc());
                    st.setInt(4, pro.getExistencia());
                       st.setInt(5, pro.getMarca_id());
                          st.setInt(6, pro.getCategoria_id());
                          st.executeUpdate();
       }catch(SQLException e){
           System.out.println(e+"Error");
           conectar.cerrarConexion();
           return -1;
       }
       conectar.cerrarConexion();
       return 0;
   }
   
   public int Actualizar(producto pro)throws SQLException{
       try{
           CallableStatement st=conectar.conectar().
                   prepareCall("{CALL actualizar_producto(?,?,?,?,?)}");
           st.setInt(1, pro.getProducto_id());
           st.setString(2, pro.getDescripcion());
           st.setInt(3, pro.getCantidad());
           st.setDouble(4, pro.getPrecio_produc());
           st.setInt(5, pro.getExistencia());
           st.executeUpdate();
           
       }catch(SQLException e){
           System.out.println(e+"Error");
           conectar.cerrarConexion();
           return -1;
       }
       conectar.cerrarConexion();
       return 0;
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
    public void productopormarcacategoria() throws JRException{
        conectar.conectar();
        String path="C:\\Users\\gusta\\OneDrive\\Escritorio\\Documentos"
        + "\\NetBeansProjects\\DollarStore1\\src\\Reportes\\Productos.jrxml";
        
        JasperReport jr;
        
        try{
        jr=JasperCompileManager.compileReport(path);
            JasperPrint mostrarReporte =JasperFillManager.fillReport
                (jr, null, conectar.conectar());
        
            JasperViewer.viewReport(mostrarReporte, false);
        
    }catch(JRException e){
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Error"+e);
    }
    
    }
}





