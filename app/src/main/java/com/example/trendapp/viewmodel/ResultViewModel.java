package com.example.trendapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trendapp.model.Clarifai;
import com.example.trendapp.model.Prediction;

import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class ResultViewModel extends AndroidViewModel {
    public MutableLiveData<List<Prediction>> resultLiveData = new MutableLiveData<>();

    public ResultViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendToClarifai(final byte[] byteArray) {
        RetrieveDetection retrieveDetection = new RetrieveDetection();
        retrieveDetection.execute(byteArray);
    }

    private class RetrieveDetection extends AsyncTask<byte[],Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {


        @Override
        protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(byte[]... bytes) {
            ClarifaiClient client = Clarifai.getInstance();
            final PredictRequest<Concept> generalModel = client.getDefaultModels().apparelModel().predict();

            return generalModel.
                    withInputs(ClarifaiInput.forImage(ClarifaiImage.of(bytes[0])))
                    .executeSync();
        }

        @Override
        protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> listClarifaiResponse) {
            final List<ClarifaiOutput<Concept>> predictions = listClarifaiResponse.get();
            int size = predictions.get(0).data().size();
            ArrayList<Prediction> prediction = new ArrayList<>();
            for (int i=0; i<size; i++){
                //double value = predictions.get(0).data().get(i).value();
                //if (value>0.86) {
                prediction.add(new Prediction(predictions.get(0).data().get(i).name(),predictions.get(0).data().get(i).value()));
                //}
            }
            //Toast.makeText(ImageResultActivity.this, prediction.get(0), Toast.LENGTH_SHORT).show();
            resultLiveData.setValue(prediction);
        }
    }
}
