package com.example.trendapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.trendapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowProductActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    private String BASE_URL = "https://www.tokopedia.com/search?st=product&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        String product = getIntent().getStringExtra("product_name");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(BASE_URL+product);
        webView.getSettings().setJavaScriptEnabled(true);


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
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
