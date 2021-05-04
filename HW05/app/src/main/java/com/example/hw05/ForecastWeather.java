package com.example.hw05;

public class ForecastWeather {
    int id;
    double temp;
    double tempMax;
    double tempMin;
    int humidity;
    String description;
    String date;
    String icon;

    public ForecastWeather() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = convertTemp(temp);
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = convertTemp(tempMax);
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = convertTemp(tempMin);
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private double convertTemp(double changeTemp){
        int scale = (int) Math.pow(10, 1);
        double value = (changeTemp - 273.15) * 9/5 + 32;
        return (double) Math.round(value * scale) / scale;
    }
}
