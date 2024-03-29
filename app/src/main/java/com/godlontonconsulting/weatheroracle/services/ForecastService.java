package com.godlontonconsulting.weatheroracle.services;


import com.godlontonconsulting.weatheroracle.Constants;
import com.godlontonconsulting.weatheroracle.models.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastService {
    public static void getForecast(Double lat, Double lng, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = Constants.BaseURL + Constants.API_Key + "/" + lat + "," + lng+"?units=si";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Forecast> processResults(Response response) {
        ArrayList<Forecast> forecasts = new ArrayList<>();

        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject forecastServiceJSON = new JSONObject(jsonData);

                long timeOffset = forecastServiceJSON.getLong("offset");
                double lat = forecastServiceJSON.getDouble("latitude");
                double lng = forecastServiceJSON.getDouble("longitude");

                JSONObject weeklySummaryJSON = forecastServiceJSON.getJSONObject("daily");
                String weeklySummary = weeklySummaryJSON.getString("summary");

                JSONArray dailySummaryJSON = forecastServiceJSON.getJSONObject("daily").getJSONArray("data");
                for (int i = 0; i < dailySummaryJSON.length(); i++) {
                    JSONObject summaryForecastJSON = dailySummaryJSON.getJSONObject(i);
                    long time = summaryForecastJSON.getLong("time");
                    String summary = summaryForecastJSON.getString("summary");
                    String icon = summaryForecastJSON.getString("icon");
                    double minTemp = summaryForecastJSON.getDouble("temperatureMin");
                    double maxTemp = summaryForecastJSON.getDouble("temperatureMax");
                    double humidity = summaryForecastJSON.getDouble("humidity");
                    double precipitation = summaryForecastJSON.getDouble("precipProbability");
                    long sunrise = summaryForecastJSON.getLong("sunriseTime");
                    long sunset = summaryForecastJSON.getLong("sunsetTime");

                    Forecast forecast = new Forecast(time, summary, icon, minTemp, maxTemp, timeOffset, lat, lng, humidity, precipitation, sunrise, sunset, weeklySummary);
                    forecasts.add(forecast);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecasts;
    }
}
