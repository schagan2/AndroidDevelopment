package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.reflect.KCallable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Assignment: HW05
 * File name: MainActivity.java[HW05.app]
 * Names: Aakansha Chauhan, Sindhura Chaganti
 */
public class MainActivity extends AppCompatActivity implements CityFragment.CityListener, CurrentWeatherFragment.CurrentWeatherListener {
    private static final String API_KEY = "d432604a745eede743de2816a35c9ded";
    OkHttpClient client = new OkHttpClient();
    Data.City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initially, set the page to view the list of cities
        setTitle(R.string.cities);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new CityFragment())
                .commit();
    }

    @Override
    public void setCurrentWeather(Data.City city) {
        //When clicking on city should open the detailed temperature of that city.
        setTitle(R.string.current_weather);
        this.city = city;
        getCurrentWeather(city);
    }

    //Method to get all the current temperature details of the city.
    private void getCurrentWeather(Data.City city) {
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather?").newBuilder()
                .addQueryParameter("q", city.getCity())
                .addQueryParameter("appid", API_KEY)
                .build();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        JSONObject main = json.getJSONObject("main");
                        Weather weather = new Weather();
                        weather.setTemp(main.getDouble("temp"));
                        weather.setTemp_min(main.getDouble("temp_min"));
                        weather.setTemp_max(main.getDouble("temp_max"));
                        weather.setHumidity(main.getInt("humidity"));
                        weather.setCode(json.getInt("cod"));

                        JSONObject wind = json.getJSONObject("wind");
                        weather.setWindSpeed(wind.getDouble("speed"));
                        weather.setWindDegree(wind.getDouble("deg"));

                        JSONArray weatherArray = json.getJSONArray("weather");
                        for(int i=0; i<weatherArray.length(); i++){
                            JSONObject weatherJson = weatherArray.getJSONObject(i);
                            weather.setId(weatherJson.getInt("id"));
                            weather.setDescription(weatherJson.getString("description"));
                            weather.setIcon(weatherJson.getString("icon"));
                        }

                        JSONObject clouds = json.getJSONObject("clouds");
                        weather.setCloudiness(clouds.getInt("all"));

                        runOnUiThread(() -> {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.containerView, CurrentWeatherFragment.newInstance(city, weather))
                                    .addToBackStack(null)
                                    .commit();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    //Method to get the forecast weathers for the particular city.
    @Override
    public void forecastWeather() {
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast?").newBuilder()
                .addQueryParameter("q", city.getCity())
                .addQueryParameter("appid", API_KEY)
                .build();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ArrayList<ForecastWeather> forecastList = new ArrayList<>();
                    try {
                        JSONObject forecastJSON = new JSONObject(response.body().string());
                        Log.d("TAG", "onResponse: "+ forecastJSON);

                        JSONArray weathers = forecastJSON.getJSONArray("list");

                        for(int i=0; i<weathers.length(); i++){
                            JSONObject weather = weathers.getJSONObject(i);
                            ForecastWeather forecast = new ForecastWeather();
                            forecast.setDate(weather.getString("dt_txt"));

                            JSONObject main = weather.getJSONObject("main");
                            forecast.setTemp(main.getDouble("temp"));
                            forecast.setTempMax(main.getDouble("temp_max"));
                            forecast.setTempMin(main.getDouble("temp_min"));
                            forecast.setHumidity(main.getInt("humidity"));

                            JSONArray weatherArray = weather.getJSONArray("weather");
                            for(int j=0; j<weatherArray.length(); j++){
                                JSONObject weatherJson = weatherArray.getJSONObject(j);
                                forecast.setId(weatherJson.getInt("id"));
                                forecast.setIcon(weatherJson.getString("icon"));
                                forecast.setDescription(weatherJson.getString("description"));
                            }

                            forecastList.add(forecast);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.containerView, ForecastWeatherFragment.newInstance(city, forecastList))
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}