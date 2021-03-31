package com.example.trendapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trendapp.model.Prediction;
import com.example.trendapp.model.Trend;
import com.example.trendapp.model.retrofit.ApiService;
import com.example.trendapp.model.retrofit.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationViewModel extends AndroidViewModel {
    public MutableLiveData<List<Trend>> resultLiveData = new MutableLiveData();

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchData(){
        /*new FetchData().execute();*/
        Call<ResponseBody> call = ApiService.getInstance().getTrends();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    Type type = new TypeToken<List<Trend>>(){}.getType();
                    List<Trend> trends = new Gson().fromJson(data.getString("data"),type);
                    resultLiveData.setValue(trends);

                } catch (JSONException | IOException e) {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private class FetchData extends AsyncTask<Void,Void, List<Trend>>{

        @Override
        protected List<Trend> doInBackground(Void... voids) {
            List<Trend> trends = new ArrayList<>();
            List<Prediction> predictions = new ArrayList<>();
            *//*predictions.add(new Prediction("Jacket",0.87d));
            predictions.add(new Prediction("Denim",0.77d));
            predictions.add(new Prediction("Sweater",0.74d));

            trends.add(new Trend("Kota Jakarta Selatan","",predictions));
            trends.add(new Trend("Kota Bandung","",predictions));
            trends.add(new Trend("Kota Depok","",predictions));*//*
            return trends;
        }

        @Override
        protected void onPostExecute(List<Trend> trends) {
            super.onPostExecute(trends);
            resultLiveData.setValue(trends);
        }
    }*/

}
