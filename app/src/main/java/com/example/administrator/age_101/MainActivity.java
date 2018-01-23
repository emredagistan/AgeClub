package com.example.administrator.age_101;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_REQUEST_CODE = 0;
    //private static final int CONTENT_VIEW_ID = 10101010;

    private ViewFlipper vf;
    private ImageView generatedQR;
    private TextView qrContent, personalInfo;
    private Toast qrToast;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        String userData = intent.getStringExtra("data");
        Gson gsonUser = new GsonBuilder().create();
        User myUser = gsonUser.fromJson(userData, User.class);//User.getInstance();
        myUser.setInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // TEST STATE //
        Discount d1 = new Discount("AAAAAA");
        Discount d2 = new Discount("BBBBBB");
        List<Discount> dcs = new ArrayList<>();
        dcs.add(d1);
        dcs.add(d2);
        // TEST STATE //


        generatedQR = (ImageView)findViewById(R.id.myImageQR);
        Button qrButton = (Button)findViewById(R.id.qr_button);
        Button qrCreate = (Button)findViewById(R.id.qrCreate);
        qrContent = (TextView)findViewById(R.id.qrContent);
        personalInfo = (TextView)findViewById(R.id.personalInfo);

        final ListView dc = (ListView)findViewById(R.id.discountContent);
        DiscountAdapter da = new DiscountAdapter(this, dcs);
        dc.setAdapter(da);

        vf = (ViewFlipper)findViewById(R.id.vf);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userID))
                .setText(User.getInstance().getName() + " " + User.getInstance().getSurname());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userMail)).setText(User.getInstance().getMail());

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getInstance().getType() == 1){
                    if (qrToast != null) {
                        qrToast.cancel();
                    }
                    qrToast= Toast.makeText(getApplicationContext(), "Sadece üye işyerleri QR yakalayabilir!", Toast.LENGTH_SHORT);
                    qrToast.show();
                } else if(User.getInstance().getType() == 2){
                    if(requestCamera()){
                        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                        integrator.setPrompt("Scan a QR Code");
                        integrator.setCameraId(0);
                        integrator.initiateScan();
                    }
                }
            }
        });

        qrCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeGenerator myQRCode = new QRCodeGenerator();
                try {
                    int width = 500;
                    int height = 500;
                    com.google.zxing.Writer writer = new QRCodeWriter();
                    String text = myQRCode.toString(); // Whatever you need to encode in the QR code
                    BitMatrix bm = writer
                            .encode(text, BarcodeFormat.QR_CODE, width, height);
                    Bitmap bitmap = Bitmap.createBitmap(width, height,
                            Bitmap.Config.ARGB_8888);

                    for (int i = 0; i < width; i++) {// width
                        for (int j = 0; j < height; j++) {// height
                            bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK
                                    : Color.WHITE);
                        }
                    }
                    generatedQR.setImageBitmap(bitmap);
                    Gson gsonQR = new GsonBuilder().create();
                    String qrGsonContent = gsonQR.toJson(myQRCode);
                    qrContent.setText(qrGsonContent);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main){
            vf.setDisplayedChild(0); //main
        } else if (id == R.id.nav_camera) {
            vf.setDisplayedChild(1);//qr code
        } else if (id == R.id.nav_gallery) {
            Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
            MainActivity.this.startActivity(mapIntent);
            //vf.setDisplayedChild(2);//map
        } else if (id == R.id.nav_slideshow) {
            vf.setDisplayedChild(3);//discounts
        } else if (id == R.id.nav_manage) {
            personalInfo.setText(User.getInstance().getCardId());/* TODO personal info here*/
            vf.setDisplayedChild(4);//personal information
        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        TextView view = (TextView)findViewById(R.id.qrView);
        view.setText(result.getContents());


    }

    private boolean requestCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                return checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
