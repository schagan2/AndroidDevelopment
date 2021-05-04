package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastWeatherFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CITY = "city";
    private static final String ARG_FORECAST = "forecast";

    private Data.City city;
    private ArrayList<ForecastWeather> forecastList;
    ForecastWeathersAdapter adapter;
    TextView cityName;
    ListView weathers;

    public ForecastWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param city Parameter 1.
     * @return A new instance of fragment ForecastWeatherFragment.
     */
    public static ForecastWeatherFragment newInstance(Data.City city, ArrayList<ForecastWeather> forecastArray) {
        ForecastWeatherFragment fragment = new ForecastWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        args.putSerializable(ARG_FORECAST, forecastArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (Data.City) getArguments().getSerializable(ARG_CITY);
            forecastList = (ArrayList<ForecastWeather>) getArguments().getSerializable(ARG_FORECAST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast_weather, container, false);
        cityName = view.findViewById(R.id.forecastCity);
        weathers = view.findViewById(R.id.forecastWeathers);
        cityName.setText(city.getCity()+", "+city.getCountry());

        adapter = new ForecastWeathersAdapter(getContext(), R.layout.row_forecast_weather, forecastList);
        weathers.setAdapter(adapter);

        return view;
    }

    class ForecastWeathersAdapter extends ArrayAdapter{

        public ForecastWeathersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ForecastWeather> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_forecast_weather, parent, false);
            }

            ForecastWeather forecast = (ForecastWeather) getItem(position);
            TextView dateAndTime = convertView.findViewById(R.id.dateAndTime);
            TextView forecastTemp = convertView.findViewById(R.id.forecastTemp);
            TextView forecastMax = convertView.findViewById(R.id.forecastMax);
            TextView forecastMin = convertView.findViewById(R.id.forecastMin);
            TextView forecastHumidity = convertView.findViewById(R.id.forecastHumidity);
            TextView forecastDesc = convertView.findViewById(R.id.forecastDesc);
            ImageView icon = convertView.findViewById(R.id.weatherIcon);

            dateAndTime.setText(forecast.getDate());
            forecastTemp.setText(forecast.getTemp() +" F");
            forecastMax.setText(forecast.getTempMax()+" F");
            forecastMin.setText(forecast.getTempMin()+" F");
            forecastHumidity.setText(forecast.getHumidity()+"%");
            forecastDesc.setText(forecast.getDescription());

            String url = "https://openweathermap.org/img/wn/"+forecast.getIcon()+".png";
            Picasso.get().load(url).resize(70, 70).into(icon);
            return convertView;
        }
    }
}