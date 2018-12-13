
public class Guitar
{
    private int guitarID;
    private String brand;
    private String model;
    private String neckWood;
    private String bodyWood;
    private String topWood;
    private String fingerBoardWood;
    private String color;
    private String neck;
    private String neckPickupBrand;
    private String neckPickupModel;
    private String middlePickupBrand;
    private String middlePickupModel;
    private String bridgePickupBrand;
    private String bridgePickupModel;
    private String country;
    private int condition;

    public Guitar()
    {

    }

    public int getGuitarID() {
        return guitarID;
    }

    public void setGuitarID(int guitarID) {
        this.guitarID = guitarID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNeckWood() {
        return neckWood;
    }

    public void setNeckWood(String neckWood) {
        this.neckWood = neckWood;
    }

    public String getBodyWood() {
        return bodyWood;
    }

    public void setBodyWood(String bodyWood) {
        this.bodyWood = bodyWood;
    }

    public String getTopWood() {
        return topWood;
    }

    public void setTopWood(String topWood) {
        this.topWood = topWood;
    }

    public String getFingerBoardWood() {
        return fingerBoardWood;
    }

    public void setFingerBoardWood(String fingerBoardWood) {
        this.fingerBoardWood = fingerBoardWood;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getNeckPickupBrand() {
        return neckPickupBrand;
    }

    public void setNeckPickupBrand(String neckPickupBrand) {
        this.neckPickupBrand = neckPickupBrand;
    }

    public String getNeckPickupModel() {
        return neckPickupModel;
    }

    public void setNeckPickupModel(String neckPickupModel) {
        this.neckPickupModel = neckPickupModel;
    }

    public String getMiddlePickupBrand() {
        return middlePickupBrand;
    }

    public void setMiddlePickupBrand(String middlePickupBrand) {
        this.middlePickupBrand = middlePickupBrand;
    }

    public String getMiddlePickupModel() {
        return middlePickupModel;
    }

    public void setMiddlePickupModel(String middlePickupModel) {
        this.middlePickupModel = middlePickupModel;
    }

    public String getBridgePickupBrand() {
        return bridgePickupBrand;
    }

    public void setBridgePickupBrand(String bridePickupBrand) {
        this.bridgePickupBrand = bridePickupBrand;
    }

    public String getBridgePickupModel() {
        return bridgePickupModel;
    }

    public void setBridgePickupModel(String bridgePickupModel) {
        this.bridgePickupModel = bridgePickupModel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String toString() {
        return "Brand: " + brand + "\nModel: " + model + "\nNeck Wood: " + neckWood + "\nBody Wood: " + bodyWood +
                "\nTop Wood: " + topWood + "\nFinger Board Wood: " + fingerBoardWood + "\nColor: " + color + "\nNeck: " + neck +
                "\nNeck Pickup Brand " + neckPickupBrand + "\nMiddle Pickup Brand: " + middlePickupBrand + "\nMiddle Pickup Model: " + middlePickupModel +
                "\nBridge Pickup Brand" + bridgePickupBrand + "\nBridge Pickup Model: " + bridgePickupModel + "\nCountry: " + country + "\nCondition: " + condition + "\n";
    }

}
