package sfod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    
    public static Producte seleccionaProducte(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM producte WHERE codi=\""+codi+"\"";
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            Integer eban= rs1.getInt("eban");
            String esElectronic= rs1.getString("esElectronic");
            if(esElectronic.equals("s")){
                List<ItemProducteElectronic> llista= obtenirItemsProducteElectronic(conn, codi);
                return new ProducteElectronic(codi, eban, descripcio, llista);
            }
            else return new Producte(codi, eban, descripcio);
        }
        else throw new SQLException();
    }
    
    public static List<ElementCercable> seleccionaProductesCercables(Connection conn, String camp, String codi) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(camp.equals("codi")){
            if(codi.endsWith("?") && codi.startsWith("?")) sql= "SELECT codi, descripcio FROM producte WHERE "+camp+" LIKE '%"+codi.substring(1, codi.length()-1)+"%'";
            else if(codi.endsWith("?")) sql= "SELECT codi, descripcio FROM producte WHERE "+camp+" LIKE '"+codi.substring(0, codi.length()-1)+"%'";
            else if(codi.startsWith("?")) sql= "SELECT codi, descripcio FROM producte WHERE "+camp+" LIKE '%"+codi.substring(1, codi.length())+"'";
            else sql= "SELECT codi, descripcio FROM producte WHERE codi=\""+codi+"\"";
        }
        else sql= "SELECT codi, descripcio FROM producte WHERE "+camp+" LIKE '%"+codi+"%'";
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            list.add(new ElementCercable(codi, descripcio));
        }
        return list;
    }
    
    public static Venedor seleccionaVenedor(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM venedor WHERE num="+Integer.parseInt(codi);
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            Integer numVenedor= rs1.getInt("num");
            String nomComplet= rs1.getString("nomComplet");
            String tractament= rs1.getString("tractament");
            Integer telefon= rs1.getInt("telefon");
            String email= rs1.getString("email");
            String direccio= rs1.getString("direccio");
            Integer codiPostal= rs1.getInt("codiPostal");
            String poblacio= rs1.getString("poblacio");
            String provincia= rs1.getString("provincia");
            String codiPais= rs1.getString("codiPais");
            String nomPais= rs1.getString("nomPais");
            String informacioAddicional= rs1.getString("informacioAddicional");
            return new Venedor(numVenedor, nomComplet, tractament, telefon, email, direccio, codiPostal, poblacio, provincia, codiPais, nomPais, informacioAddicional);
        }
        else throw new SQLException();
    }
    
    public static List<ElementCercable> seleccionaVenedorsCercables(Connection conn, String stringVenedor) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(stringVenedor.equals("*")){
            sql= "SELECT num, nomComplet FROM venedor";
        }
        else{
            Integer numVenedor= Integer.parseInt(stringVenedor);
            sql= "SELECT num, nomComplet FROM venedor WHERE num="+numVenedor;
        }
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("num");
            String nomComplet= rs1.getString("nomComplet");
            list.add(new ElementCercable(num.toString(), nomComplet));
        }
        return list;
    }
    
    public static void afegir(Connection conn, Venedor ven) throws SQLException{
        if(ven.venedorValid()){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO venedor VALUES("+ven.getNum()+",\""+ven.getNomComplet()+"\",\""+ven.getTractament()+"\","+ven.getTelefon()+",\""+ven.getEmail()+"\",\""+ven.getDireccio()
                    +"\","+ven.getCodiPostal().toString()+",\""+ven.getPoblacio()+"\",\""+ven.getProvincia()+"\",\""+ven.getCodiPais()+"\",\""+ven.getNomPais()+"\",\""+ven.getInformacioAddicional()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzar(Connection conn, Venedor ven) throws SQLException{
        if(ven.venedorValid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE venedor SET nomComplet=\""+ven.getNomComplet()+"\", tractament=\""+ven.getTractament()+"\", telefon="+ven.getTelefon().toString()+", email=\""+ven.getEmail()+"\", direccio=\""+ven.getDireccio()
                    +"\", codiPostal="+ven.getCodiPostal().toString()+", poblacio=\""+ven.getPoblacio()+"\", provincia=\""+ven.getProvincia()+"\", codiPais=\""+ven.getCodiPais()+"\", nomPais=\""+ven.getNomPais()
                    +"\", informacioAddicional=\""+ven.getInformacioAddicional()+"\" WHERE num="+ven.getNum().toString();
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void eliminarVenedor(Connection conn, Integer numVenedor) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM venedor WHERE num="+numVenedor;
        stm.executeUpdate(sql);
    }
    
    public static void eliminarProducte(Connection conn, String codiProducte) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM producte WHERE codi=\""+codiProducte+"\"";
        stm.executeUpdate(sql);
    }
    
    public static void afegir(Connection conn, Proveidor prov) throws SQLException{
        if(prov.proveidorValid()){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO proveidor VALUES(\""+prov.getNum()+"\",\""+prov.getNom()+"\",\""+prov.getEspecialitat()+"\",\""+prov.getEmail()+"\",\""+prov.getTempsEntrega()+"\")";
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
    
    public static boolean existeixVenedor(Connection conn, Integer num) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM venedor WHERE num="+num);
        if(rs1.next()) return true;
        else return false;
    }
}