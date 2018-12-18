package driverside.ojekkeren.com.ojekkerendriverside;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andi on 5/1/2016.
 */
public class POJODirectionGoogle {

    @SerializedName("routes")
    private List<POJORouteGoogle> routes;

    public List<POJORouteGoogle> getRoutes() {
        return routes;
    }



}
