package com.example.getmybus;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmybus.Recyclerview_bus_details.BusAdapter;
import com.example.getmybus.Recyclerview_bus_details.Busdata;
import com.example.getmybus.Recyclerview_bus_details.RecyclerviewAdapterBus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.non_sliding_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        int height = 57;
        int width = 43;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_yellow);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        final Marker sydeney_marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
//                Log.d("The zoom level is", String.valueOf(cameraPosition.zoom));
                if(cameraPosition.zoom > 10.0) {
//                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    sydeney_marker.setVisible(true);

                } else {
//                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    sydeney_marker.setVisible(false);
                }
            }
        });
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
        final BusAdapter adapter = new BusAdapter(list);
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
}
