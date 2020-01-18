package com.example.getmybus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmybus.data.ListData;
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

public class Front_activity extends AppCompatActivity {
    TextView src,dest;
    Point mydestination;
    Point mysource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g");
        src = findViewById(R.id.src_act_main);
        dest = findViewById(R.id.dest_act_main);
        src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g")
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(Front_activity.this);
                startActivityForResult(intent, 10);
            }
        });
        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : "pk.eyJ1IjoiYW5hbmQ5Mjg4IiwiYSI6ImNrNHk2dHJpdDA3dHEzZm82Y2hnY252cjEifQ.W-3fm0taJg_noVA_zzJO7g")
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(Front_activity.this);
                startActivityForResult(intent, 11);
            }
        });
        LinearLayout layout = findViewById(R.id.activity_last_item);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Front_activity.this,MapsActivity.class));
            }
        });
    }    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 10) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            mysource = Point.fromLngLat(((Point) selectedCarmenFeature.geometry()).longitude(),
                    ((Point) selectedCarmenFeature.geometry()).latitude());

            Toast.makeText(this,mysource.toString(),Toast.LENGTH_SHORT).show();
            Log.d("get", "onResponse: "+mysource.toString());
            List<String> url = new ArrayList<>();
            url.add(String.valueOf(mysource.latitude()));
            url.add(String.valueOf(mysource.longitude()));
            url.add(String.valueOf(mydestination.latitude()));
            url.add(String.valueOf(mydestination.longitude()));
            Intent i = new Intent(Front_activity.this,MapsActivity.class);
            ListData.setMylist(url);
            startActivity(i);

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


        }
    }

}
