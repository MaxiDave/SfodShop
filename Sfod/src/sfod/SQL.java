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

    public static boolean existeixElementVendible(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT codi FROM ElemsVendibles WHERE codi=\""+codi+"\"");
        if(rs1.next()) return true;
        else return false;
    }
    
    public static boolean existeixProducte(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT codiElem FROM Productes WHERE codiElem=\""+codi+"\"");
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
                if(eban == 0) eban= null;
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
        String sql= "INSERT INTO Productes VALUES (\""+prod.getCodi()+"\", "+prod.getEBAN()+", 0)";
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
    
    private static void eliminarEV(Statement stm, String codiElem) throws SQLException{
        String sql= "DELETE FROM ElemsVendibles WHERE codi=\""+codiElem+"\"";
        stm.executeUpdate(sql);
    }
    
    private static void eliminarProducte(Statement stm, String codiElem) throws SQLException{
        String sql= "DELETE FROM Productes WHERE codiElem=\""+codiElem+"\"";
        stm.executeUpdate(sql);
    }
    
    private static void eliminarProducteElectronic(Statement stm, String codiElem) throws SQLException{
        String sql= "DELETE FROM ProductesElects WHERE codiP=\""+codiElem+"\"";
        stm.executeUpdate(sql);
    }
    
    public static void eliminarElemVendible(Connection conn, String codiElem, String tipusProducte) throws SQLException{
        Statement stm= conn.createStatement();
        if(tipusProducte.equals("Electrònic")){
            eliminarProducteElectronic(stm, codiElem);
            eliminarProducte(stm, codiElem);
            eliminarEV(stm, codiElem);
        }
        else if(tipusProducte.equals("Varis")){
            eliminarProducte(stm, codiElem);
            eliminarEV(stm, codiElem);
        }
        else eliminarEV(stm, codiElem);
    }
    
    //Consultes Venedor
    
    public static boolean existeixVenedor(Connection conn, Integer num) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM Venedors WHERE numVen="+num);
        if(rs1.next()) return true;
        else return false;
    }
    
    public static List<ElementCercable> seleccionaVenedorsCercables(Connection conn, String stringVenedor) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(stringVenedor.equals("*")){
            sql= "SELECT numVen, nom, cog1, cog2 FROM Venedors";
        }
        else{
            Integer numVenedor= Integer.parseInt(stringVenedor);
            sql= "SELECT numVen, nom, cog1, cog2 FROM Venedors WHERE numVen="+numVenedor;
        }
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("numVen");
            String nom= rs1.getString("nom");
            String cog1= rs1.getString("cog1");
            String cog2= rs1.getString("cog2");
            list.add(new ElementCercable(num.toString(), nom+" "+cog1+" "+cog2));
        }
        return list;
    }
    
    public static Venedor seleccionaVenedor(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM Venedors v INNER JOIN Paisos pa ON v.codiPais = pa.codi WHERE v.numVen="+Integer.parseInt(codi);
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            Integer numVenedor= rs1.getInt("v.numVen");
            String nom= rs1.getString("v.nom");
            String cog1= rs1.getString("v.cog1");
            String cog2= rs1.getString("v.cog2");
            String tractament= rs1.getString("v.tractament");
            Integer telefon= rs1.getInt("v.telefon");
            String email= rs1.getString("v.email");
            String direccio= rs1.getString("v.direccio");
            Integer codiPostal= rs1.getInt("v.codiPostal");
            String poblacio= rs1.getString("v.poblacio");
            String provincia= rs1.getString("v.provincia");
            String codiPais= rs1.getString("v.codiPais");
            String nomPais= rs1.getString("pa.nomPais");
            String infoAdd= rs1.getString("v.infoAdd");
            return new Venedor(numVenedor, nom, cog1, cog2, tractament, telefon, email, direccio, codiPostal, poblacio, provincia, codiPais, nomPais, infoAdd);
        }
        else throw new SQLException();
    }
    
    //Càrrega Venedors
    
    public static Map<Integer, String> carregarVenedors(Connection conn) throws SQLException{
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT numVen, nom, cog1, cog2 FROM Venedors");
        Map<Integer, String> venedors= new HashMap<>();
        while(rs1.next()){
            Integer num= rs1.getInt("numVen");
            String nom= rs1.getString("nom");
            String cog1= rs1.getString("cog1");
            String cog2= rs1.getString("cog2");
            venedors.put(num, nom+" "+cog1+" "+cog2);
        }
        return venedors;
    }
    
    //Modificadors Venedor
    
    public static void afegirVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.valid() && existeixPais(conn, ven.getCodiPais(), ven.getNomPais())){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO Venedors VALUES("+ven.getNum()+",\""+ven.getNom()+"\",\""+ven.getCog1()+"\",\""+ven.getCog2()+"\",\""+ven.getTractament()+"\","+ven.getTelefon()+",\""+ven.getEmail()+"\",\""+ven.getDireccio()
                    +"\","+ven.getCodiPostal().toString()+",\""+ven.getPoblacio()+"\",\""+ven.getProvincia()+"\",\""+ven.getCodiPais()+"\",\""+ven.getInformacioAddicional()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarVenedor(Connection conn, Venedor ven) throws SQLException{
        if(ven.valid() && existeixPais(conn, ven.getCodiPais(), ven.getNomPais())){
            Statement stm= conn.createStatement();
            String sql= "UPDATE Venedors SET nom=\""+ven.getNom()+"\", cog1=\""+ven.getCog1()+"\", cog2=\""+ven.getCog2()+"\", tractament=\""+ven.getTractament()+"\", telefon="+ven.getTelefon().toString()+", email=\""+ven.getEmail()+"\", direccio=\""+ven.getDireccio()
                    +"\", codiPostal="+ven.getCodiPostal().toString()+", poblacio=\""+ven.getPoblacio()+"\", provincia=\""+ven.getProvincia()+"\", codiPais=\""+ven.getCodiPais()
                    +"\", infoAdd=\""+ven.getInformacioAddicional()+"\" WHERE numVen="+ven.getNum().toString();
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void eliminarVenedor(Connection conn, Integer numVenedor) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM Venedors WHERE numVen="+numVenedor;
        stm.executeUpdate(sql);
    }
    
    //Consultes Proveidor
    
    public static boolean existeixProveidor(Connection conn, Integer num) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT * FROM Proveidors WHERE numProv="+num);
        if(rs1.next()) return true;
        else return false;
    }
    
    public static List<ElementCercable> seleccionaProveidorsCercables(Connection conn, String stringProveidor) throws SQLException{
        List<ElementCercable> list= new ArrayList<>();
        Statement stm= conn.createStatement();
        String sql;
        if(stringProveidor.equals("*")){
            sql= "SELECT numProv, nomProv FROM Proveidors";
        }
        else{
            Integer numProveidor= Integer.parseInt(stringProveidor);
            sql= "SELECT numProv, nomProv FROM Proveidors WHERE numProv="+numProveidor;
        }
	ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("numProv");
            String nom= rs1.getString("nomProv");
            list.add(new ElementCercable(num.toString(), nom));
        }
        return list;
    }
    
    public static Proveidor seleccionaProveidor(Connection conn, String codi) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT * FROM Proveidors p INNER JOIN Paisos pa ON p.codiPais = pa.codi WHERE p.numProv="+Integer.parseInt(codi);
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            Integer numProveidor= rs1.getInt("p.numProv");
            String nom= rs1.getString("p.nomProv");
            String especialitat= rs1.getString("p.esp");
            String email= rs1.getString("p.email");
            String tempsEntrega= rs1.getString("p.tempsEntr");
            String codiPais= rs1.getString("p.codiPais");
            String nomPais= rs1.getString("pa.nomPais");
            String informacioAddicional= rs1.getString("p.infoAdd");
            return new Proveidor(numProveidor, nom, especialitat, email, tempsEntrega, codiPais, nomPais, informacioAddicional);
        }
        else throw new SQLException();
    }
    
    //Càrrega Proveidors
    
    public static Map<Integer, String> carregarProveidors(Connection conn) throws SQLException{
        Statement stm= conn.createStatement();
        ResultSet rs1= stm.executeQuery("SELECT numProv, nomProv FROM Proveidors");
        Map<Integer, String> proveidors= new HashMap<>();
        while(rs1.next()){
            Integer num= rs1.getInt("numProv");
            String nom= rs1.getString("nomProv");
            proveidors.put(num, nom);
        }
        return proveidors;
    }
    
    //Modificadors Proveidor
    
    public static void afegirProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.valid() && existeixPais(conn, prov.getCodiPais(), prov.getNomPais())){
            Statement stm= conn.createStatement();
            String sql= "INSERT INTO Proveidors VALUES(\""+prov.getNum()+"\",\""+prov.getNom()+"\",\""+prov.getEspecialitat()+"\",\""+prov.getEmail()+"\",\""+prov.getTempsEntrega()+"\",\""+prov.getInformacioAddicional()+"\", \""+prov.getCodiPais()+"\")";
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void actualitzarProveidor(Connection conn, Proveidor prov) throws SQLException{
        if(prov.valid() && existeixPais(conn, prov.getCodiPais(), prov.getNomPais())){
            Statement stm= conn.createStatement();
            String sql= "UPDATE Proveidors SET nomProv=\""+prov.getNom()+"\", esp=\""+prov.getEspecialitat()
                    +"\", email=\""+prov.getEmail()+"\", tempsEntr=\""+prov.getTempsEntrega()
                    +"\", infoAdd=\""+prov.getInformacioAddicional()+"\", codiPais=\""+prov.getCodiPais()+"\" WHERE numProv="+prov.getNum();
            stm.executeUpdate(sql);
        }
        else throw new SQLException();
    }
    
    public static void eliminarProveidor(Connection conn, Integer numProveidor) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "DELETE FROM Proveidors WHERE numProv="+numProveidor;
        stm.executeUpdate(sql);
    }
    
    //Consultes Compra
    
    public static ObservableList<Compra> obtenirCompres(Connection conn, String infoVenedor, String infoProveidor, LocalDate inici, LocalDate fi) throws SQLException{
        Integer venedor= null;
        if(infoVenedor != null) venedor= Integer.parseInt(infoVenedor.substring(0,1));
        Integer proveidor= null;
        if(infoProveidor != null) proveidor= Integer.parseInt(infoProveidor.substring(0,1));
        ObservableList<Compra> contingut= FXCollections.observableArrayList();
        Statement stm= conn.createStatement();
        Date dataInici= Date.valueOf(inici);
        Date dataFi= Date.valueOf(fi);
        String sql;
        if(venedor == null && proveidor == null) sql= "SELECT c.numC, p.numProv, p.nomProv, v.numVen, v.nom, v.cog1, v.cog2, c.data FROM Compres c INNER JOIN Proveidors p ON c.numProv = p.numProv INNER JOIN Venedors v ON c.numVen = v.numVen WHERE data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        else if(venedor == null) sql= "SELECT c.numC, p.numProv, p.nomProv, v.numVen, v.nom, v.cog1, v.cog2, c.data FROM Compres c INNER JOIN Proveidors p ON c.numProv = p.numProv AND p.numProv="+proveidor+" INNER JOIN Venedors v ON c.numVen = v.numVen WHERE data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        else if(proveidor == null) sql= "SELECT c.numC, p.numProv, p.nomProv, v.numVen, v.nom, v.cog1, v.cog2, c.data FROM Compres c INNER JOIN Proveidors p ON c.numProv = p.numProv INNER JOIN Venedors v ON c.numVen = v.numVen AND v.numVen="+venedor+" WHERE data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        else sql= "SELECT c.numC, p.numProv, p.nomProv, v.numVen, v.nom, v.cog1, v.cog2, c.data FROM Compres c INNER JOIN Proveidors p ON c.numProv = p.numProv AND p.numProv="+proveidor+" INNER JOIN Venedors v ON c.numVen = v.numVen AND v.numVen="+venedor+" WHERE data BETWEEN '"+dataInici+"' AND '"+dataFi+"'";
        ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            Integer num= rs1.getInt("c.numC");
            Integer numProv= rs1.getInt("p.numProv");
            String nomProv= rs1.getString("p.nomProv");
            Integer numVen= rs1.getInt("v.numVen");
            String nomVen= rs1.getString("v.nom");
            String cog1Ven= rs1.getString("v.cog1");
            String cog2Ven= rs1.getString("v.cog2");
            Date data= rs1.getDate("data");
            Compra aux= new Compra(numProv+". "+nomProv, numVen+". "+nomVen+" "+cog1Ven+" "+cog2Ven, data.toLocalDate());
            aux.setNum(num);
            contingut.add(aux);
        }
        return contingut;
    }
    
    public static long obtenirNumInsCompra(Connection conn) throws SQLException{
        long resultat= 0;
        Statement stm= conn.createStatement();
        String sql= "SELECT MAX(numC) FROM Compres";
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
    
    public static void obtenirLiniesCompra(Connection conn, Compra compra) throws SQLException{
        Statement stm= conn.createStatement();
        String sql= "SELECT lC.codiElem, lC.PVC, lC.des, lC.unitats, eV.descripcio FROM LiniesCompra lC INNER JOIN Compres c ON lC.numC = c.numC AND c.numC="+compra.getNum()+" INNER JOIN ElemsVendibles eV ON lC.codiElem=eV.codi";
        ResultSet rs1= stm.executeQuery(sql);
        while(rs1.next()){
            LiniaCompra lC= new LiniaCompra(rs1.getString("lC.codiElem"), rs1.getString("eV.descripcio"), rs1.getDouble("lC.PVC"), rs1.getInt("lC.unitats"), rs1.getDouble("lC.des"));
            compra.afegirLiniaCompra(lC);
        }
    }
    
    //Modificacions Compra
    
    public static void afegirCompra(Connection conn, Compra compra) throws SQLException{
        if(compra.valida()){
            Statement stm= conn.createStatement();
            LocalDate data= compra.getData();
            String sql= "INSERT INTO Compres VALUES("+compra.getNum()+","+compra.getNumProv()+","+compra.getNumVen()+","+compra.getImportTotal()+", '"+data.getYear()+"-"+data.getMonthValue()+"-"+data.getDayOfMonth()+"')";
            stm.executeUpdate(sql);

            Iterator<LiniaCompra> it= compra.getIteradorLC();
            while(it.hasNext()){
                LiniaCompra aux= it.next();
                if(existeixProducte(conn, aux.getCodiProducte())){
                    String sql2= "UPDATE Productes SET stock = stock + "+aux.getUnitats()+" WHERE codiElem=\""+aux.getCodiProducte()+"\"";
                    stm.executeUpdate(sql2);
                }
                String sql3= "INSERT INTO LiniesCompra VALUES ("+compra.getNum()+", \""+aux.getCodiProducte()+"\", "+aux.getPVC()+", "+aux.getDescompte()+", "+aux.getPFVC()+", "+aux.getUnitats()+", "+aux.getPT()+")";
                stm.executeUpdate(sql3);
            }
        }
        else throw new SQLException();
    }
    
    //Consultes Paisos
    
    public static String obtenirNomPais(Connection conn, String codiPais) throws SQLException{
        String nomPais;
        Statement stm= conn.createStatement();
        String sql= "SELECT nomPais FROM Paisos WHERE codi=\""+codiPais+"\"";
        ResultSet rs1= stm.executeQuery(sql);
        if(rs1.next()){
            nomPais= rs1.getString("nomPais");
        }
        else nomPais= null;
        return nomPais;
    }
    
    public static Boolean existeixPais(Connection conn, String codiPais, String nomPais) throws SQLException{
        Statement stm= conn.createStatement();
	ResultSet rs1= stm.executeQuery("SELECT codi FROM Paisos WHERE codi=\""+codiPais+"\" AND nomPais=\""+nomPais+"\"");
        if(rs1.next()) return true;
        else return false;
    }
}