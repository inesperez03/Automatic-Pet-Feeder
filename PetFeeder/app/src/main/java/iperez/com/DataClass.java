package iperez.com;

public class DataClass {

    private String dataName;
    private String dataAnimal;
    private String dataWeight;
    private String dataYears;
    private String dataMonths;
    private String dataImage;
    private String key;

    //Esta será la llave correspondiente del animal en firebase

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataAnimal() {
        return dataAnimal;
    }

    public String getDataWeight() {
        return dataWeight;
    }

    public String getDataYears() {
        return dataYears;
    }

    public String getDataMonths() {
        return dataMonths;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getDataFood() {
        double weight = Double.parseDouble(dataWeight);
        double foodAmount = weight * 20; //2% peso corporal -> gramos
        return String.format(String.valueOf(foodAmount));
    }

    public String getDataWater() {
        double weight = Double.parseDouble(dataWeight);
        double waterAmount = weight * 60; //kg * 60ml = ml agua diarios
        return String.format(String.valueOf(waterAmount));
    }

    public DataClass(String dataName, String dataAnimal, String dataWeight, String dataYears, String dataMonths, String dataImage) {
        this.dataName = dataName;
        this.dataAnimal = dataAnimal;
        this.dataWeight = dataWeight;
        this.dataYears = dataYears;
        this.dataMonths = dataMonths;
        this.dataImage = dataImage;
    }
    public DataClass(){

    }
}
