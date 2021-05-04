package com.example.hw05;

import java.io.Serializable;

public class Weather implements Serializable {
//"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}],"base":"stations","main":{"temp":290.15,"feels_like":290.3,"temp_min":289.26,"temp_max":290.93,"pressure":1018,"humidity":82},"visibility":10000,"wind":{"speed":1.54,"deg":200},"clouds":{"all":75},"dt":1616634802,"sys":{"type":1,"id":3648,"country":"US","sunrise":1616584855,"sunset":1616629082},"timezone":-14400,"id":4460243,"name":"Charlotte","cod":200}

    int id;
    double temp;
    double temp_max;
    double temp_min;
    String description;
    int humidity;
    double windSpeed;
    double windDegree;
    int cloudiness;
    String icon;
    int code;

    public Weather() {
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

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = convertTemp(temp_max);
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = convertTemp(temp_min);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = convertSpeed(windSpeed);
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
    }

    private double convertTemp(double changeTemp){
        int scale = (int) Math.pow(10, 1);
        double value = (changeTemp - 273.15) * 9/5 + 32;
        return (double) Math.round(value * scale) / scale;
    }

    private double convertSpeed(double changeSpeed){
        return changeSpeed * 2.237;
    }
}
