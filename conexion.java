/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author juriel
 */
public class conexion {
    private static final String URL="jdbc:sqlserver://localhost:1433;"
            + "databaseName=DB_DOLLARSTORE;"+"integratedSecurity=true;"+
            "encrypt=true;trustServerCertificate=true";
    
    private static conexion instancia=null;
    private static Connection conex=null;
    
    private conexion(){}
    
    public Connection conectar(){
        try{
             conex=DriverManager.getConnection(URL);
             System.out.println("Conexión Establecida");
             return conex;              
        }catch (SQLException e){
            System.out.println("Error en conexión: "+e);
        }
        return conex;
    }
    
    public void cerrarConexion() throws SQLException{
        try{
            conex.close();
            System.out.println("Conexión Cerrada");
        }catch(SQLException e){
            System.out.println("Error en la Conexión");
            conex.close();
        }finally{
            conex.close();
        }
    }
    
    public static conexion getInstancia(){
        if(instancia==null){
            instancia=new conexion();
                    
        }
        return instancia;
    }
     
            
}
