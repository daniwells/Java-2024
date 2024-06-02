/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lid2jvl
 */
public class MyDatabase {
    public Connection conn = null;
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String nameDatabase = "javaproject";
    private final String local = "jdbc:mysql://localhost:3306/"+nameDatabase;
    private final String login = "root";
    private final String password = "root";
    
    public boolean getConnection(){
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(local, login, password);
            return true;
        }catch(ClassNotFoundException error){
            System.out.println("Drive not founded!"+error.toString());
            return false;
        }catch(SQLException error){
            System.out.println("Fail in the connection!"+error.toString());
            return false;
        }
        
    }
}
