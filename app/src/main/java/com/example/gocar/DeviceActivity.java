package com.example.gocar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceActivity extends AppCompatActivity {
    public SQLiteHandler db;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnMap;
    private Button btnSubmit;
    private EditText inputReview;
    public static TextView checkReview;
    TextView ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        db = new SQLiteHandler(getApplicationContext());
        btnSubmit = (Button) findViewById(R.id.submitReview);
        btnMap = (Button) findViewById(R.id.map);
        checkReview = (TextView) findViewById(R.id.checkReview);
        checkReview.setMovementMethod(new ScrollingMovementMethod());
        inputReview=(EditText) findViewById(R.id.textReview);
        String id = getIntent().getStringExtra("ID");
        String name = getIntent().getStringExtra("NAME");
        String year = getIntent().getStringExtra("YEAR");
        String text = "CAR NAME : " + name
                + "\n" +
                "CAR MODEL : " + year;

        ALL= findViewById(R.id.Data);
        ALL.setText(text);
        getReviews();



        btnMap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview(inputReview.getText().toString().trim());
            }
        });



    }
    public void getReviews() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_REVIEW_FETCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray reviews = new JSONArray(response);

                    String selected_car_id = String.valueOf(MainActivity.vehiclesListSorted.get(MainActivity.index).getId());

                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject vehicleObject = reviews.getJSONObject(i);
                        String UserReview = vehicleObject.getString("UserReview");
                        String Carid = vehicleObject.getString("Carid");
                        int userid = vehicleObject.getInt("userid");
                        if(selected_car_id.equalsIgnoreCase("" + Carid)) {
                            checkReview.append("  User " + userid + ": " + UserReview + '\n');
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeviceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        ;
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void addReview(final String review) {
        String tag_string_req = "req_review";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REVIEW_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String UniqueId = jObj.getString("UniqueId");
                        JSONObject review = jObj.getJSONObject("reviews");
                        String Carid = review.getString("Carid");
                        String userid = review.getString("userid");
                        String UserReview = review.getString("UserReview");

                        // Inserting row in reviews table
                        db.addReview(UniqueId , userid , Carid , UserReview);

                        checkReview.append("  User " + userid + ": " + UserReview + '\n');

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error in posting the review: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected java.util.Map<String, String> getParams() {

                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();

                String userid = user.get("id");
                String Carid = String.valueOf(MainActivity.vehiclesListSorted.get(MainActivity.index).getId());

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("Carid", Carid);
                params.put("UserReview", review);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq , tag_string_req);
    }
}
