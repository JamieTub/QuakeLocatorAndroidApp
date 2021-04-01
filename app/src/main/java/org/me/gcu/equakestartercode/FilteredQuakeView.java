package org.me.gcu.equakestartercode;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

//inspiration from https://gist.github.com/ishitcno1/9545088

public class FilteredQuakeView extends Fragment implements DatePickerDialog.OnDateSetListener {

    private View view;
    private ArrayList<Earthquake> earthquakes;
    private QuakeDateFilterAdapter quakeDateFilterAdapter;
    private RecyclerView recyclerView;
    private TextView filterStartText;
    private TextView filterEndText;
    private LocalDate filterStart;
    private LocalDate filterEnd;
    private Button backToList;
    private Button filterQuakes;
    private ArrayList<Earthquake> filtered;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle){

        view = layoutInflater.inflate(R.layout.activity_quake_filter, viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.filteredQuakes);
        LinearLayoutManager LLM = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LLM);

        backToList = (Button) view.findViewById(R.id.backButton);
        backToList.setOnClickListener(this::backToQuakeList);

        filterQuakes = (Button) view.findViewById(R.id.filterButton);
        filterQuakes.setOnClickListener(this::filterQuakes);

        Bundle b = requireArguments();
        this.earthquakes = (ArrayList<Earthquake>) b.getSerializable("ARRAYLIST");

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void filterQuakes(View v) {
        if(filterStart == null || filterEnd == null || filterStart.isAfter(filterEnd)){
            Toast.makeText(view.getContext(), "Date range is not valid.", Toast.LENGTH_SHORT).show();
        }else{
            ArrayList<Earthquake> filteredQuakes = new ArrayList<>();
            for(int i = 0; i < earthquakes.size(); i++){
                LocalDate quakeDate = earthquakes.get(i).getQuakeDate();
                if(quakeDate.isBefore(filterEnd.plusDays(1)) && quakeDate.isAfter(filterStart.minusDays(1))){
                    filteredQuakes.add(earthquakes.get(i));
                }
            }

            filtered = filteredQuakes;
            ArrayList<Earthquake> filteredEarthquakes = setFilteredValues();
            FilteredQuakeListAdapter filteredQuakeListAdapter = new FilteredQuakeListAdapter(view.getContext(), filteredEarthquakes);
            recyclerView.setAdapter(filteredQuakeListAdapter);

        }
    }

    private void backToQuakeList(View view) {
        Bundle bundle = new Bundle();
        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, HomeFrag.class, bundle)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        //new calendar
        Calendar c = Calendar.getInstance();

        //set calendar vars
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);

        //formatting the date to a string format
        String filterDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        if(quakeDateFilterAdapter.getFlag() == QuakeDateFilterAdapter.FLAG_START_DATE){
            filterStartText.setText(filterDate);
            filterStart = LocalDate.parse(filterDate, dateTimeFormatter);
        }else if(quakeDateFilterAdapter.getFlag() == QuakeDateFilterAdapter.FLAG_END_DATE){
            filterEndText.setText(filterDate);
            filterEnd = LocalDate.parse(filterDate, dateTimeFormatter);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle b){

        quakeDateFilterAdapter = new QuakeDateFilterAdapter();
        quakeDateFilterAdapter.setTargetFragment(this, 5678);

        this.filterStartText = (TextView) view.findViewById(R.id.filterStart);
        this.filterEndText = (TextView) view.findViewById(R.id.filterEnd);

        filterStartText.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view){
                quakeDateFilterAdapter.setFlag(QuakeDateFilterAdapter.FLAG_START_DATE);
                quakeDateFilterAdapter.show(getParentFragmentManager(), "PICK DATE");
            }
        });
        filterEndText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                quakeDateFilterAdapter.setFlag(QuakeDateFilterAdapter.FLAG_END_DATE);
                quakeDateFilterAdapter.show(getParentFragmentManager(), "PICK DATE");
            }
        });
    }

    private ArrayList<Earthquake> setFilteredValues(){
        ArrayList<Earthquake> filteredQuakes = new ArrayList<>();
        filteredQuakes.add(getMostNorth());
        filteredQuakes.add(getMostSouth());
        filteredQuakes.add(getMostWest());
        filteredQuakes.add(getMostEast());
        filteredQuakes.add(getLargestMag());
        filteredQuakes.add(getDeepest());
        filteredQuakes.add(getShallow());

        return filteredQuakes;
    }

    private Earthquake getMostNorth(){
        Earthquake north = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if(filtered.get(i).getQuakeLat() > north.getQuakeLat()){
                north = filtered.get(i);
            }
        }
        return north;
    }

    private Earthquake getMostSouth(){
        Earthquake south = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if(filtered.get(i).getQuakeLat() < south.getQuakeLat()){
                south = filtered.get(i);
            }
        }
        return south;
    }

    private Earthquake getMostWest(){
        Earthquake west = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if(filtered.get(i).getQuakeLon() < west.getQuakeLon()){
                west = filtered.get(i);
            }
        }
        return west;
    }

    private Earthquake getMostEast(){
        Earthquake east = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if(filtered.get(i).getQuakeLon() > east.getQuakeLon()){
                east = filtered.get(i);
            }
        }
        return east;
    }

    private Earthquake getLargestMag(){
        Earthquake largestMag = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if(filtered.get(i).getQuakeMagnitude() > largestMag.getQuakeMagnitude()){
                largestMag = filtered.get(i);
            }
        }
        return largestMag;
    }

    private Earthquake getDeepest(){
        Earthquake deepest = filtered.get(0);
        for (int i = 0; i < filtered.size(); i++){
            if (filtered.get(i).getQuakeDepth() > deepest.getQuakeDepth()){
                deepest = filtered.get(i);
            }
        }
        return deepest;
    }

    private Earthquake getShallow(){
        Earthquake shallow = filtered.get(0);
        for(int i = 0; i < filtered.size(); i++){
            if (filtered.get(i).getQuakeDepth() < shallow.getQuakeDepth()){
                shallow = filtered.get(i);
            }
        }
        return shallow;
    }
}
