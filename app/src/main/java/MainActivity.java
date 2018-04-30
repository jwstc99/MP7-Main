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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

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
        // start building the textview
        atextviewresult = findViewById(R.id.coinName);
        btextviewresult = findViewById(R.id.price);

        ctextviewresult = findViewById(R.id.text2);
        dtextviewresult = findViewById(R.id.text3);
        etextviewresult = findViewById(R.id.text4);
        ftextviewresult = findViewById(R.id.text5);
        gtextviewresult = findViewById(R.id.text6);
        htextviewresult = findViewById(R.id.text7);
       myQueue = Volley.newRequestQueue(this);

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
     * @param requestQueue1 request queue for project
     */
    /** I am not entirely sure what I am doing here, I just basically fowllow this video: https://www.youtube.com/watch?v=y2xtLqP8dSQ, very useful tho. I
     * redo some part of it because I don't think your stringrequest would work. This part is just get the data from json and put them into texts.
     *
     * it can either be run here or the one below.
     * @param requestQueue1 request queue for Json
     */
    private void startAPICall(final RequestQueue requestQueue1) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, TARGET_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(response).getAsJsonArray();
                try {

                    String priceBtc = response.getString("price_btc");
                    String name = response.getString("name");
                    String priceUsd = response.getString("price_usd");
                    String change24h = response.getString("percent_change_24h");
                    String marketCap = response.getString("market_cap_usd");
                    String change7d = response.getString("percent_change_7d");
                    String change1h = response.getString("percent_change_1h");
                    String totalSupply = response.getString("total_supply");

                    atextviewresult.append(name);
                    btextviewresult.append(priceUsd);
                    ctextviewresult.append(change24h);
                    dtextviewresult.append(priceBtc);
                    etextviewresult.append(marketCap);
                    ftextviewresult.append(change1h);
                    gtextviewresult.append(change7d);
                    htextviewresult.append(totalSupply);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
    });
        myQueue.add(request);

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
        //NEEDED: Fill TextView boxes here.
        /** something is wrong here I dont know how to fix this.
         *
         * */
try {

            JsonObject jsonObject = data.getAsJsonObject();
    String priceBtc = jsonObject.get("price_btc").getAsString();
    String name = jsonObject.get("name").getAsString();
    String priceUsd = jsonObject.get("price_usd").getAsString();
    String change24h = jsonObject.get("percent_change_24h").getAsString();
    String marketCap = jsonObject.get("market_cap_usd").getAsString();
    String change7d = jsonObject.get("percent_change_7d").getAsString();
    String change1h = jsonObject.get("percent_change_1h").getAsString();
    String totalSupply = jsonObject.get("total_supply").getAsString();

    atextviewresult.setText(name);
    btextviewresult.setText(priceUsd);
    ctextviewresult.setText(change24h);
    dtextviewresult.setText(priceBtc);
    etextviewresult.setText(marketCap);
    ftextviewresult.setText(change1h);
    gtextviewresult.setText(change7d);
    htextviewresult.setText(totalSupply);
    Log.d(TAG, response.toString());
} catch (Exception ignored) {
}
/** I think this part does not work, though keep it just in case
for (JsonElement i: jsonArray) {

    String priceBtc = i.getAsJsonObject().get("price_btc").getAsString();
    String name = i.getAsJsonObject().get("name").getAsString();
    String priceUsd = i.getAsJsonObject().get("price_usd").getAsString();
    String change24h = i.getAsJsonObject().get("percent_change_24h").getAsString();
    String marketCap = i.getAsJsonObject().get("market_cap_usd").getAsString();
    String change7d = i.getAsJsonObject().get("percent_change_7d").getAsString();
    String change1h = i.getAsJsonObject().get("percent_change_1h").getAsString();
    String totalSupply = i.getAsJsonObject().get("total_supply").getAsString();
    atextviewresult.append(name);
    btextviewresult.append(priceUsd);
    ctextviewresult.append(change24h);
    dtextviewresult.append(priceBtc);
    etextviewresult.append(marketCap);
    ftextviewresult.append(change1h);
    gtextviewresult.append(change7d);
    htextviewresult.append(totalSupply);
}
 */
        JsonObject coinObject;
        for (JsonElement j : jsonArray) {
            if (j.getAsJsonObject().get("name").getAsString().equals(selectedCoin)) {
                coinObject = j.getAsJsonObject();
                break;
            }
        }

        Log.d(TAG, coins.toString());
    }

// declaring all the variable that I am using to represent the texts

    private TextView atextviewresult, btextviewresult, ctextviewresult, dtextviewresult,
    etextviewresult, ftextviewresult,gtextviewresult, htextviewresult;
    private RequestQueue myQueue;

    /**
     * Sets the coin selected in the spinner to local variable.
     * @param parent idk
     * @param view idk
     * @param i pos of item
     * @param l idk
     */
    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int i, final long l) {
        selectedCoin = (String) parent.getItemAtPosition(i);
        startAPICall(requestQueue);
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {

    }
}
