package com.example.getmybus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmybus.Utils.GpsUtils;
import com.example.getmybus.data.ListData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Front_activity extends AppCompatActivity implements LocationListener{
    TextView src, dest;
    Point mydestination;
    Point mysource;
    ImageView gps_btn,continue_btn;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps_btn = findViewById(R.id.gps_btn);
        continue_btn = findViewById(R.id.continue_front);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g");
        src = findViewById(R.id.src_act_main);
        dest = findViewById(R.id.dest_act_main);
        src.setOnClickListener(v -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g")
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(Front_activity.this);
            startActivityForResult(intent, 10);
        });
        dest.setOnClickListener(v -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g")
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(Front_activity.this);
            startActivityForResult(intent, 11);
        });
        continue_btn.setOnClickListener(v -> {
            if (mysource == null && mydestination == null){
                Toast.makeText(this,"source and destination is not selected",Toast.LENGTH_SHORT).show();
            }
            else if (mysource == null){
                Toast.makeText(this,"source is not selected",Toast.LENGTH_SHORT).show();
            }
            else if (mydestination == null){
                Toast.makeText(this,"destination is not selected",Toast.LENGTH_SHORT).show();
            }
            else {
                List<String> url = new ArrayList<>();
                url.add(String.valueOf(mysource.latitude()));
                url.add(String.valueOf(mysource.longitude()));
                url.add(String.valueOf(mydestination.latitude()));
                url.add(String.valueOf(mydestination.longitude()));
                Intent i = new Intent(Front_activity.this,MapsActivity.class);
                String query = String.valueOf(mysource.latitude()+","+mysource.longitude()+"&dest="+mydestination.latitude()+","+mydestination.longitude());
                i.putExtra("query",query);
                ListData.setMylist(url);
                startActivity(i);
            }
        });
        LinearLayout layout = findViewById(R.id.activity_last_item);
        layout.setOnClickListener(v -> startActivity(new Intent(Front_activity.this, MapsActivity.class)));
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_btn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Toast.makeText(Front_activity.this, "pk", Toast.LENGTH_SHORT).show();
                                new GpsUtils(Front_activity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                                    @Override
                                    public void gpsStatus(boolean isGPSEnable) {
                                        // turn on GPS
                                        Toast.makeText(Front_activity.this, "gps enabled", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        });
            } else {
                new GpsUtils(Front_activity.this).turnGPSOn(isGPSEnable -> {
                    // turn on GPS
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
                    }
                    Toast.makeText(Front_activity.this, "gps enabled", Toast.LENGTH_SHORT).show();
                });
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
                Toast.makeText(this,"location manager called",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 10) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            mysource = Point.fromLngLat(((Point) selectedCarmenFeature.geometry()).longitude(),
                    ((Point) selectedCarmenFeature.geometry()).latitude());
            src.setText(selectedCarmenFeature.placeName());
            MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                    .accessToken("pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g")
                    .query(mysource)
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();
            reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    Toast.makeText(Front_activity.this,response.body().toJson().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable t) {

                }
            });
            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

        }
        if (resultCode == Activity.RESULT_OK && requestCode == 11) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            mydestination = Point.fromLngLat(((Point) selectedCarmenFeature.geometry()).longitude(),
                    ((Point) selectedCarmenFeature.geometry()).latitude());
            dest.setText(selectedCarmenFeature.placeName());


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,"location changed"+location.getLatitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
