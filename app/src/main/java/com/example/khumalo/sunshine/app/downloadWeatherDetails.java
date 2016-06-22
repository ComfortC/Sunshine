package com.example.khumalo.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class downloadWeatherDetails extends AsyncTask< String, Void, Void> {

    // These two need to be declared outside the try/catch
    // so that they can be closed in the finally block.
    private final String LOG_TAG = downloadWeatherDetails.class.getSimpleName();



    @Override
    protected Void doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String longtude = "18.474089";
        String latitude = "-33.992180";
        String format = "json";
        String units = "metric";
        int numDays = 7;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {

            final String FORECAST_BASE_URL ="http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                              .appendQueryParameter("lat", params[0])
                              .appendQueryParameter("lon", params[1])
                              .appendQueryParameter(FORMAT_PARAM, format)
                               .appendQueryParameter(UNITS_PARAM, units)
                               .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                               .appendQueryParameter(APPID_PARAM, "d83ea33d1b513ef282362ec07e456129").build();

            Log.d(LOG_TAG, "Url String is " + builtUri.toString());
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        Log.d(LOG_TAG,forecastJsonStr );
        return null;
    }


}
