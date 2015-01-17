package de.eatgate.placessearch;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;



public class MainActivity extends ActionBarActivity {

    private PlacesService placesService;
    private static final String API_KEY = "AIzaSyCi2JeBSkQ8RugIVb-BA5bvgbDEF9G-zto";
    GPS gps;
    private String types = "restaurant|food|establishment";
    private String radius = "500.0";
    private ArrayList<Place> places = new ArrayList<Place>();
    private ListView placesListView;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPS(this);
        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude: " + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            //  alert.showAlertDialog(MainActivity.this, "GPS Status",
            //          "Couldn't get location information. Please enable GPS",
            //          false);
            // stop executing code by return
            return;
        }

        new GetPlaces(MainActivity.this,radius,types).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to Load Google places
     * */
    private class GetPlaces extends AsyncTask<Void, Void, String> {

        private String types;
        private Context context;
        private String radius;

        public GetPlaces(Context context, String radius, String types){
            this.context = context;
            this.types = types;
            this.radius = radius;
        }
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            places = new ArrayList<Place>();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(Void... arg0) {
            // creating Places class object
            placesService = new PlacesService(API_KEY,radius,types);

            places = placesService.findPlaces(gps.getLatitude(), // 28.632808
                    gps.getLongitude()); // 77.218276
            Log.e("Info","Anzahl: " + places.size());
            for (int i = 0; i < places.size(); i++) {

                Place placeDetail = places.get(i);
                Log.e("Leer", "places : " + placeDetail.getName());
            }
            return "";
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            placesListView = (ListView) findViewById(R.id.listplaces);
            adapter = new ListViewAdapter(MainActivity.this,places);
            placesListView.setAdapter(adapter);

        }

    }

}

