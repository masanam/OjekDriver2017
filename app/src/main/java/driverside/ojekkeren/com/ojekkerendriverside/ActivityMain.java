package driverside.ojekkeren.com.ojekkerendriverside;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

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
                    AdapterListGetOrders adapter = new AdapterListGetOrders(ActivityMain.this, ordersData);
                    listView.setAdapter(adapter);
                }

            }


            @Override
            public void failure(RetrofitError error) {
                Log.w("RetrofitError", error.getMessage());
            }
        });
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
