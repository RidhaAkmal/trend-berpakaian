package com.example.trendapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.trendapp.R;

import butterknife.BindView;

public class FormActivity extends AppCompatActivity {
    Button button;
    RadioGroup gendergrup, mediagrup, dcgrup, eventgrup;
    RadioButton genderbutton,mediabutton,dcbutton;
    private Spinner spin;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Form Rekomendasi");

        gendergrup = findViewById(R.id.genderg);
        mediagrup = findViewById(R.id.mediag);
        dcgrup = findViewById(R.id.dresscodeg);
        spin=(Spinner) findViewById(R.id.spinner);
        Button buttonApply=findViewById(R.id.button);
        buttonApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int radiogenderId=gendergrup.getCheckedRadioButtonId();
                genderbutton=findViewById(radiogenderId);
                int radiomediaId=mediagrup.getCheckedRadioButtonId();
                mediabutton=findViewById(radiomediaId);
                int radiodcId=dcgrup.getCheckedRadioButtonId();
                dcbutton=findViewById(radiodcId);


                Intent intent = new Intent(FormActivity.this, ShowMediaActivity.class);
                intent.putExtra("url", mediabutton.getText().toString());
                intent.putExtra("gender", genderbutton.getText().toString());
                intent.putExtra("dc", dcbutton.getText().toString());
                intent.putExtra("event", spin.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
