package sfod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public abstract class SQL {
    private static final String server= "jdbc:mysql://sql11.freemysqlhosting.net/sql11186567";
    private static String user="sql11186567";
    private static String pass="fmyLWFTGUu";
    private static final String driver="com.mysql.jdbc.Driver";
    
    public static Connection connectar(String user, String pass) throws SQLException, ClassNotFoundException{
        Connection conexion;
        Class.forName(driver);
        if("maxi".equals(user)) user= SQL.user;
        if("111".equals(pass)) pass= SQL.pass;
        conexion=DriverManager.getConnection(server, user, pass);
        return conexion;
    }
    
    private static String escriureCamp(Camp camp){
        String resultat= camp.obtNom()+" "+camp.obtTipus();
        if(camp.teEspai()) resultat+= "("+camp.obtEspai()+") ";
        else resultat+=" ";
        if(!camp.potSerNull()) resultat+= "not NULL";
        
    }
    
    public static void crearTaula(Connection conn, String nom, List<Camp> camps) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "CREATE TABLE "+nom+" (";
        Iterator it= camps.iterator();
        while(it.hasNext()){
            sql+= escriureCamp(it.next());
        }
    }
}