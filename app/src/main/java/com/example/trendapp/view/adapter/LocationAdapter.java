package com.example.trendapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trendapp.R;
import com.example.trendapp.model.Trend;
import com.example.trendapp.model.retrofit.ApiService;
import com.example.trendapp.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Context context;
    List<Trend> trends;
    private OnClickListener listener;

    public LocationAdapter(Context context, List<Trend> trends) {
        this.context = context;
        this.trends = trends;
    }

    public void updateData(List<Trend> trends){
        this.trends.clear();
        this.trends.addAll(trends);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trend trend = trends.get(position);
        holder.textKota.setText(trend.getKota());
        Util.loadImage(holder.imageView,Util.getUrl(trend.getImageUrl()),Util.getProgressDrawable(context));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(trend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.kota)
        TextView textKota;
        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.cardSurface)
        ViewGroup cardSurface;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    public interface OnClickListener{
        void onClick(Trend trend);
    }
}
