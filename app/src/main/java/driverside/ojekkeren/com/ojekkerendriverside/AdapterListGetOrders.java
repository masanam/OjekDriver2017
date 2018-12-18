package driverside.ojekkeren.com.ojekkerendriverside;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andi on 4/29/2016.
 */
public class AdapterListGetOrders  extends ArrayAdapter<POJOOrdersData> {

    ArrayList<POJOOrdersData> ordersData;
    private Context context;

    public AdapterListGetOrders(Context context, ArrayList<POJOOrdersData> ordersDatas) {
        super(context, 0, ordersDatas);
        this.context = context;
        this.ordersData = new ArrayList<POJOOrdersData>();
        this.ordersData.addAll(ordersDatas);
    }



    public String getServerAPIServer(){
        Context context = getContext();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        String base_url = getServerAPIServer();

        final POJOOrdersData ordersDataView = ordersData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_orders, parent, false);
        }

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");
        TextView totalBiayaText = (TextView) convertView.findViewById(R.id.totalBiayaText);
        totalBiayaText.setTypeface(face);
        TextView jarak = (TextView) convertView.findViewById(R.id.jarak);
        jarak.setTypeface(face);
        TextView from = (TextView) convertView.findViewById(R.id.tempatjemput);
        from.setTypeface(face);
        TextView typeordertext = (TextView) convertView.findViewById(R.id.typeorderText);
        typeordertext.setTypeface(face);
        TextView to = (TextView) convertView.findViewById(R.id.tujuandest);
        to.setTypeface(face);
        LinearLayout ambilbook = (LinearLayout) convertView.findViewById(R.id.ambilbooking);
        LinearLayout itemslayout = (LinearLayout) convertView.findViewById(R.id.itemslayout);

        TextView itemsToDeliver = (TextView) convertView.findViewById(R.id.itemsToDeliver);
        itemsToDeliver.setTypeface(face);

        totalBiayaText.setText(ordersDataView.getPrice());
        jarak.setText(ordersDataView.getDistance()+" KM");

        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if(ordersDataView.getTypeorder().equals("3")){

            typeordertext.setText("Food Delivery");
            totalBiayaText.setText(ordersDataView.getPrice());
            itemslayout.setVisibility(View.VISIBLE);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(base_url)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            APIGetFoodOrdersByOrderId getOrdersInProgressREST = restAdapter.create(APIGetFoodOrdersByOrderId.class);

            final View finalConvertView = convertView;
            getOrdersInProgressREST.getFoods("getFoodOrderByOrderId", ordersDataView.getOrderid(), new Callback<List<POJOFoodOrders>>() {

                @Override
                public void success(List<POJOFoodOrders> ordersDatas, Response response) {
                    ArrayList<POJOFoodOrders> ordersData = new ArrayList<POJOFoodOrders>();
                    if (ordersDatas.size() == 0) {
                        Log.w("ordersData", "0");
                    } else {
                        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sansation_Regular.ttf");
                        TextView itemsToDeliver = (TextView) finalConvertView.findViewById(R.id.itemsToDeliver);
                        itemsToDeliver.setTypeface(face);

                        for (int i = 0; i < ordersDatas.size(); i++) {
                            itemsToDeliver.setText(ordersDatas.get(i).getFoodname() + " (" + ordersDatas.get(i).getQuantity() + ")");
                        }
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.w("RetrofitError", error.getMessage());
                }
            });
            from.setText(ordersDataView.getAddressfrom()+" - "+ordersDataView.getFrom());
        }else if(ordersDataView.getTypeorder().equals("2")) {
            typeordertext.setText("Deliver Item");
            totalBiayaText.setText(ordersDataView.getPrice());
            itemslayout.setVisibility(View.VISIBLE);
            itemsToDeliver.setText(ordersDataView.getItemtodeliver());
            from.setText(ordersDataView.getFrom()+", "+ordersDataView.getAddressfrom());
        }else{
            typeordertext.setText("Pickup");
            itemsToDeliver.setText("");
            itemslayout.setVisibility(View.GONE);
            String[] splitPrice = ordersDataView.getPrice().split("\\.");

            totalBiayaText.setText(currencyFormatter.format(Double.parseDouble(splitPrice[0])) );
            from.setText(ordersDataView.getFrom()+", "+ordersDataView.getAddressfrom());
        }

        to.setText(ordersDataView.getTo()+", "+ordersDataView.getAddressto());

        ambilbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Warning!");
                adb.setMessage("You can't cancelled booking, Are you sure ?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), ActivityAmbilBooking.class);
                        intent.putExtra("orderid",ordersDataView.getOrderid());
                        intent.putExtra("phoneNum",ordersDataView.getPhoneNum());
                        context.startActivity(intent);
                    }
                });
                adb.setNegativeButton("Not sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                adb.show();
            }
        });

        return convertView;
    }
}
