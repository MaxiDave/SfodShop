package sfod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class SQL {
    private static final String server= "jdbc:mysql://sql11.freemysqlhosting.net/sql11186567";
    private static String user="sql11186567";
    private static String pass="fmyLWFTGUu";
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String[] itemsTitols= new String[]{"marca","any","disseny","cpu","gpu","pantalla","ram","rom","camara","bateria","sim","nfc","usb","sensors"};
    private static final String[] itemsTitolsMaj= new String[]{"Marca","Any","Disseny","CPU","GPU","Pantalla","RAM","ROM","Càmara","Bateria","SIM","NFC","USB","Sensors"};
    
    public static Connection connectar(String user, String pass) throws SQLException, ClassNotFoundException{
        Connection conexion;
        Class.forName(driver);
        if("maxi".equals(user)) user= SQL.user;
        if("111".equals(pass)) pass= SQL.pass;
        conexion=DriverManager.getConnection(server, user, pass);
        return conexion;
    }
    
    public static List<ItemProducteElectronic> obtenirItemsProducteElectronic(Connection conn, String codi) throws SQLException{
        List<ItemProducteElectronic> llista= new ArrayList();
        Statement stm= conn.createStatement();
        Integer i= 0;
        while(i<=13){
            String sql="SELECT "+itemsTitols[i]+" FROM producteElectronic WHERE codi=\""+codi+"\"";
            ResultSet rs1= stm.executeQuery(sql);
            rs1.next();
            String item= rs1.getString(itemsTitols[i]);
            llista.add(new ItemProducteElectronic(itemsTitolsMaj[i],item));
            i++;
        }
        return llista;
    }
    
    public static List<Producte> selecionaProducte(Connection conn, String camp, String codi) throws SQLException{
        List<Producte> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(camp.equals("codi")){
            if(codi.endsWith("?") && codi.startsWith("?")) sql= "SELECT * FROM producte WHERE "+camp+" LIKE '%"+codi.substring(1, codi.length()-1)+"%'";
            else if(codi.endsWith("?")) sql= "SELECT * FROM producte WHERE "+camp+" LIKE '"+codi.substring(0, codi.length()-1)+"%'";
            else if(codi.startsWith("?")) sql= "SELECT * FROM producte WHERE "+camp+" LIKE '%"+codi.substring(1, codi.length())+"'";
            else sql= "SELECT * FROM producte WHERE codi=\""+codi+"\"";
        }
        else sql= "SELECT * FROM producte WHERE "+camp+" LIKE '%"+codi+"%'";
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            Integer eban= rs1.getInt("eban");
            String esElectronic= rs1.getString("esElectronic");
            if(esElectronic.equals("s")){
                List<ItemProducteElectronic> llista= obtenirItemsProducteElectronic(conn, codi);
                list.add(new ProducteElectronic(codi, eban, descripcio, llista));
            }
            else list.add(new Producte(codi, eban, descripcio));
        }
        return list;
    }
    
    public static void afegir(Connection conn, Venedor ven) throws SQLException{
        if(ven.venedorValid()){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO venedor VALUES(\""+ven.getNum()+"\",\""+ven.getNom()+"\",\""+ven.getCognom1()+"\",\""+ven.getCognom2()+"\",\""+ven.getTelefon()+"\",\""+ven.getEmail()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void afegir(Connection conn, Proveidor prov) throws SQLException{
        if(prov.proveidorValid()){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO proveidor VALUES(\""+prov.getNum()+"\",\""+prov.getNom()+"\",\""+prov.getEspecialitat()+"\",\""+prov.getEmail()+"\",\""+prov.getTempsEntrega()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarTelefonVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.venedorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE venedor SET telefon=\""+ven.getTelefon()+"\" WHERE num=\""+ven.getNum()+"\"";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarEmailVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.venedorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE venedor SET email=\""+ven.getEmail()+"\" WHERE num=\""+ven.getNum()+"\"";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarEspecialitatProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.proveidorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE proveidor SET especialitat=\""+prov.getEspecialitat()+"\" WHERE num=\""+prov.getNum()+"\"";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarEmailProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.proveidorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE proveidor SET email=\""+prov.getEmail()+"\" WHERE num=\""+prov.getNum()+"\"";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarTempsEntregaProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.proveidorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE proveidor SET tempsENtrega=\""+prov.getTempsEntrega()+"\" WHERE num=\""+prov.getNum()+"\"";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void afegir(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String electronic;
        if(prod instanceof ProducteElectronic) electronic="s";
        else electronic="n";
        String sql= "INSERT INTO producte VALUES (\""+prod.getCodi()+"\", \""+prod.getDescripcio()+"\", "+prod.getEBAN()+", \""+electronic+"\")";
        stm.executeUpdate(sql);
        if(prod instanceof ProducteElectronic){
            List<String> items= new ArrayList();
            ProducteElectronic aux= (ProducteElectronic)prod;
            Iterator<ItemProducteElectronic> it= aux.getItemsIterator();
            while(it.hasNext()){
                items.add(it.next().toString());
            }
            Iterator<String> itItem= items.iterator();
            String sql2= "INSERT INTO producteElectronic (marca,any,disseny,cpu,gpu,pantalla,ram,rom,camara,bateria,sim,nfc,usb,sensors,codi) VALUES ("
                +"\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()
                +"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()
                +"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+prod.getCodi()+"\")";
            stm.executeUpdate(sql2);
            String sql3= "UPDATE producte SET esElectronic=\"s\" WHERE codi=\""+prod.getCodi()+"\"";
            stm.executeUpdate(sql3);
        }
        else{
            String sql3= "UPDATE producte SET esElectronic=\"n\" WHERE codi=\""+prod.getCodi()+"\"";
            stm.executeUpdate(sql3);
        }
    }
    
    public static void actualitzar(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "UPDATE producte SET descripcio=\""+prod.getDescripcio()+"\", eban="+prod.getEBAN()+" WHERE codi=\""+prod.getCodi()+"\"";
        stm.executeUpdate(sql);
        if(prod instanceof ProducteElectronic){
            List<String> items= new ArrayList();
            ProducteElectronic aux= (ProducteElectronic)prod;
            Iterator<ItemProducteElectronic> it= aux.getItemsIterator();
            while(it.hasNext()) items.add(it.next().toString());
            Iterator<String> itItem= items.iterator();
            String sql2= "UPDATE producteElectronic SET marca=\""+itItem.next()+"\", any=\""+itItem.next()+"\", disseny=\""+itItem.next()+"\", cpu=\""+itItem.next()+"\", gpu=\""+itItem.next()+
                "\", pantalla=\""+itItem.next()+"\", ram=\""+itItem.next()+"\", rom=\""+itItem.next()+"\", camara=\""+itItem.next()+"\", bateria=\""+itItem.next()+"\", sim=\""+itItem.next()+"\", nfc=\""+itItem.next()+
                "\", usb=\""+itItem.next()+"\", sensors=\""+itItem.next()+"\" WHERE codi=\""+prod.getCodi()+"\"";
            stm.executeUpdate(sql2);
        }
    }
    
    public static boolean existeixProducte(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM producte WHERE codi=\""+codi+"\"");
        if(rs1.next()) return true;
        else return false;
    }
    
    public static ObservableList<Venedor> carregarVenedors(Connection conn) throws SQLException{
        ObservableList<Venedor> llista= FXCollections.observableArrayList();
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT * FROM venedor");
        while(rs1.next()){
            String num= rs1.getString("num");
            String nom= rs1.getString("nom");
            String cognom1= rs1.getString("cognom1");
            String cognom2= rs1.getString("cognom2");
            String telefon= rs1.getString("telefon");
            String email= rs1.getString("email");
            Venedor aux= new Venedor(num, nom, cognom1, cognom2, telefon, email);
            llista.add(aux);
        }
        return llista;
    }
    
    public static ObservableList<Proveidor> carregarProveidors(Connection conn) throws SQLException{
        ObservableList<Proveidor> llista= FXCollections.observableArrayList();
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT * FROM proveidor");
        while(rs1.next()){
            String num= rs1.getString("num");
            String nom= rs1.getString("nom");
            String especialitat= rs1.getString("especialitat");
            String email= rs1.getString("email");
            String tempsEntrega= rs1.getString("tempsEntrega");
            Proveidor aux= new Proveidor(num, nom, especialitat, email, tempsEntrega);
            llista.add(aux);
        }
        return llista;
    }
}