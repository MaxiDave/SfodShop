package sfod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
    
    public static Producte selecionaProducte(Connection conn, String codi) throws Exception{
        String sql= "SELECT * FROM producte WHERE codi=\""+codi+"\"";
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            Integer eban= rs1.getInt("eban");
            return new Producte(codi, eban, descripcio);
        }
        else throw new Exception();
    }
    
    private static String escriureCamp(Camp camp){
        String resultat= camp.obtNom()+" "+camp.obtTipus();
        if(camp.teEspai()) resultat+= "("+camp.obtEspai()+") ";
        else resultat+= " ";
        if(!camp.potSerNull()) resultat+= "not NULL";
        resultat+= ", ";
        return resultat;
    }
    
    public static void crearTaula(Connection conn, String nom, List<Camp> camps) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "CREATE TABLE IF NOT EXISTS "+nom+" (";
        Iterator<Camp> it= camps.iterator();
        while(it.hasNext()){
            Camp aux= it.next();
            sql+= escriureCamp(aux);
            if(aux.isPrimaryKey()) sql+= "PRIMARY KEY ( "+aux.obtNom()+" ) , ";
            if(aux.isForeignKey()) sql+= "FOREIGN KEY ( "+aux.obtNom()+" ) , ";
        }
        sql= sql.substring(0, sql.length() - 2);
        sql+= ")";
        
        stm.executeUpdate(sql);
    }
    
    public static Boolean existeixTaula(Connection conn, String nom) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("select * from "+nom);
        if(rs1.next()) return true;
        else return false;
    }
    
    public static void eliminarTaula(Connection conn, String nom) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE TABLE "+nom;
        stm.executeUpdate(sql);
    }
    
    public static void afegir(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "INSERT INTO producte VALUES (\""+prod.getCodi()+"\", \""+prod.getDescripcio()+"\", "+prod.getEBAN()+")";
        stm.executeUpdate(sql);
    }
    
    public static void actualitzar(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "UPDATE producte SET descripcio=\""+prod.getDescripcio()+"\", eban="+prod.getEBAN()+" WHERE codi=\""+prod.getCodi()+"\"";
        stm.executeUpdate(sql);
    }
}