package age.club.administrator.age_101;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.os.Environment;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int MY_REQUEST_CODE = 0;
    //private static final int CONTENT_VIEW_ID = 10101010;

    private ViewFlipper vf;
    private ImageView generatedQR;
    private TextView qrContent, nameTextView, emailTextView, cardNumberTextView;
    private Toast qrToast;
    Intent intent;
    private DiscountAdapter discountAdapter, discountAdapterShowcase;
    private DiscountCategorizer discountCategorizer, discountCategorizerShowcase;
    private Button changePassword;
    private NavigationView navigationView;
    private int lastClickedMenuItemId;
    private String postTransactionURL = "http://212.175.137.237/ageClub/QR/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // TEST STATE //
        /*Discount d1 = new Discount();
        Discount d2 = new Discount();
        d1.setCampaignName("AA");
        d2.setCampaignName("BB");
        List<Discount> dcs = new ArrayList<>();
        dcs.add(d1);
        dcs.add(d2);*/
        // TEST STATE //



        generatedQR = findViewById(R.id.myImageQR);
        Button qrButton = findViewById(R.id.qr_button);
        Button qrCreate = findViewById(R.id.qrCreate);
        qrContent = findViewById(R.id.qrContent);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        cardNumberTextView = findViewById(R.id.cardNumberTextView);
        changePassword = findViewById(R.id.changePassword);

        vf = findViewById(R.id.vf);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        discountAdapter = new DiscountAdapter(this, getApplicationContext());
        discountAdapterShowcase = new DiscountAdapter(this, getApplicationContext());
        discountCategorizer = new DiscountCategorizer(getApplicationContext(), discountAdapter);

        discountCategorizerShowcase = new DiscountCategorizer(getApplicationContext(), discountAdapterShowcase);
        discountAdapter.setDiscounts(discountCategorizer.getAllCategories());

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userID))
                .setText(User.getInstance().getName() + " " + User.getInstance().getSurname());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userMail)).setText(User.getInstance().getMail());

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

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
                    else{
                        Toast.makeText(MainActivity.this, "Bu işlem için öncelikle yetki vermelisiniz.", Toast.LENGTH_SHORT).show();
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
                    Gson gsonQR = new GsonBuilder().create();
                    String qrGsonContent = gsonQR.toJson(myQRCode); // Whatever you need to encode in the QR code

                    BitMatrix bm = writer
                            .encode(qrGsonContent, BarcodeFormat.QR_CODE, width, height);
                    Bitmap bitmap = Bitmap.createBitmap(width, height,
                            Bitmap.Config.ARGB_8888);

                    for (int i = 0; i < width; i++) {// width
                        for (int j = 0; j < height; j++) {// height
                            bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK
                                    : Color.WHITE);
                        }
                    }
                    generatedQR.setImageBitmap(bitmap);
                    qrContent.setText("Karekod " + User.getInstance().getCardId() + " kart numarası ile, "
                            + myQRCode.getTimeStamp() + " tarihinde oluşturulmuştur.");
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordWeb = new Intent("android.intent.action.VIEW",
                        Uri.parse("http://www.ageenerji.com.tr"));
                startActivity(changePasswordWeb);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } /*else {
            super.onBackPressed();
        }*/
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

        if (id == R.id.action_settings) { //Logout
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
            return true;
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id != R.id.nav_gallery){
            lastClickedMenuItemId = id;
        }

        if (id == R.id.nav_main){
            /*final ListView dcShowcase = findViewById(R.id.discountShowcase);
            discountAdapterShowcase.setDiscounts(discountCategorizerShowcase.getShowcase());
            dcShowcase.setAdapter(discountAdapterShowcase);

            dcShowcase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = dcShowcase.getItemAtPosition(i);
                    TextView v = view.findViewById(R.id.discountText);
                    ImageView discountImage = view.findViewById(R.id.discountImage);
                    if(requestReadExternalStorage() && requestWriteExternalStorage()){
                        discountImage.setDrawingCacheEnabled(true);
                        Bitmap bitmap = discountImage.getDrawingCache();
                        File root = Environment.getExternalStorageDirectory();
                        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                        try {
                            cachePath.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(cachePath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, v.getText());
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                        startActivity(Intent.createChooser(shareIntent, "Share using"));
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Bu işlem için öncelikle yetki vermelisiniz.", Toast.LENGTH_SHORT).show();
                    }

                }
            });*/

            vf.setDisplayedChild(0); //main
        } else if (id == R.id.nav_camera) {
            vf.setDisplayedChild(1);//qr code
        } else if (id == R.id.nav_gallery) {
            Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
            mapIntent.putExtra("categoryA", discountCategorizer.getCategoryA());
            mapIntent.putExtra("categoryB", discountCategorizer.getCategoryB());
            mapIntent.putExtra("categoryC", discountCategorizer.getCategoryC());
            startActivity(mapIntent);
            //vf.setDisplayedChild(2);//map
        } else if (id == R.id.nav_slideshow) {
            final ListView dc = findViewById(R.id.discountContent);
            Spinner sp = findViewById(R.id.spinnerDiscount);



            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {

                    switch (selected){
                        case 0:
                            discountAdapter.setDiscounts(discountCategorizer.getAllCategories());
                            break;
                        case 1:
                            discountAdapter.setDiscounts(discountCategorizer.getCategoryA());
                            break;
                        case 2:
                            discountAdapter.setDiscounts(discountCategorizer.getCategoryB());
                            break;
                        case 3:
                            discountAdapter.setDiscounts(discountCategorizer.getCategoryC());
                            break;

                    }
                    dc.setAdapter(discountAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            dc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Object o = dc.getItemAtPosition(i);
                    TextView v = view.findViewById(R.id.discountText);
                    ImageView discountImage = view.findViewById(R.id.discountImage);
                    if(requestReadExternalStorage() && requestWriteExternalStorage()){
                        discountImage.setDrawingCacheEnabled(true);
                        Bitmap bitmap = discountImage.getDrawingCache();
                        File root = Environment.getExternalStorageDirectory();
                        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                        try {
                            cachePath.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(cachePath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, v.getText());
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                        startActivity(Intent.createChooser(shareIntent, "Share using"));
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Bu işlem için öncelikle yetki vermelisiniz.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            vf.setDisplayedChild(3);//discounts

        } else if (id == R.id.nav_manage) {
            nameTextView.setText(User.getInstance().getName() + " " + User.getInstance().getSurname());
            emailTextView.setText(User.getInstance().getMail());
            cardNumberTextView.setText(User.getInstance().getCardId());
            vf.setDisplayedChild(4);//personal information
        } else if (id == R.id.nav_share) {
            Intent resetPasswordWeb = new Intent("android.intent.action.VIEW",
                    Uri.parse("http://www.ageenerji.com.tr"));
            startActivity(resetPasswordWeb);
        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(User.getInstance().getType() == 2){
            Gson gson = new GsonBuilder().create();
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result.getContents() != null){
                try{
                    QRCodeGenerator postQR = gson.fromJson(result.getContents(), QRCodeGenerator.class);
                    final QrTransaction postTransaction = new QrTransaction();
                    postTransaction.setCustomerID(postQR.getCardNumber());
                    postTransaction.setTime(postQR.getTimeStamp());
                    postTransaction.setCompanyID(User.getInstance().getCardId());


                    StringRequest myStringRequest = new StringRequest(Request.Method.POST, postTransactionURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.

                            if (response.trim().equals("0")) { //trim response to remove whitespaces
                                Toast.makeText(getApplicationContext(), "QR gönderme başarısız oldu!", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.trim().equals("1")){
                                Toast.makeText(getApplicationContext(), "QR başarıyla gönderildi!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Bilinmeyen bir hata oluştu!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                            Toast.makeText(getApplicationContext(), "Volley Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<>();
                            MyData.put("customerID", postTransaction.getCustomerID()); //Add the data you'd like to send to the server.
                            MyData.put("companyID", postTransaction.getCompanyID());
                            MyData.put("time", postTransaction.getTime());
                            return MyData;
                        }
                    };
                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(myStringRequest);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Uyumsuz QR algılandı!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.getMenu().findItem(lastClickedMenuItemId).setChecked(true);
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

    private boolean requestWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
                return checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }

    }

    private boolean requestReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
                return checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
