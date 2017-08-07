package sfod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQL {
    private static String server= "jdbc:mysql://sql11.freemysqlhosting.net/sql11186567";
    //private static String user="sql11186567";
    //private static String pass="fmyLWFTGUu";
    private static String driver="com.mysql.jdbc.Driver";
    
    public static Connection connectar(String user, String pass) throws SQLException, ClassNotFoundException{
        Connection conexion;
        Class.forName(driver);
        conexion=DriverManager.getConnection(server, user, pass);
        return conexion;
    } 
}