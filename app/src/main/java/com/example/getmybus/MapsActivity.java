package com.example.getmybus;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmybus.Recyclerview_bus_details.BusAdapter;
import com.example.getmybus.Recyclerview_bus_details.Busdata;
import com.example.getmybus.Recyclerview_bus_details.RecyclerviewAdapterBus;
import com.example.getmybus.data.ListData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.psoffritti.slidingpanel.PanelState;
import com.psoffritti.slidingpanel.SlidingPanel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    Marker sydeney_marker;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    SlidingPanel slidingPanel;
    ImageView slider;
    PolylineOptions polylineOptions;
//    TextView alt,lon;
    Double alt,lon;
    Double lat;
    Double lng;
    Boolean pannel_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pannel_state = false;
        setContentView(R.layout.map_layout);
        setupGps();
        slider = findViewById(R.id.drag_view);
        slidingPanel = findViewById(R.id.sliding_panel);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.non_sliding_view);
        slidingPanel.addSlideListener(new SlidingPanel.OnSlideListener() {
            @Override
            public void onSlide(@NotNull SlidingPanel slidingPanel, @NotNull PanelState panelState, float v) {
                if (panelState == PanelState.COLLAPSED){
                    pannel_state = false;
                }
                else {
                    pannel_state = true;
                }
            }
        });
        mapFragment.getMapAsync(this);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pannel_state){
                    slidingPanel.slideTo(PanelState.EXPANDED);
                }
                else{
                    slidingPanel.slideTo(PanelState.COLLAPSED);
                }

            }
        });
        String url = "https://getmybus.herokuapp.com?waypoints=";
        OkHttpClient okHttpClient = new OkHttpClient();
        List<String> coordinates= ListData.getMylist();
//        List<String> coordinates= new ArrayList<>();
//        coordinates.add("13.43041");coordinates.add("52.51085");coordinates.add("13.42467");coordinates.add("52.50881");coordinates.add("13.42024");coordinates.add("52.50698");
        boolean first_tiem = true;
        for (int i=0;i<coordinates.size();i=i+2){
            if (first_tiem){
                url = url.concat(coordinates.get(i)+","+coordinates.get(i+1));
                first_tiem=false;
            }
            else {
                url = url.concat(";"+coordinates.get(i)+","+coordinates.get(i+1));
            }
        }
        Toast.makeText(this,url,Toast.LENGTH_LONG).show();
        Log.d("tag", "onCreate: "+url);
        Request request = new Request.Builder()
                .url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(MapsActivity.this,"oh bad",Toast.LENGTH_SHORT).show();
                Log.d("df", "onFailure: "+request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Toast.makeText(MapsActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                String out = response.body().string();
                Log.d("df", "onResponse: "+out);
                try {
                    JSONObject object = new JSONObject(out);
                    JSONArray jsonArray = object.getJSONArray("coordinates");
                    polylineOptions = new PolylineOptions();

                    for (int i = 0;i<jsonArray.length();i++){
                        lat = (Double) jsonArray.getJSONArray(i).get(0);
                        lng = (Double) jsonArray.getJSONArray(i).get(1);
                        polylineOptions.add(new LatLng(lng,lat));
                    }
                    Log.d("odada", "onResponse: "+jsonArray.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addPolyline(polylineOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lng,lat)));
            }
        });
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
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala"));
        final BusAdapter adapter = new BusAdapter(this,list);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }

//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
////        Drawable background = ContextCompat.getDrawable(context, R.drawable.pin_point);
////        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth() + 0, vectorDrawable.getIntrinsicHeight() + 0);
//        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        background.draw(canvas);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
    public void setupGps(){
//        alt = findViewById(R.id.laltitude);
//        lon = findViewById(R.id.longitude);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(2000);
        locationRequest.setInterval(3000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        sydeney_marker.setPosition(new LatLng(wayLatitude,wayLongitude));
//                        alt.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                    }
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,getMainLooper());
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
            // already permission granted
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            sydeney_marker.setPosition(new LatLng(wayLatitude,wayLongitude));
//                            lon.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
