package sfodshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQL {
    /*
    private static String server= "jdbc:mysql://sql11.freemysqlhosting.net/sql11186567";
    private static String user="sql11186567";
    private static String pass="fmyLWFTGUu";
    private static String driver="com.mysql.jdbc.Driver";
    */
    
    public static Connection connectar(String server, String user, String pass, String driver) throws SQLException{
        Connection conexion= null;
        try{
            Class.forName(driver);
            conexion=DriverManager.getConnection(server, user, pass);
            
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("CREATE TABLE Productes\n" +
                "(ref VARCHAR(20) NOT NULL,\n" +
                "codi INT(15),\n" +
                "desc VARCHAR(100) NOT NULL,\n" +
                "stock INT(5) NOT NULL default 0,\n" +
                "sMin INT(5) default NULL,\n" +
                "sMin INT(5) default NULL);");
            
        } catch(ClassNotFoundException | SQLException e){ 
            System.out.println(e);
        }
        
        return conexion;
    } 
}