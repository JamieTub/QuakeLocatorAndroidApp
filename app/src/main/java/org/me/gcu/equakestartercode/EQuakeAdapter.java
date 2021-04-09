package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class EQuakeAdapter extends RecyclerView.Adapter<EQuakeAdapter.ViewHolder> {
    Context context;
    ArrayList<Earthquake> earthquakes;

    //default constructor
    public EQuakeAdapter(Context context, ArrayList<Earthquake> earthquakes){
        this.context = context;
        this.earthquakes = earthquakes;
    }

    @NonNull
    @Override
    public EQuakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int view){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list, parent, false);
        EQuakeAdapter.ViewHolder vHolder = new EQuakeAdapter.ViewHolder(v);
        return vHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView quakeTitle;
        TextView quakeMag;
        TextView quakeDate;

        public ViewHolder(View v){
            super(v);
            quakeTitle = (TextView) v.findViewById(R.id.quakeTitle);
            quakeMag = (TextView) v.findViewById(R.id.quakeMagnitude);
            quakeDate = (TextView) v.findViewById(R.id.quakeDate);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int adapterIndex = getAdapterPosition();

                    Bundle b = new Bundle();
                    b.putSerializable("ARRAYLIST", (Serializable) earthquakes);
                    b.putInt("index", adapterIndex);

                    ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.frameLayout, QuakeMapView.class, b)
                            .commit();
                }
            });
            }
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EQuakeAdapter.ViewHolder viewHolder, int pos){
        viewHolder.quakeTitle.setText((String) earthquakes.get(pos).getQuakeLocation());
        viewHolder.quakeDate.setText("Date: " + earthquakes.get(pos).getStringQuakeDate());

        //set colours for mag in here
        if(earthquakes.get(pos).getQuakeMagnitude() > 2) {
            viewHolder.quakeMag.setTextColor(Color.RED);
        } else if(earthquakes.get(pos).getQuakeMagnitude() > 1) {
            viewHolder.quakeMag.setTextColor(Color.GREEN);
        } else {
            viewHolder.quakeMag.setTextColor(Color.BLUE);
        }
        viewHolder.quakeMag.setText("Magnitude: " + earthquakes.get(pos).getQuakeMagnitude());
    }

    @Override
    public int getItemCount(){
        //functions returns the number of items that exist in the adapter
        return earthquakes.size();
    }
}
