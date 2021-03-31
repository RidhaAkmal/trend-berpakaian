package com.example.trendapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.trendapp.util.Util;
import com.example.trendapp.view.adapter.LocationAdapter;
import com.example.trendapp.viewmodel.LocationViewModel;
import com.example.trendapp.R;
import com.example.trendapp.model.Trend;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.nestedScrollView)
    ViewGroup nestedScrollView;
    LocationAdapter adapter;
    LocationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Location");

        adapter = new LocationAdapter(this,new ArrayList<>());
        adapter.setOnClickListener(new LocationAdapter.OnClickListener() {
            @Override
            public void onClick(Trend trend) {
                Intent intent = new Intent(LocationActivity.this,ImageResultActivity.class);
                intent.putExtra(Util.TREND_EXTRA,trend);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        setLoadingView(true);
        viewModel.fetchData();
        observeViewModel();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLoadingView(boolean isLoading){
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        nestedScrollView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void observeViewModel() {
        viewModel.resultLiveData.observe(this, new Observer<List<Trend>>() {
            @Override
            public void onChanged(List<Trend> trends) {
                adapter.updateData(trends);
                setLoadingView(false);
            }
        });
    }
}
