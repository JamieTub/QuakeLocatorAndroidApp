package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class QuakeMapView extends Fragment implements OnMapReadyCallback {
    private ArrayList<Earthquake> earthquakes;
    private View v;
    private GoogleMap qMap;
    private Button backToQuakeList;
    private Earthquake earthquake;
    private int quakeArrayIndex = -1;
    private TextView quakeLoc;
    private TextView quakeMag;
    private TextView quakeLat;
    private TextView quakeLon;
    private TextView quakeDepth;
    private TextView quakeDate;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle){
        v = layoutInflater.inflate(R.layout.frag_quake_map, viewGroup, false);
        backToQuakeList = (Button) v.findViewById(R.id.backToQuakeList);
        backToQuakeList.setOnClickListener(this::OnClick);

        Bundle b = requireArguments();
        this.quakeArrayIndex = (int)b.getInt("index", -1);
        this.earthquakes =  (ArrayList<Earthquake>) b.getSerializable("ARRAYLIST");
        return v;
    }

    private void OnClick(View view) {
        ((MainActivity)getActivity()).startProgress();
//        Bundle bundle = new Bundle();
//        getParentFragmentManager().beginTransaction()
//            .setReorderingAllowed(true)
//            .replace(R.id.frameLayout, HomeFrag.class, bundle)
//            .commit();
   }

    @Override
    public void onViewCreated(@NonNull View v, Bundle bundle){
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);

        //find all the text views
        quakeLoc = v.findViewById(R.id.infoQuakeLocation);
        quakeDate = v.findViewById(R.id.infoQuakeDate);
        quakeMag = v.findViewById(R.id.infoQuakeMagnitude);
        quakeLat = v.findViewById(R.id.infoQuakeLat);
        quakeLon = v.findViewById(R.id.infoQuakeLon);
        quakeDepth = v.findViewById(R.id.infoQuakeDepth);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap quakeMap){
        qMap = quakeMap;
        LatLng start = new LatLng(55, 0);

        if(quakeArrayIndex == -1){
            //setting markers for earthquakes

            qMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Earthquake e = (Earthquake) marker.getTag();

                    quakeLoc.setText(e.getQuakeLocation());
                    quakeDate.setText("Date: " + e.getStringQuakeDate());
                    quakeMag.setText("Magnitude: " + e.getQuakeMagnitude());
                    quakeLat.setText("Lat: " + e.getQuakeLat());
                    quakeLon.setText("Lon: " + e.getQuakeLon());
                    quakeDepth.setText("Depth: " + e.getQuakeDepth() + "km");
                    return false;
                }
            });

            for(int i= 0; i < earthquakes.size(); i++){
                Earthquake e = earthquakes.get(i);

                quakeDate.setText("James Lawn S1918451");
                quakeMag.setText("Select an earthquake to show more information.");

                //setting colours for the magnitude of each earthquake
                float colour;
                if(e.getQuakeMagnitude() > 2) {
                    colour = BitmapDescriptorFactory.HUE_RED;
                } else if(e.getQuakeMagnitude() > 1) {
                    colour = BitmapDescriptorFactory.HUE_ORANGE;
                } else {
                    colour = BitmapDescriptorFactory.HUE_YELLOW;
                }

                //setting up the map markers for the list view
                LatLng latLon = new LatLng(e.getQuakeLat(), e.getQuakeLon());
                Marker marker =  qMap.addMarker(new MarkerOptions()
                        .position(latLon)
                        .title(e.getQuakeLocation())
                        .snippet("Magnitude: " + e.getQuakeMagnitude())
                        .icon(BitmapDescriptorFactory.defaultMarker(colour)));
                        marker.setTag(e);
            }

        }
        else{
            Earthquake e = earthquakes.get(quakeArrayIndex);

            //setting colours for map markers
            float colour;
            if(e.getQuakeMagnitude() > 2) {
                colour = BitmapDescriptorFactory.HUE_RED;
            } else if(e.getQuakeMagnitude() > 1) {
                colour = BitmapDescriptorFactory.HUE_ORANGE;
            } else {
                colour = BitmapDescriptorFactory.HUE_YELLOW;
            }

            //setting the text views for the earthquake information
            quakeLoc.setText(e.getQuakeLocation());
            quakeDate.setText("Date: " + e.getStringQuakeDate());
            quakeMag.setText("Magnitude: " + e.getQuakeMagnitude());
            quakeLat.setText("Lat: " + e.getQuakeLat());
            quakeLon.setText("Lon: " + e.getQuakeLon());
            quakeDepth.setText("Depth: " + e.getQuakeDepth() + "km");

            start = new LatLng(e.getQuakeLat(), e.getQuakeLon());
            Marker marker =  qMap.addMarker(new MarkerOptions()
                    .position(start)
                    .title(e.getQuakeLocation())
                    .icon(BitmapDescriptorFactory.defaultMarker(colour))
                    .snippet("Magnitude: " + e.getQuakeMagnitude()));

            marker.showInfoWindow();
        }
        qMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 5));
    }
}
