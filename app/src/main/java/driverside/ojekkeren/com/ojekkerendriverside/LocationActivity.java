package driverside.ojekkeren.com.ojekkerendriverside;

/**
 * Created by User Pc on 24/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.util.Log;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocationActivity extends Activity {

    private View btnShowLocation;
    private TextView longtitudeText;
    private TextView latutideText;


    private final String TAG_DRIVER = "driverNik";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_DRIVERNAME = "driverName";
    JSONParser jsonParser = new JSONParser();
    private static final String url_update_location = "http://webtvasia.id/dashboard/api.php?task=simpanLocation";

    // GPSTracker class
    private GPSDetector gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

        gps = new GPSDetector(LocationActivity.this);
        new SaveLocationDriver().execute();


        final ListView listView = (ListView) findViewById(R.id.listView);

        String base_url = getServerAPIServer();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIGetOrdersAvailable getOrdersInProgressREST = restAdapter.create(APIGetOrdersAvailable.class);

        getOrdersInProgressREST.getOrdersList("getOrders", new Callback<List<POJOOrdersData>>() {

            @Override
            public void success(List<POJOOrdersData> ordersDatas, Response response) {
                ArrayList<POJOOrdersData> ordersData = new ArrayList<POJOOrdersData>();
                if (ordersDatas.size() == 0) {

                } else {
                    for (int i = 0; i < ordersDatas.size(); i++) {
                        ordersData.add(ordersDatas.get(i));
                    }
                    AdapterListGetOrders adapter = new AdapterListGetOrders(LocationActivity.this, ordersData);
                    listView.setAdapter(adapter);
                }

            }


            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
            }
        });
    }

    // test//
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveLocationDriver extends AsyncTask<String, String, String> {
        // JSON Node names
        private static final String TAG_DRIVER = "driverNik";
        private static final String TAG_LATITUDE = "latitude";
        private static final String TAG_LONGITUDE = "longitude";
        private static final String TAG_DRIVERNAME = "driverName";

        Location location = null;

        private static final String url_update_location = "http://webtvasia.id/dashboard/api.php?task=simpanLocation";

        // url to delete product
        // Progress Dialog
        private ProgressDialog pDialog;

        // JSON parser class
        JSONParser jsonParser = new JSONParser();
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        /*   gps = new GPSDetector(LocationActivity.this);*/

   // check if GPS enabled

/**
* Before starting background thread Show Progress Dialog
* */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new ProgressDialog(LocationActivity.this);
            pDialog.setMessage("Driver Location updating ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            final DBDriverAccount dbAccount = new DBDriverAccount(getApplicationContext());
            String driverNik = null;
            String NamaDriver = null;
            if (dbAccount.getCurrentMemberDetails().getIsLogged() != null) {
                POJODrivers akunLoggedCheck = dbAccount.getCurrentMemberDetails();

                if (akunLoggedCheck.getIsLogged().equals("1")) {
                    driverNik = akunLoggedCheck.getDrivernik();
                    NamaDriver = akunLoggedCheck.getDrivername();
                }
            }


               //String driverNik = "123";//txtDesc.getText().toString();
               final Intent intent = getIntent();
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair(TAG_DRIVER, driverNik));
               params.add(new BasicNameValuePair(TAG_DRIVERNAME, NamaDriver));
               params.add(new BasicNameValuePair(TAG_LATITUDE, String.valueOf(latitude)));
               params.add(new BasicNameValuePair(TAG_LONGITUDE, String.valueOf(longitude)));


               // sending modified data through http request
               // Notice that update product url accepts POST method
               JSONObject json = jsonParser.makeHttpRequest(url_update_location,
                       "POST", params);
               Log.w("param", String.valueOf(params));

               // check json success tag
               try {
                   int success = json.getInt("sukses");

                   if (success == 1) {
                       // successfully updated
                       Intent i = getIntent();
                       // send result code 100 to notify about product update
                       setResult(100, i);
                       finish();
                   } else {
                       // failed to update product
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
// test//


    private void getCurrentLocation () {
        // create class object
        gps = new GPSDetector(LocationActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //update data to textviews
            longtitudeText.setText(String.valueOf(longitude));
            latutideText.setText(String.valueOf(latitude));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.stopUsingGPS(); //stop using GPS service on pause
    }

    public void updateLocation() {
        new SaveLocationDriver().execute();
    }

    public String getServerAPIServer(){
        Context context = getApplicationContext();
        String ServerAPI;
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ServerAPI = ai.metaData.get("ServerAPI").toString();
        return ServerAPI;
    }
}
