import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

import java.io.*;

public class DBUtil {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static String search = "SELECT Guitar.gid, Brand.Brand, Model.Model, Color.Color, nW.Wood, bW.Wood, tW.Wood, fbW.Wood, Neck.Neck, nPB.PickupBrand as NeckPickupBrand, nPM.PickupModel as NeckPickupModel, mPB.PickupBrand as MiddlePickupBrand, mPM.PickupModel as MiddlePickupModel, bPB.PickupBrand as BridgePickupBrand, bPM.PickupModel as BridgePickupModel, Guitar.conditionid, Country.Country" +
            " FROM Guitar\n" +
            " LEFT JOIN Brand on Guitar.bid = Brand.bid\n" +
            " LEFT JOIN Model on Guitar.mid = Model.mid AND Guitar.bid = Model.bid\n" +
            " LEFT JOIN Color on Guitar.cid = Color.cid\n" +
            " LEFT JOIN Wood nW on Guitar.nWid = nW.wid\n" +
            " LEFT JOIN Wood bW on Guitar.bWid = bW.wid\n" +
            " LEFT JOIN Wood tW on Guitar.tWid = tW.wid\n" +
            " LEFT JOIN Wood fbW on Guitar.fbWid = fbW.wid\n" +
            " LEFT JOIN PickupBrand nPB on Guitar.nPBid = nPB.PBid\n" +
            " LEFT JOIN PickupBrand bPB on Guitar.bPBid = bPB.PBid\n" +
            " LEFT JOIN Pickup nPM on Guitar.nPMid = nPM.PMid AND Guitar.nPBid = nPM.PBid\n" +
            " LEFT JOIN Pickup bPM on Guitar.bPMid = bPM.PMid AND Guitar.bPBid = bPM.PBid\n" +
            " LEFT JOIN PickupBrand mPB on Guitar.mPBid = mPB.PBid\n" +
            " LEFT JOIN Pickup mPM on Guitar.mPMid = mPM.PMid AND Guitar.mPBid = mPM.PBid\n" +
            " LEFT JOIN Country on Guitar.countryid = Country.countryid\n" +
            " LEFT JOIN Neck on Guitar.nid = Neck.nid\n" +
            " WHERE ";

    private static String lastSearch = "";
    private static ArrayList<String> lastsearchValues;
    private static int lastnumSelected;
    private static int exportNum = 1;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/GuitarDB?serverTimezone=UTC","root","mei");
    }

    public static ObservableList<ObservableList> searchAllGuitars() {
        ObservableList<ObservableList> guitarList = FXCollections.observableArrayList();
        try {
            Connection conn = getConnection();

//            PreparedStatement ps = conn.prepareStatement("SELECT Guitar.gid, Brand.Brand, Model.Model, Color.Color, nW.Wood, bW.Wood, tW.Wood, fbW.Wood, Neck.Neck, nPB.PickupBrand as NeckPickupBrand, nPM.PickupModel as NeckPickupModel, mPB.PickupBrand as MiddlePickupBrand, mPM.PickupModel as MiddlePickupModel, bPB.PickupBrand as BridgePickupBrand, bPM.PickupModel as BridgePickupModel, Guitar.conditionid, Country.Country" +
//                    " FROM Guitar\n" +
//                    " LEFT JOIN Brand on Guitar.bid = Brand.bid\n" +
//                    " LEFT JOIN Model on Guitar.mid = Model.mid AND Guitar.bid = Model.bid\n" +
//                    " LEFT JOIN Color on Guitar.cid = Color.cid\n" +
//                    " LEFT JOIN Wood nW on Guitar.nWid = nW.wid\n" +
//                    " LEFT JOIN Wood bW on Guitar.bWid = bW.wid\n" +
//                    " LEFT JOIN Wood tW on Guitar.tWid = tW.wid\n" +
//                    " LEFT JOIN Wood fbW on Guitar.fbWid = fbW.wid\n" +
//                    " LEFT JOIN PickupBrand nPB on Guitar.nPBid = nPB.PBid\n" +
//                    " LEFT JOIN PickupBrand bPB on Guitar.bPBid = bPB.PBid\n" +
//                    " LEFT JOIN Pickup nPM on Guitar.nPMid = nPM.PMid AND Guitar.nPBid = nPM.PBid\n" +
//                    " LEFT JOIN Pickup bPM on Guitar.bPMid = bPM.PMid AND Guitar.bPBid = bPM.PBid\n" +
//                    " LEFT JOIN PickupBrand mPB on Guitar.mPBid = mPB.PBid\n" +
//                    " LEFT JOIN Pickup mPM on Guitar.mPMid = mPM.PMid AND Guitar.mPBid = mPM.PBid\n" +
//                    " LEFT JOIN Country on Guitar.countryid = Country.countryid\n" +
//                    " LEFT JOIN Neck on Guitar.nid = Neck.nid\n" +
//                    " ORDER BY gid ASC");

            String query = "{ call GetAllGuitars() }";
            CallableStatement stmt = conn.prepareCall(query);

            writeToLog(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                guitarList.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return guitarList;
    }

    public static ObservableList<ObservableList> search(int numSelected, ArrayList<String> columnsToSearch, ArrayList<String> searchValues) {
        lastsearchValues = new ArrayList<String>(searchValues);
        lastnumSelected = numSelected;

        ObservableList<ObservableList> guitarList = FXCollections.observableArrayList();
        try {
            Connection conn = getConnection();
            String sql = "" + search;

            for (int i = 0; i < numSelected; i++) {
                if (i < numSelected - 1) {
                    sql += columnsToSearch.get(i) + " = ? AND ";
                }
                else {
                    sql += columnsToSearch.get(i) + " = ?";
                }
            }
            sql += " ORDER BY gid ASC;";
            lastSearch = sql;
            PreparedStatement ps = conn.prepareStatement(sql);
            //System.out.println(numSelected);
            for (int i = 0; i < numSelected; i++) {
                System.out.println(i);
                ps.setString(i + 1, searchValues.get(i));
            }
            System.out.println(ps);
            if (numSelected > 0) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(rs.getString(i));
                    }
                    guitarList.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        //System.out.println(guitarList);
        return guitarList;
    }

    public static void exportToCSV() {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(lastSearch);
            for (int i = 0; i < lastnumSelected; i++) {
                System.out.println(i);
                ps.setString(i + 1, lastsearchValues.get(i));
            }
            ResultSet rs = ps.executeQuery();
            String fileName = "Export/export_" + exportNum + ".csv";
            PrintWriter pw = new PrintWriter(new File(fileName));
            StringBuilder sb = new StringBuilder();
            System.out.println("in export");
            System.out.println(lastSearch);
            while (rs.next()) {
                sb.setLength(0);
                int id = rs.getInt("gid");
                String brand = rs.getString("Brand");
                String model = rs.getString("Model");
                String color = rs.getString("Color");
                String neckWood = rs.getString("nW.Wood");
                String bodyWood = rs.getString("bW.Wood");
                String topWood = rs.getString("tW.Wood");
                String fingerBoardWood = rs.getString("fbW.Wood");
                String neck = rs.getString("Neck");
                String neckPickupBrand = rs.getString("NeckPickupBrand");
                String neckPickupModel = rs.getString("NeckPickupModel");
                String middlePickupBrand = rs.getString("MiddlePickupBrand");
                String middlePickupModel = rs.getString("MiddlePickupModel");
                String bridgePickupBrand = rs.getString("BridgePickupBrand");
                String bridgePickupModel = rs.getString("BridgePickupModel");
                int conditionid = rs.getInt("conditionid");
                String country = rs.getString("Country");
                sb.append(id);
                sb.append(",");
                sb.append(brand);
                sb.append(",");
                sb.append(model);
                sb.append(",");
                sb.append(color);
                sb.append(",");
                sb.append(neckWood);
                sb.append(",");
                sb.append(bodyWood);
                sb.append(",");
                sb.append(topWood);
                sb.append(",");
                sb.append(fingerBoardWood);
                sb.append(",");
                sb.append(neck);
                sb.append(",");
                sb.append(neckPickupBrand);
                sb.append(",");
                sb.append(neckPickupModel);
                sb.append(",");
                sb.append(middlePickupBrand);
                sb.append(",");
                sb.append(middlePickupModel);
                sb.append(",");
                sb.append(bridgePickupBrand);
                sb.append(",");
                sb.append(bridgePickupModel);
                sb.append(",");
                sb.append(conditionid);
                sb.append(",");
                sb.append(country);
                sb.append('\n');
                pw.write(sb.toString());
            }
            pw.close();
            exportNum++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String deleteGuitar(String gid) {
        int id = Integer.parseInt(gid);
        try {
            Connection conn = getConnection();
            String sql = "DELETE FROM Guitar WHERE gid = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            writeToLog(ps.toString());
            ps.executeUpdate();
        }
        catch (Exception e) {
            return "Guitar " + gid + " was not deleted from the Database.\n";
        }
        String feedback = "Guitar " + gid + " was successfully deleted from the Database.\n";
        return feedback;
    }

    public static String putguitar(Guitar g1)
    {
        try {
            Connection conn = getConnection();
            int bid;
            int mid = 0;
            int nWid;
            int bWid;
            int tWid;
            int fbWid;
            int cid;
            int nid;
            int nPBid;
            int nPMid = 0;
            int mPBid;
            int mPMid = 0;
            int bPBid;
            int bPMid = 0;
            int couid;
            int conid;
            try
            {
                conn = getConnection();
                conn.setAutoCommit(false);

                String checkBrand = "SELECT * FROM Brand WHERE Brand = ?";
                PreparedStatement stmt = conn.prepareStatement(checkBrand);
                stmt.setString(1, g1.getBrand());
                ResultSet resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    bid = resultSet.getInt("bid");

                }
                else
                {
                    String addBrand = "INSERT INTO Brand(Brand) VALUES (?)";
                    stmt = conn.prepareStatement(addBrand);
                    stmt.setString(1,g1.getBrand());
                    stmt.executeUpdate();
                    checkBrand = "SELECT * FROM Brand WHERE Brand = ?";
                    stmt = conn.prepareStatement(checkBrand);
                    stmt.setString(1, g1.getBrand());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    bid = resultSet.getInt("bid");
                }

                String checkModel = "SELECT * FROM Model WHERE Model = ? AND bid = ?";
                stmt = conn.prepareStatement(checkModel);
                stmt.setString(1, g1.getModel());
                stmt.setInt(2, bid);
                resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    mid = resultSet.getInt("mid");
                }
                else
                {
                    String findModels = "SELECT mid FROM Model WHERE bid = ?";
                    stmt = conn.prepareStatement(findModels);
                    stmt.setInt(1, bid);
                    resultSet = stmt.executeQuery();


                    while (resultSet.next()) {
                        if (mid < resultSet.getInt("mid")) {
                            mid = resultSet.getInt("mid");
                        }

                    }
                    mid++;
                    String addModel = "INSERT INTO Model(bid,mid,Model) VALUES (?,?,?)";
                    stmt = conn.prepareStatement(addModel);
                    stmt.setInt(1, bid);
                    stmt.setInt(2, mid);
                    stmt.setString(3, g1.getModel());
                    stmt.executeUpdate();
                }

                String checknWood = "SELECT wid FROM Wood WHERE Wood = ?";
                stmt = conn.prepareStatement(checknWood);
                stmt.setString(1,g1.getNeckWood());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    nWid = resultSet.getInt("wid");
                }
                else
                {
                    String addWood = "INSERT INTO Wood(Wood) VALUES (?)";
                    stmt = conn.prepareStatement(addWood);
                    stmt.setString(1,g1.getNeckWood());
                    stmt.executeUpdate();
                    checknWood = "SELECT * FROM Wood WHERE Wood = ?";
                    stmt = conn.prepareStatement(checknWood);
                    stmt.setString(1, g1.getNeckWood());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    nWid = resultSet.getInt("wid");
                }

                String checkbWood = "SELECT wid FROM Wood WHERE Wood = ?";
                stmt = conn.prepareStatement(checkbWood);
                stmt.setString(1,g1.getBodyWood());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    bWid = resultSet.getInt("wid");
                }
                else
                {
                    String addWood = "INSERT INTO Wood(Wood) VALUES (?)";
                    stmt = conn.prepareStatement(addWood);
                    stmt.setString(1,g1.getBodyWood());
                    stmt.executeUpdate();
                    checkbWood = "SELECT * FROM Wood WHERE Wood = ?";
                    stmt = conn.prepareStatement(checkbWood);
                    stmt.setString(1, g1.getBodyWood());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    bWid = resultSet.getInt("wid");
                }

                String checktWood = "SELECT wid FROM Wood WHERE Wood = ?";
                stmt = conn.prepareStatement(checktWood);
                stmt.setString(1,g1.getTopWood());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    tWid = resultSet.getInt("wid");
                }
                else
                {
                    String addWood = "INSERT INTO Wood(Wood) VALUES (?)";
                    stmt = conn.prepareStatement(addWood);
                    stmt.setString(1,g1.getTopWood());
                    stmt.executeUpdate();
                    checktWood = "SELECT * FROM Wood WHERE Wood = ?";
                    stmt = conn.prepareStatement(checktWood);
                    stmt.setString(1, g1.getTopWood());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    tWid = resultSet.getInt("wid");
                }

                String checkfbWood = "SELECT wid FROM Wood WHERE Wood = ?";
                stmt = conn.prepareStatement(checkfbWood);
                stmt.setString(1,g1.getFingerBoardWood());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    fbWid= resultSet.getInt("wid");
                }
                else
                {
                    String addWood = "INSERT INTO Wood(Wood) VALUES (?)";
                    stmt = conn.prepareStatement(addWood);
                    stmt.setString(1,g1.getFingerBoardWood());
                    stmt.executeUpdate();
                    checktWood = "SELECT * FROM Wood WHERE Wood = ?";
                    stmt = conn.prepareStatement(checkfbWood);
                    stmt.setString(1, g1.getFingerBoardWood());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    fbWid = resultSet.getInt("wid");
                }

                String checkColor = "SELECT cid FROM Color WHERE Color = ?";
                stmt = conn.prepareStatement(checkColor);
                stmt.setString(1,g1.getColor());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    cid = resultSet.getInt("cid");
                }
                else
                {
                    String addColor = "INSERT INTO Color(Color) VALUES (?)";
                    stmt = conn.prepareStatement(addColor);
                    stmt.setString(1,g1.getColor());
                    stmt.executeUpdate();
                    checkColor = "SELECT * FROM Color WHERE Color = ?";
                    stmt = conn.prepareStatement(checkColor);
                    stmt.setString(1, g1.getColor());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    cid = resultSet.getInt("cid");
                }

                String checkNeck = "SELECT nid FROM neck WHERE Neck = ?";
                stmt = conn.prepareStatement(checkNeck);
                stmt.setString(1,g1.getNeck());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    nid = resultSet.getInt("nid");
                }
                else
                {
                    String addNeck = "INSERT INTO neck(Neck) VALUES (?)";
                    stmt = conn.prepareStatement(addNeck);
                    stmt.setString(1,g1.getNeck());
                    stmt.executeUpdate();
                    checkNeck = "SELECT * FROM neck WHERE Neck = ?";
                    stmt = conn.prepareStatement(checkNeck);
                    stmt.setString(1, g1.getNeck());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    nid = resultSet.getInt("nid");
                }

                String checkNPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                stmt = conn.prepareStatement(checkNPB);
                stmt.setString(1, g1.getNeckPickupBrand());
                resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    nPBid = resultSet.getInt("PBid");

                }
                else
                {
                    String addPB = "INSERT INTO PickupBrand(PickupBrand) VALUES (?)";
                    stmt = conn.prepareStatement(addPB);
                    stmt.setString(1,g1.getNeckPickupBrand());
                    stmt.executeUpdate();
                    checkNPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                    stmt = conn.prepareStatement(checkNPB);
                    stmt.setString(1, g1.getNeckPickupBrand());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    nPBid = resultSet.getInt("PBid");
                }

                String checkNPM = "SELECT * FROM Pickup WHERE PBid = ? AND PickupModel = ?";
                stmt = conn.prepareStatement(checkNPM);
                stmt.setInt(1, nPBid);
                stmt.setString(2, g1.getNeckPickupModel());
                resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    nPMid = resultSet.getInt("PMid");
                }
                else
                {
                    String findNeckPickuPickupModels = "SELECT PMid FROM Pickup WHERE PBid = ?";
                    stmt = conn.prepareStatement(findNeckPickuPickupModels);
                    stmt.setInt(1, nPBid);
                    resultSet = stmt.executeQuery();


                    while (resultSet.next()) {
                        if (nPMid < resultSet.getInt("PMid")) {
                            nPMid = resultSet.getInt("PMid");
                        }

                    }
                    nPMid++;
                    String addPickuPickupModel = "INSERT INTO Pickup(PBid,PMid,PickupModel) VALUES (?,?,?)";
                    stmt = conn.prepareStatement(addPickuPickupModel);
                    stmt.setInt(1, nPBid);
                    stmt.setInt(2, nPMid);
                    stmt.setString(3, g1.getNeckPickupModel());
                    stmt.executeUpdate();
                }

                String checkMPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                stmt = conn.prepareStatement(checkMPB);
                stmt.setString(1, g1.getMiddlePickupBrand());
                resultSet = stmt.executeQuery();
                if(g1.getMiddlePickupBrand() == null)
                {
                    mPBid = 0;
                }
                else if(resultSet.next())
                {
                    mPBid = resultSet.getInt("PBid");

                }
                else
                {
                    String addPB = "INSERT INTO PickupBrand(PickupBrand) VALUES (?)";
                    stmt = conn.prepareStatement(addPB);
                    stmt.setString(1,g1.getMiddlePickupBrand());
                    stmt.executeUpdate();
                    checkMPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                    stmt = conn.prepareStatement(checkMPB);
                    stmt.setString(1, g1.getMiddlePickupBrand());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    mPBid = resultSet.getInt("PBid");
                }

                String checkMPM = "SELECT * FROM Pickup WHERE PBid = ? AND PickupModel = ?";
                stmt = conn.prepareStatement(checkMPM);
                stmt.setInt(1, mPBid);
                stmt.setString(2, g1.getMiddlePickupModel());
                resultSet = stmt.executeQuery();
                if(g1.getMiddlePickupModel() == null)
                {
                    mPMid = 0;
                }
                else if(resultSet.next())
                {
                    mPMid = resultSet.getInt("PMid");
                }
                else
                {
                    String findMiddlePickuPickupModels = "SELECT PMid FROM Pickup WHERE PBid = ?";
                    stmt = conn.prepareStatement(findMiddlePickuPickupModels);
                    stmt.setInt(1, mPBid);
                    resultSet = stmt.executeQuery();


                    while (resultSet.next()) {
                        if (mPMid < resultSet.getInt("PMid")) {
                            mPMid = resultSet.getInt("PMid");
                        }

                    }
                    mPMid++;
                    String addPickuPickupModel = "INSERT INTO pickup(PBid,PMid,PickupModel) VALUES (?,?,?)";
                    stmt = conn.prepareStatement(addPickuPickupModel);
                    stmt.setInt(1, mPBid);
                    stmt.setInt(2, mPMid);
                    stmt.setString(3, g1.getMiddlePickupModel());
                    stmt.executeUpdate();
                }

                String checkBPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                stmt = conn.prepareStatement(checkBPB);
                stmt.setString(1, g1.getBridgePickupBrand());
                resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    bPBid = resultSet.getInt("PBid");
                }
                else
                {
                    String addPB = "INSERT INTO PickupBrand(PickupBrand) VALUES (?)";
                    stmt = conn.prepareStatement(addPB);
                    stmt.setString(1,g1.getBridgePickupBrand());
                    stmt.executeUpdate();
                    checkBPB = "SELECT * FROM PickupBrand WHERE PickupBrand = ?";
                    stmt = conn.prepareStatement(checkBPB);
                    stmt.setString(1, g1.getBridgePickupBrand());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    bPBid = resultSet.getInt("PBid");
                }

                String checkBPM = "SELECT * FROM Pickup WHERE PBid = ? AND PickupModel = ?";
                stmt = conn.prepareStatement(checkBPM);
                stmt.setInt(1, bPBid);
                stmt.setString(2, g1.getBridgePickupModel());
                resultSet = stmt.executeQuery();
                if(resultSet.next())
                {
                    bPMid = resultSet.getInt("PMid");
                }
                else
                {
                    String findMiddlePickuPickupModels = "SELECT PMid FROM Pickup WHERE PBid = ?";
                    stmt = conn.prepareStatement(findMiddlePickuPickupModels);
                    stmt.setInt(1, bPBid);
                    resultSet = stmt.executeQuery();


                    while (resultSet.next()) {
                        if (bPMid < resultSet.getInt("PMid")) {
                            bPMid = resultSet.getInt("PMid");
                        }

                    }
                    bPMid++;
                    String addPickuPickupModel = "INSERT INTO pickup(PBid,PMid,PickupModel) VALUES (?,?,?)";
                    stmt = conn.prepareStatement(addPickuPickupModel);
                    stmt.setInt(1, bPBid);
                    stmt.setInt(2, bPMid);
                    stmt.setString(3, g1.getBridgePickupModel());
                    stmt.executeUpdate();
                }

                String checkCountry = "SELECT countryid FROM Country WHERE Country = ?";
                stmt = conn.prepareStatement(checkCountry);
                stmt.setString(1,g1.getCountry());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    couid = resultSet.getInt("countryid");
                }
                else
                {
                    String addCountry = "INSERT INTO Country(Country) VALUES (?)";
                    stmt = conn.prepareStatement(addCountry);
                    stmt.setString(1,g1.getCountry());
                    stmt.executeUpdate();
                    checkCountry = "SELECT * FROM Country WHERE Country = ?";
                    stmt = conn.prepareStatement(checkCountry);
                    stmt.setString(1, g1.getCountry());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    couid = resultSet.getInt("countryid");
                }

                String checkCondition = "SELECT * FROM guitarCondition WHERE conditionid = ?";
                stmt = conn.prepareStatement(checkCondition);
                stmt.setInt(1,g1.getCondition());
                resultSet = stmt.executeQuery();

                if(resultSet.next())
                {
                    conid = resultSet.getInt("conditionid");
                }
                else
                {
                    String addCondition = "INSERT INTO guitarCondition(conditionid) VALUES (?)";
                    stmt = conn.prepareStatement(addCondition);
                    stmt.setInt(1,g1.getCondition());
                    stmt.executeUpdate();
                    checkCondition = "SELECT * FROM guitarCondition WHERE conditionid = ?";
                    stmt = conn.prepareStatement(checkCondition);
                    stmt.setInt(1, g1.getCondition());
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    conid = resultSet.getInt("conditionid");
                }

                String createGuitar = "INSERT INTO guitar(bid, " +
                        "mid, nWid, bWid, tWid, fbWid, cid, nid, " +
                        "nPBid, nPMid, mPBid, mPMid, bPBid, bPMid, countryid, " +
                        "conditionid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                stmt = conn.prepareStatement(createGuitar);
                stmt.setInt(1,bid );
                stmt.setInt(2,mid );
                stmt.setInt(3,nWid );
                stmt.setInt(4,bWid );
                stmt.setInt(5,tWid );
                stmt.setInt(6,fbWid );
                stmt.setInt(7,cid );
                stmt.setInt(8,nid );
                stmt.setInt(9,nPBid);
                stmt.setInt(10,nPMid );
                stmt.setInt(11,mPBid );
                stmt.setInt(12,mPMid );
                stmt.setInt(13,bPBid );
                stmt.setInt(14,bPMid );
                stmt.setInt(15,couid );
                stmt.setInt(16,conid );
                writeToLog(stmt.toString());
                stmt.executeUpdate();
            }
            catch (Exception e ) {
                if (conn != null) {
                    try {
                        System.err.print("Transaction is being rolled back");
                        conn.rollback();
                    } catch(SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            } finally {
                conn.setAutoCommit(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Guitar inserted???";
    }

    public static String updateGuitar(int gid, int index, String newVal) {
        int updateID;
        String[] colNames = new String[]{"cid", "nWid", "bWid", "tWid", "fbWid", "nid", "countryid", "conditionid"};
        String[] tablecolnames = new String[]{"cid","wid","wid","wid","wid","nid","countryid", "conditionid"};
        String[] tables = new String[]{"Color","Wood",",Wood","Wood","Wood","Neck","Country", "GuitarCondition"};

        System.out.println(index);
        try {
            Connection conn = getConnection();
            if(index == 7)
            {
                int cond = Integer.parseInt(newVal);
                String updateCond = "UPDATE Guitar SET conditionid = ? WHERE gid = ?";
                PreparedStatement stmt = conn.prepareStatement(updateCond);
                stmt.setInt(1,cond);
                stmt.setInt(2,gid);
                stmt.executeUpdate();
                return "Guitar was update succesfully! :>)\n";
            }

            String checkUpdate = "SELECT " + tablecolnames[index] + " FROM " + tables[index] + " WHERE " +  tables[index] + " = ?";
            PreparedStatement stmt = conn.prepareStatement(checkUpdate);
            stmt.setString(1,newVal);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next())
            {
                updateID = resultSet.getInt(tablecolnames[index]);
            }
            else
            {
                String insert = "INSERT INTO " + tables[index] + "(" + tables[index] + ") VALUES (?)";
                stmt = conn.prepareStatement(insert);
                stmt.setString(1,newVal);
                stmt.executeUpdate();
                checkUpdate =  "SELECT " + tablecolnames[index] + " FROM " + tables[index] + " WHERE " +  tables[index] + " = ?";
                stmt = conn.prepareStatement(checkUpdate);
                stmt.setString(1, newVal);
                resultSet = stmt.executeQuery();
                resultSet.next();
                updateID = resultSet.getInt(tablecolnames[index]);
            }
            PreparedStatement ps = conn.prepareStatement("UPDATE Guitar SET " + colNames[index] + " = ? WHERE gid = ?");
            ps.setInt(1, updateID);
            ps.setInt(2, gid);
            writeToLog(ps.toString());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return "Guitar " + gid + " was not updated succesfully.\n";
        }
        return "Guitar " + gid + " was updated succesfully.\n";
    }

    public static void writeToLog(String query) {
        String filePath = "Logs/log_file";
        File file = new File(filePath);
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;
        try {
            fr = new FileWriter(file, true); // true to append
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);
            pr.println(query);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pr.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
