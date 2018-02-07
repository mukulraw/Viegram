package com.relinns.viegram.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private static ArrayList<String> resultList;
    private String place_id;
    private SharedPreferences preferences;
    private String location = "";
    private String address = "";

    public PlaceAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        @SuppressWarnings("UnnecessaryLocalVariable") Filter filter = new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    location = constraint.toString();
                    location = location.replace(" ", "%20");
                    resultList = autocomplete(location);
                    // Assign the rideListItems to the FilterResults
                    filterResults.values = resultList;
                    Log.d("filterResults", "" + filterResults.toString());
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        Log.d("jsonresults", jsonResults + "");
        try {
            String mainurl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + input + "&components=country:" + preferences.getString("country_iso", "") + "&language=pt_BR&key=AIzaSyB9BbU3f60TlfRxbv-b-PzhgbnDaIXnbPA";
            Log.d("url", ">>>>" + mainurl);
            URL url = new URL(mainurl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
                Log.d("jsonresults", jsonResults + "");
            }
        } catch (MalformedURLException e) {
            Log.d("TaxiAll", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.d("TaxiAll", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                JSONObject obj = predsJsonArray.getJSONObject(i);
                address = obj.getString("description");
                //  place_id: "ChIJYTN9T-plUjoRM9RjaAunYW4",
                place_id = obj.getString("place_id");
                Log.d("address", address);
                resultList.add(obj.getString("description"));
            }
        } catch (JSONException e) {
            Log.d("sf", "Cannot process JSON results", e);
        }
        return resultList;
    }
}
