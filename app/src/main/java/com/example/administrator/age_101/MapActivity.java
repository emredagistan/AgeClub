package com.example.administrator.age_101;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
//import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
//import android.location.Location;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.maps.Projection;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int REQUEST_lOCATION=90;
    static final LatLng AgeKonum = new LatLng(39.9046083, 32.8650663);
    static final LatLng EsatDortYol = new LatLng(39.9095337,32.8621499);
    static final LatLng CankayaHastanesi = new LatLng(39.904623,32.863391);
    static final LatLng YildizAspava = new LatLng(39.9052647,32.8655242);
    static final LatLng PorsukGiyim = new LatLng(39.9087027,32.8619281);
    static final LatLng kizilay = new LatLng(39.9198004,32.8545718);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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

        // Ankarayı ekledim harita ilk açıldığında
        if(mMap != null)
        {

            Spinner sp = findViewById(R.id.spinner);
            sp.setSelection(0);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {

                    switch (selected){
                        case 0:
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(PorsukGiyim).title("Porsuk Alternetif Giyim"));
                            mMap.addMarker(new MarkerOptions().position(YildizAspava).title("Yıldız Aspava"));
                            mMap.addMarker(new MarkerOptions().position(CankayaHastanesi).title("Özel Çankaya Hastanesi"));
                        break;
                        case 1:
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(PorsukGiyim).title("Porsuk Alternetif Giyim"));
                        break;
                        case 2:
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(YildizAspava).title("Yıldız Aspava"));
                        break;
                        case 3:
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(CankayaHastanesi).title("Özel Çankaya Hastanesi"));

                        break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            //LatLng myLoc = new LatLng(mMap.getMyLocation().getLongitude(), mMap.getMyLocation().getLongitude());
            // myLoc büyük ihtimalle geç geldiği için program crash veriyor
            PolylineOptions options = new PolylineOptions().add(AgeKonum).add(EsatDortYol).width(0)
                    .color(Color.BLUE).visible(true).geodesic(true);
            mMap.addPolyline(options); // width 0 olduğu için çizgiler invizible

            mMap.setTrafficEnabled(true);

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
}
