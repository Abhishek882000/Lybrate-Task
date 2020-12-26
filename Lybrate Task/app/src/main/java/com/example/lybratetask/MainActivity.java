package com.example.lybratetask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ListItemAdapter mAdapter;
    private ArrayList<ListItem> mListItems;
    AlertDialog.Builder builder;
    RequestQueue requestQueue;

    String URL = "https://developers.zomato.com/api/v2.1/search?cuisines=Indian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method to bind the views with their respective ID's
        initViews();

        //To check whether we have active internet connection or not
        if (CheckNetwork.isInternetAvailable(MainActivity.this)) {
            //Method to fetch all the hotels
            searchRestaurants();
        } else {
            showMessage();
        }

    }


    public void initViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        builder = new AlertDialog.Builder(this);
    }


    public void searchRestaurants() {
        //code to show the loading icon when data is being retrieved
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("restaurants");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject res = jsonArray.getJSONObject(i);
                        JSONObject res1 = res.getJSONObject("restaurant");
                        JSONObject res2 = res1.getJSONObject("R");

                        //JsonObject to string type so we can show it on TextViews
                        String restaurantNames = String.valueOf(res1.get("name"));
                        String cuisinesNames = String.valueOf(res1.get("cuisines"));
                        String averageCost = String.valueOf(res1.get("average_cost_for_two"));
                        String timings = String.valueOf(res1.get("timings"));

                        //creating the instance of ListItem class to add the values here
                        ListItem listItem = new ListItem(
                                restaurantNames + "",
                                cuisinesNames + "",
                                averageCost + "Rs",
                                timings + ""
                        );
                        mListItems.add(listItem);
                    }
                    mAdapter = new ListItemAdapter(mListItems, MainActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myApp", "Error is" + error);
            }
        }) {

            //This code is to send headers with API
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "d4061a9dccbd1748ab1247595363067d");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    //Below method is used for searching items using cuisine Type
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    public void showMessage() {
        builder.setMessage("Do you want to close the application?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("No Internet Connection!");
        alert.show();
    }
}
