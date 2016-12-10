/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.yoav.mynewsunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yoav.mynewsunshine.data.WeatherContract;

public class DetailActivity extends AppCompatActivity {

    private static final int DETAIL_LOADER = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.detail, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    Log.d("xxx", "onOptionsItemSelected: ");

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      startActivity(new Intent(this, SettingsActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
    /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

      private static final String LOG_TAG = DetailActivity.class.getSimpleName();

      private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
//      private String mForecastStr;

        private ShareActionProvider mShareActionProvider;
        private String mForecast;

        private static final int DETAIL_LOADER = 0;

        private static final String[] FORECAST_COLUMNS = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };

        // these constants correspond to the projection defined above, and must change if the
        // projection changes
        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;

    public PlaceholderFragment() {
        setHasOptionsMenu(true);
    }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.v(LOG_TAG, "In onCreateLoader");
            Intent intent = getActivity().getIntent();
            if (intent == null) {
            return null;
            }

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
            getActivity(),
            intent.getData(),
            FORECAST_COLUMNS,
            null,
            null,
            null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.v(LOG_TAG, "In onLoadFinished");
            if (!data.moveToFirst()) { return; }

            String dateString = Utility.formatDate(
                    data.getLong(COL_WEATHER_DATE));

            String weatherDescription =
                    data.getString(COL_WEATHER_DESC);

            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

            String low = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
            detailTextView.setText(mForecast);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        @Override
        public void onActivityCreated( Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      Intent intent = getActivity().getIntent();
      View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

//        if (intent != null && intent.hasExtra(intent.EXTRA_TEXT)) {
//        //String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//          mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//        ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
//      }

//        if (intent != null) {
//            mForecastStr = intent.getDataString();
//        }
//
//        if (null != mForecastStr) {
//            ((TextView) rootView.findViewById(R.id.detail_text))
//                    .setText(mForecastStr);
//        }
//      return rootView;
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
      @Override
      public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//          super.onCreateOptionsMenu(menu, inflater);
          // Inflate the menu; this adds items to the action bar if it is present.
          inflater.inflate(R.menu.detailfragment, menu);

              // Retrieve the share menu item
                      MenuItem menuItem = menu.findItem(R.id.action_share);

              // Get the provider and hold onto it to set/change the share intent.
//                      ShareActionProvider mShareActionProvider =
//                      (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
          mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

              // Attach an intent to this ShareActionProvider.  You can update this at any time,
                      // like when the user selects a new piece of data they might like to share.
//                              if (mShareActionProvider != null ) {
          // If onLoadFinished happens before this, we can go ahead and set the share intent now.
          if (mForecast != null) {
              mShareActionProvider.setShareIntent(createShareForecastIntent());
//          } else {
//              Log.d(LOG_TAG, "Share Action Provider is null?");
         }
      }

      private Intent createShareForecastIntent(){
          Intent shareIntent = new Intent(Intent.ACTION_SEND);
          shareIntent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET));
          shareIntent.setType("text/plain");
//          shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
          shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
          return shareIntent;
      }

  }
}