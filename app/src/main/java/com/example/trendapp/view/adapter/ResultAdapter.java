package com.example.trendapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trendapp.R;
import com.example.trendapp.model.Prediction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    Context context;
    ArrayList<Prediction> prediction;
    OnViewClickListener listener;



    public ResultAdapter(Context context, ArrayList<Prediction> prediction) {
        this.context = context;
        this.prediction = prediction;
    }

    public void updateData(ArrayList<Prediction> newData){
        prediction = newData;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnViewClickListener listener){
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textResult.setText(prediction.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(prediction.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return prediction.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_result)
        TextView textResult;
        View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            this.itemView = itemView;
        }
    }

    public interface OnViewClickListener{
        void onClick(String productName);
    }
}
