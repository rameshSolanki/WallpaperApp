package com.fridayapp.wallpaperapp.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.model.WallpaperModel;
import com.github.wallpoo.Wallpo;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import static com.fridayapp.wallpaperapp.api.RetrofitInstance.BASE_URL;


public class WallpaperDetailActivity extends AppCompatActivity {
    Bundle bundle;
    String page_no, image_location, macs;
    TextView aks_description, windows_linux, mac;
    ImageView imageViewIcon;
    MaterialButton previous, next;
    public static int currentPosition;
    MaterialButton btnHomeScreen, btnLockScreen;
    private static final String KEY_CURRENT_POSITION = "com.fridayapp.expenseapp.key.currentPosition";
    public ArrayList<WallpaperModel> data = new ArrayList<>();
    private Target target;
    RelativeLayout appBarLayout;

    public WallpaperDetailActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);
            // Return here to prevent adding additional GridFragments when changing orientation.
            return;
        }

        setContentView(R.layout.wallpaper_set);
        //aks_description = findViewById(R.id.aks_description);
        appBarLayout = findViewById(R.id.appBarLayout);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        btnHomeScreen = findViewById(R.id.btnHomeScreen);
        btnLockScreen = findViewById(R.id.btnLockScreen);
        imageViewIcon = findViewById(R.id.header_image);
        bundle = getIntent().getExtras();
        page_no = bundle.getString("page_no");
        image_location = bundle.getString("image_location");
        data = getIntent().getParcelableArrayListExtra("data");
        currentPosition = getIntent().getIntExtra("pos", 0);
        //aks_description.setText("" + page_no);
//        Animation am= AnimationUtils.loadAnimation(this,R.anim.fade_enter);
//        appBarLayout.startAnimation(am);
        Picasso.get()
                .load(BASE_URL + "/uploads/" + image_location)
                .error(R.drawable.ic_baseline_hourglass_empty_24)
                .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                .into(imageViewIcon);
        //getSupportActionBar().setTitle("" + page_no);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = currentPosition - 1;
                if (currentPosition >= 0) {
                    //imageViewIcon.setText(""+data.get(currentPosition).getImage_location());
                    Picasso.get()
                            .load(BASE_URL + "/uploads/" + data.get(currentPosition).getImage_location())
                            .error(R.drawable.ic_baseline_hourglass_empty_24)
                            .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                            .into(imageViewIcon);
                } else {
                    currentPosition = currentPosition + 1;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = currentPosition + 1;
                if (currentPosition < data.size()) {
                    //imageViewIcon.setText(""+data.get(currentPosition).getImage_location());
                    Picasso.get()
                            .load(BASE_URL + "/uploads/" + data.get(currentPosition).getImage_location())
                            .error(R.drawable.ic_baseline_hourglass_empty_24)
                            .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                            .into(imageViewIcon);
                } else {
                    currentPosition = currentPosition - 1;
                }
            }
        });

        btnHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                Wallpo.setMainScreenWallpaper(WallpaperDetailActivity.this, imageViewIcon, "Wallpaper Set");
            }
        });

        btnLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                Wallpo.setLockScreenWallpaper(WallpaperDetailActivity.this, imageViewIcon, "LockWallpaper Set");
            }
        });

//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), "Download Started...", Toast.LENGTH_SHORT).show();
//                boolean success = true;
//                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"WallpaperApp");
//                if(!wallpaperDirectory.exists()){
//                    success = wallpaperDirectory.mkdir();
//                }
//                if (success) {
//                    downloadImg();
//                    btnHomeScreen.setEnabled(!btnHomeScreen.isEnabled());
//                    Toast.makeText(getBaseContext(), "Downloaded successfully...", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getBaseContext(), "The app was not allowed to write to your storage. Please consider granting it this permission", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        });

    }

    private void downloadImg() {
        target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String filename = "img" + new Date().getTime() + ".png";

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Walls" + File.separator, data.get(currentPosition).getImage_location());
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.get()
                .load(BASE_URL + "/uploads/" + image_location)
                .into(target);
    }
    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}