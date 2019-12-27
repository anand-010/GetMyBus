package com.example.getmybus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class GpsActivity extends AppCompatActivity implements LocationListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3600, 0, (LocationListener) this);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("Location is", String.valueOf(locationGPS));
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
                        Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT);
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(this, "Permission denied ", Toast.LENGTH_SHORT);
                    }
                    return;
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }
        }

        @Override
        public void onLocationChanged (Location location){
            Toast.makeText(this, String.valueOf(location), Toast.LENGTH_SHORT);
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){
            Toast.makeText(this, "Status changed", Toast.LENGTH_SHORT);
        }

        @Override
        public void onProviderEnabled (String provider){
            Toast.makeText(this, "Provider enabled", Toast.LENGTH_SHORT);
        }

        @Override
        public void onProviderDisabled (String provider){
            Toast.makeText(this, "Provider disabled", Toast.LENGTH_SHORT);
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
}
