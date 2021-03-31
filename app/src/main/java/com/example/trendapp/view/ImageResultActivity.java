package com.example.trendapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.trendapp.R;
import com.example.trendapp.model.Prediction;
import com.example.trendapp.util.Util;
import com.example.trendapp.view.adapter.LocationAdapter;
import com.example.trendapp.view.adapter.ResultAdapter;
import com.example.trendapp.viewmodel.ResultViewModel;
import com.example.trendapp.model.Trend;
import com.example.trendapp.model.retrofit.ApiService;
import com.example.trendapp.model.retrofit.Client;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageResultActivity extends AppCompatActivity {
    private static final String TAG = "ImageResultActivity";
    private static final int UPLOADING = 0;
    private static final int DONE = 1;
    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.mainview)
    ViewGroup mainView;

    @BindView(R.id.place)
    TextView placeText;

    @BindView(R.id.item)
    TextView itemText;

    Trend trend;

    ResultAdapter adapter;
    ResultViewModel viewModel;
    boolean isLoading = false;
    boolean isLocationDone = false;
    boolean isPredictionDone = false;

    Location location = null;
    int looper = 0;

    boolean isReadOnly = false;
    String namaKota;
    Prediction fashionItem;
    MenuItem uploadMenu;

    LocationManager locationManager;

    final int REQUEST_CODE = 1;

    private byte[] byteArray;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            byteArray = Util.convertToByte(bitmap);
            doPredict();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.d(TAG, "onBitmapFailed: "+e.getMessage());
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_result);
        ButterKnife.bind(this);

        configActionBar();

        setInitialRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        observeViewModel();
        trend = getIntent().getParcelableExtra(Util.TREND_EXTRA);

        setLoadingView(true);
        if (trend == null){
            setInitialView();
            if (isGpsEnabled()) {
                try {
                    fetchPlaceInformation();
                } catch (IOException e) {
                    Toast.makeText(this, "Error getting location", Toast.LENGTH_SHORT).show();
                }
                doPredict();
            } else {
                buildAlertNoGps();
            }
        } else {
            isReadOnly = true;
            setExistTrendToView();
            //setLoadingView(false);
            loadImageFromServiceAndPredict();

        }



    }

    private void setInitialRecyclerView() {
        adapter = new ResultAdapter(this,new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(product -> {
            Intent intent = new Intent(ImageResultActivity.this, ShowProductActivity.class);
            intent.putExtra("product_name", product);
            startActivity(intent);
        });
    }

    private void loadImageFromServiceAndPredict() {
        /*Glide.with(this)
                .asBitmap().load(Util.getUrl(trend.getImageUrl()))
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  Toast.makeText(ImageResultActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {

                                  byteArray = Util.convertToByte(bitmap);
                                  doPredict();

                                  return false;
                              }
                          }
                ).submit();*/
        Picasso.get().load(Util.getUrl(trend.getImageUrl())).into(target);

    }

    private void setExistTrendToView() {
        Util.loadImage(imageView,Util.getUrl(trend.getImageUrl()),Util.getProgressDrawable(this));
        itemText.setText(trend.getFashionItem());
        placeText.setText(trend.getKota());
        isLocationDone = true;
    }

    private boolean isGpsEnabled() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    private void buildAlertNoGps() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        Toast.makeText(ImageResultActivity.this, "Silahkan diulangi", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        showDialogNotGranted();
                    }
                }).create();
        alertDialog.show();
    }

    private void doPredict() {
        viewModel.sendToClarifai(byteArray);
        setLoadingView(true);
    }

    private void setLoadingView(boolean isLoading) {
        this.isLoading = isLoading;
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mainView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void fetchPlaceInformation() throws IOException {
        getLocation(LocationManager.GPS_PROVIDER);
        getLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void getLocation(String provider){
        setLoadingView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }

        if (location == null) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                setLocationToView(location);
            } else {
                locationManager.requestLocationUpdates(provider, 0, 0,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                setLocationToView(location);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                Toast.makeText(ImageResultActivity.this, "onStatusChanged", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                Toast.makeText(ImageResultActivity.this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                Toast.makeText(ImageResultActivity.this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void setLocationToView(Location location){
        if (looper == 0){
            ImageResultActivity.this.location = location;
            Geocoder geocoder = new Geocoder(ImageResultActivity.this);
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            namaKota = addressList.get(0).getSubAdminArea();
            placeText.setText(namaKota);
            isLocationDone = true;
            checkForVisibleView();
            looper++;
        }
    }

    private void showDialogNotGranted() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Some features cannot be executed because of denied permission")
                .setCancelable(false)
                .setNegativeButton("Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                showDialogNotGranted();
            }
            return;
        }
    }

    private void checkForVisibleView() {
        boolean isLoading = !(isPredictionDone && isLocationDone);
        setLoadingView(isLoading);
    }

    private void observeViewModel() {
        viewModel.resultLiveData.observe(this, new Observer<List<Prediction>>() {
            @Override
            public void onChanged(List<Prediction> predictions) {
                if (!isReadOnly){
                    fashionItem = predictions.get(0);
                    itemText.setText(fashionItem.getName());
                }
                predictions.remove(0);
                List<Prediction> recommendation = predictions;
                adapter.updateData((ArrayList<Prediction>) recommendation);
                isPredictionDone = true;
                checkForVisibleView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu,menu);
        uploadMenu = menu.findItem(R.id.upload);
        if (isReadOnly){
            uploadMenu.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setInitialView() {
        byteArray = getIntent().getByteArrayExtra("gambar");
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    private void configActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Prediction");
    }

    @OnClick(R.id.item)
    void onFashionItemClicked(){
        Intent intent = new Intent(ImageResultActivity.this, ShowProductActivity.class);
        intent.putExtra("product_name", itemText.getText().toString());
        startActivity(intent);
    }
    /*public void connectClarifai(){
        client = new ClarifaiBuilder("f64706cccca84d75bab891f95e58355f")
                .client(new OkHttpClient())
                .buildSync();

    }*/



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.upload:
                //Toast.makeText(this, "uploading", Toast.LENGTH_SHORT).show();
                uploadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadData() {
        uploadMenu.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        File f = new File(getCacheDir(), "jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(byteArray);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("jpg", f.getName(), reqFile);

        Trend trend = new Trend(namaKota, null, fashionItem.getName(), fashionItem.getAccuracy());

        executeUpload(body,trend);
    }

    private void executeUpload(MultipartBody.Part body, Trend trend) {
        Client service = ApiService.getInstance();
        Call<ResponseBody> req = service.postImage(body,trend.getKota(),trend.getFashionItem(),trend.getAccuracy());
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    //jsonObject.getString("msg");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ImageResultActivity.this, "upload "+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure message
                t.printStackTrace();
                Toast.makeText(ImageResultActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
