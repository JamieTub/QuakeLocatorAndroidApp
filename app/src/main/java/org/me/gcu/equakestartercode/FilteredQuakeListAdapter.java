package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FilteredQuakeListAdapter extends RecyclerView.Adapter<FilteredQuakeListAdapter.ViewHolder> {

    ArrayList<Earthquake> earthquakes;
    Context context;
    String notable[] = {
            "Most Northerly Earthquake",
            "Most Southerly Earthquake",
            "Most Westerly Earthquake",
            "Most Easterly Earthquake",
            "Largest Magnitude Earthquake",
            "Deepest Earthquake",
            "Shallowest Earthquake"
    };

    //default constructor
    public FilteredQuakeListAdapter(Context context, ArrayList<Earthquake> earthquakes){
        this.context = context;
        this.earthquakes =  earthquakes;
    }

    @NonNull
    @Override
    public FilteredQuakeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.filtered_quake_item, parentViewGroup, false);

        FilteredQuakeListAdapter.ViewHolder viewHolder = new FilteredQuakeListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FilteredQuakeListAdapter.ViewHolder holder, int pos) {

        holder.qNotable.setText(notable[pos]);
        holder.qTitle.setText((String) earthquakes.get(pos).getQuakeLocation());
        holder.qDate.setText((String) earthquakes.get(pos).getStringQuakeDate());

        //setting earthquakes magnitude to match
        if(earthquakes.get(pos).getQuakeMagnitude() > 2){
            holder.qMag.setTextColor(Color.RED);
        }
        else if(earthquakes.get(pos).getQuakeMagnitude() > 1){
            holder.qMag.setTextColor(Color.GREEN);
        }
        else {
            holder.qMag.setTextColor(Color.BLUE);
        }
        holder.qMag.setText("Magnitude: " + earthquakes.get(pos).getQuakeMagnitude());
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    //init the views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView qNotable;
        TextView qTitle;
        TextView qDate;
        TextView qMag;

        public ViewHolder(View v){
            super(v);
            qNotable = (TextView) v.findViewById(R.id.notableQuake);
            qTitle = (TextView) v.findViewById(R.id.filteredQuakeTitle);
            qDate = (TextView) v.findViewById(R.id.filteredQuakeDate);
            qMag = (TextView) v.findViewById(R.id.filteredQuakeMag);
        }
    }


}
