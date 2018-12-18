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

public class FinalConfirmationOrder extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    String theDuration = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_confirmation_order);

        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap))
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
        String OrderID = getIntent().getStringExtra("orderid");


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



        final String base_url = getServerAPIServer();



        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(base_url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        APIGetOrdersAvailable getOrdersInProgressREST = restAdapter.create(APIGetOrdersAvailable.class);

        getOrdersInProgressREST.getOrdersByOrderIdId("getOrdersById", OrderID, new Callback<POJOOrdersData>() {

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
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.w("Error", error.getMessage().toString());
                    }
                });



                if (ordersDatas.getTypeorder().equals("3")) {
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
                } else if (ordersDatas.getTypeorder().equals("2")) {

                } else {
                }



            }

            @Override
            public void failure(RetrofitError error) {
                // Log.w("RetrofitError getOrdersById", error.getMessage());
            }
        });


    }



// test//

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
