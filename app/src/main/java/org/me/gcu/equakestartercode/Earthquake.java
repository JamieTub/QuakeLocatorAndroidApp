package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Earthquake implements Serializable {

    //building earthquake props
    private String quakeTitle;
    private String quakeDescription;
    private String quakeLink;
    private LocalDate quakeDate;
    private String quakeCategory;
    private double quakeLat;
    private double quakeLon;

    //empty earthquake constructor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Earthquake(){
        this.quakeTitle = "";
        this.quakeDescription = "";
        this.quakeLink = "";
        this.quakeDate = LocalDate.now();
        this.quakeCategory = "";
        this.quakeLat = 0.00;
        this.quakeLon = 0.00;
    }

    //getters and setters for the earthquake properties
    public String getQuakeLocation(){
        List<String> splitList = new ArrayList<String>(Arrays.asList(quakeDescription.split(";")));
        String location = splitList.get(1).substring(1);
        if(location.equals("Location: ")){
            return "Location: Not Specified";
        }
        return location;
    }

    public double getQuakeMagnitude(){
        List<String> splitList = new ArrayList<String>(Arrays.asList(quakeDescription.split(";")));
        try{
            return Double.parseDouble(splitList.get(4).substring(splitList.get(4).length() - 3));
        }catch(Exception e){
            return 0;
        }
    }

    public double getQuakeDepth(){
        List<String> splitList = new ArrayList<String>(Arrays.asList(quakeDescription.split(";")));
        try{
            return Double.parseDouble(splitList.get(3).substring(8, splitList.get(3).length() - 4));
        }catch (Exception e){
            return 0;
        }
    }

    public String getQuakeTitle(){
        return quakeTitle;
    }

    public void setQuakeTitle(String quakeTitle){
        this.quakeTitle = quakeTitle;
    }

    public String getQuakeDescription() {
        return quakeDescription;
    }

    public void setQuakeDescription(String quakeDescription){
        this.quakeDescription = quakeDescription;
    }

    public String getQuakeLink(){
        return quakeLink;
    }

    public void setQuakeLink(String quakeLink){
        this.quakeLink = quakeLink;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStringQuakeDate(){
        return quakeDate.getDayOfMonth() + " " + quakeDate.getMonth() + " " + quakeDate.getYear();
    }

    public LocalDate getQuakeDate(){
        return quakeDate;
    }

    public void setQuakeDate(LocalDate quakeDate){
        this.quakeDate = quakeDate;
    }

    public String getQuakeCategory() {
        return quakeCategory;
    }

    public void setQuakeCategory(String quakeCategory) {
        this.quakeCategory = quakeCategory;
    }

    public double getQuakeLat() {
        return quakeLat;
    }

    public void setQuakeLat(double quakeLat) {
        this.quakeLat = quakeLat;
    }

    public double getQuakeLon() {
        return quakeLon;
    }

    public void setQuakeLon(double quakeLon) {
        this.quakeLon = quakeLon;
    }
}
