package sfod;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class SQL {
    private static final String server= "jdbc:mysql://sql11.freemysqlhosting.net/sql11186567";
    private static String user="sql11186567";
    private static String pass="fmyLWFTGUu";
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String[] itemsTitols= new String[]{"marca","any","disseny","cpu","gpu","pantalla","ram","rom","camara","bateria","sim","nfc","usb","sensors"};
    private static final String[] itemsTitolsMaj= new String[]{"Marca","Any","Disseny","CPU","GPU","Pantalla","RAM","ROM","Càmara","Bateria","SIM","NFC","USB","Sensors"};
    
    //Connecta a la BDD
    
    public static Connection connectar(String user, String pass) throws SQLException, ClassNotFoundException{
        Connection conexion;
        Class.forName(driver);
        if("maxi".equals(user)) user= SQL.user;
        if("111".equals(pass)) pass= SQL.pass;
        conexion=DriverManager.getConnection(server, user, pass);
        return conexion;
    }
    
    //Consultes Producte

    public static boolean existeixProducte(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT codi FROM ElemsVendibles WHERE codi=\""+codi+"\"");
        if(rs1.next()) return true;
        else return false;
    }
    
    public static List<ElementCercable> seleccionaProductesCercables(Connection conn, String camp, String codi) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(camp.equals("codi")){
            if(codi.endsWith("?") && codi.startsWith("?")) sql= "SELECT * FROM ElemsVendibles WHERE codi LIKE '%"+codi.substring(1, codi.length()-1)+"%'";
            else if(codi.endsWith("?")) sql= "SELECT * FROM ElemsVendibles WHERE codi LIKE '"+codi.substring(0, codi.length()-1)+"%'";
            else if(codi.startsWith("?")) sql= "SELECT * FROM ElemsVendibles WHERE codi LIKE '%"+codi.substring(1, codi.length())+"'";
            else sql= "SELECT * FROM ElemsVendibles WHERE codi=\""+codi+"\"";
        }
        else sql= "SELECT * FROM ElemsVendibles WHERE descripcio LIKE '%"+codi+"%'";
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            list.add(new ElementCercable(codi, descripcio));
        }
        return list;
    }
    
    public static ElemVendible seleccionaElemVendible(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM ElemsVendibles WHERE codi=\""+codi+"\"";
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            codi= rs1.getString("codi");
            String descripcio= rs1.getString("descripcio");
            
            String sql2= "SELECT * FROM Productes WHERE codiElem=\""+codi+"\"";
            ResultSet rs2= stm.executeQuery(sql2);
            if(rs2.next()){
                Integer eban= rs2.getInt("eban");
                Integer stock= rs2.getInt("stock");
                
                String sql3= "SELECT * FROM ProductesElects WHERE codiP=\""+codi+"\"";
                ResultSet rs3= stm.executeQuery(sql3);
                if(rs3.next()){
                    List<ItemProducteElectronic> llista= new ArrayList();
                    for(int i=0; i<=13; i++) llista.add(new ItemProducteElectronic(itemsTitolsMaj[i], rs3.getString(itemsTitols[i])));
                    return new ProducteElectronic(codi, descripcio, eban, stock, llista);
                }
                else return new Producte(codi, descripcio, eban, stock);
            }
            else return new ElemVendible(codi, descripcio);
        }
        else throw new SQLException();
    }
    
    //Modificadors Producte
    
    private static void afegirProducteElectronic(Connection conn, ProducteElectronic pE) throws SQLException{
        Statement stm= conn.createStatement();
        Iterator<ItemProducteElectronic> itItem= pE.getItemsIterator();
        String sql= "INSERT INTO ProductesElects VALUES (\""+pE.getCodi()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()
                    +"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()
                    +"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\",\""+itItem.next()+"\")";
        stm.executeUpdate(sql);
    }
    
    private static void afegirProducte(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "INSERT INTO Productes VALUES (\""+prod.getCodi()+"\", "+prod.getEBAN()+", "+prod.getStock()+")";
        stm.executeUpdate(sql);
    }
    
    private static void afegirEV(Connection conn, ElemVendible eV) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "INSERT INTO ElemsVendibles VALUES (\""+eV.getCodi()+"\", \""+eV.getDescripcio()+"\")";
        stm.executeUpdate(sql);
    }
    
    public static void afegirElemVendible(Connection conn, ElemVendible eV) throws SQLException{
        if(eV instanceof ProducteElectronic){
            if(eV.valid()){
                afegirEV(conn, eV);
                afegirProducte(conn, (Producte)eV);
                afegirProducteElectronic(conn, (ProducteElectronic)eV);
            }
            else throw new SQLException();
        }
        else if(eV instanceof Producte){
            if(eV.valid()){
                afegirEV(conn, eV);
                afegirProducte(conn, (Producte)eV);
            }
            else throw new SQLException();
        }
        else{
            if(eV.valid()) afegirElemVendible(conn, eV);
            else throw new SQLException();
        }
    }
    
    private static void actualitzarProducteElectronic(Connection conn, ProducteElectronic pE) throws SQLException{
        Statement stm= conn.createStatement();
        Iterator<ItemProducteElectronic> itItem= pE.getItemsIterator();
        String sql= "UPDATE ProductesElects SET marca=\""+itItem.next()+"\", any=\""+itItem.next()+"\", disseny=\""+itItem.next()+"\", cpu=\""+itItem.next()+"\", gpu=\""+itItem.next()+
                    "\", pantalla=\""+itItem.next()+"\", ram=\""+itItem.next()+"\", rom=\""+itItem.next()+"\", camara=\""+itItem.next()+"\", bateria=\""+itItem.next()+"\", sim=\""+itItem.next()+"\", nfc=\""+itItem.next()+
                    "\", usb=\""+itItem.next()+"\", sensors=\""+itItem.next()+"\" WHERE codiP=\""+pE.getCodi()+"\"";
        stm.executeUpdate(sql);
    }
    
    private static void actualitzarProducte(Connection conn, Producte prod) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "UPDATE Productes SET eban="+prod.getEBAN()+", stock="+prod.getStock()+" WHERE codiElem=\""+prod.getCodi()+"\"";
        stm.executeUpdate(sql);
    }
    
    private static void actualitzarEV(Connection conn, ElemVendible eV) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "UPDATE ElemsVendibles SET descripcio=\""+eV.getDescripcio()+"\" WHERE codi=\""+eV.getCodi()+"\"";
        stm.executeUpdate(sql);
    }
    
    public static void actualitzarElemVendible(Connection conn, ElemVendible eV) throws SQLException{
        if(eV instanceof ProducteElectronic){
            if(eV.valid()){
                actualitzarEV(conn, eV);
                actualitzarProducte(conn, (Producte)eV);
                actualitzarProducteElectronic(conn, (ProducteElectronic)eV);
            }
            else throw new SQLException();
        }
        else if(eV instanceof Producte){
            if(eV.valid()){
                actualitzarEV(conn, eV);
                actualitzarProducte(conn, (Producte)eV);
            }
            else throw new SQLException();
        }
        else{
            if(eV.valid()) actualitzarEV(conn, eV);
            else throw new SQLException();
        }
    }
    
    public static void eliminarProducte(Connection conn, String codiProducte) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM producte WHERE codi=\""+codiProducte+"\"";
        stm.executeUpdate(sql);
        String sql2= "DELETE FROM stock WHERE codiProducte=\""+codiProducte+"\"";
        stm.executeUpdate(sql2);
    }
    
    //Consultes Venedor
    
    public static boolean existeixVenedor(Connection conn, Integer num) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM venedor WHERE num="+num);
        if(rs1.next()) return true;
        else return false;
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
            return null;
            //return new Venedor(numVenedor, nomComplet, tractament, telefon, email, direccio, codiPostal, poblacio, provincia, codiPais, nomPais, informacioAddicional);
        }
        else throw new SQLException();
    }
    
    //Càrrega Venedors
    
    public static Map<Integer, String> carregarVenedors(Connection conn) throws SQLException{
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT num, nomComplet FROM venedor");
        Map<Integer, String> venedors= new HashMap<>();
        while(rs1.next()){
            Integer num= rs1.getInt("num");
            String nom= rs1.getString("nomComplet");
            venedors.put(num, nom);
        }
        return venedors;
    }
    
    //Modificadors Venedor
    
    public static void afegirVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.valid()){
            Statement stm= conn.createStatement();
            String sql= null;
            //String sql= "INSERT INTO venedor VALUES("+ven.getNum()+",\""+ven.getNomComplet()+"\",\""+ven.getTractament()+"\","+ven.getTelefon()+",\""+ven.getEmail()+"\",\""+ven.getDireccio()
                   // +"\","+ven.getCodiPostal().toString()+",\""+ven.getPoblacio()+"\",\""+ven.getProvincia()+"\",\""+ven.getCodiPais()+"\",\""+ven.getNomPais()+"\",\""+ven.getInformacioAddicional()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.valid()){
            Statement stm= conn.createStatement();
            String sql= null;
            //String sql= "UPDATE venedor SET nomComplet=\""+ven.getNomComplet()+"\", tractament=\""+ven.getTractament()+"\", telefon="+ven.getTelefon().toString()+", email=\""+ven.getEmail()+"\", direccio=\""+ven.getDireccio()
                    //+"\", codiPostal="+ven.getCodiPostal().toString()+", poblacio=\""+ven.getPoblacio()+"\", provincia=\""+ven.getProvincia()+"\", codiPais=\""+ven.getCodiPais()+"\", nomPais=\""+ven.getNomPais()
                    //+"\", informacioAddicional=\""+ven.getInformacioAddicional()+"\" WHERE num="+ven.getNum().toString();
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void eliminarVenedor(Connection conn, Integer numVenedor) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM venedor WHERE num="+numVenedor;
        stm.executeUpdate(sql);
    }
    
    //Consultes Proveidor
    
    public static boolean existeixProveidor(Connection conn, Integer num) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM proveidor WHERE num="+num);
        if(rs1.next()) return true;
        else return false;
    }
    
    public static List<ElementCercable> seleccionaProveidorsCercables(Connection conn, String stringProveidor) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(stringProveidor.equals("*")){
            sql= "SELECT num, nom FROM proveidor";
        }
        else{
            Integer numProveidor= Integer.parseInt(stringProveidor);
            sql= "SELECT num, nom FROM proveidor WHERE num="+numProveidor;
        }
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("num");
            String nom= rs1.getString("nom");
            list.add(new ElementCercable(num.toString(), nom));
        }
        return list;
    }
    
    public static Proveidor seleccionaProveidor(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM proveidor WHERE num="+Integer.parseInt(codi);
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            Integer numProveidor= rs1.getInt("num");
            String nom= rs1.getString("nom");
            String especialitat= rs1.getString("especialitat");
            String email= rs1.getString("email");
            String tempsEntrega= rs1.getString("tempsEntrega");
            String informacioAddicional= rs1.getString("informacioAddicional");
            return null;
            //return new Proveidor(numProveidor, nom, especialitat, email, tempsEntrega, informacioAddicional);
        }
        else throw new SQLException();
    }
    
    //Càrrega Proveidors
    
    public static Map<Integer, String> carregarProveidors(Connection conn) throws SQLException{
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT num, nom FROM proveidor");
        Map<Integer, String> proveidors= new HashMap<>();
        while(rs1.next()){
            Integer num= rs1.getInt("num");
            String nom= rs1.getString("nom");
            proveidors.put(num, nom);
        }
        return proveidors;
    }
    
    //Modificadors Proveidor
    
    public static void afegirProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.valid()){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO proveidor VALUES(\""+prov.getNum()+"\",\""+prov.getNom()+"\",\""+prov.getEspecialitat()+"\",\""+prov.getEmail()+"\",\""+prov.getTempsEntrega()+"\",\""+prov.getInformacioAddicional()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.valid()){
            Statement stm= conn.createStatement();
            String sql= "UPDATE proveidor SET nom=\""+prov.getNom()+"\", especialitat=\""+prov.getEspecialitat()
                    +"\", email=\""+prov.getEmail()+"\", tempsEntrega=\""+prov.getTempsEntrega()
                    +"\", informacioAddicional=\""+prov.getInformacioAddicional()+"\" WHERE num="+prov.getNum().toString();
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void eliminarProveidor(Connection conn, Integer numProveidor) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM proveidor WHERE num="+numProveidor;
        stm.executeUpdate(sql);
    }
    
    //Consultes Compra
    
    public static ObservableList<Compra> obtenirCompres(Connection conn, String infoVenedor, String infoProveidor, LocalDate inici, LocalDate fi) throws SQLException{
        ObservableList<Compra> contingut= FXCollections.observableArrayList();
        Statement stm= conn.createStatement();
        Date dataInici= Date.valueOf(inici);
        Date dataFi= Date.valueOf(fi);
        String sql;
        if(infoVenedor == null && infoProveidor == null) sql= "SELECT * FROM compra WHERE data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        else if(infoVenedor == null) sql= "SELECT * FROM compra WHERE proveidor = \""+infoProveidor+"\" AND data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        else sql= "SELECT * FROM compra WHERE venedor = \""+infoVenedor+"\" AND proveidor = \""+infoProveidor+"\" AND data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("num");
            String proveidor= rs1.getString("proveidor");
            String venedor= rs1.getString("venedor");
            Double importTotal= rs1.getDouble("importTotal");
            Date data= rs1.getDate("data");
            Compra aux= new Compra(proveidor, venedor, data.toLocalDate());
            aux.setNum(num);
            aux.setImportTotal(importTotal);
            contingut.add(aux);
        }
        return contingut;
    }
    
    public static long obtenirNumInsCompra(Connection conn) throws SQLException{
        long resultat= 0;
        Statement stm= conn.createStatement();
        String sql= "SELECT MAX(num) FROM compra";
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            resultat= rs1.getLong(1);
            Integer anyActual= Calendar.getInstance().get(Calendar.YEAR);
            anyActual= Integer.parseInt(String.valueOf(anyActual).substring(2, 4));
            if(resultat == 0){
                resultat= (anyActual*100000000)+1;
            }
            else{
                Integer anyObtingut= Integer.parseInt(String.valueOf(resultat).substring(0, 2));
                if(anyObtingut.equals(anyActual)){
                    resultat++;
                }
                else resultat= (anyActual*100000000)+1;
            }
        }
        return resultat;
    }
    
    //Modificacions Compra
    
    public static void afegirCompra(Connection conn, Compra compra) throws SQLException{
        if(compra.valida()){
            Statement stm= conn.createStatement();
            LocalDate data= compra.getData();
            String sql= "INSERT INTO compra (num, proveidor, venedor, importTotal, data) VALUES("+compra.getNum()+",\""+compra.getProveidor()+"\",\""+compra.getVenedor()+"\","+compra.getImportTotal()+", '"+data.getYear()+"-"+data.getMonthValue()+"-"+data.getDayOfMonth()+"')";
            stm.executeUpdate(sql);

            Iterator<LiniaCompra> it= compra.getIteradorLC();
            while(it.hasNext()){
                LiniaCompra aux= it.next();
                Statement stm2= conn.createStatement();
                String sql2= "UPDATE stock SET quantitat = quantitat + "+aux.getUnitats()+" WHERE codiProducte=\""+aux.getCodiProducte()+"\"";
                stm2.executeUpdate(sql2);
            }
        }
        else throw new SQLException();
    }
}