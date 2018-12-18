package driverside.ojekkeren.com.ojekkerendriverside;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 4/29/2016.
 */
public interface APICheckConnection {
    @GET("/api.php")
    public void checkConn(@Query("task") String task , Callback<POJOCheckConnection> response);
}
