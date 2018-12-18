package driverside.ojekkeren.com.ojekkerendriverside;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.*;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityAmbilBooking extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    String theDuration = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_ambil_booking);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.setMyLocationEnabled(true);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        // Check if we were successful in obtaining the map.
        if (mMap != null) {


            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
            Criteria criteria = new Criteria();
            String provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            android.location.Location location = locationManager.getLastKnownLocation(provider);

            if (location == null) {
                Toast.makeText(this, "Geo Coder Not Avaiable", Toast.LENGTH_LONG).show();
            } else {
                LatLng userLocation = new LatLng(location.getLatitude(),  location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));
            }

        }

        final Intent intent = getIntent();

        Log.w("get order id", intent.getStringExtra("orderid"));

        final String OrderID = intent.getStringExtra("orderid");
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");
        final TextView totalBiayaText = (TextView) findViewById(R.id.totalBiayaText);
        totalBiayaText.setTypeface(face);
        final TextView jarak = (TextView) findViewById(R.id.jarak);
        jarak.setTypeface(face);
        final TextView durasi = (TextView) findViewById(R.id.durasi);
        durasi.setTypeface(face);
        final TextView from = (TextView) findViewById(R.id.tempatjemput);
        from.setTypeface(face);
        final TextView typeordertext = (TextView) findViewById(R.id.typeorderText);
        typeordertext.setTypeface(face);
        TextView pesan = (TextView) findViewById(R.id.pesan);
        pesan.setTypeface(face);
        final TextView to = (TextView) findViewById(R.id.tujuandest);
        to.setTypeface(face);
        LinearLayout callcustomer = (LinearLayout) findViewById(R.id.callLay);
        LinearLayout pickup = (LinearLayout) findViewById(R.id.pickupLay);
        final LinearLayout itemslayout = (LinearLayout) findViewById(R.id.itemslayout);

        final TextView itemsToDeliver = (TextView) findViewById(R.id.itemsToDeliver);
        itemsToDeliver.setTypeface(face);

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ActivityAmbilBooking.this);
                adb.setTitle("Warning!");
                adb.setMessage("Pick Up Customer ?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SaveProductDetails().execute();
                        //Intent intent = new Intent(getApplicationContext(), FinalConfirmationOrder.class);
                        //context.startActivity(intent);
                        Intent intent = new Intent(getBaseContext(), FinalConfirmationOrder.class);
                        intent.putExtra("orderid",OrderID);
                        startActivity(intent);

                        //Intent myIntent = new Intent(ActivityAmbilBooking.this, FinalConfirmationOrder.class);
                        //startActivity(myIntent);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                adb.show();
            }
        });

        callcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ActivityAmbilBooking.this);
                adb.setTitle("Warning!");
                adb.setMessage("Call the customer ?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intentCall = new Intent(Intent.ACTION_CALL);
                        intentCall.setData(Uri.parse("tel:" + intent.getStringExtra("phoneNum")));
                        startActivity(intentCall);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                adb.show();
            }
        });

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

        pesan.setText("Hi.. " + NamaDriver + " (NIK :" + driverNik + ") , Ready to work ?");


        final String base_url = getServerAPIServer();

        Locale locale = new Locale("id", "ID");
        final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIGetOrdersAvailable getOrdersInProgressREST = restAdapter.create(APIGetOrdersAvailable.class);

        getOrdersInProgressREST.getOrdersByOrderIdId("getOrdersById", intent.getStringExtra("orderid"), new Callback<POJOOrdersData>() {

            @Override
            public void success(final POJOOrdersData ordersDatas, Response response) {

                if (!ordersDatas.getFrom().matches("")) {
                    String[] getCoor = ordersDatas.getLatlangFrom().split(",");
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(getCoor[0]), Double.parseDouble(getCoor[1]))).icon(BitmapDescriptorFactory.fromResource(R.drawable.mcycle)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getCoor[0]), Double.parseDouble(getCoor[1])), 17));
                }

                String base_url = "http://maps.googleapis.com/";

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(base_url)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();

                APIDirectionInterfaceGoogle reqinterface = restAdapter.create(APIDirectionInterfaceGoogle.class);



                reqinterface.getJson(ordersDatas.getFrom(), ordersDatas.getTo(), new Callback<POJODirectionGoogle>() {


                    @Override
                    public void success(POJODirectionGoogle directionResultPojos, Response response) {
                        mMap.clear();
                        ArrayList<LatLng> routelist = new ArrayList<LatLng>();
                        double startLat = 0, startLong = 0;
                        double endLat = 0, endLong = 0;

                        if (directionResultPojos.getRoutes().size() > 0) {
                            ArrayList<LatLng> decodelist;
                            POJORouteGoogle routeA = directionResultPojos.getRoutes().get(0);
                            if (routeA.getLegs().size() > 0) {
                                List<POJOSteps> steps = routeA.getLegs().get(0).getSteps();
                               // String legs = String.valueOf(routeA.getLegs().get(0));
                                POJOSteps step;
                                Log.w("Legs duration: ", String.valueOf(routeA.getLegs().get(0).getDuration().getText()));
                                theDuration = String.valueOf(routeA.getLegs().get(0).getDuration().getText());
                                driverside.ojekkeren.com.ojekkerendriverside.Location location;
                                String polyline;
                                for (int i = 0; i < steps.size(); i++) {
                                    step = steps.get(i);
                                    location = step.getStart_location();
                                    if (i == 0) {
                                        startLat = location.getLat();
                                        startLong = location.getLng();
                                    }

                                    routelist.add(new LatLng(location.getLat(),  location.getLng()));
                                    polyline = step.getPolyline().getPoints();
                                    decodelist = POJORouteDecode.decodePoly(polyline);
                                    routelist.addAll(decodelist);
                                    location = step.getEnd_location();
                                    routelist.add(new LatLng(location.getLat(),  location.getLng()));

                                    endLat = location.getLat();
                                    endLong =  location.getLng();
                                }
                            }
                        }

                        if (routelist.size() > 0) {
                            PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.GREEN);

                            for (int i = 0; i < routelist.size(); i++) {
                                rectLine.add(routelist.get(i));
                            }

                            // Adding route on the map
                            mMap.addPolyline(rectLine);

                            mMap.addMarker(new MarkerOptions().position(new LatLng(startLat, startLong)).title("Estimate Time").snippet(theDuration).icon(BitmapDescriptorFactory.fromResource(R.drawable.mbike)));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(endLat, endLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.target)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLat, startLong), 13));
                            durasi.setText(theDuration);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.w("Error", error.getMessage().toString());
                    }
                });

                totalBiayaText.setText(ordersDatas.getPrice());
                jarak.setText(ordersDatas.getDistance() + " Km");


                if (ordersDatas.getTypeorder().equals("3")) {
                    typeordertext.setText("Food Delivery");
                    totalBiayaText.setText(ordersDatas.getPrice());
                    restAdapter = new RestAdapter.Builder()
                            .setEndpoint(base_url)
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .build();

                    APIGetFoodOrdersByOrderId getOrdersInProgressREST = restAdapter.create(APIGetFoodOrdersByOrderId.class);

                    getOrdersInProgressREST.getFoods("getFoodOrderByOrderId", intent.getStringExtra("orderid"), new Callback<List<POJOFoodOrders>>() {

                        @Override
                        public void success(List<POJOFoodOrders> ordersDatas, Response response) {
                            ArrayList<POJOFoodOrders> ordersData = new ArrayList<POJOFoodOrders>();
                            if (ordersDatas.size() == 0) {
                                Log.w("ordersData", "0");
                            } else {
                                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sansation_Regular.ttf");
                                TextView itemsToDeliver = (TextView) findViewById(R.id.itemsToDeliver);
                                itemsToDeliver.setTypeface(face);

                                for (int i = 0; i < ordersDatas.size(); i++) {
                                    itemsToDeliver.setText(ordersDatas.get(i).getFoodname() + " (" + ordersDatas.get(i).getQuantity() + ")");
                                }
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //Log.w("RetrofitError getFoodOrderByOrderId", error.getMessage());
                        }
                    });
                    from.setText(ordersDatas.getAddressfrom() + " - " + ordersDatas.getFrom());
                } else if (ordersDatas.getTypeorder().equals("2")) {
                    typeordertext.setText("Deliver Item");
                    totalBiayaText.setText(ordersDatas.getPrice());
                    itemslayout.setVisibility(View.VISIBLE);
                    itemsToDeliver.setText(ordersDatas.getItemtodeliver());
                    from.setText(ordersDatas.getFrom() + ", " + ordersDatas.getAddressfrom());
                } else {
                    typeordertext.setText("PickUp");
                    itemsToDeliver.setText("");
                    itemslayout.setVisibility(View.GONE);
                    String[] splitPrice = ordersDatas.getPrice().split("\\.");

                    totalBiayaText.setText(currencyFormatter.format(Double.parseDouble(splitPrice[0])));
                    from.setText(ordersDatas.getFrom() + ", " + ordersDatas.getAddressfrom());
                }

                to.setText(ordersDatas.getTo() + ", " + ordersDatas.getAddressto());


            }

            @Override
            public void failure(RetrofitError error) {
               // Log.w("RetrofitError getOrdersById", error.getMessage());
            }
        });


    }



// test//

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        // JSON Node names
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_PRODUCT = "product";
        private static final String TAG_PID = "orderid";
        private static final String TAG_DRIVER = "driverNik";
        private static final String TAG_PRICE = "price";
        private static final String TAG_DESCRIPTION = "description";
        String pid;
        private static final String url_update_product = "http://webtvasia.id/dashboard/api.php?task=updateOrder";

        // url to delete product
        // Progress Dialog
        private ProgressDialog pDialog;

        // JSON parser class
        JSONParser jsonParser = new JSONParser();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityAmbilBooking.this);
            pDialog.setMessage("Order status updating ...");
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

            String name = "nama";//txtName.getText().toString();
            String price = "nama";//txtPrice.getText().toString();
            //String driverNik = "123";//txtDesc.getText().toString();
            final Intent intent = getIntent();
            // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, intent.getStringExtra("orderid")));
            params.add(new BasicNameValuePair(TAG_DRIVER, driverNik));



            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityAmbilBooking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://driverside.ojekkeren.com.ojekkerendriverside/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityAmbilBooking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://driverside.ojekkeren.com.ojekkerendriverside/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}
