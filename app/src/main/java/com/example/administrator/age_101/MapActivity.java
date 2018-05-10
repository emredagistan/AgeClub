package com.example.administrator.age_101;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
//import android.graphics.Point;
//import android.location.Location;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
//import com.google.android.gms.maps.Projection;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int REQUEST_lOCATION = 90;

    static final LatLng AgeKonum = new LatLng(39.9046083, 32.8650663);
    static final LatLng EsatDortYol = new LatLng(39.9095337,32.8621499);
    static final LatLng CankayaHastanesi = new LatLng(39.904623,32.863391);
    static final LatLng YildizAspava = new LatLng(39.9052647,32.8655242);
    static final LatLng PorsukGiyim = new LatLng(39.9087027,32.8619281);
    static final LatLng kizilay = new LatLng(39.9198004,32.8545718);

    ArrayList<Discount> categoryA = new ArrayList<>();
    ArrayList<Discount> categoryB = new ArrayList<>();
    ArrayList<Discount> categoryC = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        categoryA = (ArrayList<Discount>) getIntent().getSerializableExtra("categoryA");
        categoryB = (ArrayList<Discount>) getIntent().getSerializableExtra("categoryB");
        categoryC = (ArrayList<Discount>) getIntent().getSerializableExtra("categoryC");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ZoomControls zoom = findViewById(R.id.zoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        final Button btn_MapType = findViewById(R.id.btn_Sat);
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_MapType.setText(R.string.map_mode_normal);
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText(R.string.map_mode_satalite);
                }

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap != null)
        {

            Spinner sp = findViewById(R.id.spinner);
            loadAllCategories(mMap);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {

                    switch (selected){
                        case 0:
                            mMap.clear();
                            loadAllCategories(mMap);
                        break;
                        case 1:
                            mMap.clear();
                            loadACategory(mMap);
                        break;
                        case 2:
                            mMap.clear();
                            loadBCategory(mMap);
                        break;
                        case 3:
                            mMap.clear();
                            loadCCategory(mMap);
                        break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            //LatLng myLoc = new LatLng(mMap.getMyLocation().getLongitude(), mMap.getMyLocation().getLongitude());
            // myLoc büyük ihtimalle geç geldiği için program crash veriyor
            //PolylineOptions options = new PolylineOptions().add(AgeKonum).add(EsatDortYol).width(0)
            //        .color(Color.BLUE).visible(true).geodesic(true);
            //mMap.addPolyline(options); // width 0 olduğu için çizgiler invizible

            //mMap.setTrafficEnabled(true);

            //mMap.setOnMyLocationChangeListener(new LocationFollow());
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kizilay,12));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ) {
            mMap.setMyLocationEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }

    }

    /*public class LocationFollow implements GoogleMap.OnMyLocationChangeListener
    {

        @Override
        public void onMyLocationChange(Location location)
        {
            LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
            Projection p = mMap.getProjection();
            Point point = p.toScreenLocation(loc);

            CircleOptions circle = new CircleOptions();
            circle.center(loc);
            circle.fillColor(Color.RED);
            circle.radius(10);
            circle.strokeColor(1);
            mMap.addCircle(circle);
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_lOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Kullanıcı konum iznini vermedi",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void loadACategory(GoogleMap m){        //Giyim
        for(int i = 0; i < categoryA.size(); i++){
            m.addMarker((new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_clothing_store))
                    .position(new LatLng(categoryA.get(i).getX(), categoryA.get(i).getY()))
                    .title(categoryA.get(i).getCampaignName())));
        }
    }

    private void loadBCategory(GoogleMap m){        //Yemek
        for(int i = 0; i < categoryB.size(); i++){
            m.addMarker((new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_night_club))
                    .position(new LatLng(categoryB.get(i).getX(), categoryB.get(i).getY()))
                    .title(categoryB.get(i).getCampaignName())));
        }
    }

    private void loadCCategory(GoogleMap m){     //Sağlık
        for(int i = 0; i < categoryC.size(); i++){
            m.addMarker((new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_local_hospital_black_24dp))
                    .position(new LatLng(categoryC.get(i).getX(), categoryC.get(i).getY()))
                    .title(categoryC.get(i).getCampaignName())));
        }
    }

    private void loadAllCategories(GoogleMap m){
        loadACategory(m);
        loadBCategory(m);
        loadCCategory(m);
    }


}
