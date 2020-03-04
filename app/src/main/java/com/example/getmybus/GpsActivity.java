package com.example.getmybus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmybus.Recyclerview_bus_details.BusAdapter;
import com.example.getmybus.Recyclerview_bus_details.Busdata;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GpsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    Marker sydeney_marker;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.non_sliding_view);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
        }

//        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Log.d("Location is", String.valueOf(locationGPS));
//        Toast.makeText(this,String.valueOf(locationGPS.getAltitude()+locationGPS.getLatitude()),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onLocationChanged (Location location){
        wayLatitude = location.getLatitude();
        wayLongitude = location.getLongitude();
        sydeney_marker.setPosition(new LatLng(wayLatitude,wayLongitude));
//        Toast.makeText(this, String.valueOf(location.getAltitude()+location.getAltitude()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){
//        Toast.makeText(this, "Status changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled (String provider){
        Toast.makeText(this, "Provider enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled (String provider){
        Toast.makeText(this, "Provider disabled", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(i);
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
//    private Location getLastBestLocation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//            }
//        }
//        Location locationGPS = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Location locationNet = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        long GPSLocationTime = 0;
//        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }
//
//        long NetLocationTime = 0;
//
//        if (null != locationNet) {
//            NetLocationTime = locationNet.getTime();
//        }
//
//        if ( 0 < GPSLocationTime - NetLocationTime ) {
//            return locationGPS;
//        }
//        else {
//            return locationNet;
//        }
//    }
@Override
public void onMapReady(GoogleMap googleMap) {
    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    mMap = googleMap;

    // Add a marker in Sydney, Australia, and move the camera.
    LatLng sydney = new LatLng(-34, 151);
    int height = 57;
    int width = 43;
    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_yellow);
    Bitmap b=bitmapdraw.getBitmap();
    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//        final Marker sydeney_marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    sydeney_marker = mMap.addMarker(new MarkerOptions().position(sydney).title("It's me").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                CameraPosition cameraPosition = mMap.getCameraPosition();
////                Log.d("The zoom level is", String.valueOf(cameraPosition.zoom));
//                if(cameraPosition.zoom > 10.0) {
////                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                    sydeney_marker.setVisible(true);
//
//                } else {
////                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                    sydeney_marker.setVisible(false);
//                }
//            }
//        });
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    LinearLayoutManager lm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
    //TODO recycler view calculation from the database
//      for(int i=0;i<=percent)
    ArrayList<Busdata> list= new ArrayList<>();
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
//    list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
    final BusAdapter adapter = new BusAdapter(this,list);
    recyclerView.setLayoutManager(lm);
    recyclerView.setAdapter(adapter);
}
}