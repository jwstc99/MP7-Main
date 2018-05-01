package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
public class MainActivity extends AppCompatActivity {
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
     *Coin currently selected on the .
     */
    private String selectedCoin = "Bitcoin";

    /**
     * On create method.
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAPICall(requestQueue);
        Log.d(TAG, "Starting API Call ");
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Refresh button clicked");
                startAPICall(requestQueue);
            }
        });
    }
    /**
     * Starts the API Call to get information.
     * @param requestQueue1 request queue for project
     */
    private void startAPICall(final RequestQueue requestQueue1) {
        StringRequest arrayRequest = new StringRequest(
                Request.Method.GET,
                TARGET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        Log.d(TAG, "Got response.");
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
     *
     */
    private void finishAPICall(final String response) {
        Log.d(TAG, "Finishing API Call");
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(response).getAsJsonArray();
        data = jsonArray;
        for (JsonElement j : jsonArray) {
            coins.add(j.getAsJsonObject().get("name").getAsString());
        }
        Spinner spinner = findViewById(R.id.spinner);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item, coins);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                                       final int position, final long id) {
                selectedCoin = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Selected coin: " + selectedCoin);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        JsonObject coinObject = null;
        for (JsonElement j : jsonArray) {
            if (j.getAsJsonObject().get("name").getAsString().equals(selectedCoin)) {
                coinObject = j.getAsJsonObject();
                break;
            }
        }
        try {
            String priceBtc1 = coinObject.get("price_btc").getAsString();
            String name1 = coinObject.get("name").getAsString();
            String priceUsd1 = coinObject.get("price_usd").getAsString();
            String change24h1 = coinObject.get("percent_change_24h").getAsString();
            String marketCap1 = coinObject.get("market_cap_usd").getAsString();
            String change7d1 = coinObject.get("percent_change_7d").getAsString();
            String change1h1 = coinObject.get("percent_change_1h").getAsString();
            String totalSupply1 = coinObject.get("total_supply").getAsString();

            TextView coinName = findViewById(R.id.coinName);
            TextView price = findViewById(R.id.price);
            TextView change24h = findViewById(R.id.chnage24h);
            TextView priceBTC  = findViewById(R.id.priceBTC);
            TextView marketCap = findViewById(R.id.marketCap);
            TextView change7d = findViewById(R.id.change7d);
            TextView totalSupply = findViewById(R.id.totalSupply);
            TextView change1h = findViewById(R.id.change1h);

            coinName.setText(name1);
            price.setText(priceUsd1);
            change24h.setText(change24h1);
            priceBTC.setText(priceBtc1);
            marketCap.setText(marketCap1);
            change1h.setText(change1h1);
            change7d.setText(change7d1);
            totalSupply.setText(totalSupply1);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't get coin details: ");
            Log.d(TAG, e.toString());
        }
    }
}
