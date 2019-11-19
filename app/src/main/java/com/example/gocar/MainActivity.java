package com.example.gocar;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends Activity implements VehiclesAdapter.OnVehicleListener {

    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;
    private ArrayList<Vehicles> vehiclesList;
    public static ArrayList<Vehicles> vehiclesListSorted;
    private ArrayList<Vehicles> LatLo;
    private ArrayList<Double> DistanceArray;
    private ArrayList<Double> TempArray;
    private ArrayList<Integer> TempPos;
    private RecyclerView recyclerView;
    private VehiclesAdapter MonVehicleListener;
    private LocationManager locationManager;
    private Location LocationGps;
    private Location LocationNetwork;
    private Location LocationPassive;
    public static int index;

    private Double UserLat,UserLonG;
    private static  final int REQUEST_LOCATION=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.rv);
        vehiclesList = new ArrayList<>();
        vehiclesListSorted = new ArrayList<>();
        DistanceArray = new ArrayList<>();
        TempArray = new ArrayList<>();
        TempPos = new ArrayList<>();
        LatLo = new ArrayList<>();
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            OnGPS();
        }
        else
        {
            getUserLocation();
        }

        MonVehicleListener = new VehiclesAdapter(MainActivity.this, vehiclesListSorted,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SortVehicles();
        loadVehicles();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void SortVehicles() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_VEHICLES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);
                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {
                        //getting product object from json array
                        JSONObject cars = array.getJSONObject(i);
                        //adding the product to product list
                        LatLo.add(new Vehicles(
                                cars.getInt("id"),
                                cars.getString("name"),
                                cars.getString("year"),
                                cars.getDouble("latitude"),
                                cars.getDouble("longitude"),
                                cars.getString("image_path"),
                                cars.getInt("fuel_level")));
                        float[] results = new float[1];
                        LocationGps.distanceBetween(LocationGps.getLatitude(), LocationGps.getLongitude(), cars.getDouble("latitude"), cars.getDouble("longitude"), results);
                        double x =(double) results[0];
/*                        TempId.add(cars.getInt("id"));*/
                        DistanceArray.add(x);
                        TempArray.add(x);
                    }
                    Collections.sort(DistanceArray);

                    for (int j = 0; j < array.length(); j++) {
                        TempPos.add(DistanceArray.indexOf(TempArray.get(j)));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void getUserLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                UserLat=LocationGps.getLatitude();
                UserLonG=LocationGps.getLongitude();
            }
            else if (LocationNetwork !=null)
            {
                UserLat=LocationNetwork.getLatitude();
                UserLonG=LocationNetwork.getLongitude();
            }
            else if (LocationPassive !=null)
            {
                UserLat=LocationPassive.getLatitude();
                UserLonG=LocationPassive.getLongitude();
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadVehicles() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_VEHICLES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject cars = array.getJSONObject(i);

                        //adding the product to product list
                        vehiclesList.add(new Vehicles(
                                cars.getInt("id"),
                                cars.getString("name"),
                                cars.getString("year"),
                                cars.getDouble("latitude"),
                                cars.getDouble("longitude"),
                                cars.getString("image_path"),
                                cars.getInt("fuel_level")));
                    }
                    for (int k = 0; k < array.length(); k++) {
                        vehiclesListSorted.add(vehiclesList.get(TempPos.get(k)));
                    }
                    recyclerView.setAdapter(MonVehicleListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void OnVehicleClick(int position) {
        index=position;
        String id = String.valueOf(vehiclesListSorted.get(position).getId());
        String name = vehiclesListSorted.get(position).getName();
        String year = vehiclesListSorted.get(position).getYear();
        String latitude = String.valueOf(vehiclesListSorted.get(position).getLatitude());
        String longitude = String.valueOf(vehiclesListSorted.get(position).getLongitude());
            Double latit=Double.parseDouble(latitude);
            Double longit=Double.parseDouble(longitude);
        String image = vehiclesListSorted.get(position).getImage_path();
        String fuel = String.valueOf(vehiclesListSorted.get(position).getFuel_level());
        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        intent.putExtra("ID",id);
        intent.putExtra("NAME",name);
        intent.putExtra("YEAR",year);
        intent.putExtra("LATITUDE",latit);
        intent.putExtra("LONGITUDE",longit);
        intent.putExtra("IMAGE",image);
        intent.putExtra("FUEL",fuel);
        startActivity(intent);
        finish();
    }

}