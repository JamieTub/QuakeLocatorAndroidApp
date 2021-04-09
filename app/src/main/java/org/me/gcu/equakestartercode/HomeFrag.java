package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFrag extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<Earthquake> equakes;
    private Button loadMap;
    private Button loadFilter;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        // Set up the raw links to the graphical components
        view = layoutInflater.inflate(R.layout.home_frag_layout, viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.quakes);
        LinearLayoutManager LLM = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LLM);

        //get earthquake list from the main
        Bundle bundle1 = requireArguments();
        this.equakes =  (ArrayList<Earthquake>) bundle1.getSerializable("equakes");
        addQuakesToList(equakes);

        loadMap = (Button) view.findViewById(R.id.loadMapView);
        loadMap.setOnClickListener(this::loadMapView);

        loadFilter = (Button) view.findViewById(R.id.loadFilter);
        loadFilter.setOnClickListener(this::loadFilterView);
//        statusUpdater = (TextView) view.findViewById(R.id.statusUpdate);
//        progressBar = (ProgressBar) view.findViewById(R.id.xmlProgressBar);

        return view;
    }

    public void loadMapView(View view) {
        Bundle b = new Bundle();
        b.putSerializable("ARRAYLIST", (Serializable) equakes);

        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, QuakeMapView.class, b)
                .commit();
    }

    public void loadFilterView(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARRAYLIST", (Serializable) equakes);
        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, FilteredQuakeView.class, bundle)
                .commit();
    }

    //adding earthquakes to an arraylist
    public void addQuakesToList(ArrayList<Earthquake> equakes) {
        EQuakeAdapter adapter = new EQuakeAdapter(view.getContext(), equakes);
        recyclerView.setAdapter(adapter);
    }


}

