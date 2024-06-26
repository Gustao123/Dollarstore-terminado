/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.List;
import modelo.conexion.conexion;
import java.sql.CallableStatement;
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
public class DAOMarca {
    conexion conectar = conexion.getInstancia();
    
    public List obtenerDatos() throws  SQLException{
        String proced="listarmarca()";
        
        List<Map> registro=new DataBase().listar(proced);
        List<marca> marcas =new ArrayList();
        
        for(Map registros : registro){
            marca mr=new marca((int)registros.get("marca_id"),
            (String)registros.get("nombre_marca"));
            marcas.add(mr);
            
        }
        return marcas;
    }
    
    public int insertar (marca mr)throws SQLException{
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL sp_marca(?)}");
            st.setString(1, mr.getNombre_marca());
            st.executeUpdate();
            
        }catch (SQLException e){
            System.out.println(e+"Error ");
            conectar.cerrarConexion();
            return -1;
        }
        conectar.cerrarConexion();
        return 0;
    }
    
    public int Actualizar(marca mar)throws SQLException{
        try{
            CallableStatement st =conectar.conectar().
                    prepareCall("{CALL actualizar_marca(?,?)}");
            st.setInt(1, mar.getMarca_id());
            st.setString(2, mar.getNombre_marca());
            st.executeUpdate();
        }catch(SQLException e){
            System.out.println(e+"Error");
            conectar.cerrarConexion();
            return -1;
        }
        conectar.cerrarConexion();
        return 0;
    }
    
    public List<marca> buscarmarca(String parametrobusqueda)throws SQLException{
       ResultSet rs=null;
        List<Map> registros=null;
        List<marca> marcas=new ArrayList();
        List resultado=new ArrayList();
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL buscarmarca(?)}");
            st.setString(1, parametrobusqueda);
            rs=st.executeQuery();
            resultado=OrganizarDatos(rs);
            registros=resultado;
            
            for(Map registro: registros){
                marca mar=new marca((int)registro.get("marca_id"),
                (String)registro.get("nombre_marca"));
                marcas.add(mar);
            }
        }catch(SQLException e){
              System.out.println("No se a realizado la consulta:"+e.getMessage()); 
        }
        return marcas;
    }
    
    private List OrganizarDatos(ResultSet rs){
         List filas=new ArrayList();
        try{
            int numcolumnas=rs.getMetaData().getColumnCount();
            while(rs.next()){
                Map<String, Object> renglon=new HashMap();
                for(int i=1; i<=numcolumnas;i++){
                    String nombrecampo=rs.getMetaData().getColumnName(i);
                    Object valor=rs.getObject(nombrecampo);
                    renglon.put(nombrecampo, valor);
                }
                filas.add(renglon);
            }
        }catch(SQLException e){
              System.out.println(e+"Error");
        }
        return filas;
    }
}

