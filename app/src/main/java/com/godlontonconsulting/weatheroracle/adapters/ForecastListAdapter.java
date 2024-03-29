package com.godlontonconsulting.weatheroracle.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.godlontonconsulting.weatheroracle.R;
import com.godlontonconsulting.weatheroracle.models.Forecast;
import com.godlontonconsulting.weatheroracle.activities.ForecastDetailActivity;

import org.parceler.Parcels;
import java.util.ArrayList;
import butterknife.ButterKnife;


public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder> {
    private ArrayList<Forecast> mForecasts = new ArrayList<>();
    private Context mContext;

    public ForecastListAdapter(Context context, ArrayList<Forecast> forecasts) {
        mContext = context;
        mForecasts = forecasts;
    }

    @Override
    public ForecastListAdapter.ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_forecast_list_item, parent, false);
        ForecastViewHolder viewHolder = new ForecastViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastListAdapter.ForecastViewHolder holder, int position) {
        holder.bindForecast(mForecasts.get(position));
    }

    @Override
    public int getItemCount() {
        return mForecasts.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mDailyTimeTextView;
        private TextView mDailySummaryTextView;
        private Context mContext;
        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mDailyTimeTextView=(TextView) itemView.findViewById(R.id.dailyTimeTextView);
            mDailySummaryTextView=(TextView) itemView.findViewById(R.id.dailySummaryTextView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, ForecastDetailActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("forecasts", Parcels.wrap(mForecasts));
            mContext.startActivity(intent);
        }

        public void bindForecast(Forecast forecast) {
            mDailyTimeTextView.setText(forecast.getDayOfWeek());
            mDailySummaryTextView.setText(forecast.getDailySummary());
        }
    }
}
