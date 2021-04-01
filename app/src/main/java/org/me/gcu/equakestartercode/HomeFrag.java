package org.me.gcu.equakestartercode;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


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
    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private View view;
    private ArrayList<Earthquake> equakes;
    private Button loadMap;
    private Button loadFilter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        // Set up the raw links to the graphical components
        view = layoutInflater.inflate(R.layout.home_frag_layout, viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.quakes);
        LinearLayoutManager LLM = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LLM);
        startProgress();

        loadMap = (Button) view.findViewById(R.id.loadMapView);
        loadMap.setOnClickListener(this::loadMapView);

        loadFilter = (Button) view.findViewById(R.id.loadFilter);
        loadFilter.setOnClickListener(this::loadFilterView);

        progressBar = (ProgressBar) view.findViewById(R.id.xmlProgressBar);

        return view;
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new HomeFrag.Task(urlSource)).start();

    }
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            getActivity().runOnUiThread(new Runnable()
            {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void run() {
                    parseXml(result);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void parseXml(String result) {
            try {
                //setting up the xml parsing
                XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                xmlFactory.setNamespaceAware(true);
                XmlPullParser xmlParse = xmlFactory.newPullParser();

                xmlParse.setInput(new StringReader(result));
                int event = xmlParse.getEventType();

                ArrayList<Earthquake> list = new ArrayList<>();
                Earthquake earthquake = new Earthquake();

                while (event != XmlPullParser.END_DOCUMENT) {
                    if (event == XmlPullParser.START_DOCUMENT) {

                    } else if (event == XmlPullParser.START_TAG) {
                        if (xmlParse.getName().equals("item")) {
                            earthquake = new Earthquake();
                        }
                        if (xmlParse.getName().equals("title")) {
                            earthquake.setQuakeTitle(xmlParse.nextText());
                        } else if (xmlParse.getName().equals("description")) {
                            String description = xmlParse.nextText();
                            earthquake.setQuakeDescription(description);
                        } else if (xmlParse.getName().equals("link")) {
                            earthquake.setQuakeLink(xmlParse.nextText());
                        } else if (xmlParse.getName().equals("pubDate")) {
                            String xmlText = xmlParse.nextText();
                            //finding the substring of the date found in the pubDate tag
                            String extractString = xmlText.substring(xmlText.indexOf(",") + 2, xmlText.length() - 9);
                            //adding split items to any array by identifying spaces in the string
                            List<String> cut = new ArrayList<>(Arrays.asList(extractString.split(" ")));
                            //adding formatting hyphens to match formatter pattern
                            String dateString = cut.get(0) + "-" + cut.get(1) + "-" + cut.get(2);
                            //setting pattern for date formatter
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                            //parsing and setting date
                            LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
                            //setting quake date to formatted date
                            earthquake.setQuakeDate(localDate);
                        } else if (xmlParse.getName().equals("category")) {
                            earthquake.setQuakeCategory(xmlParse.nextText());
                        } else if (xmlParse.getName().equals("lat")) {
                            earthquake.setQuakeLat(Double.parseDouble(xmlParse.nextText()));
                        } else if (xmlParse.getName().equals("long")) {
                            earthquake.setQuakeLon(Double.parseDouble(xmlParse.nextText()));
                        }
                    } else if (event == XmlPullParser.END_TAG) {
                        if (xmlParse.getName().equals("item")) {
                            list.add(earthquake);
                        }
                    }
                    event = xmlParse.next();
                }
                equakes = list;
                addQuakesToList(list);
            } catch (XmlPullParserException | IOException parserException) {
                System.out.println(parserException);
            }
        }

        //adding earthquakes to an arraylist
        public void addQuakesToList (ArrayList <Earthquake> equakes) {
            EQuakeAdapter adapter = new EQuakeAdapter(view.getContext(), equakes);
            recyclerView.setAdapter(adapter);
        }
    }

    public void loadMapView(View view){
        Bundle b = new Bundle();
        b.putSerializable("ARRAYLIST", (Serializable) equakes);

        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, QuakeMapView.class, b)
                .commit();
    }

    public void loadFilterView(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARRAYLIST", (Serializable) equakes);
        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, FilteredQuakeView.class, bundle)
                .commit();
    }
    }

