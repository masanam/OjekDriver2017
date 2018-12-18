package driverside.ojekkeren.com.ojekkerendriverside;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/29/2016.
 */
public interface APIGetDriversAvailable {
    @GET("/api.php")
    public void getOrdersList(@Query("task") String task, Callback<List<POJOOrdersData>> callback);

    @GET("/api.php")
    public void getOrdersByOrderIdId(@Query("task") String task, @Query("orderid") String orderid, Callback<POJODriversData> callback);


}
