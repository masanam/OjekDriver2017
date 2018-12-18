package driverside.ojekkeren.com.ojekkerendriverside;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/29/2016.
 */
public interface APILoginDriver {
    @GET("/api.php")
    public void getJson(@Query("task") String task, @Query("drivernik") String drivernik,@Query("password") String password, Callback<POJODrivers> callback);
}
