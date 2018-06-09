package age.AgeClub.administrator.age_102;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class DiscountDetail extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 0;

    private Button shareButton, mapButton;
    private ImageView discountImage;
    private TextView discountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);

        shareButton = findViewById(R.id.shareButton);
        mapButton = findViewById(R.id.mapButton);
        discountImage = findViewById(R.id.discountImage);
        discountText = findViewById(R.id.discountText);

        Intent i = getIntent();
        Discount discount = (Discount) i.getSerializableExtra("discount");

        setDiscountToPage(discount);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    shareIntent.putExtra(Intent.EXTRA_TEXT, discountText.getText());
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                    startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
                else{
                    Toast.makeText(DiscountDetail.this, "Bu işlem için öncelikle yetki vermelisiniz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(DiscountDetail.this, MapActivity.class);
                mapIntent.putExtra("discount", discount);
                startActivity(mapIntent);
            }
        });
    }

    private void setDiscountToPage(Discount d){
        discountText.setText(d.getCampaignName());
        Picasso.with(getApplicationContext()).load(d.getImageLink()).into(discountImage);
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

}
