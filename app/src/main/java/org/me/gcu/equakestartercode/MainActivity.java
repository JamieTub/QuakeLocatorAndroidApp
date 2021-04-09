package org.me.gcu.equakestartercode;

//James Lawn S1918451

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide(); //<< this
        }

        setContentView(R.layout.activity_main);
        startProgress();
    }

    private void loadHomeFrag() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("equakes", (Serializable) earthquakes);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout, HomeFrag.class, bundle)
                .commit();
    }

    public void startProgress() {
        String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
        // Run network access on a separate thread;
        new getDataAsync().execute(urlSource);

    }

    private class getDataAsync extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... url) {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            Log.e("MyTag", "in run");

                try {
                    Log.e("MyTag", "in try");
                    aurl = new URL(url[0]);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    Log.e("MyTag", "after ready");

                    while ((inputLine = in.readLine()) != null) {
                        result = result + inputLine;
                        Log.e("MyTag", inputLine);

                    }
                    in.close();
                } catch (IOException ae) {
                    Log.e("MyTag", "ioexception in run");
                }

            System.out.println(result);
            return result;
        }


        protected void onPostExecute(String result) {
            new xmlParseAsyncTask().execute(result);
        }
    }

    private class xmlParseAsyncTask extends AsyncTask<String, Integer, ArrayList<Earthquake>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        protected ArrayList<Earthquake> doInBackground(String... args) {

            try {
                String result = args[0];

                //setting up the xml parsing
                XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                xmlFactory.setNamespaceAware(true);
                XmlPullParser xmlParse = xmlFactory.newPullParser();

                xmlParse.setInput(new StringReader(result));
                int event = xmlParse.getEventType();

                ArrayList<Earthquake> list = new ArrayList<>();
                Earthquake earthquake = new Earthquake();

                while (event != XmlPullParser.END_DOCUMENT) {
                    System.out.println("gets here");
                     if (event == XmlPullParser.START_TAG) {
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
                return list;
            } catch (XmlPullParserException | IOException parserException) {
                System.out.println(parserException);
                return null;
            }

        }

        protected void onPostExecute(ArrayList<Earthquake> list) {

            System.out.println(list);
            earthquakes = list;
            loadHomeFrag();
        }

    }


}