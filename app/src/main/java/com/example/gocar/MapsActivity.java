package com.example.gocar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        final Double lat = getIntent().getDoubleExtra("LATITUDE",0);
        final Double log = getIntent().getDoubleExtra("LONGITUDE",0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng=new LatLng(latitude,longitude);
                    LatLng latLngCar=new LatLng(lat,log);
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try{
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                        String str =addressList.get(0).getLocality()+",";
                        str +=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("I am here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.addMarker(new MarkerOptions().position(latLngCar).title("Car is here!"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCar,10.2f));
                    }
                    catch(IOException e){
                        e.printStackTrace();

                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        else if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng=new LatLng(latitude,longitude);
                    LatLng latLngCar=new LatLng(lat,log);
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try{
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                        String str =addressList.get(0).getLocality()+",";
                        str +=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("I am here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.addMarker(new MarkerOptions().position(latLngCar).title("Car is here!"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCar,10.2f));
                    }
                    catch(IOException e){
                        e.printStackTrace();

                    }
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;


    }

}
