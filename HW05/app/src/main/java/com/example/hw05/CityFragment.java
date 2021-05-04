package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CityFragment extends Fragment {
    ListView listView;
    ArrayList<Data.City> cities;
    ArrayAdapter<Data.City> adapter;
    CityListener cityListener;

    public CityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        listView = view.findViewById(R.id.listView);
        cities = Data.cities;
        adapter = new CityAdapter(getContext(), R.layout.row_item_city, cities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data.City city = cities.get(position);
                cityListener.setCurrentWeather(city);
            }
        });
        return view;
    }

    class CityAdapter extends ArrayAdapter<Data.City>{
        TextView cityText;

        public CityAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Data.City> objects) {
            super(context, resource, objects);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_city, parent, false);
            }
            Data.City city = getItem(position);
            cityText = convertView.findViewById(R.id.cityAndCountry);
            cityText.setText(city.getCity()+", "+city.getCountry());

            return convertView;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CityListener){
            cityListener = (CityListener) context;
        }else{
            throw new RuntimeException(getContext() + "must implement listener.");
        }
    }

    public interface CityListener{
        void setCurrentWeather(Data.City city);
    }
}