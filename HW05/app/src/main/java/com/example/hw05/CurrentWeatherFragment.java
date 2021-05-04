package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import okhttp3.HttpUrl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentWeatherFragment extends Fragment {

    private static final String ARG_WEATHER = "weather";
    private static final String ARG_CITY = "city";

    CurrentWeatherListener clistener;
    private Weather weather;
    private Data.City city;
    TextView cityView;
    TextView tempView;
    TextView tempMaxView;
    TextView tempMinView;
    TextView description;
    TextView humidity;
    TextView windSpeed;
    TextView windDegree;
    TextView cloudiness;
    ImageView icon;
    Button forecast;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param city
     * @param weather Parameter 1.
     * @return A new instance of fragment CurrentWeatherFragment.
     */
    public static CurrentWeatherFragment newInstance(Data.City city, Weather weather) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        args.putSerializable(ARG_WEATHER, weather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = (Weather) getArguments().getSerializable(ARG_WEATHER);
            city = (Data.City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        cityView = view.findViewById(R.id.cityName);
        tempView = view.findViewById(R.id.tempValue);
        tempMaxView = view.findViewById(R.id.tempMaxValue);
        tempMinView = view.findViewById(R.id.tempMinValue);
        description = view.findViewById(R.id.description);
        windSpeed   = view.findViewById(R.id.windSpeed);
        windDegree = view.findViewById(R.id.windDegree);
        cloudiness = view.findViewById(R.id.cloudiness);
        humidity = view.findViewById(R.id.humidity);
        forecast = view.findViewById(R.id.checkForecast);
        icon = view.findViewById(R.id.imageView);

        cityView.setText(city.getCity()+", "+city.getCountry());
        tempView.setText(weather.getTemp()+" F");
        tempMaxView.setText(weather.getTemp_max()+" F");
        tempMinView.setText(weather.getTemp_min()+" F");
        description.setText(weather.getDescription());
        windSpeed.setText(weather.getWindSpeed()+" miles/hr");
        windDegree.setText(weather.getWindDegree()+" degrees");
        humidity.setText(String.valueOf(weather.getHumidity())+" %");
        cloudiness.setText(weather.getCloudiness()+" %");

        String url = "https://openweathermap.org/img/wn/"+weather.getIcon()+".png";
        Picasso.get().load(url).resize(70, 70).into(icon);

        forecast.setOnClickListener((v) -> {
                clistener.forecastWeather();
            });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CurrentWeatherListener){
            clistener = (CurrentWeatherListener) context;
        }else{
            throw new RuntimeException(getContext()+"must implement the listener.");
        }
    }

    public interface CurrentWeatherListener{
        void forecastWeather();
    }
}