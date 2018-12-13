import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView guitarTable;
    @FXML
    private ChoiceBox brandChoiceBox;
    @FXML
    private ChoiceBox modelChoiceBox;
    @FXML
    private ChoiceBox neckWoodChoiceBox;
    @FXML
    private ChoiceBox bodyWoodChoiceBox;
    @FXML
    private ChoiceBox fingerBoardWoodChoiceBox;
    @FXML
    private ChoiceBox topWoodChoiceBox;
    @FXML
    private ChoiceBox colorChoiceBox;
    @FXML
    private ChoiceBox neckChoiceBox;
    @FXML
    private ChoiceBox neckPickupBrandChoiceBox;
    @FXML
    private ChoiceBox neckPickupModelChoiceBox;
    @FXML
    private ChoiceBox middlePickupBrandChoiceBox;
    @FXML
    private ChoiceBox middlePickupModelChoiceBox;
    @FXML
    private ChoiceBox bridgePickupBrandChoiceBox;
    @FXML
    private ChoiceBox bridgePickupModelChoiceBox;
    @FXML
    private ChoiceBox countryChoiceBox;
    @FXML
    private ChoiceBox conditionChoiceBox;
    @FXML
    private TextField brand_c;
    @FXML
    private TextField model_c;
    @FXML
    private TextField neck_wood_c;
    @FXML
    private TextField body_wood_c;
    @FXML
    private TextField finger_board_wood_c;
    @FXML
    private TextField color_c;
    @FXML
    private TextField neck_c;
    @FXML
    private TextField neck_pb_c;
    @FXML
    private TextField neck_pm_c;
    @FXML
    private TextField middle_pb_c;
    @FXML
    private TextField middle_pm_c;
    @FXML
    private TextField bridge_pb_c;
    @FXML
    private TextField bridge_pm_c;
    @FXML
    private TextField country_c;
    @FXML
    private TextField condition_c;
    @FXML
    private TextField top_wood_c;
    @FXML
    private TextField guitarIDUpdateTextField;
    @FXML
    private ChoiceBox colUpdateChoiceBox;
    @FXML
    private TextField newValueTextField;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private TextField guitar_id_d;
    @FXML
    private CheckBox deleteCheckbox;

    private ChoiceBox[] myChoiceBoxes;
    private ChoiceBox[] allChoiceBoxes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table data
        buildTableColumns();
        myChoiceBoxes = new ChoiceBox[]{brandChoiceBox, neckWoodChoiceBox, bodyWoodChoiceBox, topWoodChoiceBox, fingerBoardWoodChoiceBox,
        colorChoiceBox, neckChoiceBox, neckPickupBrandChoiceBox, middlePickupBrandChoiceBox, bridgePickupBrandChoiceBox, countryChoiceBox, conditionChoiceBox};
        allChoiceBoxes = new ChoiceBox[]{brandChoiceBox, modelChoiceBox, neckWoodChoiceBox, bodyWoodChoiceBox, topWoodChoiceBox, fingerBoardWoodChoiceBox,
                colorChoiceBox, neckChoiceBox, neckPickupBrandChoiceBox, neckPickupModelChoiceBox, middlePickupBrandChoiceBox, middlePickupModelChoiceBox, bridgePickupBrandChoiceBox,
                bridgePickupModelChoiceBox, countryChoiceBox, conditionChoiceBox};
        buildChoiceBoxItems();
        buildUpdateChoiceBox();
        outputTextArea.setEditable(false);
        deleteCheckbox.setDisable(true);
    }

    public void displayAllGuitars() {
        ObservableList<ObservableList> guitarList = DBUtil.searchAllGuitars();
        guitarTable.setItems(guitarList);
    }

    public void clearTable() {
        guitarTable.getItems().clear();
        for (int i = 0; i < allChoiceBoxes.length; i++) {
            allChoiceBoxes[i].getItems().clear();
        }
        buildChoiceBoxItems();
    }

    public void searchGuitars() {
        String[] columnNames = new String[]{"Brand", "Model", "nW.Wood", "bW.Wood", "tW.Wood", "fbW.Wood", "Color", "Neck", "nPB.PickupBrand", "nPM.PickupModel", "mPB.PickupBrand", "mPM.PickupModel",
        "bPB.PickupBrand", "bPM.PickupModel", "Country", "conditionid"};
        ArrayList<String> columnsToSearch = new ArrayList<>();
        ArrayList<String> searchValues = new ArrayList<>();
        int numSelected = 0;
        for (int i = 0; i < allChoiceBoxes.length; i++) {
            System.out.println(allChoiceBoxes[i]);
            if (!allChoiceBoxes[i].getSelectionModel().isEmpty()) {
                System.out.println(i);
                numSelected++;
                columnsToSearch.add(columnNames[i]);
                searchValues.add(allChoiceBoxes[i].getValue().toString());
            }
        }
        ObservableList<ObservableList> guitarList = DBUtil.search(numSelected, columnsToSearch, searchValues);
        guitarTable.setItems(guitarList);
    }

    public void insertGuitar() {
        if (!brand_c.getText().isEmpty() && !model_c.getText().isEmpty() && !neck_wood_c.getText().isEmpty() && !body_wood_c.getText().isEmpty() && !top_wood_c.getText().isEmpty() && !finger_board_wood_c.getText().isEmpty()
        && !color_c.getText().isEmpty() && !neck_c.getText().isEmpty() && !neck_pb_c.getText().isEmpty() && !neck_pm_c.getText().isEmpty() && !middle_pb_c.getText().isEmpty() && !middle_pm_c.getText().isEmpty()
        && !bridge_pb_c.getText().isEmpty() && !bridge_pm_c.getText().isEmpty() && !country_c.getText().isEmpty() && !condition_c.getText().isEmpty())
        {
            String brand = brand_c.getText();
            String model = model_c.getText();
            String neckWood = neck_wood_c.getText();
            String bodyWood = body_wood_c.getText();
            String topWood = top_wood_c.getText();
            String fingerBoardWood = finger_board_wood_c.getText();
            String color = color_c.getText();
            String neck = neck_c.getText();
            String neckPB = neck_pb_c.getText();
            String neckPM = neck_pm_c.getText();
            String middlePB = middle_pb_c.getText();
            String middlePM = middle_pm_c.getText();
            String bridgePB = bridge_pb_c.getText();
            String bridgePM = bridge_pm_c.getText();
            String country = country_c.getText();
            int condition = 0;
            try {
                condition = Integer.parseInt(condition_c.getText());
            } catch (Exception e) {
                outputTextArea.appendText("Condition must be an integer.\n");
                return;
            }

            String[] userStrings = new String[]{brand, model, neckWood, bodyWood, topWood, fingerBoardWood, color, neck, neckPB,
            neckPM, middlePB, middlePM, bridgePB, bridgePM, country};

            for (int i = 0; i < userStrings.length; i++) {
                if (userStrings[i].length() > 45) {
                    outputTextArea.appendText("Inputs can't be longer than 45 character.\n");
                    return;
                }
            }
            if (condition < 0 || condition > 10) {
                outputTextArea.appendText("Condition must be between 0 and 10.\n");
                return;
            }

            Guitar g = new Guitar();
            g.setBrand(brand);
            g.setModel(model);
            g.setNeckWood(neckWood);
            g.setBodyWood(bodyWood);
            g.setTopWood(topWood);
            g.setFingerBoardWood(fingerBoardWood);
            g.setColor(color);
            g.setNeck(neck);
            g.setNeckPickupBrand(neckPB);
            g.setNeckPickupModel(neckPM);
            g.setMiddlePickupBrand(middlePB);
            g.setMiddlePickupModel(middlePM);
            g.setBridgePickupBrand(bridgePB);
            g.setBridgePickupModel(bridgePM);
            g.setCountry(country);
            g.setCondition(condition);
            outputTextArea.appendText(g.toString());
            String feedback = DBUtil.putguitar(g);
            outputTextArea.appendText(feedback);
        }
        else {
            outputTextArea.appendText("Please fill out all fields to add a guitar.\n");
        }
    }

    public void deleteGuitar() {
        if (!guitar_id_d.getText().isEmpty() && deleteCheckbox.isDisabled()) {
            outputTextArea.appendText("Please confirm if you would like to delete.\n");
            deleteCheckbox.setDisable(false);
        }
        else {
            if (deleteCheckbox.isSelected() && !guitar_id_d.getText().isEmpty()) {
                String gid = guitar_id_d.getText();

                try {
                    int gid_int = Integer.parseInt(gid);
                } catch (Exception e) {
                    outputTextArea.appendText("Please enter an integer.\n");
                    return;
                }

                String feedback = DBUtil.deleteGuitar(gid);
                outputTextArea.appendText(feedback);
                deleteCheckbox.setSelected(false);
                deleteCheckbox.setDisable(true);
            }
            else {
                outputTextArea.appendText("Please enter a guitar id to delete.\n");
            }
        }
    }

    public void updateGuitar() {
        if (!colUpdateChoiceBox.getSelectionModel().isEmpty() && !guitarIDUpdateTextField.getText().isEmpty()) {
            try {
                int gid_int = Integer.parseInt(guitarIDUpdateTextField.getText());
            } catch (Exception e) {
                outputTextArea.appendText("Please enter an integer for guitar id.\n");
                return;
            }

            int index = colUpdateChoiceBox.getSelectionModel().getSelectedIndex();
            int gid = Integer.parseInt(guitarIDUpdateTextField.getText());

            String newVal = newValueTextField.getText();

            if (newVal.length() > 45) {
                outputTextArea.appendText("Your new value must be less than 45 chars.");
                return;
            }

            String feedback = DBUtil.updateGuitar(gid, index, newVal);

            outputTextArea.appendText(feedback);
        }
    }

    public void exportToCSV() {
        DBUtil.exportToCSV();
    }

    public void buildTableColumns() {
        Connection conn;
        try {
            conn = DBUtil.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT Guitar.gid, Brand.Brand, Model.Model, Color.Color, nW.Wood, bW.Wood, tW.Wood, fbW.Wood, Neck.Neck, nPB.PickupBrand, nPM.PickupModel, mPB.PickupBrand, mPM.PickupModel, bPB.PickupBrand, bPM.PickupModel, Guitar.conditionid, Country.Country" +
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
                    " ORDER BY gid ASC\n" +
                    " LIMIT 1");

            ResultSet rs = ps.executeQuery();
            // add table columns dynamically
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn();

                if (i == 0) {
                    col = new TableColumn("ID");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<Number>>() {
                        public ObservableValue<Number> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(j).toString()));
                        }
                    });
                }
                else if (i == 4) {
                    col = new TableColumn("Neck Wood");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 5) {
                    col = new TableColumn("Body Wood");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 6) {
                    col = new TableColumn("Top Wood");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 7) {
                    col = new TableColumn("Finger Board Wood");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 9) {
                    col = new TableColumn("Neck Pickup Brand");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 10) {
                    col = new TableColumn("Neck Pickup Model");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 11) {
                    col = new TableColumn("Middle Pickup Brand");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 12) {
                    col = new TableColumn("Middle Pickup Model");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 13) {
                    col = new TableColumn("Bridge Pickup Brand");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 14) {
                    col = new TableColumn("Bridge Pickup Model");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }
                else if (i == 15) {
                    col = new TableColumn("Condition");
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<Number>>() {
                        public ObservableValue<Number> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleIntegerProperty(Integer.parseInt(param.getValue().get(j).toString()));
                        }
                    });
                }
                else {
                    col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }

                guitarTable.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void buildChoiceBoxItems() {
        String[] columnNames = {"Brand", "Wood", "Wood", "Wood", "Wood", "Color", "Neck", "PickupBrand", "PickupBrand", "PickupBrand", "Country", "conditionid"};
        String[] tableNames = {"Brand", "Wood", "Wood", "Wood", "Wood", "Color", "Neck", "PickupBrand", "PickupBrand", "PickupBrand", "Country", "GuitarCondition"};
        try {
            Connection conn = DBUtil.getConnection();

            for (int i = 0; i < myChoiceBoxes.length; i++) {
                PreparedStatement ps = conn.prepareStatement("SELECT " + columnNames[i] + " FROM GuitarDB." + tableNames[i] + ";");
                ResultSet rs = ps.executeQuery();
                ArrayList<String> list = new ArrayList<String>();
                while (rs.next()) {
                    String brand = rs.getString(columnNames[i]);
                    list.add(brand);
                }
                myChoiceBoxes[i].getItems().addAll(list);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildneckPickupModelChoiceBox(ActionEvent actionEvent) {
        String selected = neckPickupBrandChoiceBox.getValue().toString();
        neckPickupModelChoiceBox.getItems().clear();
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT PickupModel FROM GuitarDB.PickupBrand pb, GuitarDB.Pickup p WHERE pb.PickupBrand = '" + selected + "' AND pb.PBid = p.PBid;");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                String model = rs.getString("PickupModel");
                list.add(model);
            }
            neckPickupModelChoiceBox.getItems().addAll(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildmiddlePickupModelChoiceBox(ActionEvent actionEvent) {
        String selected = middlePickupBrandChoiceBox.getValue().toString();
        middlePickupModelChoiceBox.getItems().clear();
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT PickupModel FROM GuitarDB.PickupBrand pb, GuitarDB.Pickup p WHERE pb.PickupBrand = '" + selected + "' AND pb.PBid = p.PBid;");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                String model = rs.getString("PickupModel");
                list.add(model);
            }
            middlePickupModelChoiceBox.getItems().addAll(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildbridgePickupModelChoiceBox(ActionEvent actionEvent) {
        String selected = bridgePickupBrandChoiceBox.getValue().toString();
        bridgePickupModelChoiceBox.getItems().clear();
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT PickupModel FROM GuitarDB.PickupBrand pb, GuitarDB.Pickup p WHERE pb.PickupBrand = '" + selected + "' AND pb.PBid = p.PBid;");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                String model = rs.getString("PickupModel");
                list.add(model);
            }
            bridgePickupModelChoiceBox.getItems().addAll(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildModelChoiceBox(ActionEvent actionEvent) {
        String selected = brandChoiceBox.getValue().toString();
        modelChoiceBox.getItems().clear();
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Model FROM GuitarDB.Model m, GuitarDB.Brand b WHERE b.Brand = '" + selected + "' AND b.bid = m.bid;");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                String brand = rs.getString("Model");
                list.add(brand);
            }
            modelChoiceBox.getItems().addAll(list);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildUpdateChoiceBox() {
        colUpdateChoiceBox.getItems().addAll("Color", "Neck Wood", "Body Wood", "Top Wood", "Finger Board Wood", "Neck", "Country", "Condition");
    }
}
