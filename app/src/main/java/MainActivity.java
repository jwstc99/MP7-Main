package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

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
     * Request Queue for API calls.
     */
    private RequestQueue requestQueue;
    /**
     * Returned data from API.
     */
    private JsonArray data = null;
    /**
     * Array of all available coins.
     */
    private ArrayList<String> coins = new ArrayList<>();
    /**
     *Coin currently selected on the spinner
     */
    private String selectedCoin;

    /**
     * On create method.
     * @param savedInstanceState daved instance state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAPICall(requestQueue);
        Log.d(TAG, "Starting API Call ");
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coins);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    /**
     * Starts the API Call to get information.
     * @param requestQueue1 request queue for project
     */
    private void startAPICall(final RequestQueue requestQueue1) {
        Log.d(TAG, "Started API Call");
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
        requestQueue1.add(arrayRequest);
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
        //NEEDED: Fill TextView boxes here.
        JsonObject coinObject;
        for (JsonElement j : jsonArray) {
            if (j.getAsJsonObject().get("name").getAsString().equals(selectedCoin)) {
                Log.d(TAG, "Found coin in list");
                coinObject = j.getAsJsonObject();
                break;
            }
        }
        TextView coinName = findViewById(R.id.coinName);
        coinName.setText("Help, i need help.");
        coinName.setVisibility(View.VISIBLE);
        Log.d(TAG, "Set label");
    }

    /**
     * Sets the coin selected in the spinner to local variable.
     * @param parent idk
     * @param view idk
     * @param i pos of item
     * @param l idk
     */
    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int i, final long l) {
        selectedCoin = coins.get(i);
        Log.d(TAG, "Selected coin: " + selectedCoin);
        startAPICall(requestQueue);
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {

    }
}
