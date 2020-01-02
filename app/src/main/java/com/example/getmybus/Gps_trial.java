package com.example.getmybus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.getmybus.Recyclerview_bus_details.AutoCompleteAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Gps_trial extends AppCompatActivity{
    TextView t1, t2;
    LocationListener locationListener;
    LocationManager locationManager;
    PlacesClient placesClient;
    AutoCompleteAdapter adapter;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String TAG = Gps_trial.class.getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_test);
        String apiKey = "AIzaSyBPXDQ1v9trRcL7df0KRor2yEUbbDoroKs";
        if(apiKey.isEmpty()){
//            responseView.setText(getString(R.string.error));
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
//        initAutoCompleteTextView();
//        t1 = findViewById(R.id.longitude);
//        t2 = findViewById(R.id.laltitude);
//
//        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//        {
//            showGPSDisabledAlertToUser();
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 77);
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }
//        }
//        Criteria crit = new Criteria();
//        crit.setAccuracy(Criteria.ACCURACY_FINE);
//        String provider = locationManager.getBestProvider(crit, true);
//        Location loc = locationManager.getLastKnownLocation(provider);
//        t1.setText(String.valueOf(loc.getLatitude()));
//        t1.setText(String.valueOf(loc.getAltitude()));
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 2, (LocationListener) this);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 77) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Toast.makeText(this,"the location changed",Toast.LENGTH_SHORT).show();
//        t1.setText(String.valueOf(location.getAltitude()));
//        t2.setText(String.valueOf(location.getLatitude()));
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//        private void showGPSDisabledAlertToUser() {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
//                    .setCancelable(false)
//                    .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(callGPSSettingIntent);
//                        }
//                    });
//            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert = alertDialogBuilder.create();
//            alert.show();
        }
//    private void initAutoCompleteTextView() {
//
//        autoCompleteTextView = findViewById(R.id.auto);
//        autoCompleteTextView.setThreshold(1);
//        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
//        adapter = new AutoCompleteAdapter(this, placesClient);
//        autoCompleteTextView.setAdapter(adapter);
//    }
//    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            try {
//                final AutocompletePrediction item = adapter.getItem(i);
//                String placeID = null;
//                if (item != null) {
//                    placeID = item.getPlaceId();
//                }
//
////                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
////                Use only those fields which are required.
//
//                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
//                        , Place.Field.LAT_LNG);
//
//                FetchPlaceRequest request = null;
//                if (placeID != null) {
//                    request = FetchPlaceRequest.builder(placeID, placeFields)
//                            .build();
//                }
//
//                if (request != null) {
//                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void onSuccess(FetchPlaceResponse task) {
////                            responseView.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            e.printStackTrace();
////                            responseView.setText(e.getMessage());
//                        }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
}
