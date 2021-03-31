package com.example.trendapp.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.trendapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ShowMediaActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    private String BASE_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_media);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String stuff =getIntent().getStringExtra("url");
        String stuff1 = getIntent().getStringExtra("gender");
        String stuff2 = getIntent().getStringExtra("dc");
        String stuff3 = getIntent().getStringExtra("event");
        String stuff0 = "ootd";

        if (stuff.equals("INSTAGRAM")){
            if (stuff3.equals("PERNIKAHAN")){
                BASE_URL="https://www.instagram.com/explore/tags/wedding";
                stuff0=stuff2;
            }
            else if (stuff3.equals("KANTOR")){
                BASE_URL="https://www.instagram.com/explore/tags/working";
                if(stuff1.equals("MALE")) {stuff0="boy";}
                else{stuff0="girl";}
            }
            else if (stuff3.equals("KEBUDAYAAN")){
                if(stuff1.equals("MALE")) {
                    BASE_URL="https://www.instagram.com/explore/tags/boysethnic";
                    stuff0="wear";
                }
                else{
                    BASE_URL="https://www.instagram.com/explore/tags/girlsethnic";
                    stuff0="wear";
                }
            }
            else if (stuff3.equals("SPORT")){
                BASE_URL="https://www.instagram.com/explore/tags/sport";
                if(stuff1.equals("MALE")) {stuff0="man";}
                else{stuff0="woman";}
            }
            else if (stuff3.equals("KAMPUS")){
                if(stuff1.equals("MALE")) {
                    BASE_URL="https://www.instagram.com/explore/tags/university";
                    stuff0="boy";
                }
                else{
                    BASE_URL="https://www.instagram.com/explore/tags/university";
                    stuff0="girl";
                }
            }
            else if (stuff3.equals("REUNI")){
                BASE_URL="https://www.instagram.com/explore/tags/reoni";
                stuff0="";
            }
            else if (stuff3.equals("ULANG TAHUN")){
                BASE_URL="https://www.instagram.com/explore/tags/birthday";
                stuff0=stuff2;
            }
            else{
                BASE_URL="https://www.instagram.com/explore/tags/ootd";
                stuff0=stuff1;}
        }
        else{
            //(stuff.equals("PINTEREST")){BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20";}
            if(stuff3.equals("PERNIKAHAN")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20wedding%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20wedding%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20wedding%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20wedding%20female%20";}}
            }
            else if (stuff3.equals("KANTOR")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20working%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20working%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20working%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20working%20female%20";}}
            }
            else if (stuff3.equals("KEBUDAYAAN")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20ethnic%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20ethnic%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20ethnic%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20ethnic%20female%20";}}
            }
            else if (stuff3.equals("SPORT")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20sport%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20sport%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20sport%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20sport%20female%20";}}
            }
            else if (stuff3.equals("KAMPUS")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20university%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20university%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20university%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20university%20female%20";}}

            }
            else if (stuff3.equals("REUNI")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=reonion%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=reonion%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=reonion%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=reonion%20female%20";}}
            }
            else if (stuff3.equals("ULANG TAHUN")){

                if(stuff1.equals("MALE")) {

                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20birthday%20male%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20birthday%20male%20";}}

                else{
                    if(stuff2.equals("CASUAL")) {
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20birthday%20female%20";
                        stuff0="casual";}

                    else{   stuff0="formal";
                        BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20birthday%20female%20";}}

            }
            else{ BASE_URL="https://id.pinterest.com/search/pins/?q=ootd%20";
                stuff0=stuff1;}
        }



        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(BASE_URL+stuff0);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();;
        }
        super.onBackPressed();
    }

    public void ScreenshotButton(View view){
        View view1 = findViewById(R.id.screenshotLayout);
        view1.setDrawingCacheEnabled(true);
        LinearLayout linear = (LinearLayout) findViewById(R.id.screenshotLayout);
        int totalHeight = linear.getChildAt(0).getHeight();
        int totalWidth = linear.getChildAt(0).getWidth();
        view1.layout(0, 0, totalWidth, totalHeight);
        view1.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);

        String filePath = Environment.getExternalStorageDirectory()+"/Screenshots/"+"atrendberpakaian"+ Calendar.getInstance().getTime().toString()+".jpg";
        //String filePath = Environment.getExternalStorageDirectory()+"/Download/"+"atrendberpakaian"+ Calendar.getInstance().getTime().toString()+".jpg";
        File fileScreenshot = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(fileScreenshot);
        intent.setDataAndType(uri,"image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
