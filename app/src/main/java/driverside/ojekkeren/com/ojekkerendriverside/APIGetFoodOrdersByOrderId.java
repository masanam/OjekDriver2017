package driverside.ojekkeren.com.ojekkerendriverside;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/30/2016.
 */
public interface APIGetFoodOrdersByOrderId {
    @GET("/api.php")
    public void getFoods(@Query("task") String task ,@Query("orderid") String orderid, Callback<List<POJOFoodOrders>> response);
}
