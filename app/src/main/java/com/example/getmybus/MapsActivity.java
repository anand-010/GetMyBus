package com.example.getmybus;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmybus.Recyclerview_bus_details.BusAdapter;
import com.example.getmybus.Recyclerview_bus_details.Busdata;
import com.example.getmybus.Recyclerview_bus_details.RecyclerviewAdapterBus;
import com.example.getmybus.data.ListData;
import com.example.getmybus.helper.DatabaseHelperRoute;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    Marker marker;
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
    ArrayList<Busdata> list= new ArrayList<>();
    BusAdapter adapter;
//    TextView alt,lon;
    Double alt,lon;
    Double lat;
    Double lng;
    Boolean pannel_state;
    private GoogleMap gooleMap;

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
//todo adding the ranking by making http request to the heroku server
        String URL = "https://getmybus.herokuapp.com/?src="+getIntent().getStringExtra("query");
        Log.d("is ready", "onCreate: "+URL);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(MapsActivity.this,"oh bad",Toast.LENGTH_SHORT).show();
                Log.d("df", "onFailure: "+request.toString());
            }
            @Override
            public void onResponse(Response response) throws IOException {
//                Toast.makeText(MapsActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                String out = response.body().string().trim();
                String[] arrOfStr = out.split(",", 8);
//                todo need to be access all the documents in this array
//                todo then display it in the recycler view below
                MapsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MapsActivity.this,URL,Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onResponse: "+out);
                        Log.d("TAG", "onResponse: "+URL);
                        Toast.makeText(MapsActivity.this,out,Toast.LENGTH_SHORT).show();
                        Toast.makeText(MapsActivity.this,"Length : "+String.valueOf(arrOfStr.length),Toast.LENGTH_SHORT).show();
                        FirebaseFirestore d = FirebaseFirestore.getInstance();
                        CollectionReference cr = d.collection("ride");
                        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                Toast.makeText(MapsActivity.this,String.valueOf(queryDocumentSnapshots.size()),Toast.LENGTH_SHORT).show();
                                if (queryDocumentSnapshots.size()>0)
                                    list.clear();
                                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()){
                                    String id = ds.getId().toString();
//                                    Toast.makeText(MapsActivity.this,"id"+id,Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MapsActivity.this,"ad"+out,Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MapsActivity.this,String.valueOf(arrOfStr[0].length()),Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MapsActivity.this,String.valueOf(id.length()),Toast.LENGTH_SHORT).show();
                                    for (int i=0;i<arrOfStr.length;i++){
                                        if (id.equals(arrOfStr[i])) {
//                                            Toast.makeText(MapsActivity.this,"equal",Toast.LENGTH_SHORT).show();
//                                            todo add the stop details to the recycler list
                                            list.add(new Busdata("Test name", "720", "test time", "test loc", id));
                                            adapter.notifyDataSetChanged();
                                            GeoPoint gp = ds.getGeoPoint("position");
                                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_yellow);
                                            Bitmap b=bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 43, 57, false);
                                            if (marker == null){
                                                marker = gooleMap.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(),gp.getLongitude())).title("It's me").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                            }
                                            else {
                                                marker.setPosition(new LatLng(gp.getLatitude(), gp.getLongitude()));
                                            }
//                                            Toast.makeText(MapsActivity.this,gp.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                });

//                String[] possible_bus_details = out.split(",");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gooleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<GeoPoint> routepoints = new ArrayList<>();
                            List<GeoPoint> stopspoints = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());
                                routepoints = (List<GeoPoint>) document.getData().get("route");
                                stopspoints = (List<GeoPoint>) document.getData().get("stops");

                            }
                            polylineOptions = new PolylineOptions();
                            Toast.makeText(MapsActivity.this,"val"+String.valueOf(routepoints.size()),Toast.LENGTH_SHORT).show();
                            for (GeoPoint gp: routepoints){
                                polylineOptions.add(new LatLng(gp.getLatitude(),gp.getLongitude()));
                            }
                            for (GeoPoint gp: stopspoints){
                                mMap.addMarker(new MarkerOptions().position(new LatLng(gp.getLatitude(),gp.getLongitude())));
                            }
                            Polyline options = mMap.addPolyline(polylineOptions);
                                    options.setWidth(6);
                                    options.setColor(0xff34cceb);
                                    options.setStartCap(new RoundCap());
                                    options.setEndCap(new RoundCap());
                            CameraUpdate center=
                                    CameraUpdateFactory.newLatLng(new LatLng(routepoints.get(0).getLatitude(),routepoints.get(0).getLongitude()));
                            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
                        } else {
                            Log.w("tag", "Error getting documents.", task.getException());
                        }
                    }
                });
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        int height = 57;
        int width = 50;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.person_loc);
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
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        list.add(new Busdata("ALP -> MUM","Kl0478","2.6 min","pallikkavala","test"));
        adapter = new BusAdapter(this,list);
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
