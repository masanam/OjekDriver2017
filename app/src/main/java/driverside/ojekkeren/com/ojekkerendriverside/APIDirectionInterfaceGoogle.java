package driverside.ojekkeren.com.ojekkerendriverside;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by andi on 5/1/2016.
 */
public interface APIDirectionInterfaceGoogle {
    @GET("/maps/api/directions/json")
    public void getJson(@Query("origin") String origin,@Query("destination") String destination, Callback<POJODirectionGoogle> callback);
}
