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

/**
 *
 * @author juriel
 */
public class DataBase {
    conexion conectar=conexion.getInstancia();
    
    public List listar (String procedimiento) throws SQLException{
        ResultSet rs=null;
        
        List resultado=new ArrayList();
        try{
            CallableStatement st=conectar.conectar().
                    prepareCall("{CALL "+procedimiento+"}");
            rs=st.executeQuery();
            resultado=OrganizarDatos(rs);
            
        }catch(SQLException e){
            System.out.println("No se a realizado la consulta:"+e.getMessage());
        }
        return resultado;
    }
    private List OrganizarDatos(ResultSet rs){
        List filas =new ArrayList();
        try{
            int numcolumnas=rs.getMetaData().getColumnCount();
            while(rs.next()){
                 Map<String, Object> renglon=new HashMap();
                for (int i=1; i<=numcolumnas; i++){
                    
                    String nombrecampo=rs.getMetaData().getColumnName(i);
                    Object valor =rs.getObject(nombrecampo);
                    
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
