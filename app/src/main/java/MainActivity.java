package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Main Activity class for MP7.
 */
public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    /**
     * Debugging tag.
     */
    private static final String TAG = "MP7:Main";
    /**
     * Target URL for API call.
     */
    private static final String TARGET_URL = "https://api.coinmarketcap.com/v1/ticker/";
    /**
     * Returned data from API.
     */
    private JsonArray data = null;
    /**
     * Array of all available coins.
     */
    private ArrayList<String> coins = new ArrayList<>();

    /**
     * On create method.
     * @param savedInstanceState daved instance state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAPICall(requestQueue);
        Log.d(TAG, "Starting API Call ");
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, coins);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    /**
     * Starts the API Call to get information.
     * @param requestQueue request queue for project
     */
    private void startAPICall(final RequestQueue requestQueue) {
        StringRequest arrayRequest = new StringRequest(
                Request.Method.GET,
                TARGET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Got response:");
                        Log.d(TAG, response);
                        finishAPICall(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d(TAG, "Error response");
                Log.w(TAG, error.toString());
            }
        });
        requestQueue.add(arrayRequest);
    }

    /**
     * Finishes the API Call process.
     * @param response response from API
     */
    private void finishAPICall(final String response) {
        Log.d(TAG, "Finishing API Call");
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(response).getAsJsonArray();
        data = jsonArray;
        for (JsonElement j : jsonArray) {
            coins.add(j.getAsJsonObject().get("name").getAsString());
        }
        Log.d(TAG, coins.toString());
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {

    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {

    }
}
