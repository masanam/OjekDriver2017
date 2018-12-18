package driverside.ojekkeren.com.ojekkerendriverside;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by andi on 5/1/2016.
 */
public class POJORouteGoogle {
    @SerializedName("overview_polyline")
    private POJOOverviewPolyLine overviewPolyLine;
    private List<POJOLegsGoogle> legs;

    public POJOOverviewPolyLine getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public List<POJOLegsGoogle> getLegs() {
        return legs;
    }


}
