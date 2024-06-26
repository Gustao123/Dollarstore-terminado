/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import modelo.conexion.conexion;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author juriel
 */
public class DAOCliente {
    conexion conectar=conexion.getInstancia();
    
    public List ObtenerDatos() throws SQLException{
        String proced= "po_listarclientes()";
        
        List<Map> registro = new DataBase().listar(proced);
        List<Cliente> clientes  = new ArrayList();
        
        for (Map registros : registro){
            Cliente cl= new Cliente((int)registros.get("cliente_id"),
                                    (String) registros.get("Nombre"),
                                    (String) registros.get("Apellido"),
                                    (String) registros.get("telefono"),
                                      (String)registros.get("direccion"));
                                      clientes.add(cl);
        }
        return clientes;
    }
      public int insertar(Cliente cl) throws SQLException{
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL sp_cliente(?,?,?,?)}");
            st.setString(1, cl.getNombre());
            st.setString(2, cl.getApellido());
            st.setString(3, cl.getTelefono());
            st.setString(4, cl.getDireccion());
            st.executeUpdate();
       }catch(SQLException e){
            System.out.println(e+"Error");
            conectar.cerrarConexion();
            return -1;
       }
        conectar.cerrarConexion();
        return 0;
    } 
      public int Actualizar(Cliente cl)throws SQLException{
          try{
              CallableStatement st=conectar.conectar().
                      prepareCall("{CALL sp_actualizar_cliente(?,?,?,?,?)}");
              st.setInt(1, cl.getcliente_id());
              st.setString(2, cl.getNombre());
              st.setString(3, cl.getApellido());
              st.setString(4, cl.getTelefono());
              st.setString(5, cl.getDireccion());
              st.executeUpdate();
              
              
          }catch(SQLException e){
              System.out.println(e+"Error");
              conectar.cerrarConexion();
              return -1;
          }
          conectar.cerrarConexion();
          return 0;
      }
      
      public int delete(int id)throws SQLException{
          try{
              CallableStatement st=conectar.conectar().
                      prepareCall("{CALL Eliminar_cliente(?)}");
              st.setInt(1, id);
              st.executeUpdate();
     
          }catch(SQLException e){
              System.out.println(e+"Error");
              conectar.cerrarConexion();
              return -1;
          }
          conectar.cerrarConexion();
          return 0;
      }
      
    public List<Cliente> buscarCliente(String parametrobusqueda) throws SQLException {
         ResultSet rs=null;
        List<Map> registros=null;
        List<Cliente> clientes=new ArrayList();
        List resultado= new ArrayList();
        try{
           CallableStatement st=conectar.conectar().
                   prepareCall("{CALL buscarcliente(?)}");
             st.setString(1, parametrobusqueda);
           rs=st.executeQuery();
            resultado=OrganizarDatos(rs);
           registros = resultado;
           
           for(Map registro:registros){
               Cliente clie=new Cliente((int)registro.get("cliente_id"),
               (String)registro.get("Nombre"),
               (String)registro.get("Apellido"),
               (String)registro.get("telefono"),
               (String)registro.get("direccion"));
               clientes.add(clie);
           
           }
       
        }catch(SQLException e){
          System.out.println("No se a realizado la consulta:"+e.getMessage());
                    
        }
       
        return clientes;
      
    }
 private List OrganizarDatos(ResultSet rs){
        List filas=new ArrayList();
        try{
            int numcolumnas = rs.getMetaData().getColumnCount();
            while (rs.next()){
                Map<String , Object> renglon=new HashMap();
                for(int i=1; i<=numcolumnas; i++){
                   String nombreCampo=rs.getMetaData().getColumnName(i);
                   Object valor=rs.getObject(nombreCampo);
                   
                   renglon.put(nombreCampo,valor);
                }
                filas.add(renglon);
            }
        }catch(SQLException e){
            System.out.println(e+"Error");
        }
        return filas;
    }



}
